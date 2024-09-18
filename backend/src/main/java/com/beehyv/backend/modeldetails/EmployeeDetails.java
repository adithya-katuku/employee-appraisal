package com.beehyv.backend.modeldetails;

import com.beehyv.backend.models.enums.Role;
import com.beehyv.backend.models.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Component
public class EmployeeDetails implements UserDetails {
    private Integer employeeId;
    private String username;
    private String password;
    private List<Role> roles;

    public EmployeeDetails(Employee employee){
        this.employeeId = employee.getEmployeeId();
        this.username = employee.getEmail();
        this.password = employee.getPassword();
        this.roles = employee.getRoles();
    }

    public EmployeeDetails(Integer employeeId, String username, List<Role> roles) {
        this.employeeId = employeeId;
        this.username = username;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
