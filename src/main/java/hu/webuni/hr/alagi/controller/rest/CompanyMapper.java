package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.model.Company;
import org.mapstruct.Mapping;

import java.util.List;

public interface CompanyMapper {

    List<CompanyDto> companiesToDtos(List<Company> companyList);

    List<CompanyDto> dtosToCompanies(List<CompanyDto> companyDtoList);

    @Mapping(target = "companyType", source = "companyType.shortName")
    @Mapping(target = "employeeDtoList", source = "employeeList")
    CompanyDto companyToDto(Company company);

    @Mapping(target = "employeeList", source = "employeeDtoList")
    @Mapping(target = "companyType", source = "companyType", qualifiedByName = "stringToCompanyType")
    Company dtoToCompany(CompanyDto companyDto);


}
