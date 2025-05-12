package customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Customerregistration extends JFrame {
    private JTextField firstNameField, lastNameField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private JTextArea addressArea;
    private JButton registerButton, cancelButton;
    private JLabel statusLabel;
    
    public Customerregistration() {
        setTitle("Car Dealership - Customer Registration");
        setSize(500, 500);
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
        JLabel titleLabel = new JLabel("Customer Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        
        // First Name
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(20);
        
        // Last Name
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField(20);
        
        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        
        // Phone
        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField(20);
        
        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        
        // Confirm Password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField(20);
        
        // Address (Multi-line)
        JLabel addressLabel = new JLabel("Address:");
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        
        // Add all form components
        formPanel.add(firstNameLabel);
        formPanel.add(firstNameField);
        formPanel.add(lastNameLabel);
        formPanel.add(lastNameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(confirmPasswordLabel);
        formPanel.add(confirmPasswordField);
        formPanel.add(addressLabel);
        formPanel.add(addressScrollPane);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
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
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input values
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String address = addressArea.getText().trim();
                
                // Validate input
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || 
                    phone.isEmpty() || password.isEmpty() || address.isEmpty()) {
                    statusLabel.setText("Please fill out all fields");
                    return;
                }
                
                if (!password.equals(confirmPassword)) {
                    statusLabel.setText("Passwords do not match");
                    return;
                }
                
                // Check if email already exists
                if (DataManager.getInstance().getCustomerByEmail(email) != null) {
                    statusLabel.setText("Email already registered. Please use a different email.");
                    return;
                }
                
                // Create and add new customer
                int customerId = DataManager.getInstance().getNextCustomerId();
                Customer newCustomer = new Customer(customerId, firstName, lastName, email, password, phone, address);
                DataManager.getInstance().addCustomer(newCustomer);
                DataManager.getInstance().setCurrentCustomer(newCustomer);
                
                JOptionPane.showMessageDialog(Customerregistration.this, 
                    "Registration successful! Welcome " + newCustomer.getFullName() + "!",
                    "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
                
                // Open the home screen
                new Customerhome();
                dispose(); // Close registration window
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Go back to login screen
                new Customerlogin();
                dispose(); // Close registration window
            }
        });
    }
} 