import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
public class MainGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String userEmail;
    public MainGUI(String userEmail) {
        this.userEmail = userEmail;
        setTitle("Main Dashboard - Expenzo");
        setSize(1600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel sidebarPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        sidebarPanel.setBackground(new Color(24, 24, 36));
        sidebarPanel.setPreferredSize(new Dimension(250, 800));
        JLabel titleLabel = new JLabel("EXPENZO", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(245, 245, 245));
        sidebarPanel.add(titleLabel);
        JButton dashboardButton = createSidebarButton("Dashboard");
        JButton profileButton = createSidebarButton("Profile");
        JButton addIncomeButton = createSidebarButton("Add Income");
        JButton addExpenseButton = createSidebarButton("Add Expense");
        JButton expenseReportButton = createSidebarButton("Expense Report");
        JButton logoutButton = createSidebarButton("Logout");
        sidebarPanel.add(dashboardButton);
        sidebarPanel.add(profileButton);
        sidebarPanel.add(addIncomeButton);
        sidebarPanel.add(addExpenseButton);
        sidebarPanel.add(expenseReportButton);
        sidebarPanel.add(logoutButton);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        JPanel dashboardPanel = createDashboardPanel();
        JPanel profilePanel = createProfilePanel();
        JPanel addIncomePanel = createAddIncomePanel();
        JPanel addExpensePanel = createAddExpensePanel();
        JPanel expenseReportPanel = createExpenseReportPanel();
        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(profilePanel, "Profile");
        mainPanel.add(addIncomePanel, "Add Income");
        mainPanel.add(addExpensePanel, "Add Expense");
        mainPanel.add(expenseReportPanel, "Expense Report");
        dashboardButton.addActionListener(e -> cardLayout.show(mainPanel, "Dashboard"));
        profileButton.addActionListener(e -> cardLayout.show(mainPanel, "Profile"));
        addIncomeButton.addActionListener(e -> cardLayout.show(mainPanel, "Add Income"));
        addExpenseButton.addActionListener(e -> cardLayout.show(mainPanel, "Add Expense"));
        expenseReportButton.addActionListener(e -> cardLayout.show(mainPanel, "Expense Report"));
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });
        add(sidebarPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(34, 34, 58));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFont(new Font("Arial", Font.BOLD, 18));
        return button;
    }
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        double totalIncome = getTotalIncome();
        double totalExpense = getTotalExpense();
        double remainingAmount = totalIncome - totalExpense;
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Total Expense", totalExpense);
        dataset.setValue("Remaining Amount", remainingAmount);
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Income vs Expense", dataset, true, true, false);
        pieChart.setBackgroundPaint(Color.DARK_GRAY);
        pieChart.getTitle().setPaint(Color.WHITE);
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(450, 350));
        chartPanel.setBackground(Color.WHITE);
        panel.add(chartPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.DARK_GRAY);
        JLabel incomeLabel = new JLabel("Total Income: " + String.format("%.2f", totalIncome));
        JLabel expenseLabel = new JLabel("Total Expense: " + String.format("%.2f", totalExpense));
        JLabel remainingLabel = new JLabel("Remaining Amount: " + String.format("%.2f", remainingAmount));
        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        incomeLabel.setFont(labelFont);
        expenseLabel.setFont(labelFont);
        remainingLabel.setFont(labelFont);
        incomeLabel.setForeground(Color.YELLOW);
        expenseLabel.setForeground(Color.RED);
        remainingLabel.setForeground(new Color(58, 134, 255));
        incomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        expenseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        remainingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(incomeLabel);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        bottomPanel.add(expenseLabel);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        bottomPanel.add(remainingLabel);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 16));
        refreshButton.setBackground(new Color(0, 0, 255));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> {
            double newTotalIncome = getTotalIncome();
            double newTotalExpense = getTotalExpense();
            double newRemainingAmount = newTotalIncome - newTotalExpense;
            incomeLabel.setText("Total Income: " + String.format("%.2f", newTotalIncome));
            expenseLabel.setText("Total Expense: " + String.format("%.2f", newTotalExpense));
            remainingLabel.setText("Remaining Amount: " + String.format("%.2f", newRemainingAmount));
            dataset.setValue("Total Expense", newTotalExpense);
            dataset.setValue("Remaining Amount", newRemainingAmount);
        });
        bottomPanel.add(refreshButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }
    private double getTotalIncome() {
        double totalIncome = 0;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT SUM(amount) FROM income WHERE user_email = ?")) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalIncome = resultSet.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching total income.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return totalIncome;
    }
    private double getTotalExpense() {
        double totalExpense = 0;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT SUM(amount) FROM expenses WHERE user_email = ?")) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalExpense = resultSet.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching total expense.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return totalExpense;
    }
    private JLabel nameLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JLabel addressLabel;
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("Profile Information");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel nameTitle = createStyledLabel("Name:");
        nameTitle.setForeground(Color.WHITE);
        nameLabel = createValueLabel();
        JLabel emailTitle = createStyledLabel("Email:");
        emailTitle.setForeground(Color.WHITE);
        emailLabel = createValueLabel();
        JLabel phoneTitle = createStyledLabel("Phone:");
        phoneTitle.setForeground(Color.WHITE);
        phoneLabel = createValueLabel();
        gbc.gridx = 0; gbc.gridy = 0;
        detailsPanel.add(nameTitle, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        detailsPanel.add(nameLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        detailsPanel.add(emailTitle, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        detailsPanel.add(emailLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        detailsPanel.add(phoneTitle, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        detailsPanel.add(phoneLabel, gbc);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT name, email, phone FROM users WHERE email = ?")) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                nameLabel.setText(resultSet.getString("name"));
                emailLabel.setText(resultSet.getString("email"));
                phoneLabel.setText(resultSet.getString("phone"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching profile data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(detailsPanel);
        return panel;
    }
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.BOLD, 16));
        label.setForeground(new Color(58, 58, 68));
        return label;
    }
    private JLabel createValueLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Roboto", Font.PLAIN, 16));
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        return label;
    }
    private JPanel createAddIncomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        JLabel titleLabel = new JLabel("Add Income");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 25));
        formPanel.setOpaque(false);
        JLabel dateLabel = createStyledLabel("Date:");
        dateLabel.setForeground(Color.WHITE);
        JTextField dateField = createInputField();
        JLabel amountLabel = createStyledLabel("Amount:");
        amountLabel.setForeground(Color.WHITE);
        JTextField amountField = createInputField();
        JLabel descriptionLabel = createStyledLabel("Description:");
        descriptionLabel.setForeground(Color.WHITE);
        JTextField descriptionField = createInputField();
        formPanel.add(dateLabel);
        formPanel.add(dateField);
        formPanel.add(amountLabel);
        formPanel.add(amountField);
        formPanel.add(descriptionLabel);
        formPanel.add(descriptionField);
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Roboto", Font.BOLD, 16));
        saveButton.setBackground(new Color(34, 139, 34));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.addActionListener(e -> {
            String date = dateField.getText().trim();
            String amount = amountField.getText().trim();
            String description = descriptionField.getText().trim();
            if (date.isEmpty() || amount.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "INSERT INTO income (user_email, date, amount, description) VALUES (?, ?, ?, ?)")) {
                statement.setString(1, userEmail);
                statement.setString(2, date);
                statement.setString(3, amount);
                statement.setString(4, description);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(panel, "Income saved successfully!");
                dateField.setText("");
                amountField.setText("");
                descriptionField.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Failed to save income. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(formPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(saveButton);
        return panel;
    }
    private JTextField createInputField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Roboto", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(100, 30));
        field.setCaretColor(new Color(32, 136, 203));
        return field;
    }
    private JPanel createAddExpensePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        JLabel titleLabel = new JLabel("Add Expense");
        titleLabel.setBackground(Color.DARK_GRAY);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 25));
        formPanel.setOpaque(false);
        JLabel dateLabel = createStyledLabel("Date:");
        dateLabel.setForeground(Color.WHITE);
        JTextField dateField = createInputField();
        JLabel amountLabel = createStyledLabel("Amount:");
        amountLabel.setForeground(Color.WHITE);
        JTextField amountField = createInputField();
        JLabel categoryLabel = createStyledLabel("Category:");
        categoryLabel.setForeground(Color.WHITE);
        JTextField categoryField = createInputField();
        formPanel.add(dateLabel);
        formPanel.add(dateField);
        formPanel.add(amountLabel);
        formPanel.add(amountField);
        formPanel.add(categoryLabel);
        formPanel.add(categoryField);
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Roboto", Font.BOLD, 16));
        saveButton.setBackground(new Color(204, 51, 0));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setPreferredSize(new Dimension(150, 40));
        saveButton.addActionListener(e -> {
            String date = dateField.getText().trim();
            String amount = amountField.getText().trim();
            String category = categoryField.getText().trim();
            if (date.isEmpty() || amount.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "INSERT INTO expenses (user_email, date, amount, category) VALUES (?, ?, ?, ?)")) {
                statement.setString(1, userEmail);
                statement.setString(2, date);
                statement.setString(3, amount);
                statement.setString(4, category);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(panel, "Expense saved successfully!");
                dateField.setText("");
                amountField.setText("");
                categoryField.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Failed to save expense. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(formPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(saveButton);
        return panel;
    }
    private JTextArea reportArea;
    private JPanel createExpenseReportPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("Expense Report", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(220, 220, 220));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setForeground(Color.WHITE);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setBackground(new Color(89, 89, 89));
        reportArea.setBorder(BorderFactory.createLineBorder(new Color(89, 89, 89), 1));
        reportArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        panel.add(scrollPane, BorderLayout.CENTER);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 16));
        refreshButton.setBackground(new Color(0, 0, 255));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        refreshButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshExpenseReport());
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.add(refreshButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        refreshExpenseReport();
        return panel;
    }
    private void refreshExpenseReport() {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Date\t\tAmount\t\tCategory\n");
        reportBuilder.append("------------------------------------------------------\n");
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT date, amount, category FROM expenses WHERE user_email = ?")) {
            statement.setString(1, userEmail);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String date = resultSet.getString("date");
                    String amount = resultSet.getString("amount");
                    String category = resultSet.getString("category");
                    reportBuilder.append(String.format("%s\t\t%s\t\t%s\n", date, amount, category));
                }
            }
            reportArea.setText(reportBuilder.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load expense report. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}