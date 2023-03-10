package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.config.HrConfigProperties;
import hu.webuni.hr.alagi.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultEmployeeService implements EmployeeService{

   @Autowired
   HrConfigProperties hrConfigProperties;
   @Override
   public int getPayRaisePercent(Employee employee) {
      return hrConfigProperties.getDef().getPercent();
   }
}
