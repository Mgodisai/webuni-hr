package hu.webuni.hr.alagi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class CompanyDto {
   private Long id;
   private String registerNumber;
   private String name;
   private String address;
   @JsonInclude(JsonInclude.Include.NON_EMPTY)
   private List<EmployeeDto> employeeDtoList;


   public CompanyDto(Long id, String registerNumber, String name, String address, List<EmployeeDto> employeeDtoList) {
      this.id = id;
      this.registerNumber = registerNumber;
      this.name = name;
      this.address = address;
      if (employeeDtoList.size()==0) {
         this.employeeDtoList = new ArrayList<>();
      } else {
         this.employeeDtoList = new ArrayList<>(employeeDtoList);
      }
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
      this.employeeDtoList = new ArrayList<>(employeeDtoList);
   }
}
