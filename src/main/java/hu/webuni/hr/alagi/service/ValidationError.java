package hu.webuni.hr.alagi.service;

import org.springframework.validation.FieldError;

import java.util.List;

public class ValidationError {
    private int errorCode;
    private String errorMessage;
    private List<FieldError> fieldErrorList;

    public ValidationError(int errorCode, String errorMessage, List<FieldError> fieldErrorList) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.fieldErrorList = fieldErrorList;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<FieldError> getFieldErrorList() {
        return fieldErrorList;
    }

    public void setFieldErrorList(List<FieldError> fieldErrorList) {
        this.fieldErrorList = fieldErrorList;
    }
}
