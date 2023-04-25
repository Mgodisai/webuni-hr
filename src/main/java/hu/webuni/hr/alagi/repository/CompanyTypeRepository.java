package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Long> {
    List<CompanyType> getCompanyTypeByShortName(String shortName);
}
