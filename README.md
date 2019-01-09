# spring-security-roles-hierarchy
Spring boot based roles hierarchy project with spring security

## Spring Security

Spring Security is a security framework that provides declarative security for Spring-based applications.  
Based on the Spring Framework, Spring Security takes full advantage of dependency injection (DI) and aspect-oriented techniques.

- Spring Security is dedicated to providing a full array of security services to Java applications in a developer-friendly and flexible way.

- It offers layered security services. Spring Security allows user to secure application at different levels, and also to secure web URLs, views, service methods, and domain model.  

- User can pick and combine these features to achieve security goals.

- At the most general level, it’s a framework composed of intercepting rules for granting, or not granting, access to resources.

- Spring Security’s interception model of security applies to two main areas of application: **URLs and method invocations.**

- Spring Security wraps around these two entry points of application and allows access only when the security constraints are satisfied.

- Both the **method call and the filter-based security** depend on a central Security Interceptor, where the main logic resides to make the decision whether or not access should be granted.

It is advised to check in ***FilterChainProxy class*** if user faces problems with spring security application.

- It composes other filters into single filter.
- It is an entry point for all web based security in spring security.

In spring boot, ***spring.factories*** properties file contains all auto config related configurations.  
***SecurityFilterAutoConfiguration class*** registers spring security filter chain with servlet container.

Spring security provides authentication, authorization and protection against common attacks out of box.

### Authentication

> It Anwers who is trying to make particular request.

Who identification can be done in many ways e.g. user credentials (username and password), single sign on token etc.  
Spring security provides out of box support for these e.g. Form based login or HTTP basic OR LDAP, JDBC, custom authentication, SAML etc.  

### Authorization

>It answers the question, what an authenticated Principal does?

Given that it is known who is trying to make particular request, to understand if he/she is allowed to do that.

#### Polyglot picture - enables XSS

It is a picture that is both valid jpeg image and js file : Insert comment in the jpeg and add js code. GIF then sees js as comment and JS sees jpeg as comment.

### OAuth

It is a framework that allows limited access to protected resources on the web. It is about authorization.

## The securityInterceptor:

- With the main logic implemented in ***AbstractSecurityInterceptor*** and with two concrete implementations in the form of ***FilterSecurityInterceptor*** and ***MethodSecurityInterceptor***, the Security Interceptor is in charge of deciding whether a particular petition should be allowed to go through to a secured resource.

- The Security Interceptor lies at the core of the Spring Security framework. Every call to a secured resource in Spring Security passes through this interceptor.

- The Security Interceptor works with a preprocessing step and a postprocessing step.

- In the preprocessing step, it looks to see whether the requested resource is secured with some metadata information (or ConfigAttribute). If it is not, the request is allowed to continue its way either to the requested URL or method.

- If the requested resource is secured, the Security Interceptor retrieves the Authentication object from the current SecurityContext.

- If necessary, the Authentication object will be authenticated against the configured AuthenticationManager.

- After the object is authenticated, ***AccessDecisionManager*** is called to determine if the authenticated entity is able to finally access the resource. ***AccessDecisionManager*** throws an ***AccessDeniedException*** if the authenticated entity is not allowed to access the resource.

- If ***AccessDecisionManager*** decides that the Authentication object is allowed to access the resource, the Authentication object is passed to RunAsManager if this is configured.

- If ***RunAsManager*** is not configured, a no-op implementation is called. ***RunAsManager*** returns either null (if it’s not configured to be used) or a new Authentication object containing the same principal, credentials, and granted authorities as the original Authentication object, plus a new set of authorities. **This new Authentication object is put into the current SecurityContext.**

- After this processing the Security Interceptor creates a new ***InterceptorStatusToken*** with information about the SecurityContext and the **ConfigAttributes**. This token will be used later in the postprocessing step of the Security Interceptor.

- At this point, the Security Interceptor is ready to allow access to the secured resource, so it passes the invocation through and the particular secured entity (either a URL or a method) is invoked.

- After the invocation returns, the second phase of the Security Interceptor comes into play, and the postprocessing begins. It involves only calling a ***AfterInvocationManager’s decide method*** if there is one configured.

- In its current implementation ***AfterInvocationManager*** delegates to instances of ***PostInvocationAuthorizationAdvice***, which ultimately can filter the returned objects or throw a ***AccessDeniedException*** if necessary. This is the case if you are using the postinvocation filters in method-level security.

### On a high level

1. **AbstractSecurityInterceptor** calls **getAttributes** on **SecurityMetadataSource**.
2. It then calls **getAuthentication** on **SecurityContext**
3. Then it calls **authenticate** on **AuthenticationManager**
4. Then it calls **decide** on **AccessDecisionManager**
5. For post processing it calls **decide** on **AfterInvocationManager.**

