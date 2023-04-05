package hu.webuni.hr.alagi.exception;

public class EntityAlreadyExistsException extends RuntimeException {

   public EntityAlreadyExistsException(long id, Class<?> clazz) {
      super(clazz.getSimpleName()+ " is already exists with id: "+id);
   }
}
