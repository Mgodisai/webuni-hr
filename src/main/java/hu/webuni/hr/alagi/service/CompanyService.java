package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.repository.CompanyRepository;
import hu.webuni.hr.alagi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CompanyService {
   private final CompanyRepository companyRepository;
   private final EmployeeRepository employeeRepository;

   public CompanyService(@Autowired CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
      this.companyRepository = companyRepository;
      this.employeeRepository = employeeRepository;
   }

   public List<Company> getAllCompanies(boolean includeEmployeeList) {
      if (!includeEmployeeList) {
         List<Company> list = companyRepository.findAll();
         return list
               .stream()
               .map(c->new Company(c.getId(), c.getRegisterNumber(), c.getName(), c.getAddress(), Collections.emptyList()))
               .toList();
      }
      return companyRepository.findAll();
   }

   public Company getCompanyById(Long id, boolean includeEmployeeList) {
      Company origCompanyDto = companyRepository.findById(id);
      if (origCompanyDto==null) {
         return null;
      }
      if (!includeEmployeeList) {
         return new Company(
               origCompanyDto.getId(),
               origCompanyDto.getRegisterNumber(),
               origCompanyDto.getName(),
               origCompanyDto.getAddress(),
               Collections.emptyList());
      }
      return origCompanyDto;
   }

   public Company createCompany(Company company) {
      if (companyRepository.findById(company.getId())!=null) {
         return null;
      }

      return companyRepository.save(company);
   }

   public boolean isCompanyExistsById(Long id) {
      return companyRepository.findById(id)!=null;
   }

   public Company updateCompany(Company company) {
      return companyRepository.save(company);
   }

   public void deleteCompany(Long id) {
      companyRepository.deleteById(id);
   }

   public CompanyDto addEmployeeToCompany(Long companyId, Employee employee) {
      return companyRepository.addEmployee(companyId, employee);
   }

   public CompanyDto removeEmployeeByIdFromCompany(Long companyId, Long employeeId) {
      return companyRepository.removeEmployee(companyId, employeeId);
   }

   public CompanyDto changeEmployeeListOfCompany(Long companyId, List<EmployeeDto> employeeDtoList) {
      return companyRepository.addNewEmployeeList(companyId, employeeDtoList);
   }



}
