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

@WebServlet("/consumer-bill")
public class ConsumerBillServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Integer consumerId =
                (Integer) session.getAttribute("userId");

        if (consumerId == null) {
            response.sendRedirect("login.html");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT c.crop_name, " +
                            "o.quantity_bought, " +
                            "o.total_price " +
                            "FROM orders o " +
                            "JOIN crops c ON o.crop_id = c.crop_id " +
                            "WHERE o.consumer_id = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, consumerId);

            ResultSet rs = ps.executeQuery();

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");
            out.println("<title>My Purchases - Direct Farm Connect</title>");
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

            // Main
            out.println("<main>");

            out.println("<div class='dashboard'>");

            out.println("<h1>🛒 My Purchases</h1>");

            out.println("<p class='dashboard-subtitle'>");
            out.println("View your purchase history and order details.");
            out.println("</p>");

            out.println("<div class='table-container'>");

            out.println("<table>");

            out.println("<tr>");
            out.println("<th>Crop Name</th>");
            out.println("<th>Quantity</th>");
            out.println("<th>Total Price</th>");
            out.println("</tr>");

            double grandTotal = 0;
            boolean found = false;

            while (rs.next()) {

                found = true;

                String cropName =
                        rs.getString("crop_name");

                int quantity =
                        rs.getInt("quantity_bought");

                double total =
                        rs.getDouble("total_price");

                grandTotal += total;

                out.println("<tr>");

                out.println("<td><strong>" +
                        cropName +
                        "</strong></td>");

                out.println("<td>" +
                        quantity +
                        " Kg</td>");

                out.println("<td>₹" +
                        total +
                        "</td>");

                out.println("</tr>");
            }

            out.println("</table>");

            out.println("</div>");

            if (found) {

                out.println("<div class='total-box'>");

                out.println("<h2>");
                out.println("Grand Total: ₹" + grandTotal);
                out.println("</h2>");

                out.println("</div>");

            } else {

                out.println("<div class='empty-message'>");

                out.println("<h2>🛒 No Purchases Yet</h2>");

                out.println("<p>");
                out.println("You haven't purchased any crops yet.");
                out.println("</p>");

                out.println("<br>");

                out.println("<a href='available-crops' class='btn'>");
                out.println("Browse Available Crops");
                out.println("</a>");

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