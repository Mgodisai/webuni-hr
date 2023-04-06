package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.model.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses={EmployeeMapper.class})
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper( CompanyMapper.class );

    List<CompanyDto> companiesToDtos(List<Company> companies);

    @Mapping(target = "employeeDtoList", source = "employeeList")
    CompanyDto companyToDto(Company company);

    @Mapping(target = "employeeList", source = "employeeDtoList")
    Company dtoToCompany(CompanyDto companyDto);
}
