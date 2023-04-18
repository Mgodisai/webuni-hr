package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT c FROM Company c JOIN c.employeeList e WHERE e.monthlySalary > :salary")
    List<Company> findByEmployeeWithSalaryGreaterThan(int salary);

    @Query("SELECT c FROM Company c WHERE SIZE(c.employeeList) > :limit")
    List<Company> findByNumberOfEmployeesGreaterThan(int limit);
}
