package oop.assignment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.Scanner;

public class collectPayment extends JFrame {

    public collectPayment(Salesman salesman, List<Booking> bookings, List<Car> cars) {
        setTitle("Collect Payment - " + salesman.getName());
        setSize(900, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"Booking ID", "Customer ID", "Car ID", "Date", "Status", "Amount", "Action"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (column == 6 && comp instanceof JButton) {
                    return comp;
                }
                return super.prepareRenderer(renderer, row, column);
            }
        };

        for (Booking b : bookings) {
            if (!b.getPayment() && b.getStatus().equals("pending") && b.getSalesmanID().equals(salesman.getID())) {
                double amount = 0.0;
                for (Car c : cars) {
                    if (c.getCarID().equals(b.getCarID())) {
                        amount = c.getPrice();
                        break;
                    }
                }
                model.addRow(new Object[]{
                        b.getBookingID(),
                        b.getCustomerID(),
                        b.getCarID(),
                        b.getDate(),
                        b.getStatus(),
                        String.format("RM %.2f", amount),
                        "Collect Payment"
                });
            }
        }

        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), (row) -> {
            String bookingID = model.getValueAt(row, 0).toString();
            Booking selected = null;

            for (Booking b : bookings) {
                if (b.getBookingID().equals(bookingID)) {
                    selected = b;
                    break;
                }
            }

            if (selected == null) return;

            String[] methods = {"Cash", "Credit Card", "Bank Transfer", "E-Wallet"};
            JComboBox<String> methodBox = new JComboBox<>(methods);
            int result = JOptionPane.showConfirmDialog(null, methodBox, "Select Payment Method", JOptionPane.OK_CANCEL_OPTION);

            if (result != JOptionPane.OK_OPTION) return;

            String method = methodBox.getSelectedItem().toString();
            if (method == null || method.trim().isEmpty()) return;

            String amount = "0";
            for (Car c : cars) {
                if (c.getCarID().equals(selected.getCarID())) {
                    amount = String.valueOf(c.getPrice());
                    c.setStatus("paid");
                    break;
                }
            }

            String date = selected.getDate();

            // Generate Payment ID
            String paymentID = "P001";
            try (Scanner scanner = new Scanner(new File("payment.txt"))) {
                int last = 0;
                while (scanner.hasNextLine()) {
                    String[] parts = scanner.nextLine().split(",");
                    if (parts.length > 0 && parts[0].startsWith("P")) {
                        last = Integer.parseInt(parts[0].substring(1));
                    }
                }
                paymentID = String.format("P%03d", last + 1);
            } catch (Exception ex) {
                // Ignore, use default P001
            }

            try (FileWriter fw = new FileWriter("payment.txt", true)) {
                fw.write(paymentID + "," + selected.getBookingID() + "," + amount + "," + method + "," + date + "\n");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save payment.");
                ex.printStackTrace();
            }

            String comment = JOptionPane.showInputDialog(this, "Enter a comment or remark for this booking:");
            if (comment == null || comment.trim().isEmpty()) return;

            String[] ratings = {"1", "2", "3", "4", "5"};
            String rating = (String) JOptionPane.showInputDialog(
                    this,
                    "Rate the customer (1 to 5 stars):",
                    "Rating",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    ratings,
                    "5"
            );
            if (rating == null) return;

            // Generate Feedback ID
            String feedbackID = "F001";
            try (Scanner scanner = new Scanner(new File("feedback.txt"))) {
                int last = 0;
                while (scanner.hasNextLine()) {
                    String[] parts = scanner.nextLine().split(",");
                    if (parts.length > 0 && parts[0].startsWith("F")) {
                        last = Integer.parseInt(parts[0].substring(1));
                    }
                }
                feedbackID = String.format("F%03d", last + 1);
            } catch (Exception ex) {
                // Ignore, use default F001
            }

            try (FileWriter fw = new FileWriter("feedback.txt", true)) {
                fw.write(feedbackID + "," + selected.getBookingID() + "," + date + "," + rating + "," + comment + "\n");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save feedback.");
                ex.printStackTrace();
            }

            selected.setStatus("done");
            selected.setPayment(true);
            Booking.saveBookings("bookings.txt", bookings);
            Car.saveCarsToFile(cars, "cars.txt");

            JOptionPane.showMessageDialog(this, "Payment and feedback saved successfully.");
            dispose();
        }));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        setVisible(true);
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setForeground(Color.WHITE);
            setBackground(new Color(46, 139, 87));
            setFont(new Font("Arial", Font.BOLD, 12));
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Collect Payment" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private int row;

        public ButtonEditor(JCheckBox checkBox, java.util.function.IntConsumer actionCallback) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(60, 179, 113));
            button.setFont(new Font("Arial", Font.BOLD, 12));

            this.button.addActionListener(e -> actionCallback.accept(row));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            label = (value == null) ? "Collect Payment" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
