# Yoga app

## Setup the DDB:
- Download MySQL command line client from your browser
- Copy the absolute path of `data.sql` which is inside `ressources\sql` folder of this project
- In the MySQL CLI, enter: `source ` followed by the absolute path of `data.sql`

## Run the Front:
- Open a terminal inside `front` folder
- Execute `npm install` to install the dependencies
- Execute `npm install cypress --save-dev` to install cypress
- Then execute `npm run start` to run the front project

## Run the back:
- Inside `back\src\main\resources\application.properties`:
  - Modify `spring.datasource.password` and `spring.datasource.username` to match with yours.
- Open a terminal inside `back` folder
- Execute `mvn spring-boot:run` (If `mvn` is not recognized, follow instructions [here](https://www.baeldung.com/install-maven-on-windows-linux-mac))

## Testing:
### Front: Unit Tests and Integration Tests
- Run `npm run test` inside `front` folder to run the Jest tests.
- The coverage will be generated here: `front\coverage\lcov-report\index.html`
  
### Front: E2E tests
- Run `npm run e2e:ci` inside `front` folder to run the E2E tests and create coverage file
- The coverage will be generated here: `front\coverage\lcov-report\index.html`
 ![image](https://github.com/user-attachments/assets/a4887109-4a71-482e-9b15-d64c56983fa0)

### Back:
- Run `mvn clean test` inside `back` folder to run Unit tests and integration tests.
- Retrieve the coverage here: `back/target/site/jacoco/index.html`
  ![image](https://github.com/user-attachments/assets/afc987de-a569-4242-bef1-b77e7cd0b06f)
