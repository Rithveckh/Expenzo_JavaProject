import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class LoginGUI extends JFrame {
    public LoginGUI() {
        setTitle("Login - Expenzo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 150, 500, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.DARK_GRAY);
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        JLabel logoLabel = new JLabel("Expenzo", JLabel.CENTER);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        logoLabel.setForeground(new Color(255, 255, 255));
        JLabel taglineLabel = new JLabel("Your Personal Finance Companion", JLabel.CENTER);
        taglineLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        taglineLabel.setForeground(new Color(220, 220, 220));
        northPanel.add(logoLabel, BorderLayout.CENTER);
        northPanel.add(taglineLabel, BorderLayout.SOUTH);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JTextField emailField = new JTextField(20);
        emailLabel.setForeground(new Color(255, 255, 255));
        styleInputField(emailField);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JPasswordField passwordField = new JPasswordField(20);
        passwordLabel.setForeground(new Color(255, 255, 255));
        styleInputField(passwordField);
        JButton loginButton = createStyledButton("Login", new Color(34, 139, 34));
        JButton forgotPasswordButton = createStyledButton("Forgot Password?", new Color(70, 130, 180));
        JButton signupButton = createStyledButton("Sign Up", new Color(255, 140, 0));
        centerPanel.add(emailLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(emailField);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(passwordLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(passwordField);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(loginButton);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(forgotPasswordButton);
        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.add(signupButton);
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        setVisible(true);
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (loginUser(email, password)) {
//                JOptionPane.showMessageDialog(this, "Login Successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.");
            }
        });
        forgotPasswordButton.addActionListener(e -> {
            dispose();
            new ForgotPasswordGUI();
        });
        signupButton.addActionListener(e -> {
            dispose();
            new SignupGUI();
        });
    }
    private boolean loginUser(String email, String password) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?")) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boolean isProfileComplete = resultSet.getBoolean("is_profile_complete");
                if (!isProfileComplete) {
                    dispose();
                    new ProfileGUI(email);
                } else {
                    dispose();
                    new MainGUI(email);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private void styleInputField(JComponent field) {
        field.setPreferredSize(new Dimension(300, 30));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(112, 128, 144), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
    public static void main(String[] args) {
        new LoginGUI();
    }
}
