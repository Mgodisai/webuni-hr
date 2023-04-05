package hu.webuni.hr.alagi.exception;

import org.springframework.validation.FieldError;

import java.util.List;

public class ValidationErrorEntity extends DefaultErrorEntity {
    private List<FieldError> fieldErrorList;

    public ValidationErrorEntity(int errorCode, String errorMessage, List<FieldError> fieldErrorList) {
        super(errorCode, errorMessage);
        this.fieldErrorList = fieldErrorList;
    }

    public List<FieldError> getFieldErrorList() {
        return fieldErrorList;
    }

    public void setFieldErrorList(List<FieldError> fieldErrorList) {
        this.fieldErrorList = fieldErrorList;
    }
}
