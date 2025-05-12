import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Customerlogin extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JLabel statusLabel;
    
    public Customerlogin() {
        setTitle("Car Dealership - Customer Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        
        setVisible(true);
    }
    
    private void initComponents() {
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Customer Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        
        // For the empty cell and login button
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        // Status label for error messages
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Center panel to hold form and status
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                
                if (email.isEmpty() || password.isEmpty()) {
                    statusLabel.setText("Please enter both email and password");
                    return;
                }
                
                boolean authenticated = DataManager.getInstance().authenticateCustomer(email, password);
                
                if (authenticated) {
                    statusLabel.setText("");
                    JOptionPane.showMessageDialog(Customerlogin.this, 
                        "Login successful. Welcome " + DataManager.getInstance().getCurrentCustomer().getFullName() + "!",
                        "Login Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Open the home screen
                    new Customerhome();
                    dispose(); // Close login window
                } else {
                    statusLabel.setText("Invalid email or password. Please try again.");
                }
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Customerregistration();
                dispose(); // Close login window
            }
        });
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Customerlogin();
            }
        });
    }
} 