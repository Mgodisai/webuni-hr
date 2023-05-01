package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.model.Employee;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CompanyTypeMapper.class, PositionMapper.class})
public interface EmployeeMapper {

    List<EmployeeDto> employeesToDtos(List<Employee> employees);

    List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);

    //@Mapping(target = "position", source = "position.name")
    @Mapping(target = "companyDto", source = "company")
    @Mapping(target = "companyDto.employeeDtoList", ignore = true)
    EmployeeDto employeeToDto(Employee employee);

    @InheritInverseConfiguration
    Employee dtoToEmployee(EmployeeDto employeeDto);

}
