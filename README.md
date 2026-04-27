# Spring Boot 4 API Versioning Demo

A comprehensive demonstration of **Spring Framework 7's and Spring Boot 4 new first-class API versioning support** - showcasing how to 
build version-aware REST APIs with multiple versioning strategies.

## Spring Boot 4 Platform Updates

1. **Java and JVM Support**: Java 25, minimum Java 17, and support for latest JVM features
2. **Jakarta EE 11 Alignment**: Servlet 6.1, JPA 3.2
3. **Kotlin 2.2 Support**: Better Gradle build integration.
4. **Cloud-Native and Containerization**:Improved Buildpacks, More efficient Docker-native builds, Micrometer 2.x + OpenTelemetry integration.
5. **Productivity & Developer Experience**: Spring Boot CLI updates, new Actuator Endpoints.
6. **Security**: Spring Security 7 with improved OAuth2.2 / OIDC
7. **Future-Proofing with Spring AI & Native APIs**: Spring AI integrations, GraalVM native image hints.

## Spring Boot 4 Features
### 1. Elegant API Versioning

Spring now supports API versioning directly in the @RequestMapping annotation, making it easier to maintain multiple versions of your REST endpoints and ensure backward compatibility.

```java
import com.kodebytes.dto.PersonResponseV1;

@RestController
@RequestMapping("/api/")
public class ProductController {

    @RequestMapping(value = "/persons/{id}", version = "1")
    public PersonResponseV1 findById(@PathVariable @Positive @NotNull Long id) { ...}

    @RequestMapping(value = "/persons/{id}", version = "2")
    public PersonResponseV1 findById(@PathVariable @Positive @NotNull Long id) { ...}
}
```


```bash
curl -X GET "http://localhost:8080/api/persons/1" \
     -H "X-API-Version: 1.0"
```

```bash
curl -X GET "http://localhost:8080/api/persons/1" \
     -H "X-API-Version: 2.0"
```


### 2. Null Safety with JSpecify and Resilience

```java
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.resilience.annotation.Retryable;

@Service
@EnableResilientMethods
@Validated
@NullMarked
public class PersonService {

    //-------------
    @ConcurrencyLimit(value = 3) // Allow only 3 concurrent calls
    public List<PersonResponseV1> getPersonsV1() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::toV1)
                .toList();
    }
    
    //-------------
    @Nullable
    @Retryable(
            includes = {ResourceNotFoundException.class, TimeoutException.class}, // Specific exceptions
            maxRetries = 3,            // Number of retries AFTER the first failure
            delay = 1000,              // 1-second base delay
            multiplier = 2.0,          // Exponential backoff (1s, 2s, 4s)
            maxDelay = 10000,          // Cap delay at 10 seconds
            jitter = 200               // Adds +/- 200ms randomness to prevent "thundering herd"
    )
    public PersonResponseV1 getPersonByIdV1(Long id) {
        return personMapper.toV1(personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Person", "id", id.toString()))
        );
    }
    //-------------
}
```

### 3. Dedicated Configuration for HTTP Clients with @ImportHttpServices

Spring Framework 7 adds the @ImportHttpServices annotation, making it easier to configure and group HTTP clients. This streamlines the setup for applications that interact with multiple external services.

```java
@Configuration(proxyBeanMethods = false)
@ImportHttpServices(group = "weather", types = {WeatherClient.class})
public class HttpClientConfig {}
```

### 4. Streaming Support with InputStream and OutputStream in HTTP Clients

HTTP clients now support streaming request and response bodies using InputStream and OutputStream. This is especially useful for handling large files or data streams efficiently.

```java
@PostMapping("/upload")
public void uploadFile(InputStream inputStream) {
    // process large file stream
}
```

### 5. New JmsClient and Enhancements to JdbcClient

The new JmsClient provides a modern API for working with JMS (Java Message Service), while JdbcClient has been enhanced for easier and more flexible database operations.

```java
JmsClient jmsClient = JmsClient.create(connectionFactory);
jmsClient.send("queue", "Hello World");

JdbcClient jdbcClient = JdbcClient.create(dataSource);
List<User> users = jdbcClient.sql("SELECT * FROM users").query(User.class).list();
```
### 6. New RestTestClient for REST API Testing

RestTestClient is a new tool for testing REST APIs, offering a fluent API and support for both live servers and mock setups. It simplifies writing and maintaining integration tests for your endpoints.

```java
RestTestClient client = RestTestClient.bindToServer("http://localhost:8080");
client.get().uri("/api/user").exchange().expectStatus().isOk();

JdbcClient jdbcClient = JdbcClient.create(dataSource);
List<User> users = jdbcClient.sql("SELECT * FROM users").query(User.class).list();
```

### 7.  Enhanced Path Matching with Improved PathPattern Support

Path matching in Spring MVC has been improved with the enhanced PathPattern support, replacing legacy options and providing more powerful and flexible URI template matching for your controllers.

```java
@RequestMapping("/**/pages/{pageName}")
public String handlePage(@PathVariable String pageName) {
    return pageName;
}
```

## Features Demonstrated in This Project.

This Spring Boot 4 application showcases all four API versioning approaches introduced in Spring Framework 7:

- **Path Segment Versioning**: `/api/v1/users` vs `/api/v2/users`
- **Request Header Versioning**: `X-API-Version: 1.0` vs `X-API-Version: 2.0`
- **Query Parameter Versioning**: `?version=1.0` vs `?version=2.0`
- **Media Type Versioning**: `Accept: application/json;version=1.0`

Each approach returns different response formats to demonstrate real-world API evolution scenarios.

## Quick Start

