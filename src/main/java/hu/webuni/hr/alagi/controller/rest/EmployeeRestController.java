package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.exception.EntityAlreadyExistsWithGivenIdException;
import hu.webuni.hr.alagi.exception.EntityNotExistsWithGivenIdException;
import hu.webuni.hr.alagi.model.Employee;
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

   private final EmployeeService employeeService;
   private final EmployeeMapper employeeMapper;

   @Autowired
   public EmployeeRestController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
      this.employeeService = employeeService;
      this.employeeMapper = employeeMapper;
   }

   @GetMapping
   public ResponseEntity<List<EmployeeDto>> getAllEmployees(@RequestParam(value="minSalary", required = false) Optional<Integer> minSalaryOptional) {
      if (minSalaryOptional.isPresent()){
         int minSalary = minSalaryOptional.get();
         List<Employee> employeeList = employeeService.getAllEmployeesUsingMinSalary(minSalary);
         return new ResponseEntity<>(employeeMapper.employeesToDtos(employeeList), HttpStatus.OK);
      } else {
         return new ResponseEntity<>(employeeMapper.employeesToDtos(employeeService.getAllEmployees()),HttpStatus.OK);
      }
   }

   @GetMapping("/{id}")
   public EmployeeDto getEmployeeById(@PathVariable Long id) {
      Employee requestedEmployee = employeeService.getEmployeeById(id);
      if (requestedEmployee==null) {
         throw new EntityNotExistsWithGivenIdException(id, Employee.class);
      } else {
         return employeeMapper.employeeToDto(requestedEmployee);
      }
   }

   @PostMapping
   public EmployeeDto addNewEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
      Employee savedEmployee = employeeService.createEmployee(employeeMapper.dtoToEmployee(employeeDto));
      if (savedEmployee==null) {
         throw new EntityAlreadyExistsWithGivenIdException(employeeDto.getId(), Employee.class);
      }
      return employeeMapper.employeeToDto(savedEmployee);
   }

   @PutMapping("/{id}")
   public EmployeeDto updateEmployeeById(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
      if (employeeService.isEmployeeExistsById(id)) {
         Employee modifiedEmployee = employeeMapper.dtoToEmployee(employeeDto);
         modifiedEmployee.setId(id);
         modifiedEmployee = employeeService.updateEmployee(modifiedEmployee);
         return employeeMapper.employeeToDto(modifiedEmployee);
      } else {
         throw new EntityNotExistsWithGivenIdException(id, Employee.class);
      }
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id) {
      if (!employeeService.isEmployeeExistsById(id)) {
         throw new EntityNotExistsWithGivenIdException(id, Employee.class);
      } else {
         employeeService.deleteEmployee(id);
         return ResponseEntity.noContent().build();
      }
   }

   @GetMapping("/raise-percentage")
   public ResponseEntity<Integer> getSalaryRaisePercentForEmployee(@RequestBody EmployeeDto employeeDto) {
      return ResponseEntity.ok(employeeService.getPayRaisePercent(employeeMapper.dtoToEmployee(employeeDto)));
   }
}