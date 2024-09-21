package com.beehyv.backend.configurations.filters;

import com.beehyv.backend.exceptionhandlers.securityexceptionhandlers.CustomAuthenticationEntryPoint;
import com.beehyv.backend.exceptions.CustomAuthException;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.services.authentication.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CustomAuthenticationEntryPoint entryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;

        if(header != null && header.startsWith("Bearer ")){
            token = header.substring(7);

            if(SecurityContextHolder.getContext().getAuthentication()==null){

                try {
                    if(jwtService.isValid(token)){
                        EmployeeDetails employeeDetails = jwtService.getEmployeeDetails(token);
                        UsernamePasswordAuthenticationToken authenticationToken
                                = new UsernamePasswordAuthenticationToken(employeeDetails, null, employeeDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                        filterChain.doFilter(request, response);
                    }
                } catch (JwtException e) {
                    entryPoint.commence(request, response, new CustomAuthException(e.getMessage()));
                }
            }
        }
        else{
            filterChain.doFilter(request, response);
        }
    }
}
