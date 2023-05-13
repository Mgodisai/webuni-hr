package hu.webuni.hr.alagi.dto;

import hu.webuni.hr.alagi.model.LeaveRequestStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveRequestDto {
   private Long id;
   @NotNull
   private LocalDate startDate;
   @NotNull
   private LocalDate endDate;

   private LocalDateTime requestDate;

   private LeaveRequestStatus leaveRequestStatus;

   @NotNull
   private Long requesterId;
   private String requesterFullName;

   private Long approverId;
   private String approverFullName;

   @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
   private LocalDateTime approveDate;


   public LeaveRequestDto() {

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

   public Long getRequesterId() {
      return requesterId;
   }

   public void setRequesterId(Long requesterId) {
      this.requesterId = requesterId;
   }

   public Long getApproverId() {
      return approverId;
   }

   public void setApproverId(Long approverId) {
      this.approverId = approverId;
   }

   public LocalDateTime getApproveDate() {
      return approveDate;
   }

   public void setApproveDate(LocalDateTime approveDate) {
      this.approveDate = approveDate;
   }

   public String getApproverFullName() {
      return approverFullName;
   }

   public void setApproverFullName(String approverFullName) {
      this.approverFullName = approverFullName;
   }

   public String getRequesterFullName() {
      return requesterFullName;
   }

   public void setRequesterFullName(String requesterFullName) {
      this.requesterFullName = requesterFullName;
   }
}
