package hu.webuni.hr.alagi.security;

import hu.webuni.hr.alagi.model.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class HrUser extends User {

   public HrUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Employee employee) {
      super(username, password, authorities);
      this.employee = employee;
   }

   private Employee employee;

   public Employee getEmployee() {
      return employee;
   }

   public void setEmployee(Employee employee) {
      this.employee = employee;
   }

}
