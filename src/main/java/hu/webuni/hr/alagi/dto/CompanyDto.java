package hu.webuni.hr.alagi.dto;

import java.util.List;

public class CompanyDto {
   private String registerNumber;
   private String name;
   private String address;
   private List<EmployeeDto> employeeDtoList;

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
}