1. **Prerequisites**: Java 25, Maven
2. **Spring Boot**: version 4.0.3
3. **Compile**: `mvn clean package`
4. **Run**: `mvn spring-boot:run` or  `java -jar target/poc-api-spring-4-0.0.1-SNAPSHOT.jar`
4. **Test**: Use the provided `api-requests.http` file (IntelliJ IDEA/VS Code) or HTTPie examples below
5. **Curl MD**: Use description provided `api-requests.curl.md` 




## 1. Elegant API Versioning
The application starts on: 
1. Api-Headers: `http://localhost:8080/api/persons`
2. Api-Segments: `http://localhost:8080/apiPathSegment/v1/persons` or `http://localhost:8080/apiPathSegment/v2/persons`
3. Api-Query-Parameters: `http://localhost:8080/apiQueryParams/persons/list?version=1.0` or  `http://localhost:8080/apiQueryParams/persons/list?version=v2`
4. Api-Content-Negotiation: `http://localhost:8080/apiMedia/persons/media`

## Configuration

Edit `src/main/java/dev/danvega/users/config/WebConfig.java` to enable different versioning strategies:

```java
//ApiVersionConfigurer
    // Uncomment the desired versioning method
    //.usePathSegment(1)           // Path-based
    .useRequestHeader("X-API-Version")  // Header-based
    //.useQueryParam("version")    // Query parameter-based
    //.useMediaTypeParameter(MediaType.APPLICATION_JSON, "version")  // Currently active
```
## Request Differences (no field id)
- **v1**: Returns `PersonRequestV1` with single `name` field: `{name, email, passport}`
- **v2**: Returns `PersonRequestV2` with separate name fields: `{firstName, lastName, email, passport}`

## Response Differences
- **v1**: Returns `PersonResponseV1` with single `name` field: `{id, name, email, passport}`
- **v2**: Returns `PersonResponseV2` with separate name fields: `{id, firstName, lastName, email, passport}`

## Alternative Configuration (application.properties)

Instead of using Java configuration in `WebConfig.java`, you can configure API versioning using `application.yml` :

```yml
spring:
  mvc:
    apiversion:
      supported: 1.0,2.0
      default: 1.0
      use:
        # Choose ONE versioning strategy:
        enabled: true
        header: X-API-Version
#        enabled: true
#        path-segment: 1
#        enabled: true
#        query-parameter: 1
#        enabled: true
#        media-type-parameter:
#          name: version
```

```properties
# Basic versioning configuration
spring.mvc.apiversion.supported=1.0,2.0
spring.mvc.apiversion.default=1.0

# Choose ONE versioning strategy:

# Path segment versioning (e.g., /apiPathSegment/v1/persons)
spring.mvc.apiversion.use.path-segment.enabled=true
spring.mvc.apiversion.use.path-segment.index=1

# Request header versioning (e.g., /api/person, X-API-Version: 1.0)
spring.mvc.apiversion.use.header.enabled=true
spring.mvc.apiversion.use.header.name=X-API-Version

# Query parameter versioning (e.g., /apiReqParam/persons?version=1.0 )
spring.mvc.apiversion.use.query-parameter.enabled=true
spring.mvc.apiversion.use.query-parameter.name=version

# Media type parameter versioning (e.g., /apiMedia/persons Accept: application/json;version=1.0)
spring.mvc.apiversion.use.media-type-parameter.enabled=true
spring.mvc.apiversion.use.media-type-parameter.name=version
```

> **Note**: When using `application.yml` or `application.properties` configuration, 
> comment out or remove the `configureApiVersioning` method in `WebConfig.java` to avoid conflicts.

---

## Technical Details

### How Spring Framework 7 API Versioning Works

Spring Framework 7's API versioning support allows you to configure how the version is resolved from requests through the ApiVersionConfigurer callback of WebMvcConfigurer. Here are the key approaches for handling versioning with your controller:

#### Path Segment Versioning (Recommended for RESTful APIs)

When using path segments, you need to specify the index of the path segment expected to contain the version, and the path segment must be declared as a URI variable like `"/{version}"` or `"/api/{version}"`.

#### Configuration Options

The framework provides several built-in options for version resolution:
- **Request Header**: Using a custom header like `X-API-Version`
- **Request Parameter**: Using a query parameter like `?version=2.0`
- **Path Segment**: Using URL paths like `/v1/persons` or `/api/1.0/persons`
- **Media Type Parameter**: Using content negotiation like `application/vnd.kodebytes.app-v2+json`

#### Key Features

The framework automatically resolves versions from requests via ApiVersionResolver, parses raw version values into Comparable<?> with an ApiVersionParser, and can send hints about deprecated versions to clients via response headers.

#### Important Notes

- Supported versions are transparently detected from versions declared in request mappings, but you can turn that off and only consider explicitly configured versions
- Requests with unsupported versions are rejected with InvalidApiVersionException resulting in a 400 response
- The `version` attribute in `@RequestMapping` and related annotations (`@GetMapping`, `@PostMapping`, etc.) is new to Spring Framework 7
- By default, a version is required when API versioning is enabled, but you can make it optional in which case the most recent version is used

#### Usage Examples

**Client requests would look like:**
- Path-based: `GET /apiPathSegment/1.0/persons` or `GET /apiPathSegment/2.0/persons`
- Header-based: `GET /api/persons` with header `X-API-Version: 2.0`
- Query parameter: `GET /apiReqParam/persons?version=2.0`
- Media Type: `GET /apiMedia/persons` with header `Accept: application/json;version=2.0`

The main advantage of Spring Framework 7's approach is that it provides a standardized, framework-level solution for API versioning rather than requiring custom implementations or workarounds as in previous versions.

## HTTPie Request Examples

api-requests.http

## Curl Request Examples

api-requests.curl.md 

### Configuration

Check `WebConfig.java` to see which versioning methods are currently enabled. Only uncommented methods in the `configureApiVersioning` method will work.