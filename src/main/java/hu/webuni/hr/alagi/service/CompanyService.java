package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CompanyService {
   private final CompanyRepository companyRepository;

   @Autowired
   public CompanyService(CompanyRepository companyRepository) {
      this.companyRepository = companyRepository;
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

   public Company addEmployeeToCompany(Long companyId, Employee employee) {
      Company company = companyRepository.findById(companyId);
      if (company == null) {
         return null;
      }
      company.getEmployeeList().add(employee);
      return company;
   }

   public Company removeEmployeeByIdFromCompany(Long companyId, Long employeeId) {
      Company company = companyRepository.findById(companyId);
      if (company == null) {
         return null;
      }
      company.getEmployeeList().remove(employeeId.intValue());
      return company;
   }

   public Company changeEmployeeListOfCompany(Long companyId, List<Employee> employeeList) {
      Company company = companyRepository.findById(companyId);
      if (company == null) {
         return null;
      }
      company.setEmployeeList(employeeList);
      return company;
   }

   public boolean isEmployeeExistsInCompany(Long companyId, Long employeeId) {
      Company company = companyRepository.findById(companyId);
      Employee employee;
      try {
         employee = company.getEmployeeList().get(employeeId.intValue());
      } catch (IndexOutOfBoundsException e) {
         return false;
      }
      return employee != null;
   }
}
