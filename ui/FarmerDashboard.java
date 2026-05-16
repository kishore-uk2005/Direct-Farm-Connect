package ui;

import util.DBConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class FarmerDashboard implements ActionListener {

    int farmerId;

    JFrame frame;
    JTextField txtCropName, txtPrice, txtQuantity;
    JButton btnAddCrop;

    public FarmerDashboard(int farmerId) {
        this.farmerId = farmerId;

        frame = new JFrame("Farmer Dashboard");
        frame.setSize(450, 350);
        frame.setLayout(null);

        JLabel l1 = new JLabel("Crop Name");
        l1.setBounds(50, 50, 120, 30);
        frame.add(l1);

        txtCropName = new JTextField();
        txtCropName.setBounds(200, 50, 180, 30);
        frame.add(txtCropName);

        JLabel l2 = new JLabel("Price per Kg");
        l2.setBounds(50, 100, 120, 30);
        frame.add(l2);

        txtPrice = new JTextField();
        txtPrice.setBounds(200, 100, 180, 30);
        frame.add(txtPrice);

        JLabel l3 = new JLabel("Quantity (Kg)");
        l3.setBounds(50, 150, 120, 30);
        frame.add(l3);

        txtQuantity = new JTextField();
        txtQuantity.setBounds(200, 150, 180, 30);
        frame.add(txtQuantity);

        btnAddCrop = new JButton("Add Crop");
        btnAddCrop.setBounds(200, 210, 120, 35);
        btnAddCrop.addActionListener(this);
        frame.add(btnAddCrop);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String cropName = txtCropName.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int quantity = Integer.parseInt(txtQuantity.getText());

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO crops (farmer_id, crop_name, price_per_kg, quantity) VALUES (?,?,?,?)"
            );

            ps.setInt(1, farmerId);
            ps.setString(2, cropName);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Crop added successfully");


            txtCropName.setText("");
            txtPrice.setText("");
            txtQuantity.setText("");

            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding crop");
        }
    }
}
