package com.kainos.ea.integration;

import com.kainos.ea.WebServiceApplication;
import com.kainos.ea.WebServiceConfiguration;
import com.kainos.ea.controller.HR;
import com.kainos.ea.exception.*;
import com.kainos.ea.model.Employee;
import com.kainos.ea.model.EmployeeRequest;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.List;

@ExtendWith(DropwizardExtensionsSupport.class)
public class HRIntegrationTest {

    static final DropwizardAppExtension<WebServiceConfiguration> APP = new DropwizardAppExtension<>(
            WebServiceApplication.class, null,
            new ResourceConfigurationSourceProvider()
    );

    @Test
    void getEmployees_shouldReturnListOfEmployees() {
        List<Employee> response = APP.client().target("http://localhost:8080/hr/employee")
                .request()
                .get(List.class);

        Assertions.assertTrue(response.size() > 0);
    }

    @Test
    void postEmployee_shouldReturnIdOfEmployee() {
        EmployeeRequest employeeRequest = new EmployeeRequest(
                30000,
                "Integration",
                "Test",
                "tbloggs@email.com",
                "1 Main Street",
                "Main Road",
                "Belfast",
                "Antrim",
                "BT99BT",
                "Northern Ireland",
                "12345678901",
                "12345678",
                "AA1A11AA"
        );

        int response = APP.client().target("http://localhost:8080/hr/employee")
                .request()
                .post(Entity.entity(employeeRequest, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Integer.class);

        Assertions.assertNotNull(response);
    }

    /*
    Integration Test Exercise 1

    Write an integration test for the GET /hr/employee/{id} endpoint

    Create an employee by calling the POST /hr/employee endpoint

    Get the ID from the response of the above call

    Call the GET /hr/employee/{id} endpoint

    Expect the response values to be the same and the employee created above

    This should pass without code changes
     */

    @Test
    void getEmployee_shouldReturnEmployee_forGivenId() {
        EmployeeRequest employeeRequest = new EmployeeRequest(
                30000,
                "Integration",
                "Test",
                "tbloggs@email.com",
                "1 Main Street",
                "Main Road",
                "Belfast",
                "Antrim",
                "BT99BT",
                "Northern Ireland",
                "12345678901",
                "12345678",
                "AA1A11AA"
        );

        int empId = APP.client().target("http://localhost:8080/hr/employee")
                .request()
                .post(Entity.entity(employeeRequest, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Integer.class);

        Employee response = APP.client().target("http://localhost:8080/hr/employee/" + empId)
                .request()
                .get()
                .readEntity(Employee.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getSalary(), employeeRequest.getSalary());
        Assertions.assertEquals(response.getFname(), employeeRequest.getFname());
        Assertions.assertEquals(response.getLname(), employeeRequest.getLname());
        Assertions.assertEquals(response.getEmail(), employeeRequest.getEmail());
        Assertions.assertEquals(response.getAddress(), employeeRequest.getAddress());
        Assertions.assertEquals(response.getAddress2(), employeeRequest.getAddress2());
        Assertions.assertEquals(response.getCity(), employeeRequest.getCity());
        Assertions.assertEquals(response.getCounty(), employeeRequest.getCounty());
        Assertions.assertEquals(response.getPostalCode(), employeeRequest.getPostalCode());
        Assertions.assertEquals(response.getCountry(), employeeRequest.getCountry());
        Assertions.assertEquals(response.getPhoneNo(), employeeRequest.getPhoneNo());
        Assertions.assertEquals(response.getBankNo(), employeeRequest.getBankNo());
        Assertions.assertEquals(response.getNin(), employeeRequest.getNin());
    }

    /*
    Integration Test Exercise 2

    Write an integration test for the POST /hr/employee method

    Call the POST /hr/employee endpoint with an employee with salary of 10000

    Expect a response with error code 400 to be returned

    This should fail, make code changes to make this test pass
     */

    @Test
    void postEmployee_shouldReturn400Error_whenSalaryTooLow() {
        EmployeeRequest employeeRequest = new EmployeeRequest(
                10000,
                "Integration",
                "Test",
                "tbloggs@email.com",
                "1 Main Street",
                "Main Road",
                "Belfast",
                "Antrim",
                "BT99BT",
                "Northern Ireland",
                "12345678901",
                "12345678",
                "AA1A11AA"
        );

        int response = APP.client().target("http://localhost:8080/hr/employee")
                .request()
                .post(Entity.entity(employeeRequest, MediaType.APPLICATION_JSON_TYPE))
                .getStatus();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(400, response);
    }

    /*
    Integration Test Exercise 3

    Write an integration test for the POST /hr/employee method

    Call the POST /hr/employee endpoint with an employee with bank number of 123

    Expect a response with error code 400 to be returned

    This should fail, make code changes to make this test pass
     */

    @Test
    void postEmployee_shouldReturn400Error_whenBankNumberTooShort() {
        EmployeeRequest employeeRequest = new EmployeeRequest(
                50000,
                "Integration",
                "Test",
                "tbloggs@email.com",
                "1 Main Street",
                "Main Road",
                "Belfast",
                "Antrim",
                "BT99BT",
                "Northern Ireland",
                "12345678901",
                "123",
                "AA1A11AA"
        );

        int response = APP.client().target("http://localhost:8080/hr/employee")
                .request()
                .post(Entity.entity(employeeRequest, MediaType.APPLICATION_JSON_TYPE))
                .getStatus();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(400, response);
    }

    /*
    Integration Test Exercise 4

    Write an integration test for the GET /hr/employee/{id} endpoint

    Call the GET /hr/employee/{id} endpoint will an id of 123456

    Expect a response with error code 400 to be returned

    This should fail, make code changes to make this test pass
     */

    @Test
    void getEmployee_should400Status_whenRequestingNonExistingEmployee() {
        int response = APP.client().target("http://localhost:8080/hr/employee/" + 123456)
                .request()
                .get()
                .getStatus();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(400, response);
    }
}
