package CONNECTION.SERVER;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import PACKETS.Packet;
import PACKETS.ServerAnswer;
import PACKETS.SignInPacket;
import PACKETS.SignUpPacket;

public class ClientHandler implements Runnable{


    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;   
    private Server server;
    private Boolean isSignedIn;

    
    public ClientHandler(Socket socket, Server server) throws ClassNotFoundException{
        

        try {
            
            this.server = server;
            this.socket = socket;
            this.isSignedIn = false;
            this.objOut = new ObjectOutputStream(this.socket.getOutputStream());
            this.objIn = new ObjectInputStream(this.socket.getInputStream());


        } catch (IOException e) {
            closeEveryThing();
            e.printStackTrace();
        }
    }




    @Override
    public void run() {
        

        try {
            while(socket.isConnected()){

                Packet<?> pckt = (Packet<?>)objIn.readObject(); // waits here until a packet from the client arrives
                reroutePacket(pckt);
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        sc.close();
        closeEveryThing();

    }

    private void reroutePacket(Packet<?> pckt) {

        switch(pckt.getPacketType()){

            case "SIGNIN":
                if(!this.isSignedIn)
                    signInHandler((SignInPacket)pckt);
                break;
                case "SIGNUP":
                if(!this.isSignedIn)
                    signUpHandler((SignUpPacket)pckt);
                break;
            default:
                System.out.println("REROUTE PACKET: unable to find packet type");
                break;

        }


    }


    private void signInHandler(SignInPacket signInPacket){

            
        boolean authCompleted = checkCredentials(signInPacket);
        if(authCompleted) isSignedIn = true;
        Packet<Boolean> srvAnswr = new ServerAnswer(authCompleted);

        try {
            objOut.writeObject(srvAnswr);
            System.out.println("SERVER: Credentials auth: " + authCompleted);
        } catch (IOException e) {
            e.printStackTrace();
        }
        

    }

    private void signUpHandler(SignUpPacket signUpPacket){

        HashMap<String, String> dict = signUpPacket.getPacketData();
        String username = dict.get("username");
        String password = dict.get("password");
        boolean userExists = checkIfUserExists(signUpPacket);

        if(!userExists){
            //Create Account...
            server.registerNewAccount(username, password);
        }

        // If userExists = true then we can't create the account
        Packet<Boolean> srvAnswer = new ServerAnswer(!userExists);
        

        try {
            objOut.writeObject(srvAnswer);
            System.out.println(userExists ? "SERVER: User exist" : "SERVER: New account created");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean checkCredentials(SignInPacket userSignInPacket){

        HashMap<String, String> dict = userSignInPacket.getPacketData();
        String usrnm = dict.get("username");
        String pass = dict.get("password");
        
        // authPassWithUsername throws NullPointerException
        try{
            boolean usernamesMatch = server.authUsernamesMatch(usrnm);
            boolean passwordsMatch = server.authPassWithUsername(usrnm, pass);

            return usernamesMatch && passwordsMatch;
        }catch(NullPointerException e){
            return false;
        }
        
    }

    private boolean checkIfUserExists(SignUpPacket userSignUpPacket){
        // Checking if username is already in the database using the same fuction to logIn a user
        return server.authUsernamesMatch(userSignUpPacket.getPacketData().get("username"));
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
    }   
    
    
    public void closeEveryThing() {
        removeClientHandler();
        try {
            if (this.objIn != null)
                this.objIn.close();
            
            if(this.objOut != null)
                this.objOut.close();
            
            if (this.socket != null)
                this.socket.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
