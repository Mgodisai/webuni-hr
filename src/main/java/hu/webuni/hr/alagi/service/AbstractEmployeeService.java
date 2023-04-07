package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.repository.EmployeeRepository;

import java.util.List;

public abstract class AbstractEmployeeService implements EmployeeService {
   private final EmployeeRepository employeeRepository;

   public AbstractEmployeeService(EmployeeRepository employeeRepository) {
      this.employeeRepository = employeeRepository;
   }

   public List<Employee> getAllEmployees() {
      return employeeRepository.findAll();
   }

   public List<Employee> getAllEmployeesUsingMinSalary(Integer minSalary) {
      return employeeRepository.findAll().stream()
               .filter(employee -> employee.getMonthlySalary() >= minSalary)
               .toList();
   }

   public Employee getEmployeeById(Long id) {
      return employeeRepository.findById(id);
   }

   public Employee createEmployee(Employee employee) {
      if (employee.getId()!=null && employeeRepository.findById(employee.getId())!=null) {
         return null;
      }
      return employeeRepository.save(employee);
   }

   public boolean isEmployeeExistsById(Long id) {
      return employeeRepository.findById(id)!=null;
   }

   public Employee updateEmployee(Employee employee) {
      return employeeRepository.save(employee);
   }

   public void deleteEmployee(Long id) {
      employeeRepository.deleteById(id);
   }
}
