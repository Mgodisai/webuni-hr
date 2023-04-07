package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Employee;

import java.util.List;

public interface EmployeeService {
   List<Employee> getAllEmployees();
   List<Employee> getAllEmployeesUsingMinSalary(Integer minSalary);
   Employee getEmployeeById(Long id);
   Employee createEmployee(Employee employee);
   boolean isEmployeeExistsById(Long id);
   Employee updateEmployee(Employee employee);
   void deleteEmployee(Long id);
   int getPayRaisePercent(Employee employee);
}
