package hu.webuni.hr.alagi.mapper;

import hu.webuni.hr.alagi.model.CompanyType;
import hu.webuni.hr.alagi.repository.CompanyTypeRepository;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CompanyTypeMapper {

    @Autowired
    protected CompanyTypeRepository companyTypeRepository;

    CompanyType stringToCompanyType(String shortName) {
        List<CompanyType> companyTypes = companyTypeRepository.getCompanyTypeByShortName(shortName);
        if (companyTypes.isEmpty()) {
            return new CompanyType(shortName, shortName);
        }
        return companyTypes.get(0);
    }

    String companyTypeToString(CompanyType companyType) {
        return companyType.getShortName();
    }
}
