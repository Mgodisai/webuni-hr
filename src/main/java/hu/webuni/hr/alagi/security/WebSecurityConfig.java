package hu.webuni.hr.alagi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

   @Autowired
   UserDetailsService userDetailsService;

   @Bean
   public AuthenticationManager authManager(HttpSecurity http) throws Exception {
      AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
      return auth.authenticationProvider(authenticationProvider()).build();
   }

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
      http
            .httpBasic()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers(endpointProvider().getUnAuthenticatedEndpoints()).permitAll()
            .requestMatchers(endpointProvider().getAuthenticatedEndpoints()).authenticated()
            .anyRequest().authenticated();

      return http.build();
   }

   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public AuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
      daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
      daoAuthenticationProvider.setUserDetailsService(userDetailsService);
      return daoAuthenticationProvider;
   }

   @Bean
   public EndpointProvider endpointProvider() {
      return new DefaultEndpointProvider();
   }
}
