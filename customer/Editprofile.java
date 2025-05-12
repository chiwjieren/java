package customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Editprofile extends JFrame {
    private Customer customer;
    private JTextField firstNameField, lastNameField, emailField, phoneField;
    private JPasswordField currentPasswordField, newPasswordField, confirmPasswordField;
    private JTextArea addressArea;
    private JButton updateProfileBtn, updatePasswordBtn, backBtn;
    private JLabel statusLabel;
    
    public Editprofile() {
        customer = DataManager.getInstance().getCurrentCustomer();
        
        if (customer == null) {
            JOptionPane.showMessageDialog(this, "No user logged in. Please login first.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        setTitle("Car Dealership - Edit Profile");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        
        setVisible(true);
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Edit Your Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Tabbed pane for profile and password
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Profile panel
        JPanel profilePanel = new JPanel(new BorderLayout(10, 10));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        // First Name
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(20);
        firstNameField.setText(customer.getFirstName());
        
        // Last Name
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField(20);
        lastNameField.setText(customer.getLastName());
        
        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        emailField.setText(customer.getEmail());
        
        // Phone
        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField(20);
        phoneField.setText(customer.getPhoneNumber());
        
        // Address (Multi-line)
        JLabel addressLabel = new JLabel("Address:");
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setText(customer.getAddress());
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
        formPanel.add(addressLabel);
        formPanel.add(addressScrollPane);
        
        // Button panel for profile
        JPanel profileButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        updateProfileBtn = new JButton("Update Profile");
        profileButtonPanel.add(updateProfileBtn);
        
        profilePanel.add(formPanel, BorderLayout.CENTER);
        profilePanel.add(profileButtonPanel, BorderLayout.SOUTH);
        
        // Password change panel
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 10));
        passwordPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create password form panel
        JPanel passwordFormPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        // Current Password
        JLabel currentPasswordLabel = new JLabel("Current Password:");
        currentPasswordField = new JPasswordField(20);
        
        // New Password
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordField = new JPasswordField(20);
        
        // Confirm New Password
        JLabel confirmPasswordLabel = new JLabel("Confirm New Password:");
        confirmPasswordField = new JPasswordField(20);
        
        // Add all password form components
        passwordFormPanel.add(currentPasswordLabel);
        passwordFormPanel.add(currentPasswordField);
        passwordFormPanel.add(newPasswordLabel);
        passwordFormPanel.add(newPasswordField);
        passwordFormPanel.add(confirmPasswordLabel);
        passwordFormPanel.add(confirmPasswordField);
        
        // Button panel for password
        JPanel passwordButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        updatePasswordBtn = new JButton("Update Password");
        passwordButtonPanel.add(updatePasswordBtn);
        
        passwordPanel.add(passwordFormPanel, BorderLayout.CENTER);
        passwordPanel.add(passwordButtonPanel, BorderLayout.SOUTH);
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Profile Information", profilePanel);
        tabbedPane.addTab("Change Password", passwordPanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Bottom panel with back button and status
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        
        // Back button
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backBtn = new JButton("Back");
        backButtonPanel.add(backBtn);
        
        // Status label
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        
        bottomPanel.add(backButtonPanel, BorderLayout.NORTH);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        updateProfileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfile();
            }
        });
        
        updatePasswordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePassword();
            }
        });
        
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void updateProfile() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();
        
        // Validate input
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || 
            phone.isEmpty() || address.isEmpty()) {
            statusLabel.setText("Please fill out all fields");
            return;
        }
        
        // Check if email is already used by another customer
        Customer existingCustomer = DataManager.getInstance().getCustomerByEmail(email);
        if (existingCustomer != null && existingCustomer.getCustomerId() != customer.getCustomerId()) {
            statusLabel.setText("Email already registered by another user");
            return;
        }
        
        // Update customer information
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhoneNumber(phone);
        customer.setAddress(address);
        
        // Save changes
        DataManager.getInstance().updateCustomer(customer);
        
        JOptionPane.showMessageDialog(this,
            "Your profile has been updated successfully!",
            "Profile Updated",
            JOptionPane.INFORMATION_MESSAGE);
        
        statusLabel.setText("");
    }
    
    private void updatePassword() {
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validate input
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill out all password fields");
            return;
        }
        
        // Check current password
        if (!customer.getPassword().equals(currentPassword)) {
            statusLabel.setText("Current password is incorrect");
            return;
        }
        
        // Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            statusLabel.setText("New passwords do not match");
            return;
        }
        
        // Check if new password is same as old password
        if (newPassword.equals(currentPassword)) {
            statusLabel.setText("New password must be different from current password");
            return;
        }
        
        // Update password
        customer.setPassword(newPassword);
        
        // Save changes
        DataManager.getInstance().updateCustomer(customer);
        
        JOptionPane.showMessageDialog(this,
            "Your password has been updated successfully!",
            "Password Updated",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Clear password fields
        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
        statusLabel.setText("");
    }
} 