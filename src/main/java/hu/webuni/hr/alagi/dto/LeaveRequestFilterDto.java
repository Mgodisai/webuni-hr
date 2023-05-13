package hu.webuni.hr.alagi.dto;

import hu.webuni.hr.alagi.model.LeaveRequestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaveRequestFilterDto {

   private LocalDateTime createDateTimeStart;
   private LocalDateTime createDateTimeEnd;
   private LeaveRequestStatus leaveRequestStatus;
   private String requesterFullName;
   private String approverFullName;
   private LocalDate startDate;
   private LocalDate endDate;

   public LeaveRequestFilterDto() {
   }

   public LocalDateTime getCreateDateTimeStart() {
      return createDateTimeStart;
   }

   public void setCreateDateTimeStart(LocalDateTime createDateTimeStart) {
      this.createDateTimeStart = createDateTimeStart;
   }

   public LocalDateTime getCreateDateTimeEnd() {
      return createDateTimeEnd;
   }

   public void setCreateDateTimeEnd(LocalDateTime createDateTimeEnd) {
      this.createDateTimeEnd = createDateTimeEnd;
   }

   public LeaveRequestStatus getLeaveRequestStatus() {
      return leaveRequestStatus;
   }

   public void setLeaveRequestStatus(LeaveRequestStatus leaveRequestStatus) {
      this.leaveRequestStatus = leaveRequestStatus;
   }

   public String getRequesterFullName() {
      return requesterFullName;
   }

   public void setRequesterFullName(String requesterFullName) {
      this.requesterFullName = requesterFullName;
   }

   public String getApproverFullName() {
      return approverFullName;
   }

   public void setApproverFullName(String approverFullName) {
      this.approverFullName = approverFullName;
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
}
