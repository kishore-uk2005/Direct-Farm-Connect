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

@WebServlet("/buy-crop")
public class BuyCropServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Integer consumerId =
                (Integer) session.getAttribute("userId");

        // Check whether consumer is logged in
        if (consumerId == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Get values from the purchase form
        String cropIdText = request.getParameter("cropId");
        String quantityText = request.getParameter("quantity");
        String payment = request.getParameter("payment");

        int cropId;
        int quantity;

        try {

            cropId = Integer.parseInt(cropIdText);
            quantity = Integer.parseInt(quantityText);

        } catch (NumberFormatException e) {

            response.setContentType("text/html;charset=UTF-8");

            PrintWriter out = response.getWriter();

            out.println("<h1>Invalid Input</h1>");
            out.println("<p>Please enter a valid crop and quantity.</p>");
            out.println("<a href='available-crops'>Back to Available Crops</a>");

            return;
        }

        // Quantity must be greater than zero
        if (quantity <= 0) {

            response.setContentType("text/html;charset=UTF-8");

            PrintWriter out = response.getWriter();

            out.println("<h1>Invalid Quantity</h1>");
            out.println("<p>Quantity must be greater than zero.</p>");
            out.println("<a href='available-crops'>Back to Available Crops</a>");

            return;
        }

        try {

            Connection con = DBConnection.getConnection();

            /*
             * Step 1:
             * Get crop price and available quantity.
             */
            String selectSql =
                    "SELECT price_per_kg, quantity " +
                            "FROM crops " +
                            "WHERE crop_id = ?";

            PreparedStatement selectPs =
                    con.prepareStatement(selectSql);

            selectPs.setInt(1, cropId);

            ResultSet rs = selectPs.executeQuery();

            // Check whether crop exists
            if (!rs.next()) {

                rs.close();
                selectPs.close();
                con.close();

                response.setContentType("text/html;charset=UTF-8");

                PrintWriter out = response.getWriter();

                out.println("<h1>Crop Not Found</h1>");
                out.println("<p>The selected crop does not exist.</p>");
                out.println("<a href='available-crops'>Back to Available Crops</a>");

                return;
            }

            /*
             * Get price and available quantity
             */
            double price =
                    rs.getDouble("price_per_kg");

            int availableQuantity =
                    rs.getInt("quantity");

            rs.close();
            selectPs.close();

            /*
             * Step 2:
             * Check whether requested quantity is available.
             */
            if (quantity > availableQuantity) {

                con.close();

                response.setContentType("text/html;charset=UTF-8");

                PrintWriter out = response.getWriter();

                out.println("<!DOCTYPE html>");
                out.println("<html>");

                out.println("<head>");
                out.println("<title>Insufficient Quantity</title>");
                out.println("<link rel='stylesheet' href='css/style.css'>");
                out.println("</head>");

                out.println("<body>");

                out.println("<header>");
                out.println("<div class='container'>");
                out.println("<div class='logo'>🌱 Direct Farm Connect</div>");

                out.println("<nav>");
                out.println("<a href='consumer-dashboard.html'>Dashboard</a>");
                out.println("</nav>");

                out.println("</div>");
                out.println("</header>");

                out.println("<main>");

                out.println("<div class='empty-message'>");

                out.println("<h2>⚠️ Insufficient Quantity</h2>");

                out.println("<p>");
                out.println("Only " + availableQuantity +
                        " Kg of this crop is currently available.");
                out.println("</p>");

                out.println("<br>");

                out.println("<a href='available-crops' class='btn'>");
                out.println("Back to Available Crops");
                out.println("</a>");

                out.println("</div>");

                out.println("</main>");

                out.println("<footer>");
                out.println("© 2026 Direct Farm Connect");
                out.println("</footer>");

                out.println("</body>");
                out.println("</html>");

                return;
            }

            /*
             * Step 3:
             * Calculate total price.
             */
            double total = quantity * price;

            /*
             * Step 4:
             * Insert order into orders table.
             */
            String insertSql =
                    "INSERT INTO orders " +
                            "(consumer_id, crop_id, quantity_bought, total_price) " +
                            "VALUES (?, ?, ?, ?)";

            PreparedStatement insertPs =
                    con.prepareStatement(insertSql);

            insertPs.setInt(1, consumerId);
            insertPs.setInt(2, cropId);
            insertPs.setInt(3, quantity);
            insertPs.setDouble(4, total);

            insertPs.executeUpdate();

            insertPs.close();

            /*
             * Step 5:
             * Decrease crop quantity.
             */
            String updateSql =
                    "UPDATE crops " +
                            "SET quantity = quantity - ? " +
                            "WHERE crop_id = ?";

            PreparedStatement updatePs =
                    con.prepareStatement(updateSql);

            updatePs.setInt(1, quantity);
            updatePs.setInt(2, cropId);

            updatePs.executeUpdate();

            updatePs.close();

            con.close();

            /*
             * Step 6:
             * Show professional purchase-success page.
             */
            response.setContentType("text/html;charset=UTF-8");

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");

            out.println(
                    "<title>Purchase Successful - Direct Farm Connect</title>"
            );

            out.println(
                    "<link rel='stylesheet' href='css/style.css'>"
            );

            out.println("</head>");

            out.println("<body>");

            // Header
            out.println("<header>");

            out.println("<div class='container'>");

            out.println(
                    "<div class='logo'>🌱 Direct Farm Connect</div>"
            );

            out.println("<nav>");

            out.println(
                    "<a href='consumer-dashboard.html'>Dashboard</a>"
            );

            out.println(
                    "<a href='index.html'>Logout</a>"
            );

            out.println("</nav>");

            out.println("</div>");

            out.println("</header>");

            // Main content
            out.println("<main>");

            out.println("<div class='success-card'>");

            // Success icon
            out.println("<div class='success-icon'>✓</div>");

            // Heading
            out.println("<h1>Purchase Successful!</h1>");

            out.println("<p>");
            out.println(
                    "Your crop has been purchased successfully."
            );
            out.println("</p>");

            // Purchase details
            out.println("<div class='purchase-summary'>");

            out.println("<p>");
            out.println(
                    "<strong>Quantity:</strong> "
                            + quantity + " Kg"
            );
            out.println("</p>");

            out.println("<p>");
            out.println(
                    "<strong>Total Amount:</strong> ₹"
                            + total
            );
            out.println("</p>");

            out.println("<p>");
            out.println(
                    "<strong>Payment:</strong> "
                            + payment
            );
            out.println("</p>");

            out.println("</div>");

            // Buttons
            out.println("<div class='page-actions'>");

            out.println(
                    "<a href='available-crops' class='btn'>"
            );

            out.println("Continue Shopping");

            out.println("</a>");

            out.println(
                    "<a href='consumer-bill' class='btn secondary-btn'>"
            );

            out.println("View My Purchases");

            out.println("</a>");

            out.println("</div>");

            out.println("</div>");

            out.println("</main>");

            // Footer
            out.println("<footer>");

            out.println("© 2026 Direct Farm Connect");

            out.println("</footer>");

            out.println("</body>");

            out.println("</html>");

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");
            out.println("<title>Database Error</title>");
            out.println("<link rel='stylesheet' href='css/style.css'>");
            out.println("</head>");

            out.println("<body>");

            out.println("<div class='empty-message'>");

            out.println("<h2>⚠️ Database Error</h2>");

            out.println("<p>");
            out.println(e.getMessage());
            out.println("</p>");

            out.println("<br>");

            out.println("<a href='available-crops' class='btn'>");
            out.println("Back to Available Crops");
            out.println("</a>");

            out.println("</div>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}