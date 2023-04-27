package hu.webuni.hr.alagi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class EmployeeDto {
   private Long id;
   @NotEmpty
   private String firstName;
   @NotEmpty
   private String lastName;
   @NotNull
   private String position;
   @Positive
   private int monthlySalary;
   @PastOrPresent
   private LocalDateTime startDate;
   private CompanyDto companyDto;

   public EmployeeDto(Long id, String firstName, String lastName, String position, int monthlySalary, LocalDateTime startDate, CompanyDto companyDto) {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.position = position;
      this.monthlySalary = monthlySalary;
      this.startDate = startDate;
      this.companyDto = companyDto;
   }

   public EmployeeDto() {
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getPosition() {
      return position;
   }

   public void setPosition(String position) {
      this.position = position;
   }

   public int getMonthlySalary() {
      return monthlySalary;
   }

   public void setMonthlySalary(int monthlySalary) {
      this.monthlySalary = monthlySalary;
   }

   public LocalDateTime getStartDate() {
      return startDate;
   }

   public void setStartDate(LocalDateTime startDate) {
      this.startDate = startDate;
   }

   public CompanyDto getCompanyDto() {
      return companyDto;
   }

   public void setCompanyDto(CompanyDto companyDto) {
      this.companyDto = companyDto;
   }
}
