package hu.webuni.hr.alagi.config;

import hu.webuni.hr.alagi.repository.EmployeeRepository;
import hu.webuni.hr.alagi.repository.PositionRepository;
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

   private final EmployeeRepository employeeRepository;
   private final PositionRepository positionRepository;

   public SmartRaiseConfig(EmployeeRepository employeeRepository, PositionRepository positionRepository) {
      this.employeeRepository = employeeRepository;
      this.positionRepository = positionRepository;
   }

   @Bean
   public EmployeeService employeeService() {
      return new SmartEmployeeService(employeeRepository, positionRepository);
   }

   @Bean
   public DateService dateService() {
      return new ComplexDateService();
   }
}
