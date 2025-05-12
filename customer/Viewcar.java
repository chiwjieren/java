package customer;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Viewcar extends JFrame {
    private JTable carTable;
    private DefaultTableModel tableModel;
    private List<Car> carList;
    private JButton viewDetailsBtn, backBtn;
    private JLabel titleLabel;
    
    public Viewcar(String title, List<Car> cars) {
        this.carList = cars;
        
        setTitle("Car Dealership - " + title);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents(title);
        
        setVisible(true);
    }
    
    private void initComponents(String titleText) {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Create table model with columns
        String[] columns = {"ID", "Make", "Model", "Year", "Type", "Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        // Create table
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        carTable.setRowHeight(25);
        carTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        TableColumnModel columnModel = carTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);  // ID
        columnModel.getColumn(1).setPreferredWidth(100); // Make
        columnModel.getColumn(2).setPreferredWidth(100); // Model
        columnModel.getColumn(3).setPreferredWidth(60);  // Year
        columnModel.getColumn(4).setPreferredWidth(100); // Type
        columnModel.getColumn(5).setPreferredWidth(100); // Price
        
        // Format currency for price column
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        columnModel.getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
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
        
        // Add cars to table
        populateTable();
        
        // Put table in scroll pane
        JScrollPane scrollPane = new JScrollPane(carTable);
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
                int selectedRow = carTable.getSelectedRow();
                if (selectedRow != -1) {
                    int carId = (int) tableModel.getValueAt(selectedRow, 0);
                    Car selectedCar = null;
                    
                    // Find the selected car
                    for (Car car : carList) {
                        if (car.getCarId() == carId) {
                            selectedCar = car;
                            break;
                        }
                    }
                    
                    if (selectedCar != null) {
                        new Cardetails(selectedCar);
                    }
                } else {
                    JOptionPane.showMessageDialog(Viewcar.this,
                        "Please select a car to view details",
                        "No Car Selected",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close this window
            }
        });
        
        // Add double-click listener
        carTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = carTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int carId = (int) tableModel.getValueAt(selectedRow, 0);
                        
                        // Find the selected car
                        for (Car car : carList) {
                            if (car.getCarId() == carId) {
                                new Cardetails(car);
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
        
        // Add rows for each car
        for (Car car : carList) {
            Object[] row = {
                car.getCarId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getType(),
                car.getPrice()
            };
            tableModel.addRow(row);
        }
        
        // Show message if no cars available
        if (carList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No cars available in this category.",
                "No Cars Found",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
} 