package CONNECTION.CLIENT;
import PACKETS.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client{
    
    private Socket clSocket;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;
    private App app;


    public Client() throws IOException, ClassNotFoundException, InterruptedException, UnknownHostException{

        
        clSocket = new Socket("localhost", 5000);
        objOut = new ObjectOutputStream(clSocket.getOutputStream());
        objIn = new ObjectInputStream(clSocket.getInputStream());

        this.app = new App(this);

        Scanner s = new Scanner(System.in);
        while(true)
            if (s.nextLine().equals("EXIT")){
                //  To Do : close app!!
                break;
            }
            

        s.close();
        this.closeEveryThing();
    }

    //0: credentials don't match a user, 1: success user logged in, 2: Unkown Error
    //int ---> return code so the app knows what kind of error came up
    public int sendSignInInfo(String username, String password){
        System.out.println("Client: User Credentials: Usrnm: "+ username +", Pswrd: "+ password);
    
        try {
                    
            Packet<?> cred = new SignInPacket(username, password);
            objOut.writeObject(cred);
            objOut.flush();
            ServerAnswer srvAnswr;
            srvAnswr = (ServerAnswer)objIn.readObject();

            if (checkServerAnswer(srvAnswr)){
                System.out.println("User Succefuly Connected");
                return 1;
            }
            else{
                //Throw UI Error fon incorrect credentials
                System.out.println("Username or Password is wrong");
                return 0;
            }
        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
            return 2;
        }

    }
    

    public int sendSignUpInfo(String username, String password, String email) {
        System.out.println("Client: User Credentials: Usrnm: "+ username +", Pswrd: "+ password + ", E-mal: " + email);

        try {
            
            Packet<?> cred = new SignUpPacket(username, password, email);
            objOut.writeObject(cred);
            objOut.flush();
            ServerAnswer srvAnswer;
            srvAnswer = (ServerAnswer)objIn.readObject();

            if(checkServerAnswer(srvAnswer)){
                // Succefuly created new account
                System.out.println("Client: Succefuly created new account");
                return 1;
            }
            else{
                // User Already exist
                System.out.println("Client: User Already exist");
                return 0;
            }


        } catch (Exception e) {
            // TODO: handle exception
            return 2;
        }

    }
    


    private boolean checkServerAnswer(ServerAnswer packet){
        
        return packet.getPacketData();        
    }

    private void closeEveryThing() {
        try {
            if (this.objIn != null)
                this.objIn.close();
            
            if(this.objOut != null)
                this.objOut.close();
            
            if (this.clSocket != null)
                this.clSocket.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
