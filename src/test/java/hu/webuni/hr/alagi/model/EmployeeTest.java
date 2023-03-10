package hu.webuni.hr.alagi.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

class EmployeeTest {

   @Test
   void getEmploymentYears() {
      Employee employee1 = new Employee("Tamas", "Varga", 1000, LocalDateTime.of(2017, Month.NOVEMBER, 1, 8,0 ));
      Employee employee2 = new Employee("Tamas", "Varga", 1000, LocalDateTime.of(2023, Month.FEBRUARY, 5, 8,0 ));

      double delta = 0.0001;

      Assertions.assertEquals(5.3333, employee1.getEmploymentYears(), delta);
      Assertions.assertEquals(0.0833, employee2.getEmploymentYears(), delta);
   }
}