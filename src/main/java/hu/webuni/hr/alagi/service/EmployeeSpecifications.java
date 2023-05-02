package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EmployeeSpecifications {

   public static Specification<Employee> fetchCompany() {
      return (root, cq, cb) -> {
         root.fetch(Employee_.company, JoinType.LEFT);
         cq.distinct(true);
         return cb.conjunction();
      };
   }

   public static Specification<Employee> hasId(Long id) {
      return (root, cq, cb)-> cb.equal(root.get(Employee_.id), id);
   }

   public static Specification<Employee> likeFirstNameIgnoreCase(String firstName) {
      return (root, cq, cb)-> cb.like(cb.upper(root.get(Employee_.firstName)), firstName.toUpperCase() + "%");
   }

   public static Specification<Employee> likeFirstName(String firstName) {
      return (root, cq, cb)-> cb.like(root.get(Employee_.firstName), firstName + "%");
   }

   public static Specification<Employee> hasPosition(Position position) {
      return (root, cq, cb)-> cb.equal(root.get(Employee_.position), position);
   }

   public static Specification<Employee> isMonthlySalaryBetweenMinAndMax(double min, double max) {
      return (root, cq, cb)-> cb.between(cb.toDouble(root.get(Employee_.monthlySalary)), min, max);
   }

   public static Specification<Employee> isStartDateOnSpecifiedDate(LocalDate specifiedDate) {
      return (root, cq, cb)-> cb.equal(root.get(Employee_.startDate).as(LocalDate.class), specifiedDate);
   }

   public static Specification<Employee> likeCompanyNameIgnoreCase(String companyName) {
      return (root, cq, cb)-> {
         Join<Employee, Company> companyJoin = root.join(Employee_.company, JoinType.LEFT);
         return cb.like(cb.upper(companyJoin.get(Company_.name)), companyName.toUpperCase() + "%");
      };
   }
}
