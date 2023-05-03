package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.exception.EntityAlreadyExistsWithGivenIdException;
import hu.webuni.hr.alagi.exception.EntityNotExistsWithGivenIdException;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;
import hu.webuni.hr.alagi.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
   public List<EmployeeDto> getAllEmployees() {
      return employeeMapper.employeesToDtos(employeeService.getAllEmployees());
   }

   @PostMapping("/search")
   public List<EmployeeDto> searchEmployees(@RequestBody EmployeeDto exampleDto, Pageable pageable) {
      Employee example = employeeMapper.dtoToEmployee(exampleDto);
      return employeeMapper.employeesToDtos(employeeService.findEmployeesByExample(example, pageable).toList());
   }

   @GetMapping("/{id}")
   public EmployeeDto getEmployeeById(@PathVariable Long id) {
      Employee requestedEmployee = employeeService.getEmployeeById(id)
            .orElseThrow(()->new EntityNotExistsWithGivenIdException(id, Employee.class));
      return employeeMapper.employeeToDto(requestedEmployee);
   }

   @PostMapping
   public EmployeeDto addNewEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
      Employee savedEmployee = employeeService.createEmployee(employeeMapper.dtoToEmployee(employeeDto))
            .orElseThrow(()->new EntityAlreadyExistsWithGivenIdException(employeeDto.getId(), Employee.class));
      return employeeMapper.employeeToDto(savedEmployee);
   }

   @PutMapping("/{id}")
   public EmployeeDto updateEmployeeById(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
      Employee modifyingEmployeeBefore = employeeMapper.dtoToEmployee(employeeDto);
      modifyingEmployeeBefore.setId(id);
      Employee modifiedEmployeeAfter = employeeService.updateEmployee(modifyingEmployeeBefore)
            .orElseThrow(()->new EntityNotExistsWithGivenIdException(id, Employee.class));
      return employeeMapper.employeeToDto(modifiedEmployeeAfter);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id) {
      if (!employeeService.isEmployeeExistedByGivenId(id)) {
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