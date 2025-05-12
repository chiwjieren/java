package customer;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class History extends JFrame {
    private JTable purchaseTable;
    private DefaultTableModel tableModel;
    private List<Purchase> purchaseList;
    private JButton viewDetailsBtn, backBtn;
    
    public History() {
        Customer currentCustomer = DataManager.getInstance().getCurrentCustomer();
        if (currentCustomer == null) {
            JOptionPane.showMessageDialog(this, "No user logged in. Please login first.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        this.purchaseList = DataManager.getInstance().getPurchasesByCustomer(currentCustomer);
        
        setTitle("Car Dealership - Purchase History");
        setSize(800, 500);
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
        JLabel titleLabel = new JLabel("Your Purchase History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Create table model with columns
        String[] columns = {"ID", "Car", "Purchase Date", "Amount", "Rating"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        // Create table
        purchaseTable = new JTable(tableModel);
        purchaseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        purchaseTable.setRowHeight(25);
        purchaseTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        TableColumnModel columnModel = purchaseTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);  // ID
        columnModel.getColumn(1).setPreferredWidth(200); // Car
        columnModel.getColumn(2).setPreferredWidth(100); // Purchase Date
        columnModel.getColumn(3).setPreferredWidth(100); // Amount
        columnModel.getColumn(4).setPreferredWidth(60);  // Rating
        
        // Format currency for amount column
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        columnModel.getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(JLabel.RIGHT);
            }
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Double) {
                    value = currencyFormat.format(value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        
        // Format ratings with stars
        columnModel.getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                int rating = value instanceof Integer ? (Integer) value : 0;
                String stars = "";
                for (int i = 0; i < rating; i++) {
                    stars += "★";
                }
                for (int i = rating; i < 5; i++) {
                    stars += "☆";
                }
                
                if (rating == 0) {
                    return super.getTableCellRendererComponent(table, "Not Rated", isSelected, hasFocus, row, column);
                } else {
                    return super.getTableCellRendererComponent(table, stars, isSelected, hasFocus, row, column);
                }
            }
        });
        
        // Add purchases to table
        populateTable();
        
        // Put table in scroll pane
        JScrollPane scrollPane = new JScrollPane(purchaseTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        viewDetailsBtn = new JButton("View Details");
        backBtn = new JButton("Back");
        
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(backBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listeners
        viewDetailsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = purchaseTable.getSelectedRow();
                if (selectedRow != -1) {
                    int purchaseId = (int) tableModel.getValueAt(selectedRow, 0);
                    
                    // Find the selected purchase
                    for (Purchase purchase : purchaseList) {
                        if (purchase.getPurchaseId() == purchaseId) {
                            new Purchasedetails(purchase);
                            break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(History.this,
                        "Please select a purchase to view details",
                        "No Purchase Selected",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        // Double-click listener
        purchaseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = purchaseTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int purchaseId = (int) tableModel.getValueAt(selectedRow, 0);
                        
                        // Find the selected purchase
                        for (Purchase purchase : purchaseList) {
                            if (purchase.getPurchaseId() == purchaseId) {
                                new Purchasedetails(purchase);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }
    
    private void populateTable() {
        // Clear existing rows
        tableModel.setRowCount(0);
        
        // Date formatter
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        // Add rows for each purchase
        for (Purchase purchase : purchaseList) {
            Object[] row = {
                purchase.getPurchaseId(),
                purchase.getCar().getYear() + " " + purchase.getCar().getMake() + " " + purchase.getCar().getModel(),
                dateFormat.format(purchase.getPurchaseDate()),
                purchase.getPurchaseAmount(),
                purchase.getRating()
            };
            tableModel.addRow(row);
        }
        
        // Show message if no purchases
        if (purchaseList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "You haven't made any purchases yet.",
                "No Purchase History",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
} 