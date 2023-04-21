package CONNECTION.CLIENT;

import CONNECTION.CLIENT.CLIENTGUI.*;
import MENU.*;
import USER.*;

public class App {
    
    private Client client;
    private logInGui logIn;
    private signUpGui signUp;
    // private orderGui orderGui;
    // private confirmOrederGui confirmOrederGui;


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
            startMenuGui();
        }
        else if (code == 2){
            //Unkown Error
        }

    }

    
    public void startMenuGui(){

        Profile profile;
        Menu menu;
        
        //  requestProfile
        profile = this.client.requestProfile();
        //  requestMenu
        menu = this.client.requestMenu();
        if(profile != null && menu != null){

            //  TODO: startMenuGUI(profile, menu)
            profile.printProfileInfo();
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
        signUp = new signUpGui(this);
    }

    
  
}
