package hu.webuni.hr.alagi.controller.web;

import hu.webuni.hr.alagi.model.Employee;
import hu.webuni.hr.alagi.service.EmployeeCrudService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

   private final EmployeeCrudService employeeCrudService;

   public EmployeeWebController(EmployeeCrudService employeeCrudService) {
      this.employeeCrudService = employeeCrudService;
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
         List<Employee> orderedEmployeeList = employeeCrudService.getAllEmployees().stream().sorted(c).toList();
         model.put("employees", orderedEmployeeList);
      } else {
         model.put("employees", employeeCrudService.getAllEmployees());
      }
      return "employees";
   }

   @GetMapping("/edit/{id}")
   public String showUpdateForm(
         @PathVariable Long id,
         Map<String, Object> model,
         final RedirectAttributes redirectAttributes) {
      model.put("action", "upd");
      Employee updatingEmployee = employeeCrudService.getEmployeeById(id);
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
   public String addNewEmployee(@ModelAttribute("employee") Employee employee, BindingResult result, RedirectAttributes redirectAttributes) {
      String message;
      boolean success;
      if (employee!=null && !result.hasErrors()) {
         if (employee.getStartDate()==null) {
            employee.setStartDate(LocalDateTime.now());
         }
         employeeCrudService.createEmployee(employee);
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
         Employee modifiedEmployee = employeeCrudService.getEmployeeById(id);
         if (modifiedEmployee!=null) {
            modifiedEmployee.setId(id);
            employeeCrudService.updateEmployee(modifiedEmployee);
            redirectAttributes.addFlashAttribute("success",true);
            redirectAttributes.addFlashAttribute("message","Updated successfully: "+modifiedEmployee.getFullName());
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
      if (employeeCrudService.isEmployeeExistsById(id)) {
         employeeCrudService.deleteEmployee(id);
         redirectAttributes.addFlashAttribute("success",true);
         redirectAttributes.addFlashAttribute("message","Deleted successfully!");
      } else {
         redirectAttributes.addFlashAttribute("success",false);
         redirectAttributes.addFlashAttribute("message","Cannot find Employee with id:"+id);
      }
      return "redirect:/employees";
   }
}