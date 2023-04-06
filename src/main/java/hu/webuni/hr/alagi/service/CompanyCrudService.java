package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.repository.CompanyRepository;
import hu.webuni.hr.alagi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CompanyCrudService {
   private final CompanyRepository companyRepository;
   private final EmployeeRepository employeeRepository;

   @Autowired
   public CompanyCrudService(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
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
      for (Employee e : company.getEmployeeList()) {
         employeeRepository.save(e);
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
      Employee savedEmployee = employeeRepository.save(employee);
      company.getEmployeeList().add(savedEmployee);
      return company;
   }

   public Company removeEmployeeByIdFromCompany(Long companyId, Long employeeId) {
      Company company = companyRepository.findById(companyId);
      if (company == null) {
         return null;
      }
      Employee deletingEmployee = employeeRepository.findById(employeeId);
      employeeRepository.delete(deletingEmployee);
      company.getEmployeeList().remove(deletingEmployee);
      return company;
   }

   public Company changeEmployeeListOfCompany(Long companyId, List<Employee> employeeList) {
      Company company = companyRepository.findById(companyId);
      if (company == null) {
         return null;
      }
      company.setEmployeeList(employeeList);
      for (Employee e : employeeList) {
         employeeRepository.save(e);
      }
      return company;
   }
}
