package hu.webuni.hr.alagi.config;

import hu.webuni.hr.alagi.service.ComplexDateService;
import hu.webuni.hr.alagi.service.DateService;
import hu.webuni.hr.alagi.service.EmployeeService;
import hu.webuni.hr.alagi.service.SmartEmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("smart")
public class SmartRaiseConfig {
   @Bean
   public EmployeeService employeeService() {
      return new SmartEmployeeService();
   }

   @Bean
   public DateService dateService() {
      return new ComplexDateService();
   }
}
