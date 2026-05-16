package ui;

import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignUp implements ActionListener {

    JFrame frame;
    JTextField txtUsername, txtPhone;
    JPasswordField txtPassword, txtConfirm;
    JComboBox<String> roleBox;
    JButton btnRegister, btnBack;

    public SignUp() {

        frame = new JFrame("Sign Up - Direct Farm Connect");

        // Full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(130, 130, 130));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int W = screen.width;

        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setBounds(0, 100, W, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        frame.add(title);

        addLabel("Username", 200);
        txtUsername = addTextField(240);

        addLabel("Phone Number", 300);
        txtPhone = addTextField(340);

        addLabel("Password", 400);
        txtPassword = addPasswordField(440);

        addLabel("Confirm Password", 500);
        txtConfirm = addPasswordField(540);

        addLabel("Role", 600);
        roleBox = new JComboBox<>(new String[]{"FARMER", "CONSUMER"});
        roleBox.setBounds((W / 2) - 200, 640, 400, 40);
        frame.add(roleBox);

        btnRegister = new JButton("Sign Up");
        btnRegister.setBounds((W / 2) - 200, 710, 180, 45);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegister.addActionListener(this);
        frame.add(btnRegister);

        btnBack = new JButton("Back");
        btnBack.setBounds((W / 2) + 20, 710, 180, 45);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.addActionListener(this);
        frame.add(btnBack);

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

    private JPasswordField addPasswordField(int y) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        JPasswordField field = new JPasswordField();
        field.setBounds((screen.width / 2) - 200, y, 400, 40);
        frame.add(field);
        return field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnBack) {
            frame.dispose();
            new StartPage();
            return;
        }

        String username = txtUsername.getText().trim();
        String phone = txtPhone.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirm.getPassword());
        String role = roleBox.getSelectedItem().toString();

        // 🔒 VALIDATIONS
        if (username.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(frame, "Enter valid 10-digit phone number");
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(frame, "Passwords do not match");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            // 🔍 CHECK PHONE ALREADY EXISTS
            PreparedStatement check = con.prepareStatement(
                    "SELECT COUNT(*) FROM users WHERE phone = ?"
            );
            check.setString(1, phone);
            ResultSet rs = check.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(frame, "Phone number already registered");
                con.close();
                return;
            }


            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users(username, phone, password, role) VALUES (?,?,?,?)"
            );

            ps.setString(1, username);
            ps.setString(2, phone);
            ps.setString(3, password);
            ps.setString(4, role);

            ps.executeUpdate();
            con.close();

            JOptionPane.showMessageDialog(frame, "Registration Successful");
            frame.dispose();
            new Login();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    frame,
                    "Registration Failed:\n" + ex.getMessage()
            );
        }
    }
}
