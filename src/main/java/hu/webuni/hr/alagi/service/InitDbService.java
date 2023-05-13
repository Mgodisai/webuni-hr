package hu.webuni.hr.alagi.service;

import hu.webuni.hr.alagi.model.*;
import hu.webuni.hr.alagi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public InitDbService(CompanyRepository companyRepository, EmployeeRepository employeeRepository, CompanyTypeRepository companyTypeRepository, PositionRepository positionRepository, LeaveRequestRepository leaveRequestRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.companyTypeRepository = companyTypeRepository;
        this.positionRepository = positionRepository;
        this.leaveRequestRepository = leaveRequestRepository;
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
        Employee employee2 = new Employee( "Géza", "Nagy", softwareDesigner, 6000, LocalDateTime.now(), companyA);
        Employee employee3 = new Employee( "Pál", "Kovács", developer, 4500, LocalDateTime.now(), companyA);
        Employee employee4 = new Employee("Mária", "Tóth", ceo, 15500, LocalDateTime.now(), companyB);
        Employee employee5 = new Employee("Tibor", "Tóth", administrator, 2500, LocalDateTime.now(), null);

        employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3, employee4, employee5));

        LeaveRequest leaveRequest1 = new LeaveRequest(LocalDate.of(2023, 2,1), LocalDate.of(2023,2,10), LocalDateTime.now(), LeaveRequestStatus.PENDING, employee1, null, null);
        LeaveRequest leaveRequest2 = new LeaveRequest(LocalDate.of(2023, 3,5), LocalDate.of(2023,5,10), LocalDateTime.now(), LeaveRequestStatus.PENDING, employee2, null, null);
        LeaveRequest leaveRequest3 = new LeaveRequest(LocalDate.of(2023, 4,10), LocalDate.of(2023,5,15), LocalDateTime.now(), LeaveRequestStatus.PENDING, employee3, null, null);
        LeaveRequest leaveRequest4 = new LeaveRequest(LocalDate.of(2023, 5,10), LocalDate.of(2023,5,15), LocalDateTime.now(), LeaveRequestStatus.PENDING, employee3, null, null);

        leaveRequestRepository.saveAll(Arrays.asList(leaveRequest1, leaveRequest2, leaveRequest3, leaveRequest4));
    }
}
