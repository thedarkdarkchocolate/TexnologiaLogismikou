package CONNECTION.SERVER;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import CONNECTION.SERVER.SERVERGUI.serverGui;
import MENU.Menu;
import USER.*;

public class Server{
    
    private ServerSocket serverSocket;
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private static ArrayList<Thread> runningThreads = new ArrayList<>();
    private final static Lock incomingOrderLock = new ReentrantLock();
    private DatabaseHandler dbHandler;
    private Thread serverCommandThread;
    private Menu menu;
    private serverGui serverGui;

    // public static void main(String args[]){

    //     new Server("");
        
    // }

    
    //TEMPORARY CONSTACTOR
    // public Server(String k){
    //     int day = 1;
    //     menuReader(Integer.toString(day));
    //     this.menu.printMenuByTimeOfService(2);
    // }



    public Server() throws IOException{

        // Initializing DatabaseHandler
        try {
            this.dbHandler = new DatabaseHandler();
        } catch (SQLException e) {
            // TODO: handle exception
        }
        

        // TODO: Create Menu Not final place just for testing(we need live menu)
        //  get current day and read the correct menu
        menuReader("1");

        //SERVER GUI STARTING...
        serverGui = new serverGui();

        this.serverSocket = new ServerSocket(5000);                // Creating server socket
        serverCommandThread = new Thread(new ServerCommandHandler());   // New Thread to wait for commands while the server is running
        serverCommandThread.start(); 
        this.startServer();




    }

    
    public void startServer(){

        while(!this.serverSocket.isClosed()){    
            try {
                System.out.println("SERVER: Waiting for client to connect");
                Socket socket = this.serverSocket.accept();                     //  Main Thread waits in this line until the client has connected
                System.out.println("SERVER: New Client Connected");
                Thread thread = new Thread();
                ClientHandler clientHandler = new ClientHandler(socket, this, thread);  //  Creating new clientHandler and running the constructor
                clientHandlers.add(clientHandler);                              //  Adding clientHandler to ArrayList

                thread = new Thread(clientHandler);                      //  new Thread starting to exec run in clientHandler
                runningThreads.add(thread);
                thread.start();

            } catch (IOException | ClassNotFoundException e) {
                closeServerSocket();
            }
        }

        // Terminating clientHandlers, their threads, serverSocket and closing DataBase Connection
        closeServer();

    }

    public boolean authLogInCredentials(String studentId, String pass){
        
        try {
            return dbHandler.logInAuth(studentId, pass);
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    public boolean checkIfUsernameExists(String studentId) {

        try {
            return this.dbHandler.checkIfUserExists(studentId);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }


    public void registerNewAccount(String cred[]){
        // cred[]: 0 --> studentId, 1 --> pass, 2 --> email
        try {
            this.dbHandler.insertToUserProfileDataBase(new Profile(cred, checkForMealProvision(cred[0])));
            this.dbHandler.insertToLogInCredDataBase(cred[0], cred[1]);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    
    private boolean checkForMealProvision(String studentId) {
        try {
            return true; //mealProvisionDataBase.get(studentId);
        } catch (Exception e) {
            return false;
        }
    }

    public Profile getProfile(String studentId) throws Exception {
        return this.dbHandler.getStudentProfileById(studentId);
    }

    public Menu getMenu(){
        return this.menu;
    }

    public void insertOrder(Order order){
        try {

            incomingOrderLock.lock();

            this.serverGui.insertIncomingOrder(order);
            // TODO: insert Order in DB


            incomingOrderLock.unlock();
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    //  Txt menu files day: String day --> 1 - 5    (Monday-Friday)
    //  Txt menu file format: The first line should always start with BREAKFAST
    //  BREAKFAST declares that the following menu is for the breakfast, a service should always start and end with the same word
    //  BREAKFAST menu ends when there is another BREAKFAST in a line, after that there should always be LAUNCH service and after that DINNER
    //  The char "-" when is present in a line, the line will always be skipped, it's used to make it easier to read the menu.txt
    //  The dish catagories don't need to follow an exact order, place your dish catagory and in the next line write the dish
    private void menuReader(String day){

        try {
            BufferedReader br = new BufferedReader(new FileReader("src/CONNECTION/SERVER/MENU_TXT/" + day + "_Menu.txt"));
            String line = br.readLine();
            String [] services = {"BREAKFAST", "LAUNCH", "DINNER"};

            if(line != null && line.equals("BREAKFAST")) {
                String currService = line;
                Menu tmpMenu = new Menu();
                String category = "";
                String [] dishCategories = Menu.getDishCategories();


                for(String ser: services){

                    while(currService.equals(ser)){
                        
                        line = br.readLine();

                        if(Arrays.stream(dishCategories).anyMatch(line::equals)){
                            category = line;
                        }
                        else if (line.equals(ser))
                            currService = "";
                        else if (line.contains("-"))
                            continue;
                        else {
                            switch(ser){
                                case "BREAKFAST":
                                    tmpMenu.insertBreakfastDish(category, line);
                                    break;
                                case "LAUNCH":
                                    tmpMenu.insertLaunchDish(category, line);
                                    break;
                                case "DINNER":
                                    tmpMenu.insertDinnerDish(category, line);
                                    break;
                                default:
                                    System.out.println("SERVER: menuReader switch hit's default");
                                    break;
                            }
                        }
                    }

                    line = br.readLine();
                    currService = line;
                }

                this.menu = tmpMenu;

            }
            else {  
                br.close();
                throw new Exception("txt file is NULL", null);
            }

            br.close();

        }catch(Exception e){
            
            e.printStackTrace();
        }        

    }

    private void closeServerSocket(){
        try {
            if(serverSocket != null)
                serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeClientHandlers(){

        for(ClientHandler clientHandler: clientHandlers)
                clientHandler.closeEveryThing();

    }

    private void closeDatabaseHandler(){
        this.dbHandler.closeDB();
    }

    private void closeServer(){

        closeClientHandlers();
        closeServerSocket();
        closeDatabaseHandler();
        
    }



    public class ServerCommandHandler implements Runnable{

        @Override
        public void run() {
            
            Scanner scanner = new Scanner(System.in);
            String command = "";

            while(!command.equals("EXIT")){

                command = scanner.nextLine();

            }

            // Terminating clientHandler, their threads and closing serverSocket 
            
            scanner.close();
            closeServer();

        }


        
    }

   
    
    
}
