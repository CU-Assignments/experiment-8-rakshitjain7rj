// Project structure:
// student-portal/
// ├── src/main/java/
// │   └── com/
// │       └── studentportal/
// │           ├── model/
// │           │   ├── User.java
// │           │   ├── Employee.java
// │           │   └── Attendance.java
// │           ├── dao/
// │           │   ├── DatabaseUtil.java
// │           │   ├── UserDAO.java
// │           │   ├── EmployeeDAO.java
// │           │   └── AttendanceDAO.java
// │           └── servlet/
// │               ├── LoginServlet.java
// │               ├── EmployeeServlet.java
// │               └── AttendanceServlet.java
// ├── src/main/webapp/
// │   ├── WEB-INF/
// │   │   └── web.xml
// │   ├── index.html
// │   ├── login.html
// │   ├── welcome.jsp
// │   ├── employee.jsp
// │   └── attendance.jsp
// └── pom.xml

// --- MODEL CLASSES ---

// User.java
package com.studentportal.model;

public class User {
    private String username;
    private String password;
    private String fullName;
    private String role;
    
    public User() {}
    
    public User(String username, String password, String fullName, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }
    
    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

// Employee.java
package com.studentportal.model;

public class Employee {
    private int id;
    private String name;
    private String department;
    private double salary;
    
    public Employee() {}
    
    public Employee(int id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}

// Attendance.java
package com.studentportal.model;

import java.util.Date;

public class Attendance {
    private int id;
    private String studentId;
    private String subject;
    private Date date;
    private String status; // Present, Absent, Late
    
    public Attendance() {}
    
