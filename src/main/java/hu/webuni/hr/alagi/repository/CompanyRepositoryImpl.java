package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.model.Position;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {
   private final List<CompanyDto> companyDtoList = new ArrayList<>();
   {
      EmployeeDto employee1 = new EmployeeDto(1L,"József", "Java", Position.CEO, 10000, LocalDateTime.of(2017, Month.NOVEMBER, 1, 8,0 ));
      EmployeeDto employee2 = new EmployeeDto(2L, "Géza", "Piton", Position.CUSTOMER_SUPPORT, 3000, LocalDateTime.of(2020, Month.JUNE, 15, 8,0 ));
      companyDtoList.add(new CompanyDto(1L, "14-02-123456", "Kerék Kft", "7400 Kaposvár, Fő u. 12.", new ArrayList<>(Arrays.asList(employee1, employee2))));
   }

   @Override
   public List<CompanyDto> findAll() {
      return new ArrayList<>(companyDtoList);
   }

   @Override
   public CompanyDto findById(Long id) {
      return companyDtoList.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElse(null);
   }

   @Override
   public CompanyDto save(CompanyDto company) {
      if (company.getId() == null) {
         long newId = companyDtoList.stream()
               .mapToLong(CompanyDto::getId)
               .max()
               .orElse(0L) + 1;
         company.setId(newId);
         companyDtoList.add(company);
         return company;
      } else {
         CompanyDto existingCompany = findById(company.getId());
         if (existingCompany != null) {
            int index = companyDtoList.indexOf(existingCompany);
            companyDtoList.set(index, company);
         } else {
            companyDtoList.add(company);
         }
         return company;
      }
   }

   @Override
   public void deleteById(Long id) {
      companyDtoList.removeIf(company -> company.getId().equals(id));
   }

   @Override
   public CompanyDto addEmployee(Long companyId, EmployeeDto employee) {
      CompanyDto company = findById(companyId);
      if (company != null) {
         company.getEmployeeDtoList().add(employee);
         save(company);
         return company;
      }
      return null;
   }

   @Override
   public CompanyDto removeEmployee(Long companyId, Long employeeId) {
      CompanyDto company = findById(companyId);
      if (company != null) {
         company.getEmployeeDtoList().removeIf(employee -> employee.getId().equals(employeeId));
         save(company);
         return company;
      }
      return null;
   }

   @Override
   public CompanyDto addNewEmployeeList(Long companyId, List<EmployeeDto> employeeDtoList) {
      CompanyDto company = findById(companyId);
      if (company != null) {
         company.setEmployeeDtoList(employeeDtoList);
         save(company);
         return company;
      }
      return null;
   }
}
