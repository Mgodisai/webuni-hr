package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses={CompanyMapper.class})
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper( EmployeeMapper.class );

    List<EmployeeDto> employeesToDtos(List<Employee> employees);

    List<Employee> dtosToemployees(List<EmployeeDto> employeeDtos);

    @Mapping(target = "companyDto", source = "company")
    EmployeeDto employeeToDto(Employee employee);

    @Mapping(target = "company", source = "companyDto")
    Employee dtoToEmployee(EmployeeDto employeeDto);
}
