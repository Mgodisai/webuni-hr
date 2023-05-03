package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.exception.EntityAlreadyExistsWithGivenIdException;
import hu.webuni.hr.alagi.exception.EntityNotExistsWithGivenIdException;
import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.service.CompanyService;
import hu.webuni.hr.alagi.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

   // GET MAPPINGS
   @GetMapping
   public List<CompanyDto> getAllCompanies(@RequestParam(value="full", required = false) Optional<Boolean> includeEmployeeList) {
      List<Company> companyList = companyService.getAllCompanies(includeEmployeeList.orElse(false));

      return includeEmployeeList.orElse(false)
            ? companyMapper.companiesToDtos(companyList)
            : companyMapper.companiesToSummaryDtos(companyList);
   }

   @GetMapping("/{companyId}")
   public CompanyDto getCompanyById(
         @PathVariable Long companyId,
         @RequestParam(value="full", required = false) Optional<Boolean> includeEmployeeList) {

      Company requestedCompany= companyService.getCompanyById(companyId, includeEmployeeList.orElse(false));
      if (requestedCompany==null) {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      } else {
         return includeEmployeeList.orElse(false)
               ? companyMapper.companyToDto(requestedCompany)
               : companyMapper.companyToSummaryDto(requestedCompany);
      }
   }

   @GetMapping(params="aboveSalary")
   public List<CompanyDto> getCompaniesAboveSalary(
         @RequestParam(value="full", required = false) Optional<Boolean> includeEmployeeList,
         @RequestParam Integer aboveSalary
   ) {
      List<Company> filteredCompanies =
            companyService.findCompaniesByEmployeeWithSalaryGreaterThan(aboveSalary, includeEmployeeList.orElse(false));
      return includeEmployeeList.orElse(false)
            ? companyMapper.companiesToDtos(filteredCompanies)
            : companyMapper.companiesToSummaryDtos(filteredCompanies);
   }

   @GetMapping(params="aboveEmployeeCount")
   public List<CompanyDto> getCompaniesWithEmployeeSalaryGreaterThan(
         @RequestParam(value="full", required = false) Optional<Boolean> includeEmployeeList,
         @RequestParam Integer aboveEmployeeCount
   ) {
      List<Company> filteredCompanies =
            companyService.findByNumberOfEmployeesGreaterThan(aboveEmployeeCount, includeEmployeeList.orElse(false));
      return includeEmployeeList.orElse(false)
            ? companyMapper.companiesToDtos(filteredCompanies)
            : companyMapper.companiesToSummaryDtos(filteredCompanies);
   }

   @GetMapping("/{companyId}/avgSalariesByPosition")
   public Map<String, Double> getCompaniesWithEmployeeSalaryGreaterThan(
         @PathVariable Long companyId
   ) {
      return employeeService.getAvgSalariesByPositionUsingCompanyId(companyId);
   }

   // POST-PUT-DELETE MAPPINGS
   @PostMapping
   public CompanyDto addNewCompany(@RequestBody CompanyDto companyDto) {
      Company savedCompany = companyService.createCompany(companyMapper.dtoToCompany(companyDto))
            .orElseThrow(()->new EntityAlreadyExistsWithGivenIdException(companyDto.getId(), Company.class));
      return companyMapper.companyToDto(savedCompany);
   }

   @PutMapping("/{companyId}")
   public CompanyDto updateCompanyById(@PathVariable Long companyId, @RequestBody CompanyDto companyDto) {
      Company modifyingCompanyBefore = companyMapper.dtoToCompany(companyDto);
      modifyingCompanyBefore.setId(companyId);

      Company modifyingCompanyAfter = companyService.updateCompany(modifyingCompanyBefore)
            .orElseThrow(()-> new EntityNotExistsWithGivenIdException(companyId, Company.class));
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

   // Handling employees
   @PostMapping("/{companyId}/employees")
   public CompanyDto addNewEmployeesToCompany(@PathVariable Long companyId, @RequestBody @Valid List<EmployeeDto> newEmployeeDtos) {
      Optional<Company> updatedCompany =
            companyService.addEmployeesToCompany(companyId, employeeMapper.dtosToEmployees(newEmployeeDtos));
      return companyMapper.companyToDto(updatedCompany
            .orElseThrow(()->new EntityNotExistsWithGivenIdException(companyId, Company.class)));
   }

   @DeleteMapping("/{companyId}/employees/{employeeId}")
   public CompanyDto removeEmployeeFromCompanyByEmployeeId(
         @PathVariable Long companyId,
         @PathVariable Long employeeId) {
      return companyMapper.companyToDto(companyService.removeEmployeeByIdFromCompany(companyId, employeeId));
   }

   @PutMapping("/{companyId}/employees")
   public CompanyDto updateEmployeesOfCompany(
         @PathVariable Long companyId,
         @RequestBody @Valid List<EmployeeDto> updatingEmployeeDtoList) {
      Company company =
            companyService.replaceEmployeeList(companyId,employeeMapper.dtosToEmployees(updatingEmployeeDtoList));
      if (company==null) {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      }
      return companyMapper.companyToDto(company);
   }
}
