package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>, JpaSpecificationExecutor<LeaveRequest> {
   @Query(value = "SELECT lr FROM LeaveRequest lr "+
         "LEFT JOIN FETCH lr.requester r "+
         "LEFT JOIN FETCH lr.approver a ",
         countQuery = "SELECT COUNT(lr) FROM LeaveRequest lr " +
               "LEFT JOIN lr.requester r " +
               "LEFT JOIN lr.approver a")
   Page<LeaveRequest> findAllLeaveRequestWithEmployees(Pageable pageable);

   @Query(value="SELECT lr FROM LeaveRequest lr "+
         "LEFT JOIN FETCH lr.requester r "+
         "LEFT JOIN FETCH lr.approver a "+
         "WHERE lr.id = :leaveRequestId")
   Optional<LeaveRequest> findLeaveRequestByIdWithEmployees(Long leaveRequestId);
}
