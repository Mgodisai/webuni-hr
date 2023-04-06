package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;
import hu.webuni.hr.alagi.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository{
   public static final Employee employee1 = new Employee(1L, "József", "Java", Position.CEO, 10000, LocalDateTime.of(2017, Month.NOVEMBER, 1, 8,0 ));
   public static final Employee employee2 = new Employee(2L, "Géza", "Piton", Position.CUSTOMER_SUPPORT, 3000, LocalDateTime.of(2020, Month.JUNE, 15, 8,0 ));
   public static final Employee employee3 = new Employee(3L, "Paszkál", "Kis", Position.HR_MANAGER,2000, LocalDateTime.of(2023, Month.JANUARY, 5, 8,0 ));
   public static final Employee employee4 = new Employee(4L, "Tibor", "Kezdő", Position.TESTER, 1000, LocalDateTime.of(2003, Month.JANUARY, 5, 8,0 ));
   public static final Employee employee5 = new Employee(5L, "Kálmán", "Kóder", Position.DEVELOPER, 5000, LocalDateTime.of(2022, Month.JANUARY, 5, 8,0));
   public static final Employee employee6 = new Employee(6L, "Béla", "Adat", Position.ADMINISTRATOR, 900, LocalDateTime.of(2011, Month.JANUARY, 5, 8,0 ));

   private final MapService<Long> mapService;
   private final Map<Long, Employee> employeeList = new HashMap<>();

   {
      employeeList.put(employee1.getId(), employee1);
      employeeList.put(employee2.getId(), employee2);
      employeeList.put(employee3.getId(), employee3);
      employeeList.put(employee4.getId(), employee4);
      employeeList.put(employee5.getId(), employee5);
      employeeList.put(employee6.getId(), employee6);
   }

   public EmployeeRepositoryImpl(@Autowired MapService<Long> mapService) {
      this.mapService = mapService;
   }

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
         Long newId = mapService.getFirstFreeKey(employeeList.keySet());
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
