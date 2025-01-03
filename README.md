

IDMS API Integration
----------------------

This project is a Spring Boot application for integrating with the IDMS API, storing data in a MySQL database, and exposing endpoints for various account-related operations. 
The application uses JWT for authentication and ensures data integrity with appropriate validations.


Features
---------
		JWT-based authentication.
		Integration with IDMS API for account and insurance operations.
		MySQL integration using Hibernate.
		Secure endpoints using Spring Security.
		Pre-defined ApiResponse structure for consistent responses.
		Tested with Postman.

  Dependencies
  ----------------
  The application uses the following dependencies
  
		Spring Boot Starter:
	  Spring Web
		Spring Data JPA
		Spring Security
		Database:
		MySQL
    HikariCP (default Spring Boot connection pool)
		Utilities:
		Jackson Datatype JSR310 for LocalDate/LocalDateTime serialization.
		jjwt for JWT token management.

  Setup Instructions
  -------------------
  Prerequisites
  
		Java: Ensure JDK 17 or higher is installed.
		MySQL: Install and set up a MySQL server.
		Maven: Ensure Maven is installed and configured.

  Clone the Repository
  
  git clone <https://github.com/de011/dims.git>

Running the Application
------------------------

Run the following Maven command to build the project

1. mvn clean install
2. mvn spring-boot:run


Authentication
--------------
1. Generate a JWT Token
   
    - http://localhost:8080/api/authenticate
   
      header pass the - Username as admin and password as DriveSoft@@!


      Note -
      
      1. Attached complete postmen collections, downlad the collection from <https://github.com/de011/idms/blob/main/idms.postman_collection.json>
         Open the postmen and import and use it.
         

      3.  MySQL Script downalod from here <https://github.com/de011/idms/blob/main/idms_account.sql>  then  import or Run the all script. 










