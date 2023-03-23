package hu.webuni.hr.alagi.controller.web;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.model.Position;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Controller
@RequestMapping("/employees")
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

   @GetMapping
   public String getEmployees(
         Map<String, Object> model,
         @RequestParam(required = false) String sortBy,
         @RequestParam(required = false) String direction) {
      if (sortBy!=null) {
          Comparator<Employee> c = switch (sortBy) {
            case "firstName" -> Comparator.comparing(Employee::getFirstName);
            case "lastName" -> Comparator.comparing(Employee::getLastName);
            case "monthlySalary" -> Comparator.comparing(Employee::getMonthlySalary);
            case "startDate" -> Comparator.comparing(Employee::getStartDate);
            default -> Comparator.comparing(Employee::getId);
         };
          if (Objects.equals(direction, "desc")) {
             c = c.reversed();
          }
         List<Employee> orderedEmployeeList = employees.stream().sorted(c).toList();
         model.put("employees", orderedEmployeeList);
      } else {
         model.put("employees", employees);
      }
      return "employees";
   }

   @GetMapping("/edit/{id}")
   public String showUpdateForm(
         @PathVariable Long id,
         Map<String, Object> model,
         final RedirectAttributes redirectAttributes) {
      model.put("action", "upd");
      Optional<Employee> updatingEmployee = employees.stream().filter(e-> Objects.equals(e.getId(), id)).findFirst();

      if(updatingEmployee.isPresent()) {
         model.putIfAbsent("employee", updatingEmployee.get());
         return "employee-form";
      } else {
         redirectAttributes.addFlashAttribute("success",false);
         redirectAttributes.addFlashAttribute("message","Employee not exists with id: "+id);
      }
      return "redirect:/employees";
   }

   @GetMapping("/new")
   public String showEmptyForm(Map<String, Object> model) {
      model.put("action", "new");
      if (model.get("employee")==null) {
         Employee newEmployee = new Employee();
         newEmployee.setId();
         model.put("employee", newEmployee);
      }
      return "employee-form";
   }

   @PostMapping("/new")
   public String addNewEmployee(@ModelAttribute("employee") Employee employee, BindingResult result, RedirectAttributes redirectAttributes) {
      String message;
      boolean success;
      if (employee!=null && !result.hasErrors()) {
         if (employee.getStartDate()==null) {
            employee.setStartDate(LocalDateTime.now());
         }
         employees.add(employee);
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

   @PostMapping("/edit/{id}")
   public String updateEmployee(
         @PathVariable Long id,
         @ModelAttribute("employee") Employee employee,
         BindingResult result,
         final RedirectAttributes redirectAttributes) {

      String view;
      if (result.hasErrors()) {
         redirectAttributes.addFlashAttribute("success",false);
         redirectAttributes.addFlashAttribute("message","Update failed!");
         redirectAttributes.addFlashAttribute("employee", employee);
         view = "redirect:/employees/edit/"+id;
      } else {
         Optional<Employee> modifiedEmployee = employees.stream().filter(e-> Objects.equals(e.getId(), id)).findFirst();
         if (modifiedEmployee.isPresent()) {
            int index = employees.indexOf(modifiedEmployee.get());
            employees.set(index, employee);
            redirectAttributes.addFlashAttribute("success",true);
            redirectAttributes.addFlashAttribute("message","Updated successfully: "+employee.getFullName());
         } else {
            redirectAttributes.addFlashAttribute("success",false);
            redirectAttributes.addFlashAttribute("message","Employee not exists with id: "+id);
         }
         view = "redirect:/employees";
      }
      return view;
   }

   @GetMapping("/delete/{id}")
   public String deleteEmployee(
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