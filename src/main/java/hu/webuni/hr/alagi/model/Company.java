package hu.webuni.hr.alagi.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_type_id")
    private CompanyType companyType;
    private String registerNumber;
    private String name;
    private String address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Employee> employeeList = new ArrayList<>();

    public Company(Long id, CompanyType companyType, String registerNumber, String name, String address, List<Employee> employeeList) {
        this.id = id;
        this.companyType = companyType;
        this.registerNumber = registerNumber;
        this.name = name;
        this.address = address;
        this.employeeList = employeeList;
    }

    public Company(CompanyType companyType, String registerNumber, String name, String address, List<Employee> employeeList) {
        this.companyType = companyType;
        this.registerNumber = registerNumber;
        this.name = name;
        this.address = address;
        this.employeeList = employeeList;
    }

    public Company() {

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
        this.employeeList = employeeList;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }
}
