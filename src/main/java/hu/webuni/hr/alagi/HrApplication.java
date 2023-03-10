package hu.webuni.hr.alagi;

import hu.webuni.hr.alagi.model.Employee;
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

   public static void main(String[] args) {
      SpringApplication.run(HrApplication.class, args);
   }

   @Override
   public void run(String... args) throws Exception {
      Employee employee1 = new Employee("Tamas", "Varga", 1000, LocalDateTime.of(2017, Month.NOVEMBER, 1, 8,0 ));
      Employee employee2 = new Employee("Tamas", "Varga", 1000, LocalDateTime.of(2020, Month.JUNE, 15, 8,0 ));
      Employee employee3 = new Employee("Tamas", "Varga", 1000, LocalDateTime.of(2023, Month.JANUARY, 5, 8,0 ));
      Employee employee4 = new Employee("Tamas", "Varga", 1000, LocalDateTime.of(2003, Month.JANUARY, 5, 8,0 ));
      Employee employee5 = new Employee("Tamas", "Varga", 1000, LocalDateTime.of(2022, Month.JANUARY, 5, 8,0 ));
      Employee employee6 = new Employee("Tamas", "Varga", 1000, LocalDateTime.of(2011, Month.JANUARY, 5, 8,0 ));

      System.out.println(employee1+", new salary: "+salaryService.getNewMonthlySalary(employee1));
      System.out.println(employee2+", new salary: "+salaryService.getNewMonthlySalary(employee2));
      System.out.println(employee3+", new salary: "+salaryService.getNewMonthlySalary(employee3));
      System.out.println(employee4+", new salary: "+salaryService.getNewMonthlySalary(employee4));
      System.out.println(employee5+", new salary: "+salaryService.getNewMonthlySalary(employee5));
      System.out.println(employee6+", new salary: "+salaryService.getNewMonthlySalary(employee6));

   }
}
