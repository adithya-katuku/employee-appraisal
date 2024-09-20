package com.beehyv.backend.services.authentication;

import com.beehyv.backend.dto.custom.CaptchaDTO;
import com.beehyv.backend.dto.request.LoginDTO;
import com.beehyv.backend.dto.response.EmployeeResponseDTO;
import com.beehyv.backend.modeldetails.EmployeeDetails;
import com.beehyv.backend.models.Employee;
import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.repositories.*;
import com.beehyv.backend.services.AppraisalService;
import com.beehyv.backend.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private EmployeeService employeeService;
    @Autowired
    private DesignationRepo designationRepo;
    @Autowired
    private AttributeRepo attributeRepo;
    @Autowired
    private AppraisalService appraisalService;

    public EmployeeResponseDTO saveEmployee(Employee employee){
        return employeeService.saveEmployee(employee);
    }

    public Map<String, String> handleLogin(LoginDTO loginDTO){
        Map<String, String> res = new HashMap<>();

//        if(!captchaService.verifyCaptcha(new CaptchaDTO(loginDTO.captchaId(), loginDTO.captchaAnswer()))){
//            res.put("message", "Invalid captcha");
//            return res;
//        }
        System.out.println("here");
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            EmployeeDetails employeeDetails = (EmployeeDetails) authentication.getPrincipal();

            res.put("jwt", jwtService.generateToken(loginDTO.email()));
            List<Role> roles = employeeDetails.getRoles();
            Role role = roles.stream().max((r1, r2)->r1.ordinal()-r2.ordinal()).orElse(Role.EMPLOYEE);
            res.put("role", role.name());

            if(role.compareTo(Role.ADMIN)!=0){
                appraisalService.checkIfEmployeeEligibleForAppraisal(employeeDetails.getPreviousAppraisalDate(), employeeDetails.getEmployeeId());
            }

            return res;
        }
        catch(AuthenticationException e){
            res.put("message", e.getMessage());
            return res;
        }
    }

}
