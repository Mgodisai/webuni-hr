package hu.webuni.hr.alagi.model;

import java.time.LocalDateTime;
import java.time.Period;

public class Employee {
   private static Long lastId=0L;
   private final Long id;
   private String firstName;
   private String lastName;
   private Position position;
   private int monthlySalary;
   private LocalDateTime startDate;

   public Employee(String firstName, String lastName, int monthlySalary, LocalDateTime startDate) {
      this.id = lastId++;
      this.firstName = firstName;
      this.lastName = lastName;
      this.monthlySalary = monthlySalary;
      this.startDate = startDate;
   }

   public Long getId() {
      return id;
   }

   public String getFullName() {
      return firstName+" "+lastName;
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

   public Position getPosition() {
      return position;
   }

   public void setPosition(Position position) {
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

   public double getEmploymentYears() {
      return Period.between(startDate.toLocalDate(), LocalDateTime.now().toLocalDate()).toTotalMonths()/12d;
   }

   @Override
   public String toString() {
      return "Employee{" +
            "id=" + id +
            ", name='" + getFullName() + '\'' +
            ", currentMonthlySalary=" + monthlySalary +
            ", years=" + getEmploymentYears() +
            '}';
   }
}