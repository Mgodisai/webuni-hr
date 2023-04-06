package hu.webuni.hr.alagi;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.repository.EmployeeRepositoryImpl;
import hu.webuni.hr.alagi.service.DateService;
import hu.webuni.hr.alagi.service.SalaryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

   private final SalaryService salaryService;
   private final DateService dateService;

   public HrApplication(SalaryService salaryService, DateService dateService) {
      this.salaryService = salaryService;
      this.dateService = dateService;
   }

   public static void main(String[] args) {
      SpringApplication.run(HrApplication.class, args);
   }

   @Override
   public void run(String... args) throws Exception {
      printResult(EmployeeRepositoryImpl.employee1);
      printResult(EmployeeRepositoryImpl.employee2);
      printResult(EmployeeRepositoryImpl.employee3);
      printResult(EmployeeRepositoryImpl.employee4);
      printResult(EmployeeRepositoryImpl.employee5);
      printResult(EmployeeRepositoryImpl.employee6);
   }

   private void printResult(Employee employee) {
      String pattern = "%s; years: %.2f; new salary: %d\n";
      System.out.printf(pattern,
            employee,
            dateService.calculateYearsBetweenDates(employee.getStartDate(), LocalDateTime.now()),
            salaryService.getNewMonthlySalary(employee)
      );
   }
}
