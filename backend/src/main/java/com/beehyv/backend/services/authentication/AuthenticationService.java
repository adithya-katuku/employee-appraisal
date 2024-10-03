package com.beehyv.backend.services.authentication;

import com.beehyv.backend.dto.custom.CaptchaDTO;
import com.beehyv.backend.dto.request.LoginRequestDTO;
import com.beehyv.backend.dto.response.AuthenticationResponseDTO;
import com.beehyv.backend.exceptions.CustomAuthException;
import com.beehyv.backend.exceptions.InvalidInputException;
import com.beehyv.backend.services.userdetailsservice.EmployeeDetailsService;
import com.beehyv.backend.userdetails.EmployeeDetails;
import com.beehyv.backend.models.enums.Role;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private EmployeeDetailsService employeeDetailsService;


    public AuthenticationResponseDTO handleLogin(LoginRequestDTO loginRequestDTO, HttpServletResponse response){
        if(!captchaService.verifyCaptcha(new CaptchaDTO(loginRequestDTO.captchaId(), loginRequestDTO.captchaAnswer()))){
            throw new InvalidInputException("Invalid captcha");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        EmployeeDetails employeeDetails = (EmployeeDetails) authentication.getPrincipal();

        return handleSuccessfulValidation(employeeDetails, response);
    }

    public AuthenticationResponseDTO refreshToken(String refreshToken, HttpServletResponse response) {
        if(!jwtService.isValid(refreshToken)){
            throw new CustomAuthException("Refresh token has expired!");
        }
        String username = jwtService.extractUsername(refreshToken);
        EmployeeDetails employeeDetails = (EmployeeDetails) employeeDetailsService.loadUserByUsername(username);
        return handleSuccessfulValidation(employeeDetails, response);
    }

    private AuthenticationResponseDTO handleSuccessfulValidation(EmployeeDetails employeeDetails, HttpServletResponse response){
        String accessToken = jwtService.generateAccessToken(employeeDetails);
        String role = employeeDetails.getRoles()
                .stream()
                .max((r1, r2) -> r1.ordinal() - r2.ordinal())
                .orElse(Role.EMPLOYEE)
                .name()
                .toLowerCase();
        String refreshToken = jwtService.generateRefreshToken(employeeDetails.getUsername());
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/refresh-token");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshCookie.setDomain("localhost");
        response.addCookie(refreshCookie);
        return new AuthenticationResponseDTO(accessToken, role);
    }

}
