package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.model.CompanyType;
import hu.webuni.hr.alagi.model.Position;
import hu.webuni.hr.alagi.repository.CompanyTypeRepository;
import hu.webuni.hr.alagi.repository.PositionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CommonMapper implements CompanyMapper, EmployeeMapper {
    @Autowired
    private CompanyTypeRepository companyTypeRepository;
    @Autowired
    private PositionRepository positionRepository;

    @Named("stringToCompanyType")
    CompanyType stringToCompanyType(String companyTypeShortName) {
        List<CompanyType> companyTypeByShortName = companyTypeRepository.getCompanyTypeByShortName(companyTypeShortName);
        return companyTypeByShortName.isEmpty() ? new CompanyType(companyTypeShortName, null) : companyTypeByShortName.get(0);
    }

    @Named("stringToPosition")
    public Position stringToPosition(String position) {
        List<Position> positionByName = positionRepository.getPositionByName(position);
        return positionByName.isEmpty() ? new Position(position, null) : positionByName.get(0);
    }
}
