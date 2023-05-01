package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.exception.EmployeeNotBelongsToTheGivenCompanyException;
import hu.webuni.hr.alagi.exception.EntityNotExistsWithGivenIdException;
import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
      return includeEmployeeList
            ? companyRepository.findAllWithEmployees()
            : companyRepository.findAll();
   }

   public Company getCompanyById(Long id, boolean includeEmployeeList) {
      Optional<Company> company = includeEmployeeList
            ? companyRepository.findCompanyByIdWithEmployees(id)
            : companyRepository.findById(id);
      return company.orElse(null);
   }

   public List<Company> findCompaniesByEmployeeWithSalaryGreaterThan(int salary, boolean includeEmployeeList) {
      return companyRepository.findByEmployeeWithSalaryGreaterThan(salary);
   }

   public List<Company> findByNumberOfEmployeesGreaterThan(int limit, boolean includeEmployeeList) {
      return companyRepository.findByNumberOfEmployeesGreaterThan(limit);
   }


   @Transactional
   public Optional<Company> createCompany(Company company) {
      if (company.getId()!=null && isCompanyExistedByGivenId(company.getId())) {
         return Optional.empty();
      } else {
         List<Employee> employeeList = company.getEmployeeList();
         company.setEmployeeList(null);
         Company savedCompany = companyRepository.save(company);

         if (employeeList != null) {
            employeeList.forEach(e -> {
               e.setCompany(savedCompany);
               employeeService.createEmployee(e);
            });
         }
         return Optional.of(companyRepository.save(savedCompany));
      }
   }

   public boolean isCompanyExistedByGivenId(Long id) {
      return companyRepository.existsById(id);
   }

   @Transactional
   public Optional<Company> updateCompany(Company company) {
      if (company.getId()==null || !isCompanyExistedByGivenId(company.getId())) {
         return Optional.empty();
      } else {
         return Optional.of(companyRepository.save(company));
      }
   }

   @Transactional
   public void deleteCompany(Long id) {
      companyRepository.deleteById(id);
   }

   @Transactional
   public Optional<Company> addEmployeesToCompany(Long companyId, List<Employee> employeeList) {
      Optional<Company> company = companyRepository.findCompanyByIdWithEmployees(companyId);
      if (company.isPresent()) {
         for (Employee e : employeeList) {
            e.setCompany(company.get());
            employeeService.createEmployee(e);
            company.get().getEmployeeList().add(e);
         }
         companyRepository.save(company.get());
         return company;
      }
      return Optional.empty();
   }

   @Transactional
   public Company removeEmployeeByIdFromCompany(Long companyId, Long employeeId) {
      Optional<Company> company = companyRepository.findCompanyByIdWithEmployees(companyId);
      if (company.isEmpty()) {
         throw new EntityNotExistsWithGivenIdException(companyId, Company.class);
      }
      Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
      if (employee.isEmpty()) {
         throw new EntityNotExistsWithGivenIdException(employeeId, Employee.class);
      }
      if (Objects.equals(employee.get().getCompany().getId(), companyId)) {
         employee.get().setCompany(null);
         employeeService.updateEmployee(employee.get());
         company.get().getEmployeeList().remove(employee.get());
      } else {
         throw new EmployeeNotBelongsToTheGivenCompanyException(employeeId, companyId);
      }
      return company.get();
   }

   @Transactional
   public Company replaceEmployeeList(Long companyId, List<Employee> employees) {
      Optional<Company> company = companyRepository.findCompanyByIdWithEmployees(companyId);
      if (company.isEmpty()) {
         return null;
      } else {
         Company existingCompany = company.get();
         for (Employee emp : existingCompany.getEmployeeList()) {
            emp.setCompany(null);
            employeeService.updateEmployee(emp);
         }
         existingCompany.getEmployeeList().clear();
         for (Employee emp : employees) {
            existingCompany.getEmployeeList().add(emp);
            emp.setCompany(existingCompany);
            employeeService.createEmployee(emp);
         }
         return companyRepository.save(existingCompany);
      }

   }
}
