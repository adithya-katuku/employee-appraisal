package com.beehyv.backend.services.authentication;

import com.beehyv.backend.exceptions.CustomAuthException;
import com.beehyv.backend.models.RefreshToken;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.repositories.RefreshTokenRepo;
import com.beehyv.backend.services.EmployeeService;
import com.beehyv.backend.userdetails.EmployeeDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Service
public class JwtService {
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private EmployeeService employeeService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    private String SECRET_KEY = "";

    public JwtService(){
        SECRET_KEY = generateSecretKey();
    }

    public String generateAccessToken(EmployeeDetails employeeDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("employee-id", employeeDetails.getEmployeeId().toString());
        claims.put("roles", employeeDetails.getRoles());

        if(!employeeDetails.getRoles().contains(Role.ADMIN)){
            employeeService.checkIfEmployeeEligibleForAppraisal(employeeDetails);
        }

        long ACCESS_TOKEN_AGE = 1000 * 60 * 5;
        return Jwts.builder()
                .claims(claims)
                .subject(employeeDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ ACCESS_TOKEN_AGE))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(EmployeeDetails employeeDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("employee-id", employeeDetails.getEmployeeId().toString());
        Date issueTime = new Date(System.currentTimeMillis());
        long REFRESH_TOKEN_AGE = 1000 * 60 * 60 * 24;
        Date expiryTime = new Date(System.currentTimeMillis()+ REFRESH_TOKEN_AGE);
        storeRefreshToken(employeeDetails, issueTime, expiryTime);

        return Jwts.builder()
                .claims(claims)
                .subject(employeeDetails.getUsername())
                .issuedAt(issueTime)
                .expiration(expiryTime)
                .signWith(getKey())
                .compact();
    }

    public void storeRefreshToken(EmployeeDetails employeeDetails, Date issueTime, Date expiryTime){
        String refreshToken = Jwts.builder()
                .subject(employeeDetails.getUsername())
                .issuedAt(issueTime)
                .expiration(expiryTime)
                .signWith(getKey())
                .compact();
        RefreshToken token = refreshTokenRepo.findById(employeeDetails.getEmployeeId()).orElse(null);
        if(token==null){
            token = new RefreshToken();
            token.setEmployeeId(employeeDetails.getEmployeeId());
        }
        token.setToken(bCryptPasswordEncoder.encode(refreshToken));
        refreshTokenRepo.save(token);
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateSecretKey(){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            Key secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new InternalError(e.getMessage());
        }
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValidAccessToken(String accessToken){
        Claims claims = extractAllClaims(accessToken);

        if(claims!=null){
            return claims.getExpiration().after(new Date());
        }
        return false;
    }
    public boolean isValidRefreshToken(String refreshToken){
        Claims claims = extractAllClaims(refreshToken);
        if(claims==null){
           throw new CustomAuthException("Refresh token is not valid.");
        }
        Integer employeeId = extractEmployeeId(claims);
        RefreshToken token = refreshTokenRepo.findById(employeeId).orElse(null);
        if(token == null){
            throw new CustomAuthException("Refresh token is not found. Please login again to continue.");
        }
        String storedToken = Jwts.builder()
                .subject(claims.getSubject())
                .issuedAt(claims.getIssuedAt())
                .expiration(claims.getExpiration())
                .signWith(getKey())
                .compact();
        if(!bCryptPasswordEncoder.matches(storedToken, token.getToken())){
            throw new CustomAuthException("Refresh token is not valid.");
        }
        return claims.getExpiration().after(new Date());
    }
    public void deleteRefreshToken(Integer employeeId) {
        refreshTokenRepo.findById(employeeId).ifPresent(refreshToken -> refreshTokenRepo.delete(refreshToken));
    }
    public EmployeeDetails getEmployeeDetails(String token){
        Claims claims = extractAllClaims(token);
        return new EmployeeDetails(extractEmployeeId(claims), extractUsername(token), extractRoles(claims));
    }

    public Integer extractEmployeeId(Claims claims){
        Object claimsEmployeeId = claims.get("employee-id");
        return Integer.valueOf(claimsEmployeeId.toString());
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public List<Role> extractRoles(Claims claims){
        Object claimRoles = claims.get("roles");
        List<Role> roles = new ArrayList<>();
        if(claimRoles instanceof List<?>){
            for(Object obj: ((List<?>) claimRoles).toArray()){
                roles.add(Role.valueOf(obj.toString()));
            }
        }
        return roles;
    }


}
