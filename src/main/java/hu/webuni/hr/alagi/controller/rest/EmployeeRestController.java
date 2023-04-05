package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.exception.EntityAlreadyExistsException;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.exception.EntityNotExistsWithGivenIdException;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.service.EmployeeCrudService;
import hu.webuni.hr.alagi.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

   private final EmployeeCrudService employeeCrudService;
   private final EmployeeService employeeService;
   private final EmployeeMapper employeeMapper;

   @Autowired
   public EmployeeRestController(EmployeeCrudService employeeCrudService, EmployeeService employeeService, EmployeeMapper employeeMapper) {
      this.employeeCrudService = employeeCrudService;
      this.employeeService = employeeService;
      this.employeeMapper = employeeMapper;
   }

   @GetMapping
   public ResponseEntity<List<EmployeeDto>> getAllEmployees(@RequestParam(value="minSalary", required = false) Optional<Integer> minSalaryOptional) {
      if (minSalaryOptional.isPresent()){
         int minSalary = minSalaryOptional.get();
         List<Employee> employeeList = employeeCrudService.getAllEmployeesUsingMinSalary(minSalary);
         return new ResponseEntity<>(employeeMapper.employeesToDtos(employeeList), HttpStatus.OK);
      } else {
         return new ResponseEntity<>(employeeMapper.employeesToDtos(employeeCrudService.getAllEmployees()),HttpStatus.OK);
      }
   }

   @GetMapping("/{id}")
   public EmployeeDto getEmployeeById(@PathVariable Long id) {
      Employee requestedEmployee = employeeCrudService.getEmployeeById(id);
      if (requestedEmployee==null) {
         throw new EntityNotExistsWithGivenIdException(id, Employee.class);
      } else {
         return employeeMapper.employeeToDto(requestedEmployee);
      }
   }

   @PostMapping
   public EmployeeDto addNewEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
      Employee savedEmployee = employeeCrudService.createEmployee(employeeMapper.dtoToEmployee(employeeDto));
      if (savedEmployee==null) {
         throw new EntityAlreadyExistsException(employeeDto.getId(), Employee.class);
      }
      return employeeMapper.employeeToDto(savedEmployee);
   }

   @PutMapping("/{id}")
   public EmployeeDto updateEmployeeById(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
      if (employeeCrudService.isEmployeeExistsById(id)) {
         Employee modifiedEmployee = employeeMapper.dtoToEmployee(employeeDto);
         modifiedEmployee.setId(id);
         modifiedEmployee = employeeCrudService.updateEmployee(modifiedEmployee);
         return employeeMapper.employeeToDto(modifiedEmployee);
      } else {
         throw new EntityNotExistsWithGivenIdException(id, Employee.class);
      }
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id) {
      if (!employeeCrudService.isEmployeeExistsById(id)) {
         throw new EntityNotExistsWithGivenIdException(id, Employee.class);
      } else {
         employeeCrudService.deleteEmployee(id);
         return ResponseEntity.noContent().build();
      }
   }

   @GetMapping("/raise-percentage")
   public ResponseEntity<Integer> getSalaryRaisePercentForEmployee(@RequestBody EmployeeDto employeeDto) {
      return ResponseEntity.ok(employeeService.getPayRaisePercent(employeeMapper.dtoToEmployee(employeeDto)));
   }
}