package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.config.HrConfigProperties;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.repository.EmployeeRepository;
import hu.webuni.hr.alagi.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultEmployeeService extends AbstractEmployeeService {

   @Autowired
   private HrConfigProperties hrConfigProperties;

   public DefaultEmployeeService(EmployeeRepository employeeRepository, PositionRepository positionRepository) {
      super(employeeRepository, positionRepository);
   }

   @Override
   public int getPayRaisePercent(Employee employee) {
      return hrConfigProperties.getDef().getPercent();
   }
}
