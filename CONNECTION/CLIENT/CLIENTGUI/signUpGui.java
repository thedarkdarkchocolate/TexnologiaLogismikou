package CONNECTION.CLIENT.CLIENTGUI;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

import CONNECTION.CLIENT.App;

public class signUpGui extends JFrame{
	
   private App app;
	private JPasswordField password;
	private JTextField firstName ,lastName, studentId, email;
	private JLabel label_password,label_name,label_lastName,label_studentId, label_email;
	private JButton signUp, signIn;
	private ImageIcon icon = new ImageIcon("src/ASSETS/logo-new.png");
	private Image image = icon.getImage();
	
	public signUpGui(App app){
		
		this.app = app;
      this.startGui();
   
   }  
   
   private void startGui(){

      setSize(800, 600);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLayout(null);

      BtnListener btnListener = new BtnListener();
      
      int labelWidth = 200;
      int labelHeight = 100;
      Image resizedImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
      
      // Create label to display image
      JLabel label_image = new JLabel(new ImageIcon(resizedImage));
      label_image.setBounds(200, 10, labelWidth, labelHeight);
      
      
      
      label_name= new JLabel("Firstname");
      label_name.setBounds(200,150,100,40);
      
      label_lastName= new JLabel("Lastname");
      label_lastName.setBounds(200,200,100,40);
      
      label_studentId= new JLabel("Student Number");
      label_studentId.setBounds(200,250,150,40);
      
      
      label_email= new JLabel("E-mail");
      label_email.setBounds(200,300,150,40);
      
      label_password= new JLabel("Password");
      label_password.setBounds(200,350,150,40);
      
      signUp = new JButton("Sign Up");// toggle button;
      signUp.setBounds(400,450,100,40);
      signUp.addActionListener(btnListener);
      
      
      
      //message= new JLabel("Αν δεν έχετε λογαριασμό κάντε:");
      // message.setBounds(200,300,250,40);
      
      firstName =new JTextField();
      firstName.setBounds(350,150,200,40);
      
      lastName=new JTextField();
      lastName.setBounds(350,200,200,40);
      
      studentId=new JTextField();
      studentId.setBounds(350,250,200,40);
      
      
      email=new JTextField();
      email.setBounds(350,300,200,40);
      
      password=new JPasswordField();
      password.setBounds(350,350,200,40);
      
      
      
      signIn= new JButton("Sign In");
      signIn.addActionListener(btnListener);
      signIn.setBounds(400,500,100,40);
      
      
      this.add(label_name);
      this.add(firstName);
      
      this.add(label_password);
      this.add(password);
      
      this.add(label_email);
      this.add(email);
      
      this.add(label_lastName);
      this.add(lastName);
      
      this.add(label_studentId);
      this.add(studentId);
      
      this.add(signUp);
      this.add(signIn);
      
      this.add(label_image);
   
      setVisible(true);
   }

   public void close(){
      this.dispose();
   }

   public void clearInputFields(){
      this.firstName.setText("");
      this.lastName.setText("");
      this.password.setText("");
      this.email.setText("");
      this.studentId.setText("");
   }

   public boolean isEmailValid(String email){

      String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";  
      //"^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$" check for at
      //  least one dot in the domain name and after the dot, it consist only with letters. The top-level domain should have only two
      //  to six letters which is also checked by this regex.
      
      // Here checks the pattern with the regex parameters and return true if the email is in the above rules 
      // return Pattern.compile(regex).matcher(email).matches();
      Pattern pattern = Pattern.compile(regex); 
      Matcher matcher = pattern.matcher(email);

      return matcher.matches();
   }


   private class BtnListener implements ActionListener{

      @Override
      public void actionPerformed(ActionEvent e) {

         if(e.getSource().equals(signUp)){
            String usrnm = studentId.getText();
            String mail = email.getText();
            String pass = new String(password.getPassword());
            String firstname = firstName.getText();
            String lastname = lastName.getText();
            if(!usrnm.isEmpty() && !pass.isEmpty() && !mail.isEmpty()){
               if(pass.length() < 5){
                  // Display Error on GUI --> password must be 5 or more characters
                  System.out.println("password must be 5 or more characters");
                  password.setText("");
               }
               // Uncomment/comment the line to check for the email format 
               // else if (!isEmailValid(mail)) {   /* TODO: Display Error on GUI --> wrong email format */ System.out.println("GUI: Wrong email format");  }
               else
               //Here we sent the info to the Class app so it be send to the server and further validate the credentials 
                  app.sendSignUpInfo(usrnm, pass, mail, firstname, lastname);
            }
            else {
               clearInputFields();
               //Display Error For Empty Username or Password or email
            }
         }
         else if (e.getSource().equals(signIn)){
            //Close loginWindow 
            close();
            //Start signUp window
            app.startSignInGUI();
         }

      }
      
  }
}
