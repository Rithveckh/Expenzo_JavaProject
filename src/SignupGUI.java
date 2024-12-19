import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
public class SignupGUI extends JFrame {
    public SignupGUI() {
        setTitle("Signup - Expenzo");
        setSize(500, 500);
        setBounds(500, 150, 500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.DARK_GRAY);
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        JLabel logoPlaceholder = new JLabel("Expenzo", JLabel.CENTER);
        logoPlaceholder.setFont(new Font("SansSerif", Font.BOLD, 28));
        logoPlaceholder.setForeground(new Color(255, 255, 255));
        JLabel tagline = new JLabel("Your Personalized Expense Tracker", JLabel.CENTER);
        tagline.setFont(new Font("SansSerif", Font.ITALIC, 14));
        tagline.setForeground(new Color(255, 255, 255));
        northPanel.add(logoPlaceholder, BorderLayout.CENTER);
        northPanel.add(tagline, BorderLayout.SOUTH);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        centerPanel.setOpaque(false);
        JLabel nameLabel = createLabel("Name:");
        nameLabel.setForeground(new Color(255, 255, 255));
        JTextField nameField = createTextField();
        JLabel emailLabel = createLabel("Email:");
        emailLabel.setForeground(new Color(255, 255, 255));
        JTextField emailField = createTextField();
        JLabel passwordLabel = createLabel("Password:");
        passwordLabel.setForeground(new Color(255, 255, 255));
        JPasswordField passwordField = createPasswordField();
        JLabel questionLabel = createLabel("Security Question:");
        questionLabel.setForeground(new Color(255, 255, 255));
        JTextField questionField = createTextField();
        JLabel answerLabel = createLabel("Answer:");
        answerLabel.setForeground(new Color(255, 255, 255));
        JTextField answerField = createTextField();
        addField(centerPanel, nameLabel, nameField);
        addField(centerPanel, emailLabel, emailField);
        addField(centerPanel, passwordLabel, passwordField);
        addField(centerPanel, questionLabel, questionField);
        addField(centerPanel, answerLabel, answerField);
        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        JButton submitButton = createButton("Submit", new Color(34, 139, 34));
        JButton backButton = createButton("Back to Login", new Color(70, 130, 180));
        southPanel.add(submitButton);
        southPanel.add(backButton);
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String question = questionField.getText();
            String answer = answerField.getText();
            if (registerUser(name, email, password, question, answer)) {
                JOptionPane.showMessageDialog(this, "Signup Successful! Please Login.");
                dispose();
                new LoginGUI();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Email already exists.");
            }
        });
        backButton.addActionListener(e -> {
            dispose();
            new LoginGUI();
        });
        setVisible(true);
    }
    private boolean registerUser(String name, String email, String password, String question, String answer) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO users (name, email, password, security_question, security_answer) VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, question);
            statement.setString(5, answer);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    private JTextField createTextField() {
        JTextField textField = new JTextField(20);
        textField.setPreferredSize(new Dimension(300, 30));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return textField;
    }
    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(300, 30));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return passwordField;
    }
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }
    private void addField(JPanel panel, JLabel label, JTextField textField) {
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(textField);
        panel.add(Box.createVerticalStrut(15));
    }
    public static void main(String[] args) {
        new SignupGUI();
    }
}