### The filter chain

- The filter chain model is what Spring Security uses to secure web applications. This model is built on top of the standard servlet filter functionality.

- The filter chain in Spring Security preprocesses and postprocesses all the HTTP requests that are sent to the application and then applies security to URLs that require it.

- The Spring Security filter chain is made up of Spring beans.

### ConfigAttribute

- The interface ***org.springframework.security.access.ConfigAttribute*** encapsulates the access information metadata present in a secured resource. For example, for our study purposes, ROLE_ADMIN is a ConfigAttribute.

### The Authentication Object

- The Authentication object is an *abstraction* that represents the entity that logs in to the system—most likely, a user.

- An Authentication object is used both when an authentication request is created (when a user logs in), to carry around the different layers and classes of the framework the requesting data, and then when it is validated, containing the authenticated entity and storing it in SecurityContext.

- The most common behavior is that when you log in to the application a new Authentication object will be created storing your user name, password, and permissions—most of which are technically known as Principal, Credentials, and Authorities, respectively.

- Authentication is an interface and there are several implementations of the same like:
- ***UsernamePasswordAuthenticationToken:*** Contains the user name and password information of the authenticated (or pending authentication) user.
- ***PreAuthenticatedAuthenticationToken:*** Handles pre-authenticated Authentication objects. *Pre-authenticated authentications* are those where the actual authentication process is handled by an external system, and Spring Security deals only with extracting the principal (or user) information out of the external system’s messages.
- ***OpenIDAuthenticationToken:*** It is used by both the OpenID filter and the OpenID authentication provider.
- ***RunAsUserToken:*** This implementation is used by the RunAsManager, which is called by the Security Interceptor, when the accessed resource contains a ConfigAttribute that starts with the prefix 'RUN_AS_'.

### SecurityContext and SecurityContextHolder

- The interface **org.springframework.security.core.context.SecurityContext** (actually, its implementation is **SecurityContextImpl**) is the place where Spring Security stores the valid Authentication object, associating it with the current thread.

- The ***org.springframework.security.core.context.SecurityContextHolder*** is the class used to access SecurityContext from many parts of the framework. It is built mainly of static methods to store and access SecurityContext.

### AuthenticationProvider

- AuthenticationProvider is the main entry point for authenticating an Authentication object. This interface has only two methods: ***Authentication authenticate(Authentication authentication) throws AuthenticationException;*** and ***boolean supports(Class<?> authentication);***
- There are many classes that currently extend this interface in security framework. Each of the implementing classes deals with a particular external provider to authenticate against like CAS, OpenID, Ldap etc. So if you come across a particular provider that is not supported and need to authenticate against it, you probably need to implement this interface with the required functionality.

### AccessDecisionManager

- AccessDecisionManager is the class in charge of deciding if a particular Authentication object is allowed or not allowed to access a particular resource.

- In its main implementations, it delegates to AccessDecisionVoter objects, which basically compares the GrantedAuthorities in the Authentication object against the ConfigAttribute(s) required by the resource that is being accessed, deciding whether or not access should be granted.

### UserDetailsService and AuthenticationUserDetailsService

- The interface ***org.springframework.security.core.userdetails.UserDetailsService*** is in charge of loading the user information from the underlying user store (in-memory, database, and so on) when an authentication request arrives in the application.

- UserDetailsService makes use of the provided user name for looking up the rest of the required user data from the datastore.

- The interface ***org.springframework.security.core.userdetails.AuthenticationUserDetailsService*** is more generic, it allows you to retrieve a *UserDetails* using an Authentication object instead of a user name String, making it more flexible to implement.

- These are the two main strategies (**AuthenticationUserDetailsService and UserDetailsService**) used for retrieving the user information when attempting authentication. They are usually called from the particular *AuthenticationProvider* that is being used in the application. e.g. The OpenIDAuthenticationProvider and CasAuthenticationProvider delegate to an AuthenticationUserDetailsService to obtain the user details, while the DaoAuthenticationProvider delegates directly to a UserDetailsService.

### UserDetails

- The interface ***org.springframework.security.core.userdetails.UserDetails*** object is the main abstraction in the system, and it’s used to represent a full user in the context of Spring Security.

- It is also made available to be accessed later in the system from any point that has access to SecurityContext. Normally, developers create their own implementation of this interface to store particular user details they need or want (like email, telephone, address, and so on). Later, they can access this information, which will be encapsulated in the Authentication object, and they can be obtained by calling the getPrincipal method on it.
