package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT DISTINCT c FROM Company c LEFT JOIN FETCH c.employeeList e LEFT JOIN FETCH e.position")
    List<Company> findAllWithEmployees();

    @Query("SELECT c FROM Company c "+
          "LEFT JOIN FETCH c.employeeList e "+
          "LEFT JOIN FETCH e.position "+
          "WHERE c.id = :id")
    Optional<Company> findCompanyByIdWithEmployees(long id);

    @Query("SELECT DISTINCT c FROM Company c "+
          "LEFT JOIN FETCH c.employeeList e "+
          "LEFT JOIN FETCH e.position p "+
          "WHERE e.monthlySalary > :salary"
    )
    List<Company> findByEmployeeWithSalaryGreaterThan(int salary);

    @Query("SELECT DISTINCT c FROM Company c "+
          "LEFT JOIN FETCH c.employeeList e "+
          "LEFT JOIN FETCH e.position p " +
          "WHERE SIZE(e) > :limit"
    )
    List<Company> findByNumberOfEmployeesGreaterThan(int limit);
}
