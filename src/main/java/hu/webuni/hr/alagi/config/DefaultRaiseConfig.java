package hu.webuni.hr.alagi.config;

import hu.webuni.hr.alagi.repository.EmployeeRepository;
import hu.webuni.hr.alagi.repository.PositionRepository;
import hu.webuni.hr.alagi.service.DefaultEmployeeService;
import hu.webuni.hr.alagi.service.EmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!smart")
public class DefaultRaiseConfig {

   private final EmployeeRepository employeeRepository;
   private final PositionRepository positionRepository;

   public DefaultRaiseConfig(EmployeeRepository employeeRepository, PositionRepository positionRepository) {
      this.employeeRepository = employeeRepository;
      this.positionRepository = positionRepository;
   }

   @Bean
   public EmployeeService employeeService() {
      return new DefaultEmployeeService(employeeRepository, positionRepository);
   }
}
