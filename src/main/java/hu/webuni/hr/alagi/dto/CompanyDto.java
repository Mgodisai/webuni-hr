package hu.webuni.hr.alagi.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import hu.webuni.hr.alagi.model.CompanyType;

import java.util.ArrayList;
import java.util.List;

public class CompanyDto {
   private Long id;
   private String registerNumber;
   private CompanyType companyType;
   private String name;
   private String address;
   @JsonManagedReference
   private List<EmployeeDto> employeeDtoList;

   public CompanyDto(Long id, String registerNumber, CompanyType companyType, String name, String address, List<EmployeeDto> employeeDtoList) {
      this.id = id;
      this.registerNumber = registerNumber;
      this.companyType = companyType;
      this.name = name;
      this.address = address;
      this.employeeDtoList = employeeDtoList;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getRegisterNumber() {
      return registerNumber;
   }

   public void setRegisterNumber(String registerNumber) {
      this.registerNumber = registerNumber;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public List<EmployeeDto> getEmployeeDtoList() {
      return employeeDtoList;
   }

   public void setEmployeeDtoList(List<EmployeeDto> employeeDtoList) {
      this.employeeDtoList = employeeDtoList;
   }

   public void addEmployeeDto(EmployeeDto employeeDto) {
      if (employeeDtoList == null) {
         employeeDtoList = new ArrayList<>();
      }
      employeeDtoList.add(employeeDto);
   }

   public CompanyType getCompanyType() {
      return companyType;
   }

   public void setCompanyType(CompanyType companyType) {
      this.companyType = companyType;
   }
}
