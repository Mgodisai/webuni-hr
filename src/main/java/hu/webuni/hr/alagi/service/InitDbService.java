package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;
import hu.webuni.hr.alagi.repository.CompanyRepository;
import hu.webuni.hr.alagi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class InitDbService {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public InitDbService(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void clearDB() {
        employeeRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Transactional
    public void insertTestData() {
        // Create companies
        Company companyA = new Company(null, "123", "Company A", "123 Main St", null);
        Company companyB = new Company(null, "456", "Company B", "456 Second St", null);

        // Create employees
        Employee employee1 = new Employee("John", "Doe", Position.DEVELOPER, 4000, LocalDateTime.now(), companyA);
        Employee employee2 = new Employee("Jane", "Doe", Position.ADMINISTRATOR, 6000, LocalDateTime.now(), companyA);
        Employee employee3 = new Employee("Bob", "Smith", Position.DEVELOPER, 4500, LocalDateTime.now(), companyB);
        Employee employee4 = new Employee("Alice", "Johnson", Position.CEO, 15500, LocalDateTime.now(), companyB);

        // Save companies and employees
        companyRepository.saveAll(Arrays.asList(companyA, companyB));
        employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3, employee4));
    }
}
