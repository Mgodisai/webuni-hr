package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.model.Position;
import hu.webuni.hr.alagi.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

   private final MapService<Long> mapService;

   private final Map<Long, EmployeeDto> employees = new HashMap<>();

   {
      employees.put(1L, new EmployeeDto(1L,"József", "Java", Position.CEO, 10000, LocalDateTime.of(2017, Month.NOVEMBER, 1, 8,0 )));
      employees.put(2L, new EmployeeDto(2L, "Géza", "Piton", Position.CUSTOMER_SUPPORT, 3000, LocalDateTime.of(2020, Month.JUNE, 15, 8,0 )));
      employees.put(3L, new EmployeeDto(3L, "Paszkál", "Kis", Position.HR_MANAGER,2000, LocalDateTime.of(2023, Month.JANUARY, 5, 8,0 )));
      employees.put(4L, new EmployeeDto(4L, "Tibor", "Kezdő", Position.TESTER, 1000, LocalDateTime.of(2003, Month.JANUARY, 5, 8,0 )));
      employees.put(5L, new EmployeeDto(5L, "Kálmán", "Kóder", Position.DEVELOPER, 5000, LocalDateTime.of(2022, Month.JANUARY, 5, 8,0)));
      employees.put(6L, new EmployeeDto(6L, "Béla", "Adat", Position.ADMINISTRATOR, 900, LocalDateTime.of(2011, Month.JANUARY, 5, 8,0 )));
   }

   public EmployeeRestController(@Autowired MapService<Long> mapService) {
      this.mapService = mapService;
   }

   @GetMapping
   public ResponseEntity<List<EmployeeDto>> getAllEmployees(@RequestParam(value="minSalary", required = false) Optional<Integer> minSalaryOptional) {
//      if (employees.isEmpty()) {
//         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//      } else
         if (minSalaryOptional.isPresent()){
         int minSalary = minSalaryOptional.get();
         List<EmployeeDto> resultList = employees
               .values().stream()
               .filter(employeeDto -> employeeDto.getMonthlySalary() >= minSalary)
               .toList();
//         if (resultList.size()==0) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//         } else {
//            return new ResponseEntity<>(resultList, HttpStatus.OK);
//         }
         return new ResponseEntity<>(resultList, HttpStatus.OK);
      } else {
         return new ResponseEntity<>(employees.values().stream().toList(),HttpStatus.OK);
      }
   }

   @GetMapping("/{id}")
   public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
      EmployeeDto requestedEmployee = employees.get(id);
      if (requestedEmployee==null) {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } else {
         return new ResponseEntity<>(requestedEmployee,HttpStatus.OK);
      }
   }

   @PostMapping
   public ResponseEntity<EmployeeDto> addNewEmployee(@RequestBody EmployeeDto employeeDto) {
      if (employees.containsKey(employeeDto.getId()) || employeeDto.getId()<1) {
         Long newId = mapService.getFirstFreeKey(employees.keySet());
         employeeDto.setId(newId);
      }
      employees.put(employeeDto.getId(), employeeDto);
      return new ResponseEntity<>(employeeDto, HttpStatus.OK);
   }

   @PutMapping("/{id}")
   public ResponseEntity<EmployeeDto> updateEmployeeById(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
      if (!employees.containsKey(id)) {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } else {
         employeeDto.setId(id);
         employees.put(id, employeeDto);
         return new ResponseEntity<>(employeeDto, HttpStatus.OK);
      }
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id) {
      if (!employees.containsKey(id)) {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      } else {
         employees.remove(id);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
   }

   @DeleteMapping
   public ResponseEntity<Void> deleteAllEmployees() {
      employees.clear();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }
}