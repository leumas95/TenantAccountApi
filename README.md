# Console Tenant Account API
A small API I made as a chalange that also happend to be my first attemt at using Kotlin and Spring.
It is a maven project and includes some cool things like:
 - Kotlin (as mentioned)
 - Spring Boot (MAGIC!)
 - Swagger
 - Cucumber

### Chalange Overview
Build an "Account" RESTful JSON API using the following technologies:
 - Java or **Kotlin**
 - Spring Boot and Spring Data JPA frameworks (don't use Spring Data REST)
 - H2 in-memory database
 - Gradle or **maven** build
 - 3-5 unit and/or integration tests for the most important areas of the code (full coverage is not required)

### Requierments
A Tenant is an account (ledger) for tracking the amount of rent paid (in Australian dollars only) by a tenant’s lease.
 
Rent Receipts can be created for a Tenant with the following rules:
 - The Tenant’s paid-to date should be automatically advanced based on the amount of rent paid
 - The paid-to date must be advanced in whole weeks only (rent period) and any remainder should be stored as rent credit which is automatically used for the next rent receipt

A Tenant has the following fields:
 - ID (auto-generated)
 - Name
 - Weekly rent amount
 - Current rent paid-to-date (auto-calculated, defaults to today)
 - Current rent credit amount (auto-calculated, defaults to 0)
 
A Rent Receipt has the following fields:
 - ID (auto-generated)
 - Amount
 
##### API Operations
The API must have the following operations:
 - Create a single Tenant
 - Get a single Tenant by ID
 - Create a single Rent Receipt
 - List all Rent Receipts for a single Tenant
 - List all Tenants which have a Rent Receipt that was created within the last “N” hours

## API Documentation
There is Swagger api explorer available at /swagger-ui.html on the server once running.
Swagger also lets you generate client side application code among other cool things!

