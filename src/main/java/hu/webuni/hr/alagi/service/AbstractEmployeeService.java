package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;
import hu.webuni.hr.alagi.repository.EmployeeRepository;
import hu.webuni.hr.alagi.repository.PositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractEmployeeService implements EmployeeService {

   private final EmployeeRepository employeeRepository;
   private final PositionRepository positionRepository;

   protected AbstractEmployeeService(EmployeeRepository employeeRepository, PositionRepository positionRepository) {
      this.employeeRepository = employeeRepository;
      this.positionRepository = positionRepository;
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

   public Page<Employee> findAllEmployeesUsingPaging(Pageable pageable) {
      return employeeRepository.findAll(pageable);
   }


   @Override
   public Page<Employee> filteredEmployeeList(Optional<Position> position, Optional<Integer> minSalary, Optional<String> firstNameStartsWith, Optional<LocalDateTime> start, Optional<LocalDateTime> endd, Pageable pageable) {
      return employeeRepository.filterEmployees(
              position.orElse(null),
              minSalary.orElse(null),
              firstNameStartsWith.orElse(null),
              start.orElse(employeeRepository.findMinStartDate()),
              endd.orElse(employeeRepository.findMaxStartDate()),
              pageable);
   }

   public List<Employee> getAllEmployeesUsingMinSalary(Integer minSalary) {
      return employeeRepository.findByMonthlySalaryGreaterThanEqual(minSalary);
   }

   public Optional<Employee> getEmployeeById(Long id) {
      return employeeRepository.findEmployeeByIdWithCompany(id);
   }

   @Transactional
   public Optional<Employee> createEmployee(Employee employee) {
      if (employee.getId()!=null && isEmployeeExistedByGivenId(employee.getId())) {
         return Optional.empty();
      }
      Position position = positionRepository.findById(employee.getPosition().getId())
            .orElseGet(() -> positionRepository.save(employee.getPosition()));
      employee.setPosition(position);
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

   @Transactional
   public void deleteEmployee(Employee employee) {
      employeeRepository.delete(employee);
   }

   public Map<String, Double> getAvgSalariesByPositionUsingCompanyId(Long companyId) {

      List<Object[]> results = employeeRepository.getAvgSalariesByPositionUsingCompanyId(companyId);
      return results.stream()
              .collect(Collectors.toMap(
                      row -> (String) row[0],
                      row -> (Double) row[1]
              ));
   }
}
