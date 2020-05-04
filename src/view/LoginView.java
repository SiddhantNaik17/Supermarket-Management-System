package view;

import util.Database;

import javax.swing.*;
import java.sql.SQLException;

public class LoginView {

    private static final int LABEL_WIDTH = 100, LABEL_HEIGHT = 25;
    private static final int FIELD_WIDTH = 100, FIELD_HEIGHT = 25;

    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public boolean loggedIn = false;

    public LoginView() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel usernamelabel = new JLabel("Username:");
        usernamelabel.setBounds(50, 15, LABEL_WIDTH, LABEL_HEIGHT);
        usernameField = new JTextField();
        usernameField.setBounds(130, 20, FIELD_WIDTH, FIELD_HEIGHT);

        JLabel passwordlabel = new JLabel("Password:");
        passwordlabel.setBounds(usernamelabel.getX(),
                usernamelabel.getY() + usernamelabel.getHeight(),
                LABEL_WIDTH, LABEL_HEIGHT);
        passwordField = new JPasswordField();
        passwordField.setBounds(usernameField.getX(),
                usernameField.getY() + usernameField.getHeight(),
                FIELD_WIDTH, FIELD_HEIGHT);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(130,100,100, 40);
        loginButton.addActionListener(actionEvent -> {
            if (login(usernameField.getText(), passwordField.getPassword()))
                new Dashboard();
            else
                JOptionPane.showMessageDialog(null, "Invalid username or password");
        });

        frame.add(usernamelabel);
        frame.add(usernameField);
        frame.add(passwordlabel);
        frame.add(passwordField);
        frame.add(loginButton);

        frame.setSize(400,200);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public boolean login(String username, char[] password) {
        Database db = new Database();
        db.query = "SELECT * FROM `user` WHERE `username` =? AND `password` =?";

        try {
            db.ps = db.getConnection().prepareStatement(db.query);
            db.ps.setString(1, username);
            db.ps.setString(2, String.valueOf(password));
            db.rs = db.ps.executeQuery();

            if(db.rs.next())
            {
                loggedIn = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        db.close();

        return loggedIn;
    }
}
