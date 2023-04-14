package GUI;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import PACKETS.App;

public class signUp extends JFrame{
	
   App app;

	JPasswordField password;
	JTextField name ,lastName, studentNumber, email;
	JLabel label_password,label_name,label_lastName,label_studentNumber,message, label_image, label_email;
	JButton signUp, signIn;
	ImageIcon icon = new ImageIcon("src/ASSETS/logo-new.png");
	Image image = icon.getImage();
	
	public signUp(App app){
		
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
      
      label_studentNumber= new JLabel("AM");
      label_studentNumber.setBounds(200,250,150,40);
      
      
      label_email= new JLabel("E-mail");
      label_email.setBounds(200,300,150,40);
      
      label_password= new JLabel("Password");
      label_password.setBounds(200,350,150,40);
      
      signUp = new JButton("Sign Up");// toggle button;
      signUp.setBounds(400,450,100,40);
      signUp.addActionListener(btnListener);
      
      
      
      //message= new JLabel("Αν δεν έχετε λογαριασμό κάντε:");
      // message.setBounds(200,300,250,40);
      
      name=new JTextField();
      name.setBounds(350,150,200,40);
      
      lastName=new JTextField();
      lastName.setBounds(350,200,200,40);
      
      studentNumber=new JTextField();
      studentNumber.setBounds(350,250,200,40);
      
      
      email=new JTextField();
      email.setBounds(350,300,200,40);
      
      password=new JPasswordField();
      password.setBounds(350,350,200,40);
      
      
      
      signIn= new JButton("Sign In");
      signIn.addActionListener(btnListener);
      signIn.setBounds(400,500,100,40);
      
      
      this.add(label_name);
      this.add(name);
      
      this.add(label_password);
      this.add(password);
      
      this.add(label_email);
      this.add(email);
      
      this.add(label_lastName);
      this.add(lastName);
      
      this.add(label_studentNumber);
      this.add(studentNumber);
      
      this.add(signUp);
      this.add(signIn);
      
      this.add(label_image);
   
      setVisible(true);
   }

   public void close(){
      this.dispose();
   }

   public void clearInputFields(){
      this.name.setText("");
      this.lastName.setText("");
      this.password.setText("");
      this.email.setText("");
      this.studentNumber.setText("");
   }


   private class BtnListener implements ActionListener{

      @Override
      public void actionPerformed(ActionEvent e) {

         if(e.getSource().equals(signUp)){
            String usrnm = studentNumber.getText();
            String mail = email.getText();
            String pass = new String(password.getPassword());
            if(!usrnm.isEmpty() && !pass.isEmpty() && !mail.isEmpty()){
               if(pass.length() < 5){
                  // Display Error on GUI --> password must be 5 or more characters
                  System.out.println("password must be 5 or more characters");
                  password.setText("");
               }else
                  app.sendSignUpInfo(usrnm, pass, mail);
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
