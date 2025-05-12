package salesman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Profile extends JFrame {
    private Salesman salesman;
    private List<Salesman> salesmenlist;

    private JTextField idField, nameField, emailField, phoneField, passwordField;
    private JButton editButton, saveButton;

    public Profile(Salesman salesman, List<Salesman> salesmenlist) {
        this.salesman = salesman;
        this.salesmenlist = salesmenlist;

        setTitle("My Profile - " + salesman.getName());
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(8, 2, 5, 5));

        // Create fields
        idField = new JTextField(salesman.getID());
        idField.setEditable(false);

        nameField = new JTextField(salesman.getName());
        nameField.setEditable(false);

        emailField = new JTextField(salesman.getEmail());
        emailField.setEditable(false);

        phoneField = new JTextField(salesman.getPhone());
        phoneField.setEditable(false);

        passwordField = new JTextField(salesman.getPassword());
        passwordField.setEditable(false);

        // Create buttons
        editButton = new JButton("Edit");
        saveButton = new JButton("Save");
        saveButton.setEnabled(false); // Save button disabled at first

        // Layout
        add(new JLabel("ID:"));
        add(idField);

        add(new JLabel("Name:"));
        add(nameField);

        add(new JLabel("Email:"));
        add(emailField);

        add(new JLabel("Phone:"));
        add(phoneField);

        add(new JLabel("Password:"));
        add(passwordField);

        add(editButton);
        add(saveButton);

        // Button Actions
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emailField.setEditable(true);
                phoneField.setEditable(true);
                passwordField.setEditable(true);
                saveButton.setEnabled(true);
                editButton.setEnabled(false);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salesman.setEmail(emailField.getText());
                salesman.setPhone(phoneField.getText());
                salesman.setPassword(passwordField.getText());

                Salesman.saveSalesmen("salesmen.txt", salesmenlist);

                JOptionPane.showMessageDialog(Profile.this, "Profile updated successfully.");

                emailField.setEditable(false);
                phoneField.setEditable(false);
                passwordField.setEditable(false);
                saveButton.setEnabled(false);
                editButton.setEnabled(true);
            }
        });

        setVisible(true);
    }
}
