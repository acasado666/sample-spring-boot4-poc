# Spring Framework 7 API Versioning Demo

A comprehensive demonstration of **Spring Framework 7's new first-class API versioning support** - showcasing how to 
build version-aware REST APIs with multiple versioning strategies.

## What This Project Proves

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



### Configuration

Check `WebConfig.java` to see which versioning methods are currently enabled. Only uncommented methods in the `configureApiVersioning` method will work.