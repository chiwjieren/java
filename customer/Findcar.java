package customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Findcar extends JFrame {
    private JComboBox<String> typeComboBox, makeComboBox;
    private JSlider minPriceSlider, maxPriceSlider;
    private JLabel minPriceLabel, maxPriceLabel;
    private JButton searchButton, backButton;
    
    // Car types and makes
    private final String[] CAR_TYPES = {"Any", "Sedan", "SUV", "Hatchback", "Sports", "Luxury", "Electric", "Supercar"};
    private final String[] CAR_MAKES = {"Any", "BMW", "Ferrari", "Ford", "Honda", "Lamborghini", "Mercedes-Benz", "Perodua", "Proton", "Tesla", "Toyota"};
    
    public Findcar() {
        setTitle("Car Dealership - Find Your Perfect Car");
        setSize(700, 500);
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
        JLabel titleLabel = new JLabel("Find Your Perfect Car");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Center panel for search criteria
        JPanel searchPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Car Type selection
        JPanel typePanel = new JPanel(new BorderLayout(10, 0));
        JLabel typeLabel = new JLabel("Car Type:");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        typeComboBox = new JComboBox<>(CAR_TYPES);
        typeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        typePanel.add(typeLabel, BorderLayout.WEST);
        typePanel.add(typeComboBox, BorderLayout.CENTER);
        
        // Car Make selection
        JPanel makePanel = new JPanel(new BorderLayout(10, 0));
        JLabel makeLabel = new JLabel("Car Make:");
        makeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        makeComboBox = new JComboBox<>(CAR_MAKES);
        makeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        makePanel.add(makeLabel, BorderLayout.WEST);
        makePanel.add(makeComboBox, BorderLayout.CENTER);
        
        // Min Price slider
        JPanel minPricePanel = new JPanel(new BorderLayout(10, 0));
        JLabel minPriceTextLabel = new JLabel("Minimum Price:");
        minPriceTextLabel.setFont(new Font("Arial", Font.BOLD, 14));
        minPriceSlider = new JSlider(JSlider.HORIZONTAL, 0, 500000, 0);
        minPriceSlider.setMajorTickSpacing(100000);
        minPriceSlider.setMinorTickSpacing(25000);
        minPriceSlider.setPaintTicks(true);
        minPriceSlider.setPaintLabels(true);
        minPriceSlider.setFont(new Font("Arial", Font.PLAIN, 12));
        minPriceLabel = new JLabel("$0");
        minPriceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        minPriceLabel.setPreferredSize(new Dimension(100, 20));
        minPricePanel.add(minPriceTextLabel, BorderLayout.WEST);
        minPricePanel.add(minPriceSlider, BorderLayout.CENTER);
        minPricePanel.add(minPriceLabel, BorderLayout.EAST);
        
        // Max Price slider
        JPanel maxPricePanel = new JPanel(new BorderLayout(10, 0));
        JLabel maxPriceTextLabel = new JLabel("Maximum Price:");
        maxPriceTextLabel.setFont(new Font("Arial", Font.BOLD, 14));
        maxPriceSlider = new JSlider(JSlider.HORIZONTAL, 0, 500000, 500000);
        maxPriceSlider.setMajorTickSpacing(100000);
        maxPriceSlider.setMinorTickSpacing(25000);
        maxPriceSlider.setPaintTicks(true);
        maxPriceSlider.setPaintLabels(true);
        maxPriceSlider.setFont(new Font("Arial", Font.PLAIN, 12));
        maxPriceLabel = new JLabel("$500,000");
        maxPriceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        maxPriceLabel.setPreferredSize(new Dimension(100, 20));
        maxPricePanel.add(maxPriceTextLabel, BorderLayout.WEST);
        maxPricePanel.add(maxPriceSlider, BorderLayout.CENTER);
        maxPricePanel.add(maxPriceLabel, BorderLayout.EAST);
        
        // Add all panels to search panel
        searchPanel.add(typePanel);
        searchPanel.add(makePanel);
        searchPanel.add(minPricePanel);
        searchPanel.add(maxPricePanel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        searchButton = new JButton("Search Cars");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton = new JButton("Back");
        buttonPanel.add(searchButton);
        buttonPanel.add(backButton);
        
        // Add elements to main panel
        mainPanel.add(searchPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        minPriceSlider.addChangeListener(e -> {
            int value = minPriceSlider.getValue();
            minPriceLabel.setText("$" + String.format("%,d", value));
            
            // Ensure min doesn't exceed max
            if (value > maxPriceSlider.getValue()) {
                maxPriceSlider.setValue(value);
            }
        });
        
        maxPriceSlider.addChangeListener(e -> {
            int value = maxPriceSlider.getValue();
            maxPriceLabel.setText("$" + String.format("%,d", value));
            
            // Ensure max isn't less than min
            if (value < minPriceSlider.getValue()) {
                minPriceSlider.setValue(value);
            }
        });
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) typeComboBox.getSelectedItem();
                String selectedMake = (String) makeComboBox.getSelectedItem();
                int minPrice = minPriceSlider.getValue();
                int maxPrice = maxPriceSlider.getValue();
                
                // Get all available cars
                List<Car> availableCars = DataManager.getInstance().getAvailableCars();
                List<Car> filteredCars = new ArrayList<>();
                
                // Filter by criteria
                for (Car car : availableCars) {
                    // Check type
                    if (!selectedType.equals("Any") && !car.getType().equals(selectedType)) {
                        continue;
                    }
                    
                    // Check make
                    if (!selectedMake.equals("Any") && !car.getMake().equals(selectedMake)) {
                        continue;
                    }
                    
                    // Check price range
                    if (car.getPrice() < minPrice || car.getPrice() > maxPrice) {
                        continue;
                    }
                    
                    // If we got here, car matches all criteria
                    filteredCars.add(car);
                }
                
                // Show results
                String title = "Search Results";
                if (!selectedType.equals("Any")) title += " - " + selectedType;
                if (!selectedMake.equals("Any")) title += " - " + selectedMake;
                title += " ($" + String.format("%,d", minPrice) + " - $" + String.format("%,d", maxPrice) + ")";
                
                new Viewcar(title, filteredCars);
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
} 