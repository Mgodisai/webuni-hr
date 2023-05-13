package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.dto.LeaveRequestFilterDto;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.LeaveRequest;
import hu.webuni.hr.alagi.model.LeaveRequestStatus;
import hu.webuni.hr.alagi.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LeaveRequestService {
   @Autowired
   private LeaveRequestRepository leaveRequestRepository;

   @Autowired
   private EmployeeService employeeService;

   @Transactional
   public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest, Long requesterId) {
      leaveRequest.setRequestDate(LocalDateTime.now());
      leaveRequest.setLeaveRequestStatus(LeaveRequestStatus.PENDING);
      leaveRequest.setApprover(null);
      leaveRequest.setApproveDate(null);
      Employee employee = employeeService.getEmployeeById(requesterId)
            .orElseThrow(()->new IllegalArgumentException("Requester not found with EmployeeId: "+requesterId));
      leaveRequest.setRequester(employee);
      return leaveRequestRepository.save(leaveRequest);
   }

   @Transactional
   public LeaveRequest updateLeaveRequest(LeaveRequest updateLeaveRequest, Long requesterId) {
      Optional<LeaveRequest> leaveRequestBeforeUpdate = leaveRequestRepository.findLeaveRequestByIdWithEmployees(updateLeaveRequest.getId());
      if (leaveRequestBeforeUpdate.isEmpty()) {
         return null;
      }
      if (leaveRequestBeforeUpdate.get().getLeaveRequestStatus()!=LeaveRequestStatus.PENDING) {
         throw new IllegalArgumentException("LeaveRequest is already handled, cannot be modified");
      }
      if (updateLeaveRequest.getLeaveRequestStatus()!= null && updateLeaveRequest.getLeaveRequestStatus() != LeaveRequestStatus.PENDING) {
         throw new IllegalArgumentException("LeaveRequest status cannot be modified using this endpoint");
      }
      if (requesterId == null) {
         throw new IllegalArgumentException("LeaveRequest cannot be modified without valid Requester");
      }
      if (updateLeaveRequest.getRequestDate()==null) {
         updateLeaveRequest.setRequestDate(leaveRequestBeforeUpdate.get().getRequestDate());
      }
      updateLeaveRequest.setLeaveRequestStatus(LeaveRequestStatus.PENDING);
      updateLeaveRequest.setApprover(null);
      updateLeaveRequest.setApproveDate(null);
      Employee employee = employeeService.getEmployeeById(requesterId)
            .orElseThrow(()->new IllegalArgumentException("Requester not found with EmployeeId: "+requesterId));
      updateLeaveRequest.setRequester(employee);
      return leaveRequestRepository.save(updateLeaveRequest);
   }

   public Optional<LeaveRequest> findById(Long id) {
      return leaveRequestRepository.findLeaveRequestByIdWithEmployees(id);
   }

   public Page<LeaveRequest> getAllLeaveRequests(Pageable pageable) {
      return leaveRequestRepository.findAllLeaveRequestWithEmployees(pageable);
   }

   public Page<LeaveRequest> findLeaveRequestsByExample(LeaveRequestFilterDto example, Pageable pageable) {
      LocalDateTime createDateTimeStart = example.getCreateDateTimeStart();
      LocalDateTime createDateTimeEnd = example.getCreateDateTimeEnd();
      String requesterFullName = example.getRequesterFullName();
      String approverFullName = example.getApproverFullName();
      LeaveRequestStatus leaveRequestStatus = example.getLeaveRequestStatus();
      LocalDate startOfLeave = example.getStartDate();
      LocalDate endOfLeave = example.getEndDate();

      Specification<LeaveRequest> spec = Specification.where(null);

      if (leaveRequestStatus != null)
         spec = spec.and(LeaveRequestSpecification.getByStatus(leaveRequestStatus));
      if (createDateTimeStart != null && createDateTimeEnd != null)
         spec = spec.and(LeaveRequestSpecification.createDateIsBetween(createDateTimeStart, createDateTimeEnd));
      if (StringUtils.hasText(requesterFullName))
         spec = spec.and(LeaveRequestSpecification.requesterNameStartsWith(requesterFullName));
      if (StringUtils.hasText(approverFullName))
         spec = spec.and(LeaveRequestSpecification.approverNameStartsWith(approverFullName));
      if (startOfLeave != null)
         spec = spec.and(LeaveRequestSpecification.isEndDateGreaterThan(startOfLeave));
      if (endOfLeave != null)
         spec = spec.and(LeaveRequestSpecification.isStartDateLessThan(endOfLeave));
      return leaveRequestRepository.findAll(spec, pageable);
   }

   @Transactional
   public Optional<LeaveRequest> handleLeaveRequest(Long id, Long approverId, boolean isApproved) {
      Employee approver = employeeService.getEmployeeById(approverId)
            .orElseThrow(()->new IllegalArgumentException("Approver not found with EmployeeId: "+approverId));
      LeaveRequest leaveRequest = findById(id)
            .orElseThrow(()->new IllegalArgumentException("LeaveRequest not found with Id: "+id));

      if (leaveRequest.getLeaveRequestStatus() == LeaveRequestStatus.PENDING) {
         leaveRequest.setLeaveRequestStatus(isApproved?LeaveRequestStatus.APPROVED:LeaveRequestStatus.REJECTED);
         leaveRequest.setApprover(approver);
         leaveRequest.setApproveDate(LocalDateTime.now());
         leaveRequest = leaveRequestRepository.save(leaveRequest);
         return Optional.of(leaveRequest);
      } else {
         return Optional.empty();
      }
   }

   @Transactional
   public void deleteLeaveRequest(Long id) {
      LeaveRequest leaveRequest = leaveRequestRepository.findById(id).orElse(null);
      if (leaveRequest==null) {
         throw new IllegalArgumentException("LeaveRequest not found with id: "+id);
      }
      if (leaveRequest.getLeaveRequestStatus() != LeaveRequestStatus.PENDING)
         throw new IllegalStateException("LeaveRequest is already handled, cannot be deleted");

      leaveRequestRepository.deleteById(id);
   }
}
