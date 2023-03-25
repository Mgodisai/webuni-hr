package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CompanyService {
   CompanyRepository companyRepository;

   @Autowired
   public CompanyService(CompanyRepository companyRepository) {
      this.companyRepository = companyRepository;
   }
   public List<CompanyDto> getAllCompanies(boolean includeEmployeeList) {
      if (!includeEmployeeList) {
         List<CompanyDto> list = companyRepository.findAll();
         return list
               .stream()
               .map(c->new CompanyDto(c.getId(), c.getRegisterNumber(), c.getName(), c.getAddress(), Collections.emptyList()))
               .toList();
      }
      return companyRepository.findAll();
   }

   public CompanyDto getCompanyById(Long id, boolean includeEmployeeList) {
      CompanyDto origCompanyDto = companyRepository.findById(id);
      if (origCompanyDto==null) {
         return null;
      }
      if (!includeEmployeeList) {
         return new CompanyDto(
               origCompanyDto.getId(),
               origCompanyDto.getRegisterNumber(),
               origCompanyDto.getName(),
               origCompanyDto.getAddress(),
               Collections.emptyList());
      }
      return origCompanyDto;
   }

   public CompanyDto createCompany(CompanyDto company) {
      if (companyRepository.findById(company.getId())!=null) {
         return null;
      }
      return companyRepository.save(company);
   }

   public boolean isCompanyExistsById(Long id) {
      return companyRepository.findById(id)!=null;
   }

   public CompanyDto updateCompany(CompanyDto company) {
      return companyRepository.save(company);
   }

   public void deleteCompany(Long id) {
      companyRepository.deleteById(id);
   }

   public CompanyDto addEmployeeToCompany(Long companyId, EmployeeDto employee) {
      return companyRepository.addEmployee(companyId, employee);
   }

   public CompanyDto removeEmployeeByIdFromCompany(Long companyId, Long employeeId) {
      return companyRepository.removeEmployee(companyId, employeeId);
   }

   public CompanyDto changeEmployeeListOfCompany(Long companyId, List<EmployeeDto> employeeDtoList) {
      return companyRepository.addNewEmployeeList(companyId, employeeDtoList);
   }

}
