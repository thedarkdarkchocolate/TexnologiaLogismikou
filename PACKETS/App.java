package PACKETS;
import GUI.*;

import CONNECTION.CLIENT.Client;


public class App {
    
    Client client;
    logInGui logIn;
    signUp signUp;

    public App (Client client) { 
        
        this.client = client;
        startSignInGUI();
    }

    public void sendSignInInfo(String user, String pass){
        int code = client.sendSignInInfo(user, pass);
        if(code == 0){
            logIn.clearInputFields();
            //USER ERROR MSG --> Invalid Credentials
        }
        else if (code == 1){
            //User Succesfuly Connected
            logIn.close();
            //Start Menu GUI
        }
        else if (code == 2){
            //Unkown Error
        }

    }

    public void sendSignUpInfo(String studentNumber, String pass, String mail) {

        int code = client.sendSignUpInfo(studentNumber, pass, mail);

        //USER ERROR MSG --> User Already Exist
        if(code == 0){
            signUp.clearInputFields();
        }
        //User Succesfuly Created Account
        else if (code == 1){
            signUp.close();
            startSignInGUI();
            //Start Menu GUI
        }
        //Unkown Error
        else if (code == 2){
        }

    }
    

    public void startSignInGUI(){
        logIn = new logInGui(this);
    }

    public void startSignUpGUI(){
        signUp = new signUp(this);
    }

    
  
}
