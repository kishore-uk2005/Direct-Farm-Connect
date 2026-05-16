package ui;

import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;

public class ConsumerBillPage {

    int consumerId;
    JFrame frame;

    public ConsumerBillPage(int consumerId) {
        this.consumerId = consumerId;

        generateBill();
        showConfirmationUI();
    }


    private void generateBill() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT c.crop_name, o.quantity_bought, o.total_price " +
                            "FROM orders o JOIN crops c ON o.crop_id = c.crop_id " +
                            "WHERE o.consumer_id = ?"
            );
            ps.setInt(1, consumerId);

            ResultSet rs = ps.executeQuery();

            File dir = new File("D:\\temp download\\billgeneration\\consumerbill");
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, "Consumer_" + consumerId + "_Bill.txt");
            FileWriter fw = new FileWriter(file);

            fw.write("CONSUMER BILL\n");
            fw.write("====================\n\n");

            boolean hasOrders = false;
            double total = 0;

            while (rs.next()) {
                hasOrders = true;

                fw.write("Crop: " + rs.getString("crop_name") + "\n");
                fw.write("Quantity: " + rs.getInt("quantity_bought") + "\n");
                fw.write("Amount: Rs." + rs.getDouble("total_price") + "\n");
                fw.write("--------------------\n");

                total += rs.getDouble("total_price");
            }

            if (!hasOrders) {
                fw.write("No purchases made yet.\n");
                fw.write("Total Paid: Rs.0\n");
            } else {
                fw.write("\nTOTAL PAID: Rs." + total + "\n");
            }

            fw.close();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error generating consumer bill");
        }
    }


    private void showConfirmationUI() {

        frame = new JFrame("Consumer Bill - Direct Farm Connect");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(130, 130, 130));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int W = screen.width;

        JLabel title = new JLabel("Consumer Bill Generated", SwingConstants.CENTER);
        title.setBounds(0, 180, W, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        frame.add(title);

        JLabel info = new JLabel(
                "Your bill has been saved successfully.",
                SwingConstants.CENTER
        );
        info.setBounds(0, 250, W, 30);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        info.setForeground(Color.WHITE);
        frame.add(info);

        JLabel path = new JLabel(
                "Location: D:\\temp download\\billgeneration\\consumerbill",
                SwingConstants.CENTER
        );
        path.setBounds(0, 300, W, 30);
        path.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        path.setForeground(Color.LIGHT_GRAY);
        frame.add(path);

        JButton btnBack = new JButton("Back to Menu");
        btnBack.setBounds((W / 2) - 150, 380, 300, 45);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.addActionListener(e -> {
            frame.dispose();
            new ConsumerMenuDashboard(consumerId);
        });
        frame.add(btnBack);

        frame.setVisible(true);
    }
}
