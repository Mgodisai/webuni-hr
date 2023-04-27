package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.model.CompanyType;
import hu.webuni.hr.alagi.repository.CompanyTypeRepository;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CompanyTypeMapper {
    protected CompanyTypeRepository companyTypeRepository;

    CompanyType stringToCompanyType(String shortName) {
        List<CompanyType> companyTypes = companyTypeRepository.getCompanyTypeByShortName(shortName);
        if (companyTypes.isEmpty()) {
            return new CompanyType(shortName, null);
        }
        return companyTypes.get(0);
    }

    String companyTypeToString(CompanyType companyType) {
        return companyType.getShortName();
    }
}
