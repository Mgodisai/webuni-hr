package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Employee_;
import hu.webuni.hr.alagi.model.LeaveRequest;
import hu.webuni.hr.alagi.model.LeaveRequestStatus;
import hu.webuni.hr.alagi.model.LeaveRequest_;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveRequestSpecification {

   public static Specification<LeaveRequest> getByStatus(LeaveRequestStatus status) {
      return (root, cq, cb)-> cb.equal(root.get(LeaveRequest_.leaveRequestStatus), status);
   }

   public static Specification<LeaveRequest> requesterNameStartsWith(String fullName) {
      return (root, cq, cb)-> {
         Expression<String> concat = cb.concat(root.get(LeaveRequest_.requester).get(Employee_.firstName), " ");
         concat = cb.concat(concat, root.get(LeaveRequest_.requester).get(Employee_.lastName));
         return cb.like(cb.upper(concat),fullName.toUpperCase() + "%");
      };
   }

   public static Specification<LeaveRequest> approverNameStartsWith(String fullName) {
      return (root, cq, cb)-> {
         Expression<String> concat = cb.concat(root.get(LeaveRequest_.approver).get(Employee_.firstName), " ");
         concat = cb.concat(concat, root.get(LeaveRequest_.approver).get(Employee_.lastName));
         return cb.like(cb.upper(concat),fullName.toUpperCase() + "%");
      };
   }

   public static Specification<LeaveRequest> createDateIsBetween(LocalDateTime startDate,
                                                                 LocalDateTime endDate) {
      return (root, cq, cb) -> cb.between(root.get(LeaveRequest_.requestDate), startDate, endDate);
   }

   public static Specification<LeaveRequest> isStartDateLessThan(LocalDate date) {
      return (root, cq, cb) -> cb.lessThan(root.get(LeaveRequest_.startDate), date);
   }

   public static Specification<LeaveRequest> isEndDateGreaterThan(LocalDate date) {
      return (root, cq, cb) -> cb.greaterThan(root.get(LeaveRequest_.endDate), date);
   }

}
