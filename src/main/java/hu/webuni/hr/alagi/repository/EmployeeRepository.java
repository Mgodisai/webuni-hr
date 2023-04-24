package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
   List<Employee> findByMonthlySalaryGreaterThanEqual(int borderNumber);
   List<Employee> findEmployeesByPosition(Position position);
   List<Employee> findEmployeesByFirstNameStartsWithIgnoreCase(String text);
   List<Employee> findEmployeesByStartDateBetween(LocalDateTime start, LocalDateTime end);

   @Query("select MIN(e.startDate) from Employee e")
   LocalDateTime findMinStartDate();
   @Query("select MAX(e.startDate) from Employee e")
   LocalDateTime findMaxStartDate();

   @Query("select e from Employee e where " +
         "(:position is null or e.position=:position) and " +
         "(:minSalary is null or e.monthlySalary >= :minSalary) and "+
         "(:firstNameStartsWith is null or upper(e.firstName) LIKE concat(upper(:firstNameStartsWith),'%')) and " +
         "(e.startDate>:startt or e.startDate=:startt) and "+
         "(e.startDate<:endd or e.startDate=:endd)"
   )
   List<Employee> filterEmployees(Position position, Integer minSalary, String firstNameStartsWith, LocalDateTime startt, LocalDateTime endd);

   @Query("SELECT e.position, AVG(e.monthlySalary) " +
           "FROM Employee e " +
           "WHERE e.company.id = :companyId " +
           "GROUP BY e.position "+
           "ORDER BY AVG(e.monthlySalary) DESC")
   List<Object[]> getAvgSalariesByPositionUsingCompanyId(Long companyId);
}
