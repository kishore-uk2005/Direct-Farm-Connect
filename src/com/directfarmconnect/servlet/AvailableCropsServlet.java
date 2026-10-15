package com.directfarmconnect.servlet;

import util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/available-crops")
public class AvailableCropsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer consumerId = (Integer) session.getAttribute("userId");

        if (consumerId == null) {
            response.sendRedirect("login.html");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");

        try {
            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT crop_id, crop_name, price_per_kg, quantity " +
                            "FROM crops WHERE quantity > 0 ORDER BY crop_id";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Available Crops - Direct Farm Connect</title>");
            out.println("<link rel='stylesheet' href='css/style.css'>");
            out.println("</head>");

            out.println("<body>");

            // Header
            out.println("<header>");
            out.println("<div class='container'>");
            out.println("<div class='logo'>🌱 Direct Farm Connect</div>");
            out.println("<nav>");
            out.println("<a href='consumer-dashboard.html'>Dashboard</a>");
            out.println("<a href='index.html'>Logout</a>");
            out.println("</nav>");
            out.println("</div>");
            out.println("</header>");

            // Main content
            out.println("<main>");
            out.println("<div class='dashboard'>");

            out.println("<h1>🥕 Available Crops</h1>");
            out.println("<p class='dashboard-subtitle'>");
            out.println("Fresh crops available directly from farmers.");
            out.println("</p>");

            out.println("<div class='table-container'>");

            out.println("<table>");

            out.println("<tr>");
            out.println("<th>Crop ID</th>");
            out.println("<th>Crop Name</th>");
            out.println("<th>Price / Kg</th>");
            out.println("<th>Available Quantity</th>");
            out.println("<th>Purchase</th>");
            out.println("</tr>");

            boolean found = false;

            while (rs.next()) {

                found = true;

                int cropId = rs.getInt("crop_id");
                String cropName = rs.getString("crop_name");
                double price = rs.getDouble("price_per_kg");
                int quantity = rs.getInt("quantity");

                out.println("<tr>");

                out.println("<td>" + cropId + "</td>");

                out.println("<td><strong>" + cropName + "</strong></td>");

                out.println("<td>₹" + price + "</td>");

                out.println("<td>" + quantity + " Kg</td>");

                out.println("<td>");

                out.println("<form action='buy-crop' method='post' class='buy-form'>");

                out.println("<input type='hidden' name='cropId' value='" + cropId + "'>");

                out.println("<input type='number' name='quantity' " +
                        "min='1' max='" + quantity + "' " +
                        "placeholder='Qty (Kg)' required>");

                out.println("<select name='payment'>");
                out.println("<option value='CASH'>CASH</option>");
                out.println("<option value='GPAY'>GPAY</option>");
                out.println("</select>");

                out.println("<button type='submit'>Buy Now</button>");

                out.println("</form>");

                out.println("</td>");

                out.println("</tr>");
            }

            out.println("</table>");

            out.println("</div>");

            if (!found) {
                out.println("<div class='empty-message'>");
                out.println("<h2>🌱 No Crops Available</h2>");
                out.println("<p>There are currently no crops available for purchase.</p>");
                out.println("</div>");
            }

            out.println("<div class='page-actions'>");
            out.println("<a href='consumer-dashboard.html' class='btn'>");
            out.println("← Back to Dashboard");
            out.println("</a>");
            out.println("</div>");

            out.println("</div>");
            out.println("</main>");

            out.println("<footer>");
            out.println("© 2026 Direct Farm Connect");
            out.println("</footer>");

            out.println("</body>");
            out.println("</html>");

            rs.close();
            ps.close();
            con.close();

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