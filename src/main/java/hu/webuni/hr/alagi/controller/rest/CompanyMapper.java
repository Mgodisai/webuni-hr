package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.CompanyType;
import hu.webuni.hr.alagi.repository.CompanyTypeRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CompanyMapper {

    @Autowired
    public CompanyTypeRepository companyTypeRepository;

    abstract List<CompanyDto> companiesToDtos(List<Company> companies);

    @Mapping(target = "employeeDtoList", source = "employeeList")
    @Mapping(target = "companyType", source = "companyType", qualifiedByName = "companyTypeToString")
    abstract CompanyDto companyToDto(Company company);

    @Mapping(target = "employeeList", source = "employeeDtoList")
    @Mapping(target = "companyType", source = "companyType", qualifiedByName = "stringToCompanyType")
    abstract Company dtoToCompany(CompanyDto companyDto);

    @Named("companyTypeToString")
    public String companyTypeToString(CompanyType companyType) {
        return companyType.getShortName();
    }

    @Named("stringToCompanyType")
    public CompanyType stringToCompanyType(String companyTypeString) {
        return companyTypeRepository.getCompanyTypeByShortName(companyTypeString);
    }
}
