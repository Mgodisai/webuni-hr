package hu.webuni.hr.alagi.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Position {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;
   private String name;

   @Enumerated(EnumType.STRING)
   private Qualification qualification;

   @OneToMany(mappedBy = "position")
   private List<Employee> employeeList;

   public Position() {
   }

   public Position(String name, Qualification qualification) {
      this.name = name;
      this.qualification = qualification;
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Qualification getQualification() {
      return qualification;
   }

   public void setQualification(Qualification qualification) {
      this.qualification = qualification;
   }

   public List<Employee> getEmployeeList() {
      return employeeList;
   }

   public void setEmployeeList(List<Employee> employeeList) {
      this.employeeList = employeeList;
   }
}
