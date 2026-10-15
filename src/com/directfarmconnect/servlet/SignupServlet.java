package com.directfarmconnect.servlet;

import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username =
                request.getParameter("username");

        String phone =
                request.getParameter("phone");

        String password =
                request.getParameter("password");

        String role =
                request.getParameter("role");

        response.setContentType("text/html");

        try {

            Connection con =
                    DBConnection.getConnection();

            // Check phone
            PreparedStatement check =
                    con.prepareStatement(
                            "SELECT COUNT(*) FROM users WHERE phone = ?"
                    );

            check.setString(1, phone);

            ResultSet rs =
                    check.executeQuery();

            rs.next();

            if (rs.getInt(1) > 0) {

                response.getWriter().println(
                        "<h1>Phone Already Registered</h1>" +
                                "<a href='signup.html'>Try Again</a>"
                );

                con.close();
                return;
            }

            String sql =
                    "INSERT INTO users " +
                            "(username, phone, password, role) " +
                            "VALUES (?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, phone);
            ps.setString(3, password);
            ps.setString(4, role);

            ps.executeUpdate();

            ps.close();
            check.close();
            rs.close();
            con.close();

            response.getWriter().println(
                    "<h1>Registration Successful</h1>" +
                            "<p>Account created successfully.</p>" +
                            "<a href='login.html'>Go to Login</a>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                    "<h1>Registration Failed</h1>" +
                            "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}