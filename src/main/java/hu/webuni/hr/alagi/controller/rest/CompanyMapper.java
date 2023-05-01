package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.Employee;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CompanyTypeMapper.class, PositionMapper.class})
public interface CompanyMapper {

    List<CompanyDto> companiesToDtos(List<Company> companyList);

    @IterableMapping(qualifiedByName = "summary")
    List<CompanyDto> companiesToSummaryDtos(List<Company> companies);

    List<CompanyDto> dtosToCompanies(List<CompanyDto> companyDtoList);

    @Mapping(target="employeeDtoList", source="employeeList")
    CompanyDto companyToDto(Company company);

    @Mapping(target = "employeeDtoList", ignore = true)
    @Named("summary")
    CompanyDto companyToSummaryDto(Company company);

    @Mapping(target="employeeList", source="employeeDtoList")
    Company dtoToCompany(CompanyDto companyDto);

    @Mapping(target = "companyDto", ignore = true)
    EmployeeDto employeeToDto(Employee employee);

    Employee dtoToEmployee(EmployeeDto employeeDto);
}
