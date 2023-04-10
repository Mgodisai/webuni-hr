package hu.webuni.hr.alagi.controller.rest;

import hu.webuni.hr.alagi.dto.EmployeeDto;
import hu.webuni.hr.alagi.exception.DefaultErrorEntity;
import hu.webuni.hr.alagi.exception.ValidationErrorEntity;
import hu.webuni.hr.alagi.model.Position;
import hu.webuni.hr.alagi.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeRestControllerIT {

   private static final String BASE_URI="/api/employees";

   @Autowired
   private WebTestClient webTestClient;

   @Autowired
   private EmployeeService employeeService;

   @Autowired
   private EmployeeMapper employeeMapper;

   public List<EmployeeDto> getAllEmployees() {
      return webTestClient
            .get()
            .uri(BASE_URI)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(EmployeeDto.class)
            .returnResult().getResponseBody();
   }

   @Test
   public void testEmployeeListsEquality() {
      assertThat(employeeMapper.employeesToDtos(employeeService.getAllEmployees()))
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyElementsOf(getAllEmployees());
   }

   @Test
   public void testGetEmployeeByValidId() {
      Long validEmployeeDtoId = 3L;
      webTestClient
            .get()
            .uri(BASE_URI+"/{id}", validEmployeeDtoId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(EmployeeDto.class);
   }

   @Test
   public void testGetEmployeeByIdInvalidId() {
      Long invalidEmployeeDtoId = -1L;
      webTestClient.get()
            .uri(BASE_URI+"/{id}", invalidEmployeeDtoId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(DefaultErrorEntity.class)
            .consumeWith(response->{
              DefaultErrorEntity e = response.getResponseBody();
               assertThat(e).isNotNull();
               assertThat(e.getErrorCode()).isEqualTo(1002);
               assertThat(e.getErrorMessage()).isEqualTo("Employee not exists with id: "+invalidEmployeeDtoId);
            });
   }

   @Test
   public void testAddNewValidEmployeeDto() {
      EmployeeDto newEmployee = new EmployeeDto(11L, "First", "Last", Position.DEVELOPER, 10, LocalDateTime.now(), null);
      webTestClient.post()
            .uri(BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newEmployee)
            .exchange()
            .expectStatus().isOk()
            .expectBody(EmployeeDto.class);
   }

   @Test
   public void testAddNewValidEmployeeDtoWithExistingIdUsingPost() {
      EmployeeDto newEmployee = new EmployeeDto(2L, "First", "Last", Position.DEVELOPER, 10, LocalDateTime.now(), null);
      webTestClient.post()
            .uri(BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newEmployee)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(DefaultErrorEntity.class)
            .consumeWith(response->{
               DefaultErrorEntity e = response.getResponseBody();
               assertThat(e).isNotNull();
               assertThat(e.getErrorCode()).isEqualTo(1001);
               assertThat(e.getErrorMessage()).isEqualTo("Employee is already exists with id: "+newEmployee.getId());
            });
   }


   @Test
   public void testUpdateValidEmployeeById() {
      Long testEmployeeId = 4L;
      EmployeeDto employeeDtoBefore = new EmployeeDto(11L, "First", "Last", Position.DEVELOPER, 10, LocalDateTime.now(), null);

      webTestClient.put()
            .uri(BASE_URI+"/{id}", testEmployeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(employeeDtoBefore)
            .exchange()
            .expectStatus().isOk()
            .expectBody(EmployeeDto.class)
            .consumeWith(response->{
               EmployeeDto employeeDto = response.getResponseBody();
               assertThat(employeeDto).isNotNull();
               assertThat(employeeDto.getId()).isEqualTo(testEmployeeId);
            });
   }

   @Test
   public void testAddNewEmployeeDtoWithInvalidSalary() {
      EmployeeDto newEmployee = new EmployeeDto(11L, "First", "Last", Position.DEVELOPER, -10, LocalDateTime.now(), null);
      EmployeeDtoValidationTestWithOneInvalidField(newEmployee, "monthlySalary", newEmployee.getMonthlySalary());
   }

   @Test
   public void testAddNewEmployeeDtoWithEmptyFirstName() {
      EmployeeDto newEmployee = new EmployeeDto(11L, "", "Last", Position.DEVELOPER, 10, LocalDateTime.now(), null);
      EmployeeDtoValidationTestWithOneInvalidField(newEmployee, "firstName", newEmployee.getFirstName());
   }

   @Test
   public void testAddNewEmployeeDtoWithEmptyLastName() {
      EmployeeDto newEmployee = new EmployeeDto(11L, "First", "", Position.DEVELOPER, 10, LocalDateTime.now(), null);
      EmployeeDtoValidationTestWithOneInvalidField(newEmployee, "lastName", newEmployee.getLastName());
   }

   @Test
   public void testAddNewEmployeeDtoWithNullLastName() {
      EmployeeDto newEmployee = new EmployeeDto(11L, "First", null, Position.DEVELOPER, 10, LocalDateTime.now(), null);
      EmployeeDtoValidationTestWithOneInvalidField(newEmployee, "lastName", newEmployee.getLastName());
   }

   @Test
   public void testAddNewEmployeeDtoWithWrongDate() {
      EmployeeDto newEmployee = new EmployeeDto(11L, "First", "Last", Position.DEVELOPER, 10, LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS), null);
      EmployeeDtoValidationTestWithOneInvalidField(newEmployee, "startDate", newEmployee.getStartDate().toString());
   }


   public void EmployeeDtoValidationTestWithOneInvalidField(EmployeeDto employeeDto, String field, Object wrongValue) {
      webTestClient.post()
            .uri(BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(employeeDto)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(ValidationErrorEntity.class)
            .consumeWith(response->{
               ValidationErrorEntity errorEntity = response.getResponseBody();
               assertThat(errorEntity).isNotNull();
               assertThat(errorEntity.getFieldErrorList().size()).isEqualTo(1);
               assertThat(errorEntity.getErrorCode()).isEqualTo(1000);
               assertThat(errorEntity.getFieldErrorList().get(0).getField()).isEqualTo(field);
               assertThat(errorEntity.getFieldErrorList().get(0).getRejectedValue()).isEqualTo(wrongValue);
            });
   }

   @Test
   public void testDeleteEmployeeByValidId() {
      Long testEmployeeId = 1L;
      webTestClient.delete()
            .uri(BASE_URI+"/{id}", testEmployeeId)
            .exchange()
            .expectStatus().isNoContent();
   }

   @Test
   public void testDeleteEmployeeByInvalidId() {
      Long invalidEmployeeDtoId = -1L;
      webTestClient.delete()
            .uri(BASE_URI+"/{id}", invalidEmployeeDtoId)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(DefaultErrorEntity.class)
            .consumeWith(response->{
               DefaultErrorEntity e = response.getResponseBody();
               assertThat(e).isNotNull();
               assertThat(e.getErrorCode()).isEqualTo(1002);
               assertThat(e.getErrorMessage()).isEqualTo("Employee not exists with id: "+invalidEmployeeDtoId);
            });
   }

//   @Test
//   public void testGetSalaryRaisePercentForEmployee() {
//      EmployeeDto testEmployee = new EmployeeDto(/* fill in constructor arguments */);
//      webTestClient.get()
//            .uri(BASE_URI+"/raise-percentage")
//            .bodyValue(testEmployee)
//            .exchange()
//            .expectStatus().isOk()
//            .expectBody(Integer.class);
//   }
}
