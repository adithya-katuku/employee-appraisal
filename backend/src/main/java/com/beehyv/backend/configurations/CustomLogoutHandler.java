package com.beehyv.backend.configurations;

import com.beehyv.backend.services.authentication.JwtService;
import com.beehyv.backend.userdetails.EmployeeDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class CustomLogoutHandler implements LogoutHandler {
    @Autowired
    private JwtService jwtService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        Cookie refreshCookie = new Cookie("refreshToken", null);
//        refreshCookie.setHttpOnly(true);
//        refreshCookie.setSecure(true);
//        refreshCookie.setPath("/refresh-token");
//        refreshCookie.setDomain("localhost");
//        refreshCookie.setMaxAge(0);
//        String header = request.getHeader("Authorization");
//        try{
//            if (header != null && header.startsWith("Bearer ")) {
//                String token = header.substring(7);
//
//                if (SecurityContextHolder.getContext().getAuthentication() == null) {
//                    if (jwtService.isValidAccessToken(token)) {
//                        EmployeeDetails employeeDetails = jwtService.getEmployeeDetails(token);
//                        UsernamePasswordAuthenticationToken authenticationToken
//                                = new UsernamePasswordAuthenticationToken(employeeDetails, null, employeeDetails.getAuthorities());
//                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                    }
//                }
//            }
//            response.addCookie(refreshCookie);
//        }
//        catch (Exception e){
//            exceptionResolver.resolveException(request, response, null, e);
//        }
//        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        request.getSession().invalidate();
//        jwtService.deleteRefreshToken(employeeId);
        try {
            response.sendRedirect("/log-out");
        } catch (IOException e) {
            exceptionResolver.resolveException(request, response, null, e);
        }
    }
}
