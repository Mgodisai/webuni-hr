package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;
import hu.webuni.hr.alagi.repository.EmployeeRepository;
import hu.webuni.hr.alagi.repository.PositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

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

   public List<Employee> getAllEmployees() {
      return employeeRepository.findAllEmployeesWithCompany();
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


   public Page<Employee> findEmployeesByExample(Employee example, Pageable pageable) {
      Specification<Employee> spec = Specification.where(EmployeeSpecifications.fetchCompany());
      if (example.getId()!=null) {
         spec = spec.and(EmployeeSpecifications.hasId(example.getId()));
      }
      if (StringUtils.hasText(example.getFirstName())) {
         spec = spec.and(EmployeeSpecifications.likeFirstNameIgnoreCase(example.getFirstName()));
      }
      if (example.getPosition().getId()!=null) {
         spec = spec.and(EmployeeSpecifications.hasPosition(example.getPosition()));
      }
      if (example.getMonthlySalary()!=null && example.getMonthlySalary()>0) {
         double min = example.getMonthlySalary()*0.95;
         double max = example.getMonthlySalary()*1.05;
         spec = spec.and(EmployeeSpecifications.isMonthlySalaryBetweenMinAndMax(min, max));
      }

      if (example.getStartDate()!=null) {
         spec = spec.and(EmployeeSpecifications.isStartDateOnSpecifiedDate(example.getStartDate().toLocalDate()));
      }

      if (example.getCompany()!=null) {
         spec = spec.and(EmployeeSpecifications.likeCompanyNameIgnoreCase(example.getCompany().getName()));
      }
      return employeeRepository.findAll(spec, pageable);
   }
}