    public Attendance(int id, String studentId, String subject, Date date, String status) {
        this.id = id;
        this.studentId = studentId;
        this.subject = subject;
        this.date = date;
        this.status = status;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

// --- DAO CLASSES ---

// DatabaseUtil.java
package com.studentportal.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/studentportal";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "password";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // SQL initialization script
    /* 
    CREATE DATABASE IF NOT EXISTS studentportal;
    USE studentportal;
    
    CREATE TABLE IF NOT EXISTS users (
        username VARCHAR(50) PRIMARY KEY,
        password VARCHAR(50) NOT NULL,
        full_name VARCHAR(100) NOT NULL,
        role VARCHAR(20) NOT NULL
    );
    
    CREATE TABLE IF NOT EXISTS employees (
        id INT PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        department VARCHAR(50) NOT NULL,
        salary DOUBLE NOT NULL
    );
    
    CREATE TABLE IF NOT EXISTS attendance (
        id INT AUTO_INCREMENT PRIMARY KEY,
        student_id VARCHAR(50) NOT NULL,
        subject VARCHAR(100) NOT NULL,
        date DATE NOT NULL,
        status VARCHAR(20) NOT NULL
    );
    
    -- Insert sample data
    INSERT INTO users VALUES ('admin', 'admin123', 'Administrator', 'ADMIN');
    INSERT INTO users VALUES ('teacher', 'teacher123', 'John Smith', 'TEACHER');
    INSERT INTO users VALUES ('student', 'student123', 'Alice Johnson', 'STUDENT');
    
    INSERT INTO employees VALUES (101, 'John Doe', 'Engineering', 75000);
    INSERT INTO employees VALUES (102, 'Jane Smith', 'Marketing', 65000);
    INSERT INTO employees VALUES (103, 'Robert Johnson', 'Human Resources', 60000);
    INSERT INTO employees VALUES (104, 'Emily Williams', 'Finance', 70000);
    */
}

// UserDAO.java
package com.studentportal.dao;

import com.studentportal.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    
    public User authenticate(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;
        
        try {
            connection = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setFullName(resultSet.getString("full_name"));
                user.setRole(resultSet.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                DatabaseUtil.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return user;
    }
}

// EmployeeDAO.java
package com.studentportal.dao;

import com.studentportal.model.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    
    public List<Employee> getAllEmployees() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Employee> employees = new ArrayList<>();
        
        try {
            connection = DatabaseUtil.getConnection();
            statement = connection.createStatement();
            String sql = "SELECT * FROM employees";
            resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("id"));
                employee.setName(resultSet.getString("name"));
                employee.setDepartment(resultSet.getString("department"));
                employee.setSalary(resultSet.getDouble("salary"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                DatabaseUtil.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return employees;
    }
    
    public Employee getEmployeeById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Employee employee = null;
        
        try {
            connection = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM employees WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                employee = new Employee();
                employee.setId(resultSet.getInt("id"));
                employee.setName(resultSet.getString("name"));
                employee.setDepartment(resultSet.getString("department"));
                employee.setSalary(resultSet.getDouble("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                DatabaseUtil.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return employee;
    }
}

// AttendanceDAO.java
package com.studentportal.dao;

import com.studentportal.model.Attendance;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    
    public boolean saveAttendance(Attendance attendance) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean result = false;
        
        try {
            connection = DatabaseUtil.getConnection();
            String sql = "INSERT INTO attendance (student_id, subject, date, status) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, attendance.getStudentId());
            statement.setString(2, attendance.getSubject());
            statement.setDate(3, new java.sql.Date(attendance.getDate().getTime()));
            statement.setString(4, attendance.getStatus());
            
            int rowsInserted = statement.executeUpdate();
            result = (rowsInserted > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                DatabaseUtil.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public List<Attendance> getAttendanceByStudentId(String studentId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Attendance> attendanceList = new ArrayList<>();
        
        try {
            connection = DatabaseUtil.getConnection();
            String sql = "SELECT * FROM attendance WHERE student_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, studentId);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Attendance attendance = new Attendance();
                attendance.setId(resultSet.getInt("id"));
                attendance.setStudentId(resultSet.getString("student_id"));
                attendance.setSubject(resultSet.getString("subject"));
                attendance.setDate(resultSet.getDate("date"));
                attendance.setStatus(resultSet.getString("status"));
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                DatabaseUtil.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return attendanceList;
    }
}

// --- SERVLET CLASSES ---

// LoginServlet.java
package com.studentportal.servlet;

import com.studentportal.dao.UserDAO;
import com.studentportal.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticate(username, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("welcome.jsp");
        } else {
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("login.html").forward(request, response);
        }
    }
}

// EmployeeServlet.java
package com.studentportal.servlet;

import com.studentportal.dao.EmployeeDAO;
import com.studentportal.model.Employee;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        EmployeeDAO employeeDAO = new EmployeeDAO();
        
        String employeeIdParam = request.getParameter("employeeId");
        if (employeeIdParam != null && !employeeIdParam.isEmpty()) {
            try {
                int employeeId = Integer.parseInt(employeeIdParam);
                Employee employee = employeeDAO.getEmployeeById(employeeId);
                
                if (employee != null) {
                    request.setAttribute("searchedEmployee", employee);
                } else {
                    request.setAttribute("searchErrorMessage", "Employee not found");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("searchErrorMessage", "Invalid employee ID");
            }
        }
        
        List<Employee> allEmployees = employeeDAO.getAllEmployees();
        request.setAttribute("employees", allEmployees);
        
        request.getRequestDispatcher("employee.jsp").forward(request, response);
    }
}

// AttendanceServlet.java
package com.studentportal.servlet;

import com.studentportal.dao.AttendanceDAO;
import com.studentportal.model.Attendance;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/attendance")
public class AttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String studentId = request.getParameter("studentId");
        if (studentId != null && !studentId.isEmpty()) {
            AttendanceDAO attendanceDAO = new AttendanceDAO();
            request.setAttribute("attendanceList", attendanceDAO.getAttendanceByStudentId(studentId));
        }
        
        request.getRequestDispatcher("attendance.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String studentId = request.getParameter("studentId");
        String subject = request.getParameter("subject");
        String dateStr = request.getParameter("date");
        String status = request.getParameter("status");
        
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateStr);
            
            Attendance attendance = new Attendance();
            attendance.setStudentId(studentId);
            attendance.setSubject(subject);
            attendance.setDate(date);
            attendance.setStatus(status);
            
            AttendanceDAO attendanceDAO = new AttendanceDAO();
            boolean success = attendanceDAO.saveAttendance(attendance);
            
            if (success) {
                request.setAttribute("successMessage", "Attendance recorded successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to record attendance");
            }
        } catch (ParseException e) {
            request.setAttribute("errorMessage", "Invalid date format");
        }
        
        request.getRequestDispatcher("attendance.jsp").forward(request, response);
    }
}

// --- JSP AND HTML FILES ---

// index.html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Portal</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            text-align: center;
        }
        .menu {
            display: flex;
            justify-content: space-around;
            margin-top: 30px;
        }
        .menu a {
            display: inline-block;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            text-align: center;
            width: 150px;
        }
        .menu a:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome to Student Portal</h1>
        <div class="menu">
            <a href="login.html">Login</a>
            <a href="employees">Employee Directory</a>
            <a href="attendance.jsp">Attendance System</a>
        </div>
    </div>
</body>
</html>

// login.html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 400px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            color: #333;
            text-align: center;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }
        button:hover {
            background-color: #45a049;
        }
        .error {
            color: red;
            margin-bottom: 15px;
        }
        .home-link {
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Login to Student Portal</h2>
        <div class="error">${errorMessage}</div>
        <form action="login" method="post">
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit">Login</button>
        </form>
        <div class="home-link">
            <a href="index.html">Back to Home</a>
        </div>
    </div>
</body>
</html>

// welcome.jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.studentportal.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            color: #333;
            text-align: center;
        }
        .welcome-message {
            text-align: center;
            margin: 20px 0;
            font-size: 18px;
        }
        .user-info {
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .user-info p {
            margin: 5px 0;
        }
        .menu {
            display: flex;
            justify-content: space-around;
            margin-top: 20px;
        }
        .menu a {
            display: inline-block;
            padding: 10px 15px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            text-align: center;
        }
        .menu a:hover {
            background-color: #45a049;
        }
        .logout {
            text-align: center;
            margin-top: 20px;
        }
        .logout a {
            color: #666;
            text-decoration: none;
        }
        .logout a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }
    %>
    <div class="container">
        <h2>Student Portal Dashboard</h2>
        <div class="welcome-message">
            Welcome, <%= user.getFullName() %>!
        </div>
        <div class="user-info">
            <p><strong>Username:</strong> <%= user.getUsername() %></p>
            <p><strong>Role:</strong> <%= user.getRole() %></p>
        </div>
        <div class="menu">
            <a href="employees">Employee Directory</a>
            <a href="attendance.jsp">Attendance System</a>
        </div>
        <div class="logout">
            <a href="index.html">Logout</a>
        </div>
    </div>
