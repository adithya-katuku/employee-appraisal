package com.beehyv.backend.services.authentication;

import com.beehyv.backend.dto.custom.RefreshTokenDTO;
import com.beehyv.backend.dto.request.RefreshTokenRequestDTO;
import com.beehyv.backend.dto.response.AuthenticationResponseDTO;
import com.beehyv.backend.exceptions.CustomAuthException;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.RefreshToken;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.repositories.RefreshTokenRepo;
import com.beehyv.backend.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    RefreshTokenRepo refreshTokenRepo;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    JwtService jwtService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public RefreshTokenDTO generateRefreshToken(Integer employeeId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEmployeeId(employeeId);
        String token = UUID.randomUUID().toString();
        refreshToken.setToken(passwordEncoder.encode(token));
        refreshToken.setExpiry(new Date(System.currentTimeMillis()+1000*60*60*24*7));
        refreshTokenRepo.save(refreshToken);

        return new RefreshTokenDTO(refreshToken.getRefreshTokenId(), token);
    }

    public AuthenticationResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        RefreshToken refreshToken = refreshTokenRepo.findById(refreshTokenRequestDTO.refreshTokenId()).orElse(null);

        if(refreshToken == null
                || refreshToken.getExpiry().before(new Date())
                || !passwordEncoder.matches(refreshTokenRequestDTO.refreshToken(), refreshToken.getToken())){
            throw new CustomAuthException("Refresh refreshToken is not valid. Please sign in with credentials.");
        }

        Employee employee = employeeService.findEmployee(refreshToken.getEmployeeId());
        String jwtToken = jwtService.generateToken(new EmployeeDetails(employee));
        String role = employee.getRoles()
                .stream()
                .max((r1, r2) -> r1.ordinal() - r2.ordinal())
                .orElse(Role.EMPLOYEE)
                .name()
                .toLowerCase();

        return new AuthenticationResponseDTO(jwtToken, refreshTokenRequestDTO.refreshTokenId(), refreshTokenRequestDTO.refreshToken(), role);
    }
}
