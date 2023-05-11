package CONNECTION.SERVER;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import MENU.Menu;
import USER.*;

public class Server{
    
    private ServerSocket serverSocket;
    private static HashMap<String, String> credentialsDataBase = new HashMap<>();
    private static HashMap<String, Profile> profilesDataBase = new HashMap<>();
    private static HashMap<String, Boolean> mealProvisionDataBase = new HashMap<>();
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private static ArrayList<Thread> runningThreads = new ArrayList<>();
    private static Queue<Order> orderQ = new PriorityQueue<Order>();
    private final static Lock queueLock = new ReentrantLock();
    private Thread serverCommandThread;
    private Menu menu;

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

        //Creating Suto DataBase
        //Temp DataBase Makes Acc from dai19000 - dai19199 || pass 19000 - 19199
        //Also creating Profile Database for the same accounts
        for(int i = 0; i < 200; i++){
            String studentId = "dai" + ((Integer)(19000 + i)).toString();
            String pass = ((Integer)(19000 + i)).toString();
            String email = studentId + "@uom.edu.gr";
            String cred[] = {studentId, pass, email};
            Profile tmp;
            
            if (i%5==0 || i == 159) tmp = new Profile(cred, true);
            else tmp = new Profile(cred, false);
            
            credentialsDataBase.put(studentId, pass);
            profilesDataBase.put(studentId, tmp);
        }

        //Adding studentIds to meal provision database
        mealProvisionDataBase.put("isc19159", true);

        

        // TODO: Create Menu Not final place just for testing(we need live menu)
        //  get current day and read the correct menu
        menuReader("1");

        //SERVER GUI STARTING...


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
                ClientHandler clientHandler = new ClientHandler(socket, this);  //  Creating new clientHandler and running the constructor
                clientHandlers.add(clientHandler);                              //  Adding clientHandler to ArrayList

                Thread thread = new Thread(clientHandler);                      //  new Thread starting to exec run in clientHandler
                runningThreads.add(thread);
                thread.start();

            } catch (IOException | ClassNotFoundException e) {
                closeServerSocket();
            }
        }
    }

    public boolean authPassWithUsername(String name, String pass){
        // credentialsDataBase when the name isn't in the data base it returns Null and equals can't compare Null with an object
        return credentialsDataBase.get(name).equals(pass);
    }

    public boolean authUsernamesMatch(String name){
        return credentialsDataBase.keySet().contains(name);
    }

    public void registerNewAccount(String cred[]){
        // cred[]: 0 --> username, 1 --> pass, 2 --> email
        credentialsDataBase.put(cred[0], cred[1]);
        profilesDataBase.put(cred[0], new Profile(cred, checkForMealProvision(cred[0])));
    }

    
    private boolean checkForMealProvision(String studentId) {
        try {
            return mealProvisionDataBase.get(studentId);
        } catch (Exception e) {
            return false;
        }
    }

    public Profile getProfile(String studentId) {
        return profilesDataBase.get(studentId);
    }

    public Menu getMenu(){
        return this.menu;
    }

    public void insertOrder(Order order){
        try {
            queueLock.lock();
            orderQ.add(order);
            queueLock.unlock();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    //  Txt menu files day: String day --> 1 - 5    (Monday-Friday)
    //  Txt menu file format: The first line should always start with BREAKFAST
    //  BREAKFAST declares that the following menu is for the breakfast, a service should always start and end with the same word
    //  BREAKFAST menu ends when there is another BREAKFAST in a line, after that there should always be LAUNCH service and after that DINNER
    //  The char "-" when is present in a line the line will always be skipped, so it's used so is easier to read the menu.txt
    //  The dish catagories don't need to follow an exact order place your dish catagory and in the next line write the dish
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

    public void closeServerSocket(){
        try {
            if(serverSocket != null)
                serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ServerCommandHandler implements Runnable{

        @Override
        public void run() {
            
            Scanner sc = new Scanner(System.in);
            String command = "";

            while(!command.equals("EXIT")){

                command = sc.nextLine();

            }

            for(ClientHandler client: clientHandlers)
                client.closeEveryThing();
                
            for (Thread tr: runningThreads){
                
                try {
                    tr.interrupt();

                } catch (Exception e) {
                    
                }
            }
            sc.close();
            closeServerSocket();

        }


        
    }

    
    
}
