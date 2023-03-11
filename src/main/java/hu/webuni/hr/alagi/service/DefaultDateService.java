package hu.webuni.hr.alagi.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;

@Service
public class DefaultDateService implements DateService{

   @Override
   public double calculateYearsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
      return Period.between(startDate.toLocalDate(), LocalDateTime.now().toLocalDate()).toTotalMonths()/12d;
   }
}
