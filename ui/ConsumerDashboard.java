package ui;

import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ConsumerDashboard implements ActionListener {

    int consumerId;

    JFrame frame;
    JComboBox<String> cropBox;
    JTextField txtQty;
    JButton btnBuy, btnBack;

    int[] cropIds = new int[100];
    double[] prices = new double[100];
    int[] quantities = new int[100];
    String[] farmerPhones = new String[100];

    public ConsumerDashboard(int consumerId) {
        this.consumerId = consumerId;

        frame = new JFrame("Buy Crop - Direct Farm Connect");


        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(130, 130, 130));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int W = screen.width;


        JLabel title = new JLabel("Buy Crop", SwingConstants.CENTER);
        title.setBounds(0, 120, W, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        frame.add(title);


        addLabel("Select Crop", 240);
        cropBox = new JComboBox<>();
        cropBox.setBounds((W / 2) - 250, 280, 500, 40);
        frame.add(cropBox);


        addLabel("Buy Quantity (Kg)", 340);
        txtQty = new JTextField();
        txtQty.setBounds((W / 2) - 250, 380, 500, 40);
        frame.add(txtQty);


        btnBack = new JButton("Back");
        btnBack.setBounds((W / 2) - 260, 460, 240, 45);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.addActionListener(this);
        frame.add(btnBack);

        btnBuy = new JButton("Buy Crop");
        btnBuy.setBounds((W / 2) + 20, 460, 240, 45);
        btnBuy.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBuy.addActionListener(this);
        frame.add(btnBuy);

        loadCrops();

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

    void loadCrops() {
        try {
            Connection con = DBConnection.getConnection();

            String query =
                    "SELECT c.crop_id, c.crop_name, c.price_per_kg, c.quantity, u.phone " +
                            "FROM crops c JOIN users u ON c.farmer_id = u.id " +
                            "WHERE c.quantity > 0";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            cropBox.removeAllItems();
            int i = 0;

            while (rs.next()) {
                cropIds[i] = rs.getInt("crop_id");
                prices[i] = rs.getDouble("price_per_kg");
                quantities[i] = rs.getInt("quantity");
                farmerPhones[i] = rs.getString("phone");

                cropBox.addItem(
                        rs.getString("crop_name") +
                                " | Rs." + prices[i] +
                                " | Available: " + quantities[i] + "kg"
                );
                i++;
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnBack) {
            frame.dispose();
            new ConsumerMenuDashboard(consumerId);
            return;
        }

        int index = cropBox.getSelectedIndex();
        int buyQty = Integer.parseInt(txtQty.getText());

        if (buyQty <= 0) {
            JOptionPane.showMessageDialog(frame, "Enter valid quantity");
            return;
        }

        if (buyQty > quantities[index]) {
            JOptionPane.showMessageDialog(frame, "Not enough quantity available");
            return;
        }

        double total = buyQty * prices[index];


        String[] options = {"CASH", "GPAY"};
        int choice = JOptionPane.showOptionDialog(
                frame,
                "Select Payment Mode",
                "Payment",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        String paymentMessage;

        if (choice == 1) { // GPAY
            paymentMessage =
                    "Payment Mode: GPAY\n" +
                            "Make payment to this number:\n" +
                            farmerPhones[index] + "\n\n";
        } else {
            paymentMessage =
                    "Payment Mode: CASH\n" +
                            "Pay directly to farmer.\n\n";
        }

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps1 = con.prepareStatement(
                    "INSERT INTO orders (consumer_id, crop_id, quantity_bought, total_price) " +
                            "VALUES (?,?,?,?)"
            );
            ps1.setInt(1, consumerId);
            ps1.setInt(2, cropIds[index]);
            ps1.setInt(3, buyQty);
            ps1.setDouble(4, total);
            ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE crops SET quantity = quantity - ? WHERE crop_id = ?"
            );
            ps2.setInt(1, buyQty);
            ps2.setInt(2, cropIds[index]);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(
                    frame,
                    "Purchase Successful!\n\n" +
                            paymentMessage +
                            "Total Amount: Rs." + total + "\n\n" +
                            "You can print your bill anytime from the Consumer Menu."
            );

            frame.dispose();
            new ConsumerMenuDashboard(consumerId);

            con.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Purchase failed");
            ex.printStackTrace();
        }
    }
}
