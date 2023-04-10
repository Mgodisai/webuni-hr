package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
   List<Employee> findEmployeesByPosition(Position position);
   List<Employee> findEmployeesByFirstNameStartingWith(String text);
   List<Employee> findEmployeesByStartingDateBetweenDates(LocalDateTime start, LocalDateTime end);
   List<Employee> getAllEmployees();
   List<Employee> filteredEmployeeList(Optional<Position> position, Optional<Integer> minSalary, Optional<String> firstNameStartsWith, Optional<LocalDateTime> start, Optional<LocalDateTime> end);
   List<Employee> getAllEmployeesUsingMinSalary(Integer minSalary);
   Optional<Employee> getEmployeeById(Long id);
   Optional<Employee> createEmployee(Employee employee);
   boolean isEmployeeExistedByGivenId(Long id);
   Optional<Employee> updateEmployee(Employee employee);
   void deleteEmployee(Long id);
   int getPayRaisePercent(Employee employee);
}
