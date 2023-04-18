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
        Company companyA = new Company(null, "123", "Company A", "7400 Kaposvár, Fő u. 2", null);
        Company companyB = new Company(null, "456", "Company B", "7400 Kaposvár, Fő u. 1", null);

        Employee employee1 = new Employee(null, "Gyula", "Kis", Position.DEVELOPER, 4000, LocalDateTime.now(), companyA);
        Employee employee2 = new Employee(null, "Géza", "Nagy", Position.ADMINISTRATOR, 6000, LocalDateTime.now(), companyA);
        Employee employee3 = new Employee(null, "Pál", "Kovács", Position.DEVELOPER, 4500, LocalDateTime.now(), companyA);
        Employee employee4 = new Employee(null, "Mária", "Tóth", Position.CEO, 15500, LocalDateTime.now(), companyB);

        companyRepository.saveAll(Arrays.asList(companyA, companyB));
        employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3, employee4));
    }
}
