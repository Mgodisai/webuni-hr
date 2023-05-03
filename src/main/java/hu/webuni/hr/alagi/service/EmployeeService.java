package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmployeeService {
   List<Employee> getAllEmployees();
   Optional<Employee> getEmployeeById(Long id);
   Optional<Employee> createEmployee(Employee employee);
   boolean isEmployeeExistedByGivenId(Long id);
   Optional<Employee> updateEmployee(Employee employee);
   void deleteEmployee(Long id);
   void deleteEmployee(Employee employee);
   int getPayRaisePercent(Employee employee);
   Map<String, Double> getAvgSalariesByPositionUsingCompanyId(Long companyId);
   Page<Employee> findEmployeesByExample(Employee example, Pageable pageable);
}
