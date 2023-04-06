package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.exception.EntityAlreadyExistsException;
import hu.webuni.hr.alagi.exception.EntityNotExistsWithGivenIdException;
import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.service.CompanyCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
public class CompanyRestController {
   private final CompanyCrudService companyCrudService;
   private final CompanyMapper companyMapper;

   @Autowired
   public CompanyRestController(CompanyCrudService companyCrudService, CompanyMapper companyMapper) {
      this.companyCrudService = companyCrudService;
      this.companyMapper = companyMapper;
   }

   @GetMapping
   public List<CompanyDto> getAllCompanies(@RequestParam(value="full", required = false) Optional<Boolean> includeEmployeeList) {
      List<Company> companyList = companyCrudService.getAllCompanies(includeEmployeeList.orElse(false));
      return companyMapper.companiesToDtos(companyList);
   }

   @GetMapping("/{companyId}")
   public CompanyDto getCompanyById(
         @PathVariable Long companyId,
         @RequestParam(value="full", required = false) Optional<Boolean> includeEmployeeList) {

      Company requestedCompany= companyCrudService.getCompanyById(companyId, includeEmployeeList.orElse(false));
      if (requestedCompany==null) {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      } else {
         return companyMapper.companyToDto(requestedCompany);
      }
   }

   @PostMapping
   public CompanyDto addNewCompany(@RequestBody CompanyDto companyDto) {
      Company savedCompany = companyCrudService.createCompany(companyMapper.dtoToCompany(companyDto));
      if (savedCompany==null) {
         throw new EntityAlreadyExistsException(companyDto.getId(), Company.class);
      }
      return companyMapper.companyToDto(savedCompany);
   }

//   @PutMapping("/{companyId}")
//   public ResponseEntity<CompanyDto> updateCompanyById(@PathVariable Long companyId, @RequestBody CompanyDto companyDto) {
//      if (companyService.isCompanyExistsById(companyId)) {
//         companyDto.setId(companyId);
//         CompanyDto updatedCompany = companyService.updateCompany(companyDto);
//         return ResponseEntity.ok(updatedCompany);
//      } else {
//         return ResponseEntity.notFound().build();
//      }
//   }
//
//   @DeleteMapping("/{companyId}")
//   public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long companyId) {
//      if (companyService.isCompanyExistsById(companyId)) {
//         companyService.deleteCompany(companyId);
//         return ResponseEntity.noContent().build();
//      } else {
//         return ResponseEntity.notFound().build();
//      }
//   }
//
//   @PostMapping("/{companyId}/employees")
//   public ResponseEntity<CompanyDto> addNewEmployeeToCompany(@PathVariable Long companyId, @RequestBody Employee newEmployee) {
//         Company updatedCompany = companyService.addEmployeeToCompany(companyId, newEmployee);
//      if (updatedCompany!=null) {
//         return ResponseEntity.ok(updatedCompany);
//      } else {
//         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//      }
//   }
//
//   @DeleteMapping("/{companyId}/employees/{employeeId}")
//   public ResponseEntity<CompanyDto> removeEmployeeToCompanyByEmployeeId(
//         @PathVariable Long companyId,
//         @PathVariable Long employeeId) {
//      Company updatedCompany = companyService.removeEmployeeByIdFromCompany(companyId, employeeId);
//      if (updatedCompany!=null) {
//         return ResponseEntity.ok(updatedCompany);
//      } else {
//         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//      }
//   }
//
//   @PutMapping("/{companyId}/employees")
//   public ResponseEntity<CompanyDto> removeEmployeeListOfCompany(
//         @PathVariable Long companyId,
//         @RequestBody List<Employee> newEmployeeList) {
//      Company updatedCompany = companyService.changeEmployeeListOfCompany(companyId, newEmployeeList);
//      if (updatedCompany!=null) {
//         return ResponseEntity.ok(updatedCompany);
//      } else {
//         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//      }
//   }
}
