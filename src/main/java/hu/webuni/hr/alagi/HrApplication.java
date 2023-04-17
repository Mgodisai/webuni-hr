package hu.webuni.hr.alagi;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.service.DateService;
import hu.webuni.hr.alagi.service.InitDbService;
import hu.webuni.hr.alagi.service.SalaryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

   private final SalaryService salaryService;
   private final DateService dateService;
   private final InitDbService initDbService;

   public HrApplication(SalaryService salaryService, DateService dateService, InitDbService initDbService) {
      this.salaryService = salaryService;
      this.dateService = dateService;
      this.initDbService = initDbService;
   }

   public static void main(String[] args) {
      SpringApplication.run(HrApplication.class, args);
   }

   @Override
   public void run(String... args) throws Exception {

      initDbService.clearDB();
      initDbService.insertTestData();

//      printResult(EmployeeRepositoryImpl.employee1);
//      printResult(EmployeeRepositoryImpl.employee2);
//      printResult(EmployeeRepositoryImpl.employee3);
//      printResult(EmployeeRepositoryImpl.employee4);
//      printResult(EmployeeRepositoryImpl.employee5);
//      printResult(EmployeeRepositoryImpl.employee6);
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