</body>
</html>

// employee.jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.studentportal.model.Employee" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Directory</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            color: #333;
            text-align: center;
        }
        .search-form {
            margin-bottom: 20px;
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 5px;
        }
        .search-form input[type="number"] {
            padding: 8px;
            width: 100px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .search-form button {
            padding: 8px 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .search-form button:hover {
            background-color: #45a049;
        }
        .search-result {
            margin-bottom: 20px;
            padding: 15px;
            background-color: #e9f7ef;
            border-radius: 5px;
        }
        .search-error {
            color: red;
            margin-bottom: 15px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #4CAF50;
            color: white;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .nav-links {
            text-align: center;
            margin-top: 20px;
        }
        .nav-links a {
            margin: 0 10px;
            text-decoration: none;
            color: #4CAF50;
        }
        .nav-links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Employee Directory</h2>
        
        <div class="search-form">
            <form action="employees" method="get">
                <label for="employeeId">Search Employee by ID:</label>
                <input type="number" id="employeeId" name="employeeId" min="1" required>
                <button type="submit">Search</button>
            </form>
        </div>
        
        <% if (request.getAttribute("searchErrorMessage") != null) { %>
            <div class="search-error">
                <%= request.getAttribute("searchErrorMessage") %>
            </div>
        <% } %>
        
        <% if (request.getAttribute("searchedEmployee") != null) { 
              Employee employee = (Employee) request.getAttribute("searchedEmployee");
        %>
            <div class="search-result">
                <h3>Search Result:</h3>
                <p><strong>ID:</strong> <%= employee.getId() %></p>
                <p><strong>Name:</strong> <%= employee.getName() %></p>
                <p><strong>Department:</strong> <%= employee.getDepartment() %></p>
                <p><strong>Salary:</strong> $<%= String.format("%.2f", employee.getSalary()) %></p>
            </div>
        <% } %>
        
        <h3>All Employees</h3>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Department</th>
                    <th>Salary</th>
                </tr>
            </thead>
            <tbody>
                <% 
                List<Employee> employees = (List<Employee>) request.getAttribute("employees");
                if (employees != null) {
                    for (Employee employee : employees) {
                %>
                <tr>
                    <td><%= employee.getId() %></td>
                    <td><%= employee.getName() %></td>
                    <td><%= employee.getDepartment() %></td>
                    <td>$<%= String.format("%.2f", employee.getSalary()) %></td>
                </tr>
                <% 
                    }
                }
                %>
            </tbody>
        </table>
        
        <div class="nav-links">
            <a href="index.html">Home</a>
            <a href="attendance.jsp">Attendance System</a>
        </div>
