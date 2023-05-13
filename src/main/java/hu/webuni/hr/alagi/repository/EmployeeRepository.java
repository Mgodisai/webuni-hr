package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

   @Query("SELECT e.position.name, AVG(e.monthlySalary) " +
           "FROM Employee e " +
           "WHERE e.company.id = :companyId " +
           "GROUP BY e.position.name "+
           "ORDER BY AVG(e.monthlySalary) DESC")
   List<Object[]> getAvgSalariesByPositionUsingCompanyId(Long companyId);

   @Query("SELECT e FROM Employee e "+
         "LEFT JOIN FETCH e.company "+
         "WHERE e.id = :id")
   Optional<Employee> findEmployeeByIdWithCompany(long id);

   @Query("SELECT e FROM Employee e "+
           "LEFT JOIN FETCH e.company "
   )
   List<Employee> findAllEmployeesWithCompany();

   Optional<Employee> findByUsername(String username);
}
