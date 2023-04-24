package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Long> {
    CompanyType getCompanyTypeByShortName(String shortName);
}
