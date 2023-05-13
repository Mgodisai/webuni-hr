package hu.webuni.hr.alagi.security;

public class DefaultEndpointProvider implements EndpointProvider {

   public static final String[] DEFAULT_UNAUTHENTICATED_ENDPOINTS = new String[] {
         "/api/login/**",
         "/swagger*/**",
         "/webjars/**",
         "/resources/**"
   };

   public static final String[] DEFAULT_AUTHENTICATED_ENDPOINTS = new String[] {
         "/api/**",
         "/api/leave-requests/**",
         "/companies/**",
         "/employees/**"
   };
   @Override
   public String[] getUnAuthenticatedEndpoints() {
      return DEFAULT_UNAUTHENTICATED_ENDPOINTS;
   }

   @Override
   public String[] getAuthenticatedEndpoints() {
      return DEFAULT_AUTHENTICATED_ENDPOINTS;
   }
}
