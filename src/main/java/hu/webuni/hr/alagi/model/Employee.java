package hu.webuni.hr.alagi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
public class Employee {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @NotEmpty
   private String firstName;
   @NotEmpty
   private String lastName;

   @Positive
   private int monthlySalary;

   @PastOrPresent
   @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
   private LocalDateTime startDate;

   @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
   @JoinColumn(name="position_id")
   private Position position;

   @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinColumn(name="company_id")
   private Company company;

   public Employee(Long id, String firstName, String lastName, Position position, int monthlySalary, LocalDateTime startDate, Company company) {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.position = position;
      this.monthlySalary = monthlySalary;
      this.startDate = startDate;
      this.company = company;
   }

   public Employee(String firstName, String lastName, Position position, int monthlySalary, LocalDateTime startDate, Company company) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.position = position;
      this.monthlySalary = monthlySalary;
      this.startDate = startDate;
      this.company = company;
   }

   public Employee() {
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
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

   public Company getCompany() {
      return company;
   }

   public void setCompany(Company company) {
      this.company = company;
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