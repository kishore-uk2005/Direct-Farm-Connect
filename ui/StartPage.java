package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPage implements ActionListener {

    JFrame frame;
    JButton btnLogin, btnSignup;

    public StartPage() {

        frame = new JFrame("Direct Farm Connect");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int W = screen.width;
        int H = screen.height;


        ImageIcon bgIcon = new ImageIcon(
                "D:\\temp download\\billgeneration\\projectimages\\app_background.jpg"
        );
        Image bgImg = bgIcon.getImage().getScaledInstance(W, H, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(bgImg));
        background.setBounds(0, 0, W, H);
        frame.add(background);


        JPanel textPanel = new JPanel();
        textPanel.setBounds((W / 2) - 400, 80, 800, 150);
        textPanel.setBackground(new Color(0, 0, 0, 130)); // semi-transparent
        textPanel.setLayout(null);
        background.add(textPanel);


        JLabel title = new JLabel("Direct Farm Connect", SwingConstants.CENTER);
        title.setBounds(0, 20, 800, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 42));
        title.setForeground(Color.WHITE);
        textPanel.add(title);


        JLabel subtitle = new JLabel(
                "A Platform for Direct Farmer-to-Consumer Trade",
                SwingConstants.CENTER
        );
        subtitle.setBounds(0, 80, 800, 30);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitle.setForeground(Color.LIGHT_GRAY);
        textPanel.add(subtitle);


        btnLogin = new JButton("Login");
        btnLogin.setBounds((W / 2) - 120, 300, 240, 45);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.addActionListener(this);
        background.add(btnLogin);


        btnSignup = new JButton("Sign Up");
        btnSignup.setBounds((W / 2) - 120, 360, 240, 45);
        btnSignup.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSignup.addActionListener(this);
        background.add(btnSignup);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.dispose();

        if (e.getSource() == btnLogin) {
            new Login();
        } else {
            new SignUp();
        }
    }

    public static void main(String[] args) {
        new StartPage();
    }
}
