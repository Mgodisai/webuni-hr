package hu.webuni.hr.alagi;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;
import hu.webuni.hr.alagi.service.DateService;
import hu.webuni.hr.alagi.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.Month;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

   @Autowired
   private SalaryService salaryService;
   @Autowired
   private DateService dateService;
   public static void main(String[] args) {
      SpringApplication.run(HrApplication.class, args);
   }

   @Override
   public void run(String... args) throws Exception {
      Employee employee1 = new Employee("József", "Java", Position.CEO, 10000, LocalDateTime.of(2017, Month.NOVEMBER, 1, 8,0 ));
      Employee employee2 = new Employee("Géza", "Piton", Position.CUSTOMER_SUPPORT, 3000, LocalDateTime.of(2020, Month.JUNE, 15, 8,0 ));
      Employee employee3 = new Employee("Paszkál", "Kis", Position.HR_MANAGER,2000, LocalDateTime.of(2023, Month.JANUARY, 5, 8,0 ));
      Employee employee4 = new Employee("Tibor", "Kezdő", Position.TESTER, 1000, LocalDateTime.of(2003, Month.JANUARY, 5, 8,0 ));
      Employee employee5 = new Employee("Kálmán", "Kóder", Position.DEVELOPER, 5000, LocalDateTime.of(2022, Month.JANUARY, 5, 8,0 ));
      Employee employee6 = new Employee("Béla", "Adat", Position.ADMINISTRATOR, 900, LocalDateTime.of(2011, Month.JANUARY, 5, 8,0 ));

      printResult(employee1);
      printResult(employee2);
      printResult(employee3);
      printResult(employee4);
      printResult(employee5);
      printResult(employee6);
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
