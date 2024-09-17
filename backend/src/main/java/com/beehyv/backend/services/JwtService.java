package com.beehyv.backend.services;

import com.beehyv.backend.enums.Role;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.repositories.EmployeeRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;


@Service
public class JwtService {
    @Autowired
    private EmployeeRepo employeeRepo;

    private String SECRET_KEY = "";

    public JwtService(){
        SECRET_KEY = generateSecretKey();
    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        Employee employee = employeeRepo.findByEmail(username);
        claims.put("employee-id", employee.getEmployeeId().toString());
        claims.put("roles", employee.getRoles());
        claims.put("designation-id", employee.getDesignation().getDesignationId().toString());
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60))
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
            throw new RuntimeException(e);
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Integer extractEmployeeId(Claims claims){
        Object claimsEmployeeId = claims.get("employee-id");
        return Integer.valueOf(claimsEmployeeId.toString());
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

    public Integer extractDesignationId(Claims claims){
        Object claimsDesignationId = claims.get("designation-id");
        return Integer.valueOf(claimsDesignationId.toString());
    }

    public EmployeeDetails isValid(String token) {
        Claims claims = extractAllClaims(token);
        if(claims.getExpiration().after(new Date())){
            return new EmployeeDetails(extractEmployeeId(claims), extractUsername(token), extractRoles(claims), extractDesignationId(claims));
        };
        return null;
    }
}
