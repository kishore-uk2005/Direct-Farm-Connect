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

@WebServlet("/add-crop")
public class AddCropServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String cropName = request.getParameter("cropName");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        HttpSession session = request.getSession();

        Integer farmerId = (Integer) session.getAttribute("userId");

        if (farmerId == null) {
            response.sendRedirect("login.html");
            return;
        }

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "INSERT INTO crops " +
                            "(farmer_id, crop_name, price_per_kg, quantity) " +
                            "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, farmerId);
            ps.setString(2, cropName);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);

            ps.executeUpdate();

            ps.close();
            con.close();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h1>Crop Added Successfully</h1>" +
                            "<p>Crop: " + cropName + "</p>" +
                            "<p>Price per Kg: Rs." + price + "</p>" +
                            "<p>Quantity: " + quantity + " Kg</p>" +
                            "<br>" +
                            "<a href='farmer-dashboard.html'>Back to Farmer Dashboard</a>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h1>Error Adding Crop</h1>" +
                            "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}