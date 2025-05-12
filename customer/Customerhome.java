package customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Customerhome extends JFrame {
    private JButton viewCarsBtn, affordableBtn, localBtn, luxuryBtn, findCarBtn, historyBtn, editProfileBtn, logoutBtn;
    private JLabel welcomeLabel;
    private Customer currentCustomer;
    
    public Customerhome() {
        currentCustomer = DataManager.getInstance().getCurrentCustomer();
        
        if (currentCustomer == null) {
            JOptionPane.showMessageDialog(this, "No user logged in. Please login first.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            new Customerlogin();
            dispose();
            return;
        }
        
        setTitle("Car Dealership - Customer Home");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        
        setVisible(true);
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Welcome header
        JPanel headerPanel = new JPanel(new BorderLayout());
        welcomeLabel = new JLabel("Welcome, " + currentCustomer.getFullName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        logoutBtn = new JButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(100, 30));
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutBtn);
        headerPanel.add(logoutPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Menu panel with options
        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        
        // Create styled buttons with icons
        viewCarsBtn = createStyledButton("View All Cars", "View all available cars for purchase");
        affordableBtn = createStyledButton("Affordable Cars", "View cars priced under $30,000");
        localBtn = createStyledButton("Local Cars", "View cars from local manufacturers");
        luxuryBtn = createStyledButton("Luxury Cars", "View luxury cars priced between $200,000-$300,000");
        findCarBtn = createStyledButton("Find Car", "Find a car that matches your criteria");
        historyBtn = createStyledButton("Purchase History", "View your past purchases and give feedback");
        editProfileBtn = createStyledButton("Edit Profile", "Update your personal information");
        
        // Add buttons to menu panel
        menuPanel.add(viewCarsBtn);
        menuPanel.add(affordableBtn);
        menuPanel.add(localBtn);
        menuPanel.add(luxuryBtn);
        menuPanel.add(findCarBtn);
        menuPanel.add(historyBtn);
        
        // Add profile edit button in a separate panel
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        profilePanel.add(editProfileBtn);
        
        // Add panels to main layout
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(profilePanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        viewCarsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Viewcar("All Available Cars", DataManager.getInstance().getAvailableCars());
            }
        });
        
        affordableBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Viewcar("Affordable Cars (Under $30,000)", DataManager.getInstance().getAffordableCars());
            }
        });
        
        localBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Viewcar("Local Cars", DataManager.getInstance().getLocalCars());
            }
        });
        
        luxuryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Viewcar("Luxury Cars ($200,000-$300,000)", DataManager.getInstance().getLuxuryCars());
            }
        });
        
        findCarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Findcar();
            }
        });
        
        historyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new History();
            }
        });
        
        editProfileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Editprofile();
            }
        });
        
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(Customerhome.this, 
                    "Are you sure you want to logout?", 
                    "Confirm Logout", 
                    JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    DataManager.getInstance().logout();
                    new Customerlogin();
                    dispose();
                }
            }
        });
    }
    
    private JButton createStyledButton(String text, String toolTip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setToolTipText(toolTip);
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(180, 100));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(48, 79, 172));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(59, 89, 182));
            }
        });
        
        return button;
    }
} 