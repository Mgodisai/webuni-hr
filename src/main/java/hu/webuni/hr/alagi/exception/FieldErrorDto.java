package hu.webuni.hr.alagi.exception;

public class FieldErrorDto {
   private String field;
   private String defaultMessage;
   private Object rejectedValue;


   public FieldErrorDto(String field, String defaultMessage, Object rejectedValue) {
      this.field = field;
      this.defaultMessage = defaultMessage;
      this.rejectedValue = rejectedValue;
   }

   public String getField() {
      return field;
   }

   public void setField(String field) {
      this.field = field;
   }

   public String getDefaultMessage() {
      return defaultMessage;
   }

   public void setDefaultMessage(String defaultMessage) {
      this.defaultMessage = defaultMessage;
   }

   public Object getRejectedValue() {
      return rejectedValue;
   }

   public void setRejectedValue(Object rejectedValue) {
      this.rejectedValue = rejectedValue;
   }
}
