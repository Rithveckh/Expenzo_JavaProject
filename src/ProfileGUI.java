import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
public class ProfileGUI extends JFrame {
    private final String userEmail;
    private JTextField dobField, phoneField, recoveryEmailField, incomeTargetField, expenditureField;
    public ProfileGUI(String email) {
        this.userEmail = email;
        setTitle("Set Up Profile - Expenzo");
        setSize(600, 500);
        setBounds(500, 150, 500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.DARK_GRAY);
        JPanel northPanel = createNorthPanel();
        add(northPanel, BorderLayout.NORTH);
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        JPanel southPanel = createSouthPanel();
        add(southPanel, BorderLayout.SOUTH);
        JButton skipButton = (JButton) centerPanel.getComponent(11);
        skipButton.setEnabled(false);
        configureSaveButton(skipButton);
        configureSkipButton(skipButton);
        configureCancelButton();
        setVisible(true);
    }
    private JPanel createNorthPanel() {
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Set Up Your Profile", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 255, 255));
        northPanel.add(titleLabel, BorderLayout.CENTER);
        return northPanel;
    }
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(6, 2, 20, 20));
        centerPanel.setOpaque(false);
        JLabel dobLabel = new JLabel("Date of Birth:");
        dobField = new JTextField();
        dobLabel.setForeground(new Color(255,255,255));
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setForeground(new Color(255,255,255));
        phoneField = new JTextField();
        JLabel recoveryEmailLabel = new JLabel("Recovery Email:");
        recoveryEmailLabel.setForeground(new Color(255,255,255));
        recoveryEmailField = new JTextField();
        JLabel incomeTargetLabel = new JLabel("Monthly Income Target:");
        incomeTargetLabel.setForeground(new Color(255,255,255));
        incomeTargetField = new JTextField();
        JLabel expenditureLabel = new JLabel("Monthly Expected Expenditure:");
        expenditureLabel.setForeground(new Color(255,255,255));
        expenditureField = new JTextField();
        JButton saveButton = new JButton("Save Profile");
        JButton skipButton = new JButton("Skip");
        dobLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        recoveryEmailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        incomeTargetLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        expenditureLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dobField.setBackground(Color.WHITE);
        phoneField.setBackground(Color.WHITE);
        recoveryEmailField.setBackground(Color.WHITE);
        incomeTargetField.setBackground(Color.WHITE);
        expenditureField.setBackground(Color.WHITE);
        saveButton.setBackground(new Color(32, 136, 203));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setPreferredSize(new Dimension(150, 40));
        skipButton.setBackground(new Color(192, 192, 192));
        skipButton.setForeground(Color.BLACK);
        skipButton.setFocusPainted(false);
        skipButton.setBorderPainted(false);
        skipButton.setPreferredSize(new Dimension(150, 40));
        centerPanel.add(dobLabel);
        centerPanel.add(dobField);
        centerPanel.add(phoneLabel);
        centerPanel.add(phoneField);
        centerPanel.add(recoveryEmailLabel);
        centerPanel.add(recoveryEmailField);
        centerPanel.add(incomeTargetLabel);
        centerPanel.add(incomeTargetField);
        centerPanel.add(expenditureLabel);
        centerPanel.add(expenditureField);
        centerPanel.add(saveButton);
        centerPanel.add(skipButton);
        return centerPanel;
    }
    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.DARK_GRAY);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(240, 50, 50));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(150, 40));
        southPanel.add(cancelButton);
        return southPanel;
    }
    private void configureSaveButton(JButton skipButton) {
        JButton saveButton = (JButton) ((JPanel) getContentPane().getComponent(1)).getComponent(10);
        saveButton.addActionListener(e -> {
            String dob = dobField.getText();
            String phone = phoneField.getText();
            String recoveryEmail = recoveryEmailField.getText();
            String incomeTarget = incomeTargetField.getText();
            String expenditure = expenditureField.getText();
            if (dob.isEmpty() || phone.isEmpty() || recoveryEmail.isEmpty() || incomeTarget.isEmpty() || expenditure.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all the fields before saving the profile.");
            } else {
                if (saveUserProfile(dob, phone, recoveryEmail, incomeTarget, expenditure)) {
                    JOptionPane.showMessageDialog(this, "Profile saved successfully!");
                    skipButton.setEnabled(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error saving profile. Please try again.");
                }
            }
        });
    }
    private void configureSkipButton(JButton skipButton) {
        skipButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "You must complete your profile setup before proceeding to the main screen.");
        });
    }
    private void configureCancelButton() {
        JButton cancelButton = (JButton) ((JPanel) getContentPane().getComponent(2)).getComponent(0);  // Cancel button in the south panel
        cancelButton.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });
    }
    private boolean saveUserProfile(String dob, String phone, String recoveryEmail, String incomeTarget, String expenditure) {
        try (Connection connection = DBConnection.getConnection()) {
            String updateQuery = "UPDATE users SET dob = ?, phone = ?, recovery_email = ?, income_target = ?, expenditure = ?, is_profile_complete = ? WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1, dob);
            statement.setString(2, phone);
            statement.setString(3, recoveryEmail);
            statement.setString(4, incomeTarget);
            statement.setString(5, expenditure);
            statement.setBoolean(6, true);
            statement.setString(7, userEmail);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                dispose();
                new MainGUI(userEmail);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
