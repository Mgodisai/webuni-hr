package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.model.Employee;
import org.mapstruct.Mapping;

import java.util.List;

public interface EmployeeMapper {

    List<EmployeeDto> employeesToDtos(List<Employee> employees);

    List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);

    @Mapping(target = "position", source = "position.name")
    EmployeeDto employeeToDto(Employee employee);

    @Mapping(target = "company", source = "companyDto")
    @Mapping(target = "position", source = "position", qualifiedByName = "stringToPosition")
    Employee dtoToEmployee(EmployeeDto employeeDto);

}
