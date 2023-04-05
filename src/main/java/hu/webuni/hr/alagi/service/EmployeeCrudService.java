package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeCrudService {
   private final EmployeeRepository employeeRepository;

   public EmployeeCrudService(@Autowired EmployeeRepository employeeRepository) {
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
