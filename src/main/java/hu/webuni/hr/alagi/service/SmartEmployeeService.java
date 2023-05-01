package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.config.HrConfigProperties;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.repository.EmployeeRepository;
import hu.webuni.hr.alagi.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;

@Service
public class SmartEmployeeService extends AbstractEmployeeService {
   @Autowired
   HrConfigProperties hrConfigProperties;
   @Autowired
   DateService dateSevice;

   public SmartEmployeeService(EmployeeRepository employeeRepository, PositionRepository positionRepository) {
      super(employeeRepository, positionRepository);
   }

   @Override
   public int getPayRaisePercent(Employee employee) {
      double years = dateSevice.calculateYearsBetweenDates(employee.getStartDate(), LocalDateTime.now());

      return hrConfigProperties.getSmart().getLimits()
            .entrySet()
            .stream()
            .filter(x->x.getKey()<years)
            .max(Comparator.comparingDouble(Map.Entry::getKey))
            .map(Map.Entry::getValue)
            .orElse(0);
   }
}
