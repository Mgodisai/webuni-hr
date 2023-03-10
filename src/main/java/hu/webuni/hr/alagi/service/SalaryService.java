package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {
   private final EmployeeService employeeService;

   public SalaryService(@Autowired EmployeeService employeeService) {
      this.employeeService = employeeService;
   }

   public int getNewMonthlySalary(Employee employee) {

      return (int)(employee.getMonthlySalary() * (1+employeeService.getPayRaisePercent(employee)/100.0));
   }
}
