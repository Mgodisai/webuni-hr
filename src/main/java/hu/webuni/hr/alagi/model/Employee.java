package hu.webuni.hr.alagi.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class Employee {
   private static Long lastId=0L;
   private Long id;
   private String firstName;
   private String lastName;
   private Position position;
   private int monthlySalary;
   @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
   private LocalDateTime startDate;

   public Employee(String firstName, String lastName, Position position, int monthlySalary, LocalDateTime startDate) {
      this.id = lastId++;
      this.firstName = firstName;
      this.lastName = lastName;
      this.position = position;
      this.monthlySalary = monthlySalary;
      this.startDate = startDate;
   }

   public Employee() {
   }

   public Long getId() {
      return id;
   }

   public void setId() {
      this.id = lastId++;
   }

   public void setId(long id) {
      this.id = id;
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

   @Override
   public String toString() {
      return "Employee{" +
            "id=" + id +
            ", position=" + position +
            ", name='" + getFullName() + '\'' +
            ", currentMonthlySalary=" + monthlySalary +
            '}';
   }
}