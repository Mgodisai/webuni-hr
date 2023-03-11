package hu.webuni.hr.alagi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

class ComplexDateServiceTest {
   DateService dateService;

   @BeforeEach
   void setUp() {
      dateService = new ComplexDateService();
   }

   @Test
   void calculateYearsBetweenDates() {
      LocalDateTime endDate1 = LocalDateTime.of(2023, Month.MARCH, 12, 8, 0);
      LocalDateTime startDate1 = LocalDateTime.of(2023, Month.MARCH, 10, 8, 0);
      LocalDateTime startDate2 = LocalDateTime.of(2022, Month.DECEMBER, 10, 8, 0);
      LocalDateTime startDate3 = LocalDateTime.of(2021, Month.DECEMBER, 10, 8, 0);

      LocalDateTime endDate2 = LocalDateTime.of(2024, Month.MARCH, 12, 8, 0);
      LocalDateTime startDate4 = LocalDateTime.of(2023, Month.DECEMBER, 10, 8, 0);

      double delta = 0.00001;

      Assertions.assertEquals(2d/365d, dateService.calculateYearsBetweenDates(startDate1, endDate1), delta);
      Assertions.assertEquals((21/365d)+(31+28+12)/365d, dateService.calculateYearsBetweenDates(startDate2, endDate1), delta);
      Assertions.assertEquals((21/365d)+(31+28+12)/365d+1, dateService.calculateYearsBetweenDates(startDate3, endDate1), delta);
      Assertions.assertEquals((21/365d)+(31+29+12)/366d, dateService.calculateYearsBetweenDates(startDate4, endDate2), delta);

   }
}