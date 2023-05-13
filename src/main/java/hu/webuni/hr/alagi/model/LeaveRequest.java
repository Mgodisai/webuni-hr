package hu.webuni.hr.alagi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class LeaveRequest {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @NotNull
   private LocalDate startDate;
   @NotNull
   private LocalDate endDate;

   private LocalDateTime requestDate;
   @Enumerated(EnumType.STRING)
   private LeaveRequestStatus leaveRequestStatus;

   @ManyToOne(fetch = FetchType.EAGER)
   private Employee requester;

   @ManyToOne(fetch = FetchType.EAGER)
   private Employee approver;

   private LocalDateTime approveDate;

   public LeaveRequest(LocalDate startDate, LocalDate endDate, LocalDateTime requestDate, LeaveRequestStatus leaveRequestStatus, Employee requester, Employee approver, LocalDateTime approveDate) {
      this.startDate = startDate;
      this.endDate = endDate;
      this.requestDate = requestDate;
      this.leaveRequestStatus = leaveRequestStatus;
      this.requester = requester;
      this.approver = approver;
      this.approveDate = approveDate;
   }

   public LeaveRequest() {
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public LocalDate getStartDate() {
      return startDate;
   }

   public void setStartDate(LocalDate startDate) {
      this.startDate = startDate;
   }

   public LocalDate getEndDate() {
      return endDate;
   }

   public void setEndDate(LocalDate endDate) {
      this.endDate = endDate;
   }

   public LocalDateTime getRequestDate() {
      return requestDate;
   }

   public void setRequestDate(LocalDateTime requestDate) {
      this.requestDate = requestDate;
   }

   public LeaveRequestStatus getLeaveRequestStatus() {
      return leaveRequestStatus;
   }

   public void setLeaveRequestStatus(LeaveRequestStatus leaveRequestStatus) {
      this.leaveRequestStatus = leaveRequestStatus;
   }

   public Employee getRequester() {
      return requester;
   }

   public void setRequester(Employee requester) {
      this.requester = requester;
   }

   public Employee getApprover() {
      return approver;
   }

   public void setApprover(Employee approver) {
      this.approver = approver;
   }

   public LocalDateTime getApproveDate() {
      return approveDate;
   }

   public void setApproveDate(LocalDateTime approveDate) {
      this.approveDate = approveDate;
   }
}
