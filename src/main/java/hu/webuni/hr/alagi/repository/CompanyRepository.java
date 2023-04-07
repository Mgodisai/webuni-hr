package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends MyCrudLikeRepository<Company, Long> {

}
