package hu.webuni.hr.alagi.service;

import java.time.LocalDateTime;

public interface DateService {
   double calculateYearsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
}
