package com.beehyv.backend.services.authentication;

import com.beehyv.backend.dto.custom.RefreshTokenDTO;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.userdetails.EmployeeDetails;
import com.beehyv.backend.services.AppraisalService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Service
public class JwtService {
    @Autowired
    private AppraisalService appraisalService;

    private String SECRET_KEY = "";

    public JwtService(){
        SECRET_KEY = generateSecretKey();
    }

    public String generateAccessToken(EmployeeDetails employeeDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("employee-id", employeeDetails.getEmployeeId().toString());
        claims.put("roles", employeeDetails.getRoles());

        if(!employeeDetails.getRoles().contains(Role.ADMIN)){
            appraisalService.checkIfEmployeeEligibleForAppraisal(employeeDetails.getPreviousAppraisalDate(), employeeDetails.getAppraisalEligibility(), employeeDetails.getEmployeeId());
        }

        return Jwts.builder()
                .claims(claims)
                .subject(employeeDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*15))
                .signWith(getKey())
                .compact();
    }
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*15))
                .signWith(getKey())
                .compact();
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

    public boolean isValid(String token){
        Claims claims = extractAllClaims(token);
        if(claims!=null){
            return claims.getExpiration().after(new Date());
        }
        return false;
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
