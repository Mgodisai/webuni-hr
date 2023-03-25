package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository {
   List<CompanyDto> findAll();
   CompanyDto findById(Long id);
   CompanyDto save(CompanyDto company);
   void deleteById(Long id);
   CompanyDto addEmployee(Long companyId, EmployeeDto employee);
   CompanyDto removeEmployee(Long companyId, Long employeeId);
   CompanyDto addNewEmployeeList(Long companyId, List<EmployeeDto> employeeDtoList);

}
