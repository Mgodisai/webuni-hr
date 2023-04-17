package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
   private final CompanyRepository companyRepository;
   private final EmployeeService employeeService;

   @Autowired
   public CompanyService(CompanyRepository companyRepository, EmployeeService employeeService) {
      this.companyRepository = companyRepository;
      this.employeeService = employeeService;
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
      Company company = companyRepository.findById(id).orElse(null);
      if (company==null) {
         return null;
      }
      if (!includeEmployeeList) {
         return new Company(
               company.getId(),
               company.getRegisterNumber(),
               company.getName(),
               company.getAddress(),
               Collections.emptyList());
      }
      return company;
   }

   public Optional<Company> createCompany(Company company) {
      if (company.getId()!=null && isCompanyExistedByGivenId(company.getId())) {
         return Optional.empty();
      } else {
         return Optional.of(companyRepository.save(company));
      }
   }

   public boolean isCompanyExistedByGivenId(Long id) {
      return companyRepository.existsById(id);
   }

   public Optional<Company> updateCompany(Company company) {
      if (company.getId()==null || !isCompanyExistedByGivenId(company.getId())) {
         return Optional.empty();
      } else {
         return Optional.of(companyRepository.save(company));
      }
   }

   public void deleteCompany(Long id) {
      companyRepository.deleteById(id);
   }

   public Optional<Company> addEmployeesToCompany(Long companyId, List<Employee> employeeList) {
      Optional<Company> company = companyRepository.findById(companyId);
      if (company.isPresent()) {
         for (Employee e : employeeList) {
            e.setCompany(company.get());
            employeeService.createEmployee(e);
         }
      }
      return company;
   }

   public List<Company> findByEmployeeWithSalaryGreaterThan(int salary) {
      return companyRepository.findByEmployeeWithSalaryGreaterThan(salary);
   }

   public List<Company> findByNumberOfEmployeesGreaterThan(int limit) {
      return companyRepository.findByNumberOfEmployeesGreaterThan(limit);
   }

//   public Company removeEmployeeByIdFromCompany(Long companyId, Long employeeId) {
//      Company company = companyRepository.findById(companyId);
//      if (company == null) {
//         return null;
//      }
//      company.getEmployeeList().remove(employeeId.intValue());
//      return company;
//   }
//
//   public Company changeEmployeeListOfCompany(Long companyId, List<Employee> employeeList) {
//      Company company = companyRepository.findById(companyId);
//      if (company == null) {
//         return null;
//      }
//      company.setEmployeeList(employeeList);
//      return company;
//   }
//
//   public boolean isEmployeeExistsInCompany(Long companyId, Long employeeId) {
//      Company company = companyRepository.findById(companyId);
//      Employee employee;
//      try {
//         employee = company.getEmployeeList().get(employeeId.intValue());
//      } catch (IndexOutOfBoundsException e) {
//         return false;
//      }
//      return employee != null;
//   }
}
