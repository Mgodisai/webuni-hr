package hu.webuni.hr.alagi.security;

public interface EndpointProvider {
   String[] getUnAuthenticatedEndpoints();
   String[] getAuthenticatedEndpoints();
}
