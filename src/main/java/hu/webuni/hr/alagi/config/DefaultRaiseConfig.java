package hu.webuni.hr.alagi.config;

import hu.webuni.hr.alagi.service.DefaultEmployeeService;
import hu.webuni.hr.alagi.service.EmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!smart")
public class DefaultRaiseConfig {
   @Bean
   public EmployeeService employeeService() {
      return new DefaultEmployeeService();
   }
}
