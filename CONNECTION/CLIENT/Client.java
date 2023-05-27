package CONNECTION.CLIENT;
import PACKETS.*;
import MENU.*;
import USER.*;
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
    private String client_Student_Number;
    private boolean free_meal_provision;
    private App app;


    public Client() throws IOException, ClassNotFoundException, InterruptedException, UnknownHostException{

        
        clSocket = new Socket("localhost", 5000);
        objOut = new ObjectOutputStream(clSocket.getOutputStream());
        objIn = new ObjectInputStream(clSocket.getInputStream());
        client_Student_Number = "";

        this.app = new App(this);

        Scanner s = new Scanner(System.in);
        while(true)
            if (s.nextLine().equals("EXIT")){
                //  TODO : close app!!
                break;
            }
            

        s.close();
        this.closeEveryThing();
    }

    //0: credentials don't match a user, 1: success user logged in, 2: Unkown Error
    //int ---> return code so the app knows what kind of error came up
    public int sendSignInInfo(String username, String password){
        System.out.println("Client: User Credentials: Username: "+ username +", Password: "+ password);
    
        try {
                    
            Packet<?> cred = new SignInPacket(username, password);
            objOut.writeObject(cred);
            objOut.flush();
            ServerAnswerPacket srvAnswr;
            srvAnswr = (ServerAnswerPacket)objIn.readObject();

            if (checkServerAnswer(srvAnswr)){
                System.out.println("Client: User Succefuly Connected");
                this.client_Student_Number = username;
                return 1;
            }
            else{
                //Throw UI Error fon incorrect credentials
                System.out.println("Client: Username or Password is wrong");
                return 0;
            }
        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
            return 2;
        }

    }
    

    public int sendSignUpInfo(String username, String password, String email, String firstName, String lastName) {
        System.out.println("Client: User Credentials: Username: "+ username +", Password: "+ password + ", E-mal: " + email);

        try {
            
            Packet<?> cred = new SignUpPacket(username, password, email, firstName, lastName);
            objOut.writeObject(cred);
            objOut.flush();
            ServerAnswerPacket srvAnswer;
            srvAnswer = (ServerAnswerPacket)objIn.readObject();

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

    
    public Profile requestProfile(){

        Packet<?> requestProfilePacket = new RequestPacket("PROFILE");
        try {
            objOut.writeObject(requestProfilePacket);
            objOut.flush();
            ProfilePacket profilePacket;
            profilePacket = (ProfilePacket)objIn.readObject();
            
            return profilePacket.getPacketData();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }
    
    public Menu requestMenu(){
        
        Packet<?> requestMenuPacket = new RequestPacket("MENU");
        
        try {

            objOut.writeObject(requestMenuPacket);
            objOut.flush();
            MenuPacket menuPacket;
            menuPacket = (MenuPacket)objIn.readObject();
           
            return menuPacket.getPacketData();
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        
    }

    public boolean sendOrder(Order order){

        Packet<?> orderPacket = new OrderPacket(order);

        try {
            
            objOut.writeObject(orderPacket);
            objOut.flush();

            ServerAnswerPacket srvAnswer;
            srvAnswer = (ServerAnswerPacket)objIn.readObject();

            if(srvAnswer.getPacketData()){
                System.out.println("Client: Order has been Accepted");
                return true;
            }else{
                System.out.println("Client: Order has been Declined");
                return false;
            }


        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

    }


    private boolean checkServerAnswer(ServerAnswerPacket packet){
        
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
