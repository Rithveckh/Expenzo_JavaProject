import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
class VerifyAnswerGUI extends JFrame {
    public VerifyAnswerGUI(String email, String question) {
        setTitle("Verify Answer - Expenzo");
        setSize(400, 400);
        setBounds(500, 150, 500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(Color.DARK_GRAY);
        JLabel titleLabel = new JLabel("Verify Your Answer", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(255,255,0));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        JLabel questionLabel = new JLabel("Security Question:");
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel displayQuestionLabel = new JLabel("<html><center>" + question + "</center></html>", JLabel.CENTER);
        displayQuestionLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        displayQuestionLabel.setForeground(Color.LIGHT_GRAY);
        displayQuestionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel answerLabel = new JLabel("Your Answer:");
        answerLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        answerLabel.setForeground(Color.WHITE);
        answerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField answerField = new JTextField(20);
        answerField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        answerField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        answerField.setForeground(Color.BLACK);
        answerField.setBackground(Color.WHITE);
        answerField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(70, 130, 180));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {
            String answer = answerField.getText().trim();
            String password = getPassword(email, answer);
            if (password != null) {
                JOptionPane.showMessageDialog(this, "Your password is: " + password);
                dispose();
                new LoginGUI();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect answer!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(questionLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(displayQuestionLabel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(answerLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(answerField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(submitButton);
        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    private String getPassword(String email, String answer) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT password FROM users WHERE email = ? AND security_answer = ?")) {
            statement.setString(1, email);
            statement.setString(2, answer);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
