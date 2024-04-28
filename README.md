# SpringJWTAuthenticator

This repository contains a JavaSpring project that implements user authentication using Spring Security and JWT token generation. The project is configured to work with both H2 and PostgreSQL databases. Upon user creation, an email containing authentication details is sent to the user for account verification. 

## Installation

1. Clone the repository:

```bash
git clone https://github.com/seu-usuario/SpringJWTAuthenticator.git
```

## Usage

### Creating a User

To create a user, send a POST request to the `/api/users/signup` endpoint with the user details in the request body. Upon successful creation, an email will be sent to the user for account verification.

### Logging In

To log in, send a POST request to the `/api/users/login` endpoint with the user credentials. Upon successful authentication, a JWT token will be returned in the response.

## Configuration

### Database Configuration

The project is configured to work with both H2 and PostgreSQL databases. By default, it uses H2 for local development. To switch to PostgreSQL, modify the `application.properties` file accordingly.

### Email Configuration

Email configuration details can be set in the `application.properties` file. Ensure that the SMTP server details are correctly configured for sending authentication emails.

## Dependencies

- Spring Boot
- Spring Security
- JWT
- H2 Database
- PostgreSQL
- Maven

## Contributing

Contributions are welcome! Please feel free to open an issue or submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
