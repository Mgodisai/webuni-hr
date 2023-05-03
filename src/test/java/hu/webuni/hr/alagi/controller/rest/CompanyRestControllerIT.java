package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.CompanyDto;
import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.exception.DefaultErrorEntity;
import hu.webuni.hr.alagi.model.Company;
import hu.webuni.hr.alagi.service.CompanyService;
import hu.webuni.hr.alagi.service.InitDbService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompanyRestControllerIT {

    private static final String BASE_URI="/api/companies";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private InitDbService initDbService;

    @Before(value="create-drop")
    public void setDb() {
        initDbService.clearDB();
        initDbService.insertTestData();
    }

    @Test
    void testAddNewEmployeeToValidCompany() {
        Long validCompanyId = 1L;
        Company companyBefore = companyService.getCompanyById(validCompanyId, true);

        EmployeeDto newEmployeeDto = new EmployeeDto(null, "First", "Last", "administrator", 100, LocalDateTime.now(), null);
        webTestClient
                .post()
                .uri(BASE_URI+"/{id}/employees", validCompanyId)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(List.of(newEmployeeDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CompanyDto.class)
                .consumeWith(response->{
                    CompanyDto companyDto = response.getResponseBody();
                    assertThat(companyDto).isNotNull();
                    assertThat(companyDto.getId()).isEqualTo(validCompanyId);
                    List<EmployeeDto> employeeDtoList= companyDto.getEmployeeDtoList();
                    assertThat(employeeDtoList).hasSize(companyBefore.getEmployeeList().size()+1);
                    assertThat(employeeDtoList).contains(newEmployeeDto);

                    Company companyAfter = companyService.getCompanyById(validCompanyId, true);
                    boolean allEmployeesMatchCompany = companyAfter.getEmployeeList().stream()
                            .allMatch(emp -> emp.getCompany().getId().equals(validCompanyId));
                    assertThat(allEmployeesMatchCompany).isTrue();
                });
    }

    @Test
    void testAddNewEmployeeToInvalidCompany() {
        Long invalidCompanyId = -1L;
        EmployeeDto newEmployeeDto = new EmployeeDto(null, "First", "Last", "administrator", 100, LocalDateTime.now(), null);

        webTestClient
                .post()
                .uri(BASE_URI+"/{id}/employees", invalidCompanyId)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(List.of(newEmployeeDto))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(DefaultErrorEntity.class)
                .consumeWith(response->{
                    DefaultErrorEntity e = response.getResponseBody();
                    assertThat(e).isNotNull();
                    assertThat(e.getErrorCode()).isEqualTo(1002);
                    assertThat(e.getErrorMessage()).isEqualTo("Company not exists with id: "+invalidCompanyId);
                });
    }

    @Test
    void removeEmployeeToCompanyByEmployeeId() {
    }

    @Test
    void updateEmployeesOfCompany() {
        Long validCompanyId = 2L;
        Company companyBefore = companyService.getCompanyById(validCompanyId, true);

        EmployeeDto newEmployeeDto1 = new EmployeeDto(null, "First1", "Last2", "administrator", 100, LocalDateTime.now(), null);
        EmployeeDto newEmployeeDto2 = new EmployeeDto(null, "First2", "Last2", "administrator", 100, LocalDateTime.now(), null);

        webTestClient
                .put()
                .uri(BASE_URI+"/{id}/employees", validCompanyId)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(List.of(newEmployeeDto1, newEmployeeDto2))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CompanyDto.class)
                .consumeWith(response->{
                    CompanyDto companyDto = response.getResponseBody();
                    assertThat(companyDto).isNotNull();
                    assertThat(companyDto.getId()).isEqualTo(validCompanyId);
                    List<EmployeeDto> employeeDtoList= companyDto.getEmployeeDtoList();
                    assertThat(employeeDtoList).hasSize(2);
                    assertThat(employeeDtoList).containsExactly(newEmployeeDto1, newEmployeeDto2);

                    Company companyAfter = companyService.getCompanyById(validCompanyId, true);
                    boolean allEmployeesMatchCompany = companyAfter.getEmployeeList().stream()
                            .allMatch(emp -> emp.getCompany().getId().equals(validCompanyId));
                    assertThat(allEmployeesMatchCompany).isTrue();
                });
    }
}