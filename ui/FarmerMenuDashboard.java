package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FarmerMenuDashboard implements ActionListener {

    int farmerId;
    JFrame frame;
    JButton btnAddCrop, btnViewCrops, btnLogout;

    public FarmerMenuDashboard(int farmerId) {
        this.farmerId = farmerId;

        frame = new JFrame("Farmer Dashboard - Direct Farm Connect");


        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(130, 130, 130));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int W = screen.width;


        JLabel title = new JLabel("Farmer Dashboard", SwingConstants.CENTER);
        title.setBounds(0, 120, W, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        frame.add(title);


        btnAddCrop = createButton("Add Crop", 260);
        btnViewCrops = createButton("View My Crops", 330);
        btnLogout = createButton("Logout", 400);

        frame.add(btnAddCrop);
        frame.add(btnViewCrops);
        frame.add(btnLogout);

        frame.setVisible(true);
    }

    private JButton createButton(String text, int y) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        JButton btn = new JButton(text);
        btn.setBounds((screen.width / 2) - 200, y, 400, 45);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        frame.dispose();

        if (e.getSource() == btnAddCrop) {
            new AddCropPage(farmerId);
        }
        else if (e.getSource() == btnViewCrops) {
            new ViewMyCropsPage(farmerId);
        }
        else {
            new StartPage();
        }
    }
}
