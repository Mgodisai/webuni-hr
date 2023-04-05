package hu.webuni.hr.alagi.exception;

public class EntityNotExistsWithGivenIdException extends RuntimeException {

   public EntityNotExistsWithGivenIdException(long id, Class<?> clazz) {
      super(clazz.getSimpleName() + " not exists with id: "+id);
   }
}
