package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.LeaveRequestDto;
import hu.webuni.hr.alagi.dto.LeaveRequestFilterDto;
import hu.webuni.hr.alagi.exception.EntityNotExistsWithGivenIdException;
import hu.webuni.hr.alagi.mapper.LeaveRequestMapper;
import hu.webuni.hr.alagi.model.LeaveRequest;
import hu.webuni.hr.alagi.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {
   @Autowired
   private LeaveRequestService leaveRequestService;
   @Autowired
   private LeaveRequestMapper leaveRequestMapper;

   @GetMapping("/manager/{managerId}")
   @PreAuthorize("authentication.principal.employee.id == #managerId")
   public List<LeaveRequestDto> getLeaveRequestsForManager(@PathVariable Long managerId, Pageable pageable) {
      Page<LeaveRequest> leaveRequestsByManager = leaveRequestService.getLeaveRequestsForManager(managerId, pageable);
      return leaveRequestMapper.requestsToDtos(leaveRequestsByManager.toList());
   }


   @GetMapping
   public List<LeaveRequestDto> getAllLeaveRequests(Pageable pageable) {
      Page<LeaveRequest> leaveRequests = leaveRequestService.getAllLeaveRequests(pageable);
      return leaveRequestMapper.requestsToDtos(leaveRequests.toList());
   }

   @GetMapping("/{id}")
   public LeaveRequestDto getLeaveRequestById(@PathVariable Long id) {
      LeaveRequest leaveRequest = leaveRequestService.findById(id)
            .orElseThrow(()->new EntityNotExistsWithGivenIdException(id, LeaveRequest.class));
      return leaveRequestMapper.requestToDto(leaveRequest);
   }

   @PostMapping(value = "/search")
   public List<LeaveRequestDto> findByExample(@RequestBody LeaveRequestFilterDto example,
                                                Pageable pageable) {
      Page<LeaveRequest> page = leaveRequestService.findLeaveRequestsByExample(example, pageable);
      return leaveRequestMapper.requestsToDtos(page.getContent());
   }

   @PostMapping
   //@PreAuthorize("#leaveRequestDto.requesterId == authentication.principal.employee.id")
   public LeaveRequestDto createLeaveRequest(@RequestBody @Valid LeaveRequestDto leaveRequestDto) {
      LeaveRequest createdLeaveRequest;
      try {
         createdLeaveRequest = leaveRequestService.createLeaveRequest(leaveRequestMapper.dtoToRequest(leaveRequestDto), leaveRequestDto.getRequesterId());
      } catch (IllegalArgumentException e) {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requester not found");
      }
      return leaveRequestMapper.requestToDto(createdLeaveRequest);
   }

   @PutMapping("/{id}")
   @PreAuthorize("#leaveRequestDto.requesterId == authentication.principal.employee.id")
   public LeaveRequestDto updateLeaveRequest(@PathVariable Long id, @RequestBody LeaveRequestDto leaveRequestDto) {
      leaveRequestDto.setId(id);
      LeaveRequest updatedLeaveRequest;
      try {
         updatedLeaveRequest = leaveRequestService.updateLeaveRequest(leaveRequestMapper.dtoToRequest(leaveRequestDto), leaveRequestDto.getRequesterId());
      } catch (IllegalArgumentException e) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
      }
      if (updatedLeaveRequest==null) {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
      return leaveRequestMapper.requestToDto(updatedLeaveRequest);
   }

   @PutMapping("/{id}/approval")
   public LeaveRequestDto handleLeaveRequest(@PathVariable Long id, @RequestParam boolean approved) {

      LeaveRequest handledLeaveRequest;
      try {
         handledLeaveRequest = leaveRequestService.handleLeaveRequest(id, approved)
               .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "LeaveRequest is already handled"));
      } catch (IllegalArgumentException e) {
         throw new EntityNotExistsWithGivenIdException(id, LeaveRequest.class);
      }
      return leaveRequestMapper.requestToDto(handledLeaveRequest);
   }

   @DeleteMapping("/{id}")
   public void deleteLeaveRequest(@PathVariable Long id) {
      try {
         leaveRequestService.deleteLeaveRequest(id);
      } catch (IllegalStateException e) {
         throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
      } catch (IllegalArgumentException e) {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
      }
   }
}