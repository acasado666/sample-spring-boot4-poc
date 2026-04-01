### Spring Framework 7 API Versioning Examples (cURL)
### All requests for testing different versioning approaches

### Response Type Differences:
 - Person: Full entity with all fields
 - PersonRequestV1: {name, email, passport} – single name field
 - PersonRequestV2: {firstName, lastName, email, passport} - separate firstName/lastName fields
 - PersonResponseV1: {id, name, email, passport} – single name field
 - PersonResponseV2: {id, firstName, lastName, email, passport} – separate firstName/lastName fields

#### Path Segment Versioning
 - Returns List<Person> – full entity with all fields
 - Note: Requires **usePathSegment(1)** to be **uncommented** in WebConfig

#### v1 – Path Segment
```bash
curl -X GET "http://localhost:8080/apiPathSegment/v1/persons"
```

#### v2 – Path Segment
```bash
curl -X GET "http://localhost:8080/apiPathSegment/v2/persons"
```

#### Request Header Versioning
#### Note: Requires useRequestHeader("X-API-Version") to be uncommented in WebConfig
- Note: Requires **usePathSegment(1)** to be **commented** in WebConfig

#### v1 - Get Request Header returns ResponseEntity<List<PersonResponseV1>>
- Returns ResponseEntity<List<PersonResponseV1>> – full entity with all fields

```bash
curl -X GET "http://localhost:8080/api/persons" \
     -H "X-API-Version: 1.0"
```

#### v2 - Get Request Header returns ResponseEntity<List<PersonResponseV2>>
- Returns ResponseEntity<List<PersonResponseV2>> – full entity with all fields

```bash
curl -X GET "http://localhost:8080/api/persons" \
     -H "X-API-Version: 2.0"
```

#### v1 - Get Request Header returns ResponseEntity<PersonResponseV1>
- Returns ResponseEntity<PersonResponseV1> – full entity with all fields

```bash
curl -X GET "http://localhost:8080/api/persons/1" \
     -H "X-API-Version: 1.0"
```

#### v2 - Get Request Header returns ResponseEntityList<List<PersonResponseV2>>
- Returns ResponseEntity<PersonResponseV2> – full entity with all fields

```bash
curl -X GET "http://localhost:8080/api/persons/1" \
     -H "X-API-Version: 2.0"
```

#### v1 - Post Request Header
- Creates a new Person and returns ResponseEntity<PersonResponseV1> – a full entity with all fields

```bash
curl -X POST "http://localhost:8080/api/persons" \
     -H "Content-Type: application/json" \
     -H "X-API-Version: 1.0" \
     -d '{
    "name": "Steve Martin",
    "email": "s.martin@gmail.com",
    "passport": "678901"
}'
```

#### v2 - Post Request Header
- Creates a new Person and returns ResponseEntity<PersonResponseV2> – a full entity with all fields

```bash
curl -X POST "http://localhost:8080/api/persons" \
     -H "Content-Type: application/json" \
     -H "X-API-Version: 2.0" \
     -d '{
  "firstName": "Daniel",
  "lastName": "Lewis",
  "email": "d.lewis@gmail.com",
  "passport": "789012"
}'
```

#### v1 - Put Request Header
- Updates a Person and returns ResponseEntity<PersonResponseV1> – a full entity with all fields

```bash
curl -X PUT "http://localhost:8080/api/persons" \
     -H "Content-Type: application/json" \
     -H "X-API-Version: 1.0" \
     -d '{
  "id": 6,
  "name": "Steve Merkel",
  "email": "s.merkel@gmail.com",
  "passport": "890123"
}'
```
#### v2 - Put Request Header
- Updates a Person and returns ResponseEntity<PersonResponseV2> – a full entity with all fields

```bash
curl -X PUT "http://localhost:8080/api/persons" \
     -H "Content-Type: application/json" \
     -H "X-API-Version: 1.0" \
     -d '{
  "id": 7,
  "firstName": "Dani",
  "lastName": "DeVito",
  "email": "d.devito@gmail.com",
  "passport": "901234"
}'
```



#### v1 - Delete Request Header
- Deletes a Person by Id and returns DELETED

```bash
curl -X DELETE "http://localhost:8080/api/persons/6" \
     -H "X-API-Version: 1.0"
```

#### v1 - Delete Request Header
- Deletes a Person by Id and returns DELETED

```bash
curl -X DELETE "http://localhost:8080/api/persons/7" \
     -H "X-API-Version: 1.0"
```

#### Query Parameter Versioning
#### v1 - Query Parameter
- Returns ResponseEntity<List<PersonResponseV1>> using Query Parameter – full entity with all fields

```bash
curl -X GET "http://localhost:8080/apiReqParam/persons?version=1.0"
```

#### v2 - Query Parameter
- Returns ResponseEntity<List<PersonResponseV2>> using Query Parameter – full entity with all fields

```bash
 curl -X GET "http://localhost:8080/apiReqParam/persons?version=v2"
```

#### Media Type Parameter Versioning
#### Note: Currently active in WebConfig with useMediaTypeParameter(MediaType.APPLICATION_JSON, "version")

#### v1 - Media Type Parameter
- Returns ResponseEntity<List<PersonResponseV1>> using Media Type Parameter Versioning – full entity with all fields

```bash
curl -X GET "http://localhost:8080/apiMedia/persons" \
     -H "Accept: application/json;version=1.0"
```

#### v2 - Media Type Parameter
- Returns ResponseEntity<List<PersonResponseV2>> Media Type Parameter Versioning – full entity with all fields

```bash
curl -X GET "http://localhost:8080/apiMedia/persons" \
     -H "Accept: application/json;version=2.0"
```