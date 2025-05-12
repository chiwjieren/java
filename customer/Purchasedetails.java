package customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Purchasedetails extends JFrame {
    private Purchase purchase;
    private JTextArea feedbackArea;
    private JButton submitButton;
    private JButton backButton;
    private JLabel[] starLabels;
    private int currentRating;
    
    public Purchasedetails(Purchase purchase) {
        this.purchase = purchase;
        this.currentRating = purchase.getRating();
        
        setTitle("Purchase Details");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Purchase Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Main Content Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Purchase Information Panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Purchase Information"));
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        
        addLabelPair(infoPanel, "Purchase ID:", String.valueOf(purchase.getId()));
        addLabelPair(infoPanel, "Car:", purchase.getCar().getMake() + " " + purchase.getCar().getModel());
        addLabelPair(infoPanel, "Purchase Date:", dateFormat.format(purchase.getPurchaseDate()));
        addLabelPair(infoPanel, "Amount:", currencyFormat.format(purchase.getAmount()));
        
        mainPanel.add(infoPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Rating Panel
        JPanel ratingPanel = new JPanel();
        ratingPanel.setBorder(BorderFactory.createTitledBorder("Rate Your Purchase"));
        JLabel ratingLabel = new JLabel("Your Rating: ");
        ratingPanel.add(ratingLabel);
        
        // Create star labels
        starLabels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            starLabels[i] = new JLabel("☆");
            starLabels[i].setFont(new Font("Arial", Font.BOLD, 24));
            starLabels[i].setForeground(Color.ORANGE);
            starLabels[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            final int rating = i + 1;
            
            starLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    currentRating = rating;
                    updateStars();
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {
                    for (int j = 0; j < 5; j++) {
                        starLabels[j].setText(j < rating ? "★" : "☆");
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    updateStars();
                }
            });
            
            ratingPanel.add(starLabels[i]);
        }
        
        mainPanel.add(ratingPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Feedback Panel
        JPanel feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.setBorder(BorderFactory.createTitledBorder("Your Feedback"));
        
        feedbackArea = new JTextArea(5, 30);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setText(purchase.getFeedback());
        
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        feedbackPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(feedbackPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        submitButton = new JButton("Submit Feedback");
        backButton = new JButton("Back");
        
        submitButton.addActionListener(e -> {
            try {
                purchase.setRating(currentRating);
                purchase.setFeedback(feedbackArea.getText());
                DataManager.getInstance().updatePurchase(purchase);
                JOptionPane.showMessageDialog(this, "Feedback submitted successfully!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error submitting feedback: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        backButton.addActionListener(e -> dispose());
        
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);
        
        mainPanel.add(buttonPanel);
        
        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);
        
        // Initialize stars based on current rating
        updateStars();
    }
    
    private void addLabelPair(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(labelComponent);
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(valueComponent);
    }
    
    private void updateStars() {
        for (int i = 0; i < 5; i++) {
            starLabels[i].setText(i < currentRating ? "★" : "☆");
        }
    }
} 