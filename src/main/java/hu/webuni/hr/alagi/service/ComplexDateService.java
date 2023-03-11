package hu.webuni.hr.alagi.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;

@Service
@Primary
public class ComplexDateService implements DateService{
   @Override
   public double calculateYearsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
      LocalDate convertedStartDate = startDate.toLocalDate();
      LocalDate convertedEndDate = endDate.toLocalDate();

      int partialDaysInStartDateYear = getDaysOfYear(convertedStartDate.getYear())-startDate.getDayOfYear();
      int partialDaysInEndDateYear = endDate.getDayOfYear();

      double partialStartDateYear = (double)partialDaysInStartDateYear/(double)getDaysOfYear(convertedStartDate.getYear());
      double partialEndDateYear = (double)partialDaysInEndDateYear/(double)getDaysOfYear(convertedEndDate.getYear());

      if (convertedStartDate.getYear()==convertedEndDate.getYear()) {
         return partialStartDateYear+partialEndDateYear-1;
      } else {
         return -1.0+convertedEndDate.getYear()-convertedStartDate.getYear()+partialStartDateYear+partialEndDateYear;
      }
   }

   private int getDaysOfYear(long year) {
      return Year.isLeap(year)?366:365;
   }
}
