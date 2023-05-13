package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.LeaveRequestDto;
import hu.webuni.hr.alagi.dto.LeaveRequestFilterDto;
import hu.webuni.hr.alagi.exception.DefaultErrorEntity;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.LeaveRequest;
import hu.webuni.hr.alagi.model.LeaveRequestStatus;
import hu.webuni.hr.alagi.service.EmployeeService;
import hu.webuni.hr.alagi.service.LeaveRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LeaveRequestControllerIT {

   private static final String BASE_URI = "/api/leave-requests";

   @Autowired
   private WebTestClient webTestClient;

   @Autowired
   private LeaveRequestService leaveRequestService;

   @Autowired
   private EmployeeService employeeService;

   public List<LeaveRequestDto> getAllLeaveRequests() {
      return webTestClient
            .get()
            .uri(BASE_URI)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(LeaveRequestDto.class)
            .returnResult().getResponseBody();
   }

   @Test
   void testGetAllLeaveRequests() {
      List<LeaveRequestDto> leaveRequestDtoList = getAllLeaveRequests();
      List<LeaveRequest> leaveRequestList = leaveRequestService.getAllLeaveRequests(null).toList();
      assertThat(leaveRequestDtoList.size()).isEqualTo(leaveRequestList.size());
   }

   @Test
   void testGetLeaveRequestByIdInvalidId() {
      Long invalidLeaveRequestId = -1L;

      DefaultErrorEntity response = webTestClient
            .get()
            .uri(BASE_URI + "/{id}", invalidLeaveRequestId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(DefaultErrorEntity.class)
            .returnResult()
            .getResponseBody();
      assertThat(response).isNotNull();
      assertThat(response.getErrorMessage()).isEqualTo("LeaveRequest not exists with id: "+invalidLeaveRequestId);
   }

   @Test
   void testGetLeaveRequestByValidId() {
      Long validLeaveRequestId = 1L;

      LeaveRequestDto leaveRequestDto = webTestClient
            .get()
            .uri(BASE_URI + "/{id}", validLeaveRequestId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(LeaveRequestDto.class)
            .returnResult()
            .getResponseBody();

      Optional<LeaveRequest> leaveRequestOptional = leaveRequestService.findById(validLeaveRequestId);
      assertThat(leaveRequestOptional).isPresent();
      assertThat(leaveRequestDto).isNotNull();

      LeaveRequest leaveRequest = leaveRequestOptional.get();

      assertThat(leaveRequest.getId()).isEqualTo(leaveRequestDto.getId());
      assertThat(leaveRequest.getLeaveRequestStatus()).isEqualTo(leaveRequestDto.getLeaveRequestStatus());
      assertThat(leaveRequest.getRequester().getId()).isEqualTo(leaveRequestDto.getRequesterId());

      if (leaveRequest.getApprover()==null) {
         assertThat(leaveRequestDto.getApproverId()).isNull();
         assertThat(leaveRequestDto.getApproverFullName()).isNull();
         assertThat(leaveRequestDto.getApproveDate()).isNull();
      } else {

         assertThat(leaveRequest.getApprover().getId()).isEqualTo(leaveRequestDto.getApproverId());
         assertThat(leaveRequest.getApprover().getFullName()).isEqualTo(leaveRequestDto.getApproverFullName());
         assertThat(leaveRequest.getApproveDate()).isEqualTo(leaveRequestDto.getApproveDate());
      }
   }

   @Test
   void testFindByExampleUsingFullName() {
      LeaveRequestFilterDto example = new LeaveRequestFilterDto();
      String searchString = "GyuLA K";
      example.setRequesterFullName(searchString);

      List<LeaveRequestDto> response = webTestClient
            .post()
            .uri(BASE_URI + "/search")
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(example)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(LeaveRequestDto.class)
            .returnResult()
            .getResponseBody();
      assertThat(response).isNotNull();
      assertThat(response).allSatisfy(
            r->{
               assertThat(r.getRequesterFullName()).containsIgnoringCase(searchString);
            });
      List<LeaveRequest> allRequest = leaveRequestService.getAllLeaveRequests(null).toList();
      List<LeaveRequest> filteredList = allRequest.stream().filter(r->r.getRequester().getFullName().toLowerCase().contains(searchString.toLowerCase())).toList();
      assertThat(response.size()).isEqualTo(filteredList.size());

      List<Long> filteredIdList = filteredList.stream().map(LeaveRequest::getId).toList();
      assertThat(response).allSatisfy(
            r->{
               assertThat(filteredIdList).contains(r.getId());
            }
      );
   }

   @Test
   void testCreateLeaveRequest() {
      LeaveRequestDto newLeaveRequest = new LeaveRequestDto();
      newLeaveRequest.setStartDate(LocalDate.of(2023,6,1));
      newLeaveRequest.setEndDate(LocalDate.of(2023, 6,10));
      newLeaveRequest.setLeaveRequestStatus(LeaveRequestStatus.PENDING);
      newLeaveRequest.setRequesterId(1L);

      webTestClient
            .post()
            .uri(BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newLeaveRequest)
            .exchange()
            .expectStatus().isOk()
            .expectBody(LeaveRequestDto.class);
   }

   @Test
   void testHandleLeaveRequest() {
     handleRequest(1L, 2L, true);
      handleRequest(2L, 3L, false);
   }

   void handleRequest(Long leaveRequestId, Long approverId, boolean approved) {
      LeaveRequestDto response = webTestClient
            .put()
            .uri(BASE_URI + "/{id}/approval?approverId={approverId}&approved={approved}", leaveRequestId, approverId, approved)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(LeaveRequestDto.class)
            .returnResult()
            .getResponseBody();
      assertThat(response).isNotNull();
      assertThat(response.getApproverId()).isNotNull();
      assertThat(response.getApproverId()).isEqualTo(approverId);
      assertThat(response.getApproveDate()).isNotNull();
      Optional<Employee> employee = employeeService.getEmployeeById(response.getApproverId());
      assertThat(employee).isPresent();
      assertThat(employee.get().getFullName()).isEqualTo(response.getApproverFullName());
      assertThat(response.getLeaveRequestStatus()).isEqualTo(approved?LeaveRequestStatus.APPROVED:LeaveRequestStatus.REJECTED);
   }

   @Test
   void testHandleLeaveRequestInvalidRequestId() {
      Long leaveRequestId = -1L;
      Long approverId = 2L;
      boolean approved = true;

      DefaultErrorEntity response = webTestClient
            .put()
            .uri(BASE_URI + "/{id}/approval?approverId={approverId}&approved={approved}", leaveRequestId, approverId, approved)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest().expectBody(DefaultErrorEntity.class)
            .returnResult().getResponseBody();
      assertThat(response).isNotNull();
      assertThat(response.getErrorMessage()).isEqualTo("LeaveRequest not exists with id: "+leaveRequestId);
   }

   @Test
   void testUpdateLeaveRequest() {
      Long leaveRequestId = 3L;
      LocalDate newStartDate = LocalDate.of(2023,10,10);
      LocalDate newEndDate = LocalDate.of(2023,10,15);
      LeaveRequestDto updatedLeaveRequest = new LeaveRequestDto();
      updatedLeaveRequest.setId(4L);
      updatedLeaveRequest.setRequesterId(2L);
      updatedLeaveRequest.setStartDate(newStartDate);
      updatedLeaveRequest.setEndDate(newEndDate);

      LeaveRequestDto response = webTestClient
            .put()
            .uri(BASE_URI + "/{id}", leaveRequestId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updatedLeaveRequest)
            .exchange()
            .expectStatus().isOk()
            .expectBody(LeaveRequestDto.class)
            .returnResult()
            .getResponseBody();


      Optional<LeaveRequest> leaveRequestOptional = leaveRequestService.findById(leaveRequestId);
      assertThat(leaveRequestOptional).isPresent();
      assertThat(response).isNotNull();

      LeaveRequest leaveRequest = leaveRequestOptional.get();

      assertThat(leaveRequest.getId()).isEqualTo(response.getId());
      assertThat(leaveRequest.getLeaveRequestStatus()).isEqualTo(LeaveRequestStatus.PENDING);
      assertThat(leaveRequest.getRequester().getId()).isEqualTo(response.getRequesterId());
      assertThat(leaveRequest.getStartDate()).isEqualTo(newStartDate);
      assertThat(leaveRequest.getEndDate()).isEqualTo(newEndDate);

   }

   @Test
   void testUpdateLeaveRequestWithInvalidId() {
      Long leaveRequestId = -1L;
      LocalDate newStartDate = LocalDate.of(2023,10,10);
      LocalDate newEndDate = LocalDate.of(2023,10,15);
      LeaveRequestDto updatedLeaveRequestWithInvalidId = new LeaveRequestDto();
      updatedLeaveRequestWithInvalidId.setId(4L);
      updatedLeaveRequestWithInvalidId.setLeaveRequestStatus(LeaveRequestStatus.APPROVED);
      updatedLeaveRequestWithInvalidId.setStartDate(newStartDate);
      updatedLeaveRequestWithInvalidId.setEndDate(newEndDate);

      webTestClient
            .put()
            .uri(BASE_URI + "/{id}", leaveRequestId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updatedLeaveRequestWithInvalidId)
            .exchange()
            .expectStatus().isNotFound();
   }

   @Test
   void testDeleteLeaveRequest() {
      Long leaveRequestId = 4L;

      webTestClient
            .delete()
            .uri(BASE_URI + "/{id}", leaveRequestId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk();
   }

   @Test
   void testDeleteLeaveRequest_invalidId() {
      Long leaveRequestId = -1L;

      webTestClient
            .delete()
            .uri(BASE_URI + "/{id}", leaveRequestId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
   }
}
