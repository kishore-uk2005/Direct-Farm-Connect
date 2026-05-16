package ui;

import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;

public class ViewMyCropsPage implements ActionListener {

    int farmerId;
    JFrame frame;
    JButton btnBack, btnPrint;
    JTextArea area;

    public ViewMyCropsPage(int farmerId) {
        this.farmerId = farmerId;

        frame = new JFrame("My Crops - Direct Farm Connect");


        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(130, 130, 130));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int W = screen.width;
        int H = screen.height;


        JLabel title = new JLabel("My Crops Overview", SwingConstants.CENTER);
        title.setBounds(0, 80, W, 45);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(Color.WHITE);
        frame.add(title);


        area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 16));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBounds((W / 2) - 450, 150, 900, 350);
        frame.add(scroll);


        btnBack = new JButton("Back to Menu");
        btnBack.setBounds((W / 2) - 220, 540, 200, 45);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.addActionListener(this);
        frame.add(btnBack);

        btnPrint = new JButton("Print Earnings Bill");
        btnPrint.setBounds((W / 2) + 20, 540, 220, 45);
        btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPrint.addActionListener(this);
        frame.add(btnPrint);

        loadCrops();

        frame.setVisible(true);
    }

    void loadCrops() {
        area.setText("Crop\tAvailable\tSold\n");
        area.append("------------------------------------------------\n");

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT c.crop_name, c.quantity, " +
                            "ISNULL(SUM(o.quantity_bought),0) AS sold " +
                            "FROM crops c LEFT JOIN orders o " +
                            "ON c.crop_id = o.crop_id " +
                            "WHERE c.farmer_id = ? " +
                            "GROUP BY c.crop_name, c.quantity"
            );
            ps.setInt(1, farmerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                area.append(
                        rs.getString("crop_name") + "\t" +
                                rs.getInt("quantity") + "\t\t" +
                                rs.getInt("sold") + "\n"
                );
            }

            con.close();
        } catch (Exception e) {
            area.setText("Error loading crops");
        }
    }


    void generateBill() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT c.crop_name, " +
                            "SUM(o.quantity_bought) AS sold, " +
                            "SUM(o.quantity_bought * c.price_per_kg) AS total " +
                            "FROM crops c JOIN orders o ON c.crop_id = o.crop_id " +
                            "WHERE c.farmer_id = ? " +
                            "GROUP BY c.crop_name"
            );
            ps.setInt(1, farmerId);

            ResultSet rs = ps.executeQuery();

            File dir = new File("D:\\temp download\\billgeneration");
            if (!dir.exists()) dir.mkdirs();

            File billFile = new File(dir, "Farmer_" + farmerId + "_Bill.txt");
            FileWriter fw = new FileWriter(billFile);

            fw.write("FARMER BILL\n");
            fw.write("========================\n");

            double grandTotal = 0;

            while (rs.next()) {
                fw.write("Crop: " + rs.getString("crop_name") + "\n");
                fw.write("Sold Qty: " + rs.getInt("sold") + "\n");
                fw.write("Amount Earned: Rs." + rs.getDouble("total") + "\n");
                fw.write("------------------------\n");

                grandTotal += rs.getDouble("total");
            }

            fw.write("TOTAL EARNED: Rs." + grandTotal + "\n");
            fw.close();
            con.close();

            JOptionPane.showMessageDialog(
                    frame,
                    "Bill generated successfully!\nSaved at:\n" + billFile.getAbsolutePath()
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error generating bill");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnBack) {
            frame.dispose();
            new FarmerMenuDashboard(farmerId);
        }

        if (e.getSource() == btnPrint) {
            generateBill();
        }
    }
}
