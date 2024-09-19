package com.beehyv.backend.services;

import com.beehyv.backend.dto.custom.CaptchaDTO;
import com.beehyv.backend.dto.mappers.EmployeeDTOMapper;
import com.beehyv.backend.dto.request.LoginDTO;
import com.beehyv.backend.dto.response.EmployeeDTO;
import com.beehyv.backend.models.Attribute;
import com.beehyv.backend.models.Designation;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private DesignationRepo designationRepo;
    @Autowired
    private AttributeRepo attributeRepo;
    @Autowired
    private NotificationRepo notificationRepo;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public EmployeeDTO saveEmployee(Employee employee){
        List<Attribute> attributes = attributeRepo.saveOrFindAll(employee.getDesignation().getAttributes());
        Designation designation = designationRepo.findByDesignation(employee.getDesignation().getDesignation());
        if(designation==null){
            employee.getDesignation().setAttributes(attributes);
            designation = designationRepo.saveOrFind(employee.getDesignation());
        }

        employee.setDesignation(designation);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        return new EmployeeDTOMapper().apply(employeeRepo.save(employee));
    }

    public Map<String, String> handleLogin(LoginDTO loginDTO){
        Map<String, String> res = new HashMap<>();

        if(!captchaService.verifyCaptcha(new CaptchaDTO(loginDTO.captchaId(), loginDTO.captchaAnswer()))){
            res.put("message", "Invalid captcha");
            return res;
        }
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            res.put("jwt", jwtService.generateToken(loginDTO.email()));
            List<Role> roles = (List<Role>) authentication.getAuthorities();
            Role role = roles.stream().max((r1, r2)->r1.ordinal()-r2.ordinal()).orElse(Role.EMPLOYEE);
            res.put("role", role.name());
            return res;
        }
        catch(AuthenticationException e){
            res.put("message", e.getMessage());
            return res;
        }
    }

}
