package hu.webuni.hr.alagi.repository;

import hu.webuni.hr.alagi.model.Employee;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MyCrudLikeRepository<Employee, Long> {

}
