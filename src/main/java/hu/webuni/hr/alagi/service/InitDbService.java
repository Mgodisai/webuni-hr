package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.*;
import hu.webuni.hr.alagi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class InitDbService {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final CompanyTypeRepository companyTypeRepository;
    private final PositionRepository positionRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitDbService(CompanyRepository companyRepository, EmployeeRepository employeeRepository, CompanyTypeRepository companyTypeRepository, PositionRepository positionRepository, LeaveRequestRepository leaveRequestRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.companyTypeRepository = companyTypeRepository;
        this.positionRepository = positionRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void clearDB() {
        employeeRepository.deleteAll();
        companyRepository.deleteAll();
        companyTypeRepository.deleteAll();
        positionRepository.deleteAll();
        leaveRequestRepository.deleteAll();
    }

    @Transactional
    public void insertTestData() {
        Position developer = new Position("developer", Qualification.BSC);
        Position ceo = new Position("ceo", Qualification.PHD);
        Position tester = new Position("administrator", Qualification.BSC);
        Position softwareDesigner = new Position("software designer", Qualification.MSC);
        Position administrator = new Position("administrator", Qualification.MATURA);
        positionRepository.saveAll(Arrays.asList(developer, ceo, tester, softwareDesigner));

        CompanyType bt = new CompanyType("Bt", "Betéti Társaság");
        CompanyType kft = new CompanyType("Kft", "Korlátolt Felelősségű Társaság");
        CompanyType zrt = new CompanyType("Zrt", "Zártkörűen Működő Részvénytársaság");
        CompanyType nyrt = new CompanyType("Nyrt", "Nyilvánosan Működő Részvénytársaság");
        companyTypeRepository.saveAll(Arrays.asList(bt, kft, zrt, nyrt));

        Company companyA = new Company(bt, "123", "A Bt.", "7400 Kaposvár, Fő u. 2", null);
        Company companyB = new Company(kft, "456", "B Kft.", "7400 Kaposvár, Fő u. 1", null);
        companyRepository.saveAll(Arrays.asList(companyA, companyB));

        Employee employee1 = new Employee("Gyula", "Kis", tester, 4000, LocalDateTime.now(), companyA);
        employee1.setUsername("emp1");
        employee1.setPassword(passwordEncoder.encode("pass1"));
        Employee employee2 = new Employee( "Géza", "Nagy", softwareDesigner, 6000, LocalDateTime.now(), companyA);
        employee2.setUsername("emp2");
        employee2.setPassword(passwordEncoder.encode("pass2"));
        employee2.setManager(employee1);
        Employee employee3 = new Employee( "Pál", "Kovács", developer, 4500, LocalDateTime.now(), companyA);
        employee3.setUsername("emp3");
        employee3.setPassword(passwordEncoder.encode("pass3"));
        employee3.setManager(employee2);
        Employee employee4 = new Employee("Mária", "Tóth", ceo, 15500, LocalDateTime.now(), companyB);
        employee4.setUsername("emp4");
        employee4.setPassword(passwordEncoder.encode("pass4"));
        employee4.setManager(employee1);
        Employee employee5 = new Employee("Tibor", "Tóth", administrator, 2500, LocalDateTime.now(), null);
        employee5.setUsername("emp5");
        employee5.setPassword(passwordEncoder.encode("pass5"));
        employee5.setManager(employee2);
        employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3, employee4, employee5));

        LeaveRequest leaveRequest1 = new LeaveRequest(LocalDate.of(2023, 2,1), LocalDate.of(2023,2,10), LocalDateTime.now(), LeaveRequestStatus.PENDING, employee1, null, null);
        LeaveRequest leaveRequest2 = new LeaveRequest(LocalDate.of(2023, 3,5), LocalDate.of(2023,5,10), LocalDateTime.now(), LeaveRequestStatus.PENDING, employee2, null, null);
        LeaveRequest leaveRequest3 = new LeaveRequest(LocalDate.of(2023, 4,10), LocalDate.of(2023,5,15), LocalDateTime.now(), LeaveRequestStatus.PENDING, employee3, null, null);
        LeaveRequest leaveRequest4 = new LeaveRequest(LocalDate.of(2023, 5,10), LocalDate.of(2023,5,15), LocalDateTime.now(), LeaveRequestStatus.PENDING, employee3, null, null);

        leaveRequestRepository.saveAll(Arrays.asList(leaveRequest1, leaveRequest2, leaveRequest3, leaveRequest4));
    }
}
