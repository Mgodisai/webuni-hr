package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;
import hu.webuni.hr.alagi.repository.EmployeeRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEmployeeService implements EmployeeService {

   private final EmployeeRepository employeeRepository;

   public AbstractEmployeeService(EmployeeRepository employeeRepository) {
      this.employeeRepository = employeeRepository;
   }

   @Override
   public List<Employee> findEmployeesByPosition(Position position) {
      return employeeRepository.findEmployeesByPosition(position);
   }

   @Override
   public List<Employee> findEmployeesByFirstNameStartingWith(String text) {
      return employeeRepository.findEmployeesByFirstNameStartsWithIgnoreCase(text);
   }

   @Override
   public List<Employee> findEmployeesByStartingDateBetweenDates(LocalDateTime start, LocalDateTime end) {
      return employeeRepository.findEmployeesByStartDateBetween(start, end);
   }


   public List<Employee> getAllEmployees() {
      return employeeRepository.findAll();
   }

   @Override
   public List<Employee> filteredEmployeeList(Optional<Position> position, Optional<Integer> minSalary, Optional<String> firstNameStartsWith, Optional<LocalDateTime> start, Optional<LocalDateTime> endd) {
      return employeeRepository.filterEmployees(
            position.orElse(null),
            minSalary.orElse(null),
            firstNameStartsWith.orElse(null),
            start.orElse(employeeRepository.findMinStartDate()),
            endd.orElse(employeeRepository.findMaxStartDate()));
   }

   public List<Employee> getAllEmployeesUsingMinSalary(Integer minSalary) {
      return employeeRepository.findByMonthlySalaryGreaterThanEqual(minSalary);
   }

   public Optional<Employee> getEmployeeById(Long id) {
      return employeeRepository.findById(id);
   }

   @Transactional
   public Optional<Employee> createEmployee(Employee employee) {
      if (employee.getId()!=null && isEmployeeExistedByGivenId(employee.getId())) {
         return Optional.empty();
      }
      return Optional.of(employeeRepository.save(employee));
   }

   public boolean isEmployeeExistedByGivenId(Long id) {
      return employeeRepository.existsById(id);
   }

   @Transactional
   public Optional<Employee> updateEmployee(Employee employee) {
      if (employee.getId()==null || !isEmployeeExistedByGivenId(employee.getId())) {
         return Optional.empty();
      }
      return Optional.of(employeeRepository.save(employee));
   }

   @Transactional
   public void deleteEmployee(Long id) {
      employeeRepository.deleteById(id);
   }
}
