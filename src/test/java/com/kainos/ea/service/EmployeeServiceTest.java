package com.kainos.ea.service;

import com.kainos.ea.dao.EmployeeDao;
import com.kainos.ea.exception.DatabaseConnectionException;
import com.kainos.ea.exception.InvalidIdException;
import com.kainos.ea.exception.UserDoesNotExistException;
import com.kainos.ea.model.Employee;
import com.kainos.ea.model.EmployeeRequest;
import com.kainos.ea.util.DatabaseConnector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    EmployeeDao employeeDao = Mockito.mock(EmployeeDao.class);
    DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);

    EmployeeService employeeService = new EmployeeService(employeeDao, databaseConnector);

    EmployeeRequest employeeRequest = new EmployeeRequest(
            30000,
            "Tim",
            "Bloggs",
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

    List<Employee> employeeList = new ArrayList<>(
            Arrays.asList(new Employee(1), new Employee(2), new Employee(3))
    );

    Connection conn;

    @Test
    void insertEmployee_shouldReturnId_whenDaoReturnsId() throws DatabaseConnectionException, SQLException {
        int expectedResult = 1;
        Mockito.when(databaseConnector.getConnection()).thenReturn(conn);
        Mockito.when(employeeDao.insertEmployee(employeeRequest, conn)).thenReturn(expectedResult);

        int result = employeeService.insertEmployee(employeeRequest);

        assertEquals(result, expectedResult);
    }

    @Test
    void insertEmployee_shouldThrowSqlException_whenDaoThrowsSqlException() throws SQLException, DatabaseConnectionException {
        Mockito.when(databaseConnector.getConnection()).thenReturn(conn);
        Mockito.when(employeeDao.insertEmployee(employeeRequest, conn)).thenThrow(SQLException.class);

        assertThrows(SQLException.class,
                () -> employeeService.insertEmployee(employeeRequest));
    }

    /*
    Mocking Exercise 1

    Write a unit test for the getEmployee method

    When the dao throws a SQLException

    Expect SQLException to be thrown

    This should pass without code changes
     */

    @Test
    void getEmployee_shouldThrowSqlException_whenDaoThrowsSqlException() throws SQLException, DatabaseConnectionException {
        Mockito.when(databaseConnector.getConnection()).thenReturn(conn);
        Mockito.when(employeeDao.getEmployee(1, conn)).thenThrow(SQLException.class);

        assertThrows(SQLException.class,
                () -> employeeService.getEmployee(1));
    }

    /*
    Mocking Exercise 2

    Write a unit test for the getEmployee method

    When the dao returns an id

    Expect the id to be returned

    This should pass without code changes
     */

    @Test
    void getEmployee_shouldReturnId_whenDaoReturnsId() throws SQLException, DatabaseConnectionException {
        Employee mockEmployee = new Employee(1);
        Mockito.when(databaseConnector.getConnection()).thenReturn(conn);
        Mockito.when(employeeDao.getEmployee(1, conn)).thenReturn(mockEmployee);

        assertEquals(employeeDao.getEmployee(1, conn).getEmployeeId(), 1);
    }

    /*
    Mocking Exercise 3

    Write a unit test for the getEmployee method

    When the id parameter is null

    Expect the dao not to be called

    This should fail, make code changes to make this test pass
     */

    @Test
    void getEmployee_shouldNotCallDao_ifIdIsNull() throws SQLException, DatabaseConnectionException {
        Mockito.when(databaseConnector.getConnection()).thenReturn(conn);

        assertThrows(InvalidIdException.class,
                () -> employeeService.getEmployee(-1));
    }

    /*
    Mocking Exercise 4

    Write a unit test for the getEmployee method

    When the dao returns null

    Expect UserDoesNotExistException to be thrown

    This should fail, make code changes to make this test pass
     */

    @Test
    void getEmployee_shouldThrowUserDoesNotExistException_whenDaoReturnsNull() throws SQLException, DatabaseConnectionException {
        Mockito.when(databaseConnector.getConnection()).thenReturn(conn);
        Mockito.when(employeeDao.getEmployee(1, conn)).thenReturn(null);

        assertThrows(UserDoesNotExistException.class,
                () -> employeeService.getEmployee(1));
    }

    /*
    Mocking Exercise 5

    Write a unit test for the getEmployees method

    When the dao returns a list of employees

    Expect the list of employees to be returned

    This should pass without code changes
     */

    @Test
    void getEmployees_shouldReturnPopulatedList_whenDaoReturnsList() throws SQLException, DatabaseConnectionException {
        Mockito.when(databaseConnector.getConnection()).thenReturn(conn);
        Mockito.when(employeeDao.getEmployees(conn)).thenReturn(employeeList);

        assertEquals(employeeService.getEmployees().get(0), employeeList.get(0));
        assertEquals(employeeService.getEmployees().get(1), employeeList.get(1));
        assertEquals(employeeService.getEmployees().get(2), employeeList.get(2));
    }

    /*
    Mocking Exercise 6

    Write a unit test for the getEmployees method

    When the dao throws a SQLException

    Expect SQLException to be thrown

    This should pass without code changes
     */

    @Test
    void getEmployees_shouldThrowSqlException_whenDaoThrowsSqlException() throws SQLException, DatabaseConnectionException {
        Mockito.when(databaseConnector.getConnection()).thenReturn(conn);
        Mockito.when(employeeDao.getEmployees(conn)).thenThrow(SQLException.class);

        assertThrows(SQLException.class,
                () -> employeeService.getEmployees());
    }
}