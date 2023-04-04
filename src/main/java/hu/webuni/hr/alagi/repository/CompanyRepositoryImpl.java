package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Repository
public class CompanyRepositoryImpl implements MyCrudLikeRepository<Company, Long> {
   private final Map<Long, Company> companyList = new HashMap<>();
   {
      Employee employee1 = new Employee("József", "Java", Position.CEO, 10000, LocalDateTime.of(2017, Month.NOVEMBER, 1, 8,0 ));
      Employee employee2 = new Employee("Géza", "Piton", Position.CUSTOMER_SUPPORT, 3000, LocalDateTime.of(2020, Month.JUNE, 15, 8,0 ));
      companyList.put(1L, new Company(1L, "14-02-123456", "Kerék Kft", "7400 Kaposvár, Fő u. 12.", new ArrayList<>(Arrays.asList(employee1, employee2))));
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
