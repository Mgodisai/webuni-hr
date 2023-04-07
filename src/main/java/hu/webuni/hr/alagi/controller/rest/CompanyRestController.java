package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.exception.EntityAlreadyExistsException;
import hu.webuni.hr.alagi.exception.EntityNotExistsWithGivenIdException;
import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
public class CompanyRestController {
   private final CompanyService companyService;
   private final CompanyMapper companyMapper;
   private final EmployeeMapper employeeMapper;

   @Autowired
   public CompanyRestController(CompanyService companyService, CompanyMapper companyMapper, EmployeeMapper employeeMapper) {
      this.companyService = companyService;
      this.companyMapper = companyMapper;
      this.employeeMapper = employeeMapper;
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
      Company savedCompany = companyService.createCompany(companyMapper.dtoToCompany(companyDto));
      if (savedCompany==null) {
         throw new EntityAlreadyExistsException(companyDto.getId(), Company.class);
      }
      return companyMapper.companyToDto(savedCompany);
   }

   @PutMapping("/{companyId}")
   public CompanyDto updateCompanyById(@PathVariable Long companyId, @RequestBody CompanyDto companyDto) {
      if (companyService.isCompanyExistsById(companyId)) {
         companyDto.setId(companyId);
         Company updatedCompany = companyService.updateCompany(companyMapper.dtoToCompany(companyDto));
         return companyMapper.companyToDto(updatedCompany);
      } else {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      }
   }

   @DeleteMapping("/{companyId}")
   public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long companyId) {
      if (companyService.isCompanyExistsById(companyId)) {
         companyService.deleteCompany(companyId);
         return ResponseEntity.noContent().build();
      } else {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      }
   }

   @PostMapping("/{companyId}/employees")
   public CompanyDto addNewEmployeeToCompany(@PathVariable Long companyId, @RequestBody @Valid EmployeeDto newEmployee) {
      if (!companyService.isCompanyExistsById(companyId)) {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      }
      Company updatedCompany = companyService.addEmployeeToCompany(companyId, employeeMapper.dtoToEmployee(newEmployee));
      return companyMapper.companyToDto(updatedCompany);
   }

   @DeleteMapping("/{companyId}/employees/{employeeId}")
   public CompanyDto removeEmployeeToCompanyByEmployeeId(
         @PathVariable Long companyId,
         @PathVariable Long employeeId) {
      if (!companyService.isCompanyExistsById(companyId)) {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      }
      if (!companyService.isEmployeeExistsInCompany(companyId, employeeId)) {
         throw new EntityNotExistsWithGivenIdException(employeeId, Employee.class);
      }
      Company updatingCompany = companyService.removeEmployeeByIdFromCompany(companyId, employeeId);
      return companyMapper.companyToDto(updatingCompany);
   }

   @PutMapping("/{companyId}/employees")
   public CompanyDto removeEmployeeListOfCompany(
         @PathVariable Long companyId,
         @RequestBody @Valid List<Employee> newEmployeeList) {
      if (!companyService.isCompanyExistsById(companyId)) {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      }
      Company updatedCompany = companyService.changeEmployeeListOfCompany(companyId, newEmployeeList);
      return companyMapper.companyToDto(updatedCompany);
   }
}
