package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Repository
public class EmployeeRepositoryImpl implements MyCrudLikeRepository<Employee, Long> {
   private final Map<Long, Employee> employeeList = new HashMap<>();

   @Override
   public List<Employee> findAll() {
      return employeeList.values().stream().toList();
   }

   @Override
   public Employee findById(Long id) {
      return employeeList.values()
              .stream()
              .filter(e -> e.getId().equals(id))
              .findFirst()
              .orElse(null);
   }

   @Override
   public long count() {
      return employeeList.size();
   }

   @Override
   public boolean existsById(Long id) {
      return employeeList.values().stream()
              .anyMatch(e -> e.getId().equals(id));
   }

   @Override
   public Employee save(Employee employee) {
      if (employee.getId() == null) {
         long newId = employeeList.values().stream()
                 .mapToLong(Employee::getId)
                 .max()
                 .orElse(0L) + 1;
         employee.setId(newId);
         employeeList.put(newId, employee);
      } else {
         employeeList.put(employee.getId(), employee);
      }
      return employee;
   }

   @Override
   public void deleteById(Long id) {
      if (id != null) {
         employeeList.remove(id);
      }
   }

   @Override
   public void delete(Employee employee) {
      deleteById(employee.getId());
   }

   @Override
   public void deleteAll() {
      employeeList.clear();
   }
}
