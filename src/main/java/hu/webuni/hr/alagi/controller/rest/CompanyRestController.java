package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
public class CompanyRestController {
   CompanyService companyService;

   @Autowired
   public CompanyRestController(CompanyService companyService) {
      this.companyService = companyService;
   }

   @GetMapping
   public ResponseEntity<List<CompanyDto>> getAllCompanies(@RequestParam(value="full", required = false) Optional<Boolean> includeEmployeeList) {
      return ResponseEntity.ok(companyService.getAllCompanies(includeEmployeeList.orElse(false)));
   }

   @GetMapping("/{companyId}")
   public ResponseEntity<CompanyDto> getCompanyById(
         @PathVariable Long companyId,
         @RequestParam(value="full", required = false) Optional<Boolean> includeEmployeeList) {
      CompanyDto requestedCompany= companyService.getCompanyById(companyId, includeEmployeeList.orElse(false));
      if (requestedCompany==null) {
         return ResponseEntity.notFound().build();
      } else {
         return ResponseEntity.ok(requestedCompany);
      }
   }

   @PostMapping
   public ResponseEntity<?> addNewCompany(@RequestBody CompanyDto companyDto) {
      CompanyDto newCompany = companyService.createCompany(companyDto);
      if (newCompany==null) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("company with the given ID ("+companyDto.getId()+") already exists");
      }
      return ResponseEntity.ok(newCompany);
   }

   @PutMapping("/{companyId}")
   public ResponseEntity<CompanyDto> updateCompanyById(@PathVariable Long companyId, @RequestBody CompanyDto companyDto) {
      if (companyService.isCompanyExistsById(companyId)) {
         companyDto.setId(companyId);
         CompanyDto updatedCompany = companyService.updateCompany(companyDto);
         return ResponseEntity.ok(updatedCompany);
      } else {
         return ResponseEntity.notFound().build();
      }
   }

   @DeleteMapping("/{companyId}")
   public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long companyId) {
      if (companyService.isCompanyExistsById(companyId)) {
         companyService.deleteCompany(companyId);
         return ResponseEntity.noContent().build();
      } else {
         return ResponseEntity.notFound().build();
      }
   }

   @PostMapping("/{companyId}/employees")
   public ResponseEntity<CompanyDto> addNewEmployeeToCompany(@PathVariable Long companyId, @RequestBody EmployeeDto newEmployee) {
         CompanyDto updatedCompany = companyService.addEmployeeToCompany(companyId, newEmployee);
      if (updatedCompany!=null) {
         return ResponseEntity.ok(updatedCompany);
      } else {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
   }

   @DeleteMapping("/{companyId}/employees/{employeeId}")
   public ResponseEntity<CompanyDto> removeEmployeeToCompanyByEmployeeId(
         @PathVariable Long companyId,
         @PathVariable Long employeeId) {
      CompanyDto updatedCompany = companyService.removeEmployeeByIdFromCompany(companyId, employeeId);
      if (updatedCompany!=null) {
         return ResponseEntity.ok(updatedCompany);
      } else {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
   }

   @PutMapping("/{companyId}/employees")
   public ResponseEntity<CompanyDto> removeEmployeeListOfCompany(
         @PathVariable Long companyId,
         @RequestBody List<EmployeeDto> newEmployeeList) {
      CompanyDto updatedCompany = companyService.changeEmployeeListOfCompany(companyId, newEmployeeList);
      if (updatedCompany!=null) {
         return ResponseEntity.ok(updatedCompany);
      } else {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
   }
}
