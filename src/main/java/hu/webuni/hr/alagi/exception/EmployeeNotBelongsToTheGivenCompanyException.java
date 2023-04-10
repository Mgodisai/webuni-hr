package hu.webuni.hr.alagi.exception;

public class EmployeeNotBelongsToTheGivenCompanyException extends RuntimeException {

   public EmployeeNotBelongsToTheGivenCompanyException(long employeeId, long companyId) {
      super("Employee with id: "+employeeId+" not belongs to the company with id: "+companyId);
   }
}
