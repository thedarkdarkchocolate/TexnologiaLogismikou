package CONNECTION.CLIENT;

import CONNECTION.CLIENT.CLIENTGUI.*;
import MENU.*;
import USER.*;

public class App {
    
    private Client client;
    private logInGui logIn;
    private signUpGui signUp;
    private mainGui mainFrame;


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
            startMainGui();
        }
        else if (code == 2){
            //Unkown Error
        }

    }

    
    public void startMainGui(){

        Profile profile;
        Menu menu;
        //  requestProfile
        profile = this.client.requestProfile();
        //  requestMenu
        menu = this.client.requestMenu();
        if(profile != null && menu != null){

            //  TODO: startMenuGUI
            //  mainGui
            profile.printProfileInfo();
            menu.printWholeDayMenu();
            this.mainFrame = new mainGui(menu, profile, this);
        }


    }

    public void sendSignUpInfo(String studentNumber, String pass, String mail, String firstName, String lastName) {

        int code = client.sendSignUpInfo(studentNumber, pass, mail, firstName, lastName);

        //USER ERROR MSG --> User Already Exist
        if(code == 0){
            //  TODO: Display user already exists error
            signUp.clearInputFields();
        }
        //User Succesfuly Created Account
        else if (code == 1){
            signUp.close();
            startSignInGUI();
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
