package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.exception.EmployeeNotBelongsToTheGivenCompanyException;
import hu.webuni.hr.alagi.exception.EntityAlreadyExistsWithGivenIdException;
import hu.webuni.hr.alagi.exception.EntityNotExistsWithGivenIdException;
import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.service.CompanyService;
import hu.webuni.hr.alagi.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
public class CompanyRestController {
   private final CompanyService companyService;
   private final CompanyMapper companyMapper;
   private final EmployeeMapper employeeMapper;
   private final EmployeeService employeeService;

   @Autowired
   public CompanyRestController(CompanyService companyService, CompanyMapper companyMapper, EmployeeMapper employeeMapper, EmployeeService employeeService) {
      this.companyService = companyService;
      this.companyMapper = companyMapper;
      this.employeeMapper = employeeMapper;
      this.employeeService = employeeService;
   }

   @GetMapping
   public List<CompanyDto> getAllCompanies(@RequestParam(value="full", required = false) Optional<Boolean> includeEmployeeList) {
      List<Company> companyList = companyService.getAllCompanies(includeEmployeeList.orElse(false));

      return companyMapper.companiesToDtos(companyList);
   }

   @GetMapping("/{companyId}")
   public CompanyDto getCompanyById(
         @PathVariable Long companyId,
         @RequestParam(value="full", required = false) Optional<Boolean> includeEmployeeList) {

      Company requestedCompany= companyService.getCompanyById(companyId, includeEmployeeList.orElse(false));
      if (requestedCompany==null) {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      } else {
         return companyMapper.companyToDto(requestedCompany);
      }
   }

   @PostMapping
   public CompanyDto addNewCompany(@RequestBody CompanyDto companyDto) {
      Company savedCompany = companyService.createCompany(companyMapper.dtoToCompany(companyDto))
            .orElseThrow(()->new EntityAlreadyExistsWithGivenIdException(companyDto.getId(), Company.class));

      if (savedCompany.getEmployeeList() != null) {
         savedCompany.getEmployeeList().forEach(e -> {
            e.setCompany(savedCompany);
            employeeService.createEmployee(e);
         });
      }

      return companyMapper.companyToDto(savedCompany);
   }

   @PutMapping("/{companyId}")
   public CompanyDto updateCompanyById(@PathVariable Long companyId, @RequestBody CompanyDto companyDto) {
      Company modifyingCompanyBefore = companyMapper.dtoToCompany(companyDto);
      modifyingCompanyBefore.setId(companyId);

      Company modifyingCompanyAfter = companyService.updateCompany(companyMapper.dtoToCompany(companyDto))
            .orElseThrow(()-> new EntityNotExistsWithGivenIdException(companyId, Company.class));

      if (modifyingCompanyAfter.getEmployeeList() != null) {
         modifyingCompanyAfter.getEmployeeList().forEach(e->{
            e.setCompany(modifyingCompanyAfter);
            employeeService.updateEmployee(e);
         });
      }
      return companyMapper.companyToDto(modifyingCompanyAfter);
   }

   @DeleteMapping("/{companyId}")
   public ResponseEntity<Void> deleteCompanyById(@PathVariable Long companyId) {
      if (companyService.isCompanyExistedByGivenId(companyId)) {
         companyService.deleteCompany(companyId);
         return ResponseEntity.noContent().build();
      } else {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      }
   }

   @PostMapping("/{companyId}/employees")
   public CompanyDto addNewEmployeesToCompany(@PathVariable Long companyId, @RequestBody @Valid List<EmployeeDto> newEmployeeDtos) {
      Optional<Company> updatedCompany = companyService.addEmployeesToCompany(companyId, employeeMapper.dtosToEmployees(newEmployeeDtos));

      return companyMapper.companyToDto(updatedCompany.orElseThrow(()->new EntityNotExistsWithGivenIdException(companyId, Company.class)));
   }

   @DeleteMapping("/{companyId}/employees/{employeeId}")
   public CompanyDto removeEmployeeToCompanyByEmployeeId(
         @PathVariable Long companyId,
         @PathVariable Long employeeId) {
      if (!companyService.isCompanyExistedByGivenId(companyId)) {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      }
      Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
      if (employee.isEmpty()) {
         throw new EntityNotExistsWithGivenIdException(employeeId, Employee.class);
      }
      if (Objects.equals(employee.get().getCompany().getId(), companyId)) {
         employee.get().setCompany(null);
         employeeService.deleteEmployee(employeeId);
      } else {
         throw new EmployeeNotBelongsToTheGivenCompanyException(employeeId, companyId);
      }
      return companyMapper.companyToDto(companyService.getCompanyById(companyId, true));
   }

   @PutMapping("/{companyId}/employees")
   public CompanyDto updateEmployeesOfCompany(
         @PathVariable Long companyId,
         @RequestBody @Valid List<EmployeeDto> updatingEmployeeDtoList) {
      if (!companyService.isCompanyExistedByGivenId(companyId)) {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      }
      List<Employee> updatingEmployeeList = employeeMapper.dtosToEmployees(updatingEmployeeDtoList);
      Company company = companyService.getCompanyById(companyId, false);

      if (updatingEmployeeList != null) {
         updatingEmployeeList.forEach(e->{
            e.setCompany(company);
            employeeService.updateEmployee(e);
         });
      }

      return companyMapper.companyToDto(companyService.getCompanyById(companyId, true));
   }

   @GetMapping(params="aboveSalary")
   public List<CompanyDto> getCompaniesAboveSalary(
           @RequestParam Optional<Boolean> full,
           @RequestParam Integer aboveSalary
   ) {
      List<Company> filteredCompanies = companyService.findByEmployeeWithSalaryGreaterThan(aboveSalary, full.orElse(false));
      return companyMapper.companiesToDtos(filteredCompanies);
   }

   @GetMapping(params="aboveEmployeeCount")
   public List<CompanyDto> getCompaniesWithEmployeeSalaryGreaterThan(
           @RequestParam Optional<Boolean> full,
           @RequestParam Integer aboveEmployeeCount
   ) {
      List<Company> filteredCompanies = companyService.findByNumberOfEmployeesGreaterThan(aboveEmployeeCount, full.orElse(false));
      return companyMapper.companiesToDtos(filteredCompanies);
   }

   @GetMapping("/{companyId}/avgSalariesByPosition")
   public Map<String, Double> getCompaniesWithEmployeeSalaryGreaterThan(
           @PathVariable Long companyId
   ) {
      return employeeService.getAvgSalariesByPositionUsingCompanyId(companyId);
   }
}
