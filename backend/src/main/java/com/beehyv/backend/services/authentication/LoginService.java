package com.beehyv.backend.services.authentication;

import com.beehyv.backend.dto.custom.CaptchaDTO;
import com.beehyv.backend.dto.custom.RefreshTokenDTO;
import com.beehyv.backend.dto.request.LoginRequestDTO;
import com.beehyv.backend.dto.request.RefreshTokenRequestDTO;
import com.beehyv.backend.dto.response.EmployeeResponseDTO;
import com.beehyv.backend.dto.response.AuthenticationResponseDTO;
import com.beehyv.backend.exceptions.InvalidInputException;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.repositories.RefreshTokenRepo;
import com.beehyv.backend.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    public EmployeeResponseDTO saveEmployee(Employee employee){
        return employeeService.saveEmployee(employee);
    }

    public AuthenticationResponseDTO handleLogin(LoginRequestDTO loginRequestDTO){
        if(!captchaService.verifyCaptcha(new CaptchaDTO(loginRequestDTO.captchaId(), loginRequestDTO.captchaAnswer()))){
            throw new InvalidInputException("Invalid captcha");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        EmployeeDetails employeeDetails = (EmployeeDetails) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(employeeDetails);
        String role = employeeDetails.getRoles()
                .stream()
                .max((r1, r2) -> r1.ordinal() - r2.ordinal())
                .orElse(Role.EMPLOYEE)
                .name()
                .toLowerCase();
        RefreshTokenDTO refreshTokenDTO = refreshTokenService.generateRefreshToken(employeeDetails.getEmployeeId());

        return new AuthenticationResponseDTO(jwtToken, refreshTokenDTO.refreshTokenId(), refreshTokenDTO.token(), role);
    }

}
