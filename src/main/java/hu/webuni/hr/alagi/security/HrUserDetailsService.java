package hu.webuni.hr.alagi.security;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class HrUserDetailsService implements UserDetailsService {

   @Autowired
   private EmployeeRepository employeeRepository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Employee employee = employeeRepository.findByUsername(username)
            .orElseThrow(()->new UsernameNotFoundException("User not found with username: "+username));
      return new HrUser(employee.getUsername(), employee.getPassword(), Arrays.asList(new SimpleGrantedAuthority("USER")), employee);
   }
}
