package ui;

import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddCropPage implements ActionListener {

    int farmerId;

    JFrame frame;
    JTextField txtName, txtPrice, txtQty;
    JButton btnAdd, btnBack;

    public AddCropPage(int farmerId) {
        this.farmerId = farmerId;

        frame = new JFrame("Add Crop - Direct Farm Connect");


        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(130, 130, 130));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int W = screen.width;


        JLabel title = new JLabel("Add New Crop", SwingConstants.CENTER);
        title.setBounds(0, 120, W, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        frame.add(title);


        addLabel("Crop Name", 240);
        txtName = addTextField(280);


        addLabel("Price per Kg", 340);
        txtPrice = addTextField(380);


        addLabel("Quantity (Kg)", 440);
        txtQty = addTextField(480);


        btnBack = new JButton("Back to Menu");
        btnBack.setBounds((W / 2) - 200, 560, 180, 45);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.addActionListener(this);
        frame.add(btnBack);

        btnAdd = new JButton("Add Crop");
        btnAdd.setBounds((W / 2) + 20, 560, 180, 45);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdd.addActionListener(this);
        frame.add(btnAdd);

        frame.setVisible(true);
    }

    private void addLabel(String text, int y) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setBounds(0, y, screen.width, 30);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        frame.add(label);
    }

    private JTextField addTextField(int y) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        JTextField field = new JTextField();
        field.setBounds((screen.width / 2) - 200, y, 400, 40);
        frame.add(field);
        return field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        if (e.getSource() == btnBack) {
            frame.dispose();
            new FarmerMenuDashboard(farmerId);
            return;
        }


        try {
            String cropName = txtName.getText();
            double price = Double.parseDouble(txtPrice.getText());
            int quantity = Integer.parseInt(txtQty.getText());

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO crops (farmer_id, crop_name, price_per_kg, quantity) VALUES (?,?,?,?)"
            );
            ps.setInt(1, farmerId);
            ps.setString(2, cropName);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);

            ps.executeUpdate();
            con.close();

            JOptionPane.showMessageDialog(frame, "Crop added successfully");

            frame.dispose();
            new ViewMyCropsPage(farmerId);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error adding crop");
        }
    }
}
