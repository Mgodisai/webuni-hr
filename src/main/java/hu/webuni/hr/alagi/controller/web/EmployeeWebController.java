package hu.webuni.hr.alagi.controller.web;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/employees")
public class EmployeeWebController {

   private final EmployeeService employeeService;

   public EmployeeWebController(EmployeeService employeeService) {
      this.employeeService = employeeService;
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
         List<Employee> orderedEmployeeList = employeeService.getAllEmployees().stream().sorted(c).toList();
         model.put("employees", orderedEmployeeList);
      } else {
         model.put("employees", employeeService.getAllEmployees());
      }
      return "employees";
   }

   @GetMapping("/edit/{id}")
   public String showUpdateForm(
         @PathVariable Long id,
         Map<String, Object> model,
         final RedirectAttributes redirectAttributes) {
      model.put("action", "upd");
      Employee updatingEmployee = employeeService.getEmployeeById(id);
      if(updatingEmployee!=null) {
         model.putIfAbsent("employee", updatingEmployee);
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
         model.put("employee", newEmployee);
      }
      return "employee-form";
   }

   @PostMapping("/new")
   public String addNewEmployee(
         @ModelAttribute @Valid Employee employee,
         BindingResult result,
         RedirectAttributes redirectAttributes) {
      if (employee!=null && !result.hasErrors()) {
         if (employee.getStartDate()==null) {
            employee.setStartDate(LocalDateTime.now());
         }
         employeeService.createEmployee(employee);
         redirectAttributes.addFlashAttribute("message", "Employee added successfully");
         redirectAttributes.addFlashAttribute("success", true);
         return "redirect:/employees";
      } else {
         StringBuilder sb = new StringBuilder("Failed to add employee:*");
         for (FieldError f : result.getFieldErrors()) {
            sb.append(f.getDefaultMessage()).append("*");
         }
         redirectAttributes.addFlashAttribute("message", sb.toString());
         redirectAttributes.addFlashAttribute("success", false);
         redirectAttributes.addFlashAttribute("employee", employee);
         return "redirect:/employees/new";
      }
   }

   @PostMapping("/edit/{id}")
   public String updateEmployee(
         @PathVariable Long id,
         @ModelAttribute @Valid Employee employee,
         BindingResult result,
         final RedirectAttributes redirectAttributes) {

      String view;
      if (result.hasErrors()) {
         StringBuilder sb = new StringBuilder("Failed to update employee:*");
         for (FieldError f : result.getFieldErrors()) {
            sb.append(f.getDefaultMessage()).append("*");
         }
         redirectAttributes.addFlashAttribute("success",false);
         redirectAttributes.addFlashAttribute("message",sb.toString());
         redirectAttributes.addFlashAttribute("employee", employee);
         view = "redirect:/employees/edit/"+id;
      } else {
         if (employeeService.isEmployeeExistsById(id)) {
            employee.setId(id);
            if (employee.getStartDate()==null) {
               employee.setStartDate(LocalDateTime.now());
            }
            employeeService.updateEmployee(employee);
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
      if (employeeService.isEmployeeExistsById(id)) {
         employeeService.deleteEmployee(id);
         redirectAttributes.addFlashAttribute("success",true);
         redirectAttributes.addFlashAttribute("message","Deleted successfully!");
      } else {
         redirectAttributes.addFlashAttribute("success",false);
         redirectAttributes.addFlashAttribute("message","Cannot find Employee with id:"+id);
      }
      return "redirect:/employees";
   }
}