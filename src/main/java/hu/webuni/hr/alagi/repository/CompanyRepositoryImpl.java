package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {
   private final Map<Long, Company> companyList = new HashMap<>();
   {
      ArrayList<Employee> defaultEmployeeList = new ArrayList<>(Arrays.asList(
            EmployeeRepositoryImpl.employee1,
            EmployeeRepositoryImpl.employee2,
            EmployeeRepositoryImpl.employee3,
            EmployeeRepositoryImpl.employee4,
            EmployeeRepositoryImpl.employee5,
            EmployeeRepositoryImpl.employee6
      ));
      companyList.put(1L, new Company(1L, "14-02-123456", "Kerék Kft", "7400 Kaposvár, Fő u. 12.", defaultEmployeeList));
   }

   @Override
   public List<Company> findAll() {
      return companyList.values().stream().toList();
   }

   @Override
   public Company findById(Long id) {
      return companyList.values()
              .stream()
              .filter(c -> c.getId().equals(id))
              .findFirst()
              .orElse(null);
   }

   @Override
   public long count() {
      return companyList.size();
   }

   @Override
   public boolean existsById(Long id) {
      return companyList.values().stream()
              .anyMatch(c -> c.getId().equals(id));
   }

   @Override
   public Company save(Company company) {
      if (company.getId() == null) {
         long newId = companyList.values().stream()
                 .mapToLong(Company::getId)
                 .max()
                 .orElse(0L) + 1;
         company.setId(newId);
         companyList.put(newId, company);
      } else {
         companyList.put(company.getId(), company);
      }
      return company;
   }

   @Override
   public void deleteById(Long id) {
      if (id != null) {
         companyList.remove(id);
      }
   }

   @Override
   public void delete(Company company) {
      deleteById(company.getId());
   }

   @Override
   public void deleteAll() {
      companyList.clear();
   }
}
