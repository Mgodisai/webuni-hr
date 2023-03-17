package hu.webuni.hr.alagi.controller.web;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Controller
public class EmployeeWebController {

   private final List<Employee> employees = new ArrayList<>();

   {
      employees.add(new Employee("József", "Java", Position.CEO, 10000, LocalDateTime.of(2017, Month.NOVEMBER, 1, 8,0 )));
      employees.add(new Employee("Géza", "Piton", Position.CUSTOMER_SUPPORT, 3000, LocalDateTime.of(2020, Month.JUNE, 15, 8,0 )));
      employees.add(new Employee("Paszkál", "Kis", Position.HR_MANAGER,2000, LocalDateTime.of(2023, Month.JANUARY, 5, 8,0 )));
      employees.add(new Employee("Tibor", "Kezdő", Position.TESTER, 1000, LocalDateTime.of(2003, Month.JANUARY, 5, 8,0 )));
      employees.add(new Employee("Kálmán", "Kóder", Position.DEVELOPER, 5000, LocalDateTime.of(2022, Month.JANUARY, 5, 8,0 )));
      employees.add(new Employee("Béla", "Adat", Position.ADMINISTRATOR, 900, LocalDateTime.of(2011, Month.JANUARY, 5, 8,0 )));
   }


   @GetMapping("/")
   public String getIndexPage() {
      return "index";
   }

   @GetMapping("/employees")
   public String getEmployees(Map<String, Object> model) {
      model.put("employees", employees);
      model.put("newEmployee", new Employee());
      return "employees";
   }

   @PostMapping("/employees")
   public String addNewEmployee(@ModelAttribute("newEmployee") Employee newEmployee, BindingResult result, RedirectAttributes redirectAttributes) {
      String message;
      boolean success;
      if (newEmployee!=null && !result.hasErrors()) {
         newEmployee.setStartDate(LocalDateTime.now());
         newEmployee.setId();
         employees.add(newEmployee);
         message = "Employee added successfully";
         success=true;
      } else {
         message = "Failed to add employee";
         success=false;
      }
      redirectAttributes.addFlashAttribute("message", message);
      redirectAttributes.addFlashAttribute("success", success);
      return "redirect:/employees";
   }

   @GetMapping("employees/delete/{id}")
   public String deleteIngredient(
         @PathVariable Long id,
         RedirectAttributes redirectAttributes) {
      Optional<Employee> deletingEmployee = employees.stream().filter(x-> Objects.equals(x.getId(), id)).findFirst();
      if (deletingEmployee.isPresent()) {
         employees.remove(deletingEmployee.get());
         redirectAttributes.addFlashAttribute("success",true);
         redirectAttributes.addFlashAttribute("message","Deleted successfully: "+deletingEmployee.get().getFullName());
      } else {
         redirectAttributes.addFlashAttribute("success",false);
         redirectAttributes.addFlashAttribute("message","Cannot find Employee with id:"+id);
      }
      return "redirect:/employees";
   }
}