package hu.webuni.hr.alagi.mapper;

import hu.webuni.hr.alagi.dto.LeaveRequestDto;
import hu.webuni.hr.alagi.model.LeaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses={PositionMapper.class})
public interface LeaveRequestMapper {
   List<LeaveRequestDto> requestsToDtos(List<LeaveRequest> leaveRequests);
   List<LeaveRequest> dtosToRequests(List<LeaveRequestDto> leaveRequestDtos);

   @Mapping(target = "approverId", source="approver.id")
   @Mapping(target = "requesterId", source="requester.id")
   @Mapping(target = "approverFullName", source="approver.fullName")
   @Mapping(target = "requesterFullName", source="requester.fullName")
   LeaveRequestDto requestToDto(LeaveRequest leaveRequest);

   @Mapping(target = "requester", ignore = true)
   @Mapping(target = "approver", ignore = true)
   LeaveRequest dtoToRequest(LeaveRequestDto leaveRequestDto);
}
