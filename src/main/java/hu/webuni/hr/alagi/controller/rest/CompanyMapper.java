package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CompanyTypeMapper.class, PositionMapper.class})
public interface CompanyMapper {

    List<CompanyDto> companiesToDtos(List<Company> companyList);

    List<CompanyDto> dtosToCompanies(List<CompanyDto> companyDtoList);

    @Mapping(target="employeeDtoList", source="employeeList")
    CompanyDto companyToDto(Company company);

    @Mapping(target="employeeList", source="employeeDtoList")
    Company dtoToCompany(CompanyDto companyDto);

    @Mapping(target = "companyDto", ignore = true)
    EmployeeDto employeeToDto(Employee employee);

    Employee dtoToEmployee(EmployeeDto employeeDto);
}
