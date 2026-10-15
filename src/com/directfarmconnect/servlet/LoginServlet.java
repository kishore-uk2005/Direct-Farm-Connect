package com.directfarmconnect.servlet;

import util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        try {
            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT id, role FROM users " +
                            "WHERE phone = ? AND password = ? AND role = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, phone);
            ps.setString(2, password);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int userId = rs.getInt("id");
                String userRole = rs.getString("role");

                // Create login session
                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);
                session.setAttribute("role", userRole);

                con.close();

                // Redirect according to role
                if (userRole.equals("FARMER")) {
                    response.sendRedirect("farmer-dashboard.html");
                } else {
                    response.sendRedirect("consumer-dashboard.html");
                }

            } else {

                con.close();

                response.setContentType("text/html");

                response.getWriter().println(
                        "<h1>Login Failed</h1>" +
                                "<p>Invalid phone number, password, or role.</p>" +
                                "<a href='login.html'>Try Again</a>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h1>Database Error</h1>" +
                            "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}