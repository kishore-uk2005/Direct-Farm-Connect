package ui;

import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login implements ActionListener {

    JFrame frame;
    JTextField txtPhone;
    JPasswordField txtPassword;
    JButton btnLogin, btnBack;

    public Login() {

        frame = new JFrame("Login - Direct Farm Connect");


        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(130, 130, 130));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int W = screen.width;
        int H = screen.height;


        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setBounds(0, 120, W, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        frame.add(title);


        JLabel lblPhone = new JLabel("Phone Number", SwingConstants.CENTER);
        lblPhone.setBounds(0, 220, W, 30);
        lblPhone.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblPhone.setForeground(Color.WHITE);
        frame.add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setBounds((W / 2) - 200, 260, 400, 40);
        frame.add(txtPhone);


        JLabel lblPass = new JLabel("Password", SwingConstants.CENTER);
        lblPass.setBounds(0, 320, W, 30);
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblPass.setForeground(Color.WHITE);
        frame.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds((W / 2) - 200, 360, 400, 40);
        frame.add(txtPassword);


        btnLogin = new JButton("Login");
        btnLogin.setBounds((W / 2) - 200, 430, 180, 45);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.addActionListener(this);
        frame.add(btnLogin);


        btnBack = new JButton("Back");
        btnBack.setBounds((W / 2) + 20, 430, 180, 45);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.addActionListener(this);
        frame.add(btnBack);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnBack) {
            frame.dispose();
            new StartPage();
            return;
        }

        String phone = txtPhone.getText();
        String password = new String(txtPassword.getPassword());

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT id, role FROM users WHERE phone=? AND password=?"
            );
            ps.setString(1, phone);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String role = rs.getString("role");

                JOptionPane.showMessageDialog(frame, "Login Successful");
                frame.dispose();

                if (role.equals("FARMER")) {
                    new FarmerMenuDashboard(userId);
                } else {
                    new ConsumerMenuDashboard(userId);
                }

            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Phone or Password");
            }

            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Login Error");
        }
    }
}
