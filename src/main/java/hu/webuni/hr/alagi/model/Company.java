package hu.webuni.hr.alagi.model;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private Long id;
    private String registerNumber;
    private String name;
    private String address;
    private List<Employee> employeeList;


    public Company(Long id, String registerNumber, String name, String address, List<Employee> employeeList) {
        this.id = id;
        this.registerNumber = registerNumber;
        this.name = name;
        this.address = address;
        this.employeeList = employeeList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = new ArrayList<>(employeeList);
    }
}
