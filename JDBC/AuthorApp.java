package JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class AuthorApp extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField firstNameField, lastNameField;
    private java.util.List<Author> batchAuthors = new ArrayList<>();

    public AuthorApp() {
        setTitle("Author Manager");
        setSize(700,400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name"}, 0);
        table = new JTable(tableModel);
        loadAuthors();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        
        firstNameField = new JTextField(10);
        lastNameField = new JTextField(10);
        JButton addButton = new JButton("Ekle (Tekli)");
        JButton addBatchButton = new JButton("Batch’e Ekle");
        JButton deleteButton = new JButton("Seçiliyi Sil");

        inputPanel.add(new JLabel("Ad:"));
        inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Soyad:"));
        inputPanel.add(lastNameField);
        inputPanel.add(addButton);
        inputPanel.add(addBatchButton);
        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addAuthor());
        addBatchButton.addActionListener(e -> addToBatch());
        deleteButton.addActionListener(e -> deleteSelectedAuthor());

        // Pencere kapandığında batch işlemi çalıştır
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                executeBatchInsert();
            }
        });

        setVisible(true);
    }

    private void loadAuthors() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Authors")) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("AuthorID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addAuthor() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        String sql = "INSERT INTO Authors (FirstName, LastName) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.executeUpdate();

            tableModel.setRowCount(0); // tabloyu temizle
            loadAuthors(); // tabloyu yeniden yükle

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addToBatch() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        batchAuthors.add(new Author(firstName, lastName));
        JOptionPane.showMessageDialog(this, "Batch’e eklendi.");
    }

    private void executeBatchInsert() {
        String sql = "INSERT INTO Authors (FirstName, LastName) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Author author : batchAuthors) {
                pstmt.setString(1, author.firstName);
                pstmt.setString(2, author.lastName);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("Batch işlemi başarıyla tamamlandı.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedAuthor() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        int authorId = (int) tableModel.getValueAt(selectedRow, 0);
        String sql = "DELETE FROM Authors WHERE AuthorID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, authorId);
            pstmt.executeUpdate();

            tableModel.removeRow(selectedRow);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthorApp());
    }
}
