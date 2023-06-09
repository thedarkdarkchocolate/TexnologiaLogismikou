package CONNECTION.CLIENT.CLIENTGUI;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import CONNECTION.CLIENT.App;

public class logInGui extends JFrame {

    private App app;
    private JPasswordField password;
    private JTextField studentID;
    private JLabel label_password, label_username, message, label_image;
    private JButton signIn, signUp;

    ImageIcon icon = new ImageIcon("src/ASSETS/logo-new.png");
    Image image = icon.getImage();

    public logInGui(App app) {

        this.app = app;
        startGui();
    }

    private void startGui() {

        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        BtnListener btnL = new BtnListener();

        int labelWidth = 200;
        int labelHeight = 100;
        Image resizedImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);

        // Create label to display image
        label_image = new JLabel(new ImageIcon(resizedImage));
        label_image.setBounds(200, 10, labelWidth, labelHeight);

        label_username = new JLabel("Student ID");
        label_username.setBounds(200, 150, 100, 40);

        label_password = new JLabel("Password");
        label_password.setBounds(200, 200, 150, 40);

        message = new JLabel("Don't have an account:");
        message.setBounds(200, 300, 250, 40);

        studentID = new JTextField();
        // studentID.setText("dai19159");
        studentID.setBounds(350, 150, 200, 40);

        password = new JPasswordField();
        password.setBounds(350, 200, 200, 40);
        // password.setText("19159");
        signIn = new JButton("Sign In");
        signIn.setBounds(400, 250, 100, 40);
        signIn.addActionListener(btnL);

        signUp = new JButton("Sign Up");
        signUp.setBounds(400, 300, 100, 40);
        signUp.addActionListener(btnL);

        this.add(label_username);
        this.add(studentID);

        this.add(label_password);
        this.add(password);

        this.add(signIn);

        this.add(message);
        this.add(signUp);

        this.add(label_image);

        setVisible(true);
    }

    public String[] getCredentials() {
        return new String[] { studentID.getText(), new String(password.getPassword()) };
    }

    public void clearInputFields() {
        this.studentID.setText("");
        this.password.setText("");
    }

    public void close() {
        this.dispose();
    }

    private class BtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource().equals(signIn)) {
                String usrnm = studentID.getText();
                String pass = new String(password.getPassword());
                if (!usrnm.isEmpty() && !pass.isEmpty())
                    app.sendSignInInfo(usrnm, pass);
                else {
                    clearInputFields();
                    // Display Error For Empty Username or Password
                }
            } else if (e.getSource().equals(signUp)) {
                // Close loginWindow
                close();
                // Start signUp window
                app.startSignUpGUI();
            }

        }

    }

}
