package CONNECTION.SERVER;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Server{
    
    private ServerSocket serverSocket;
    private static HashMap<String, String> credentialsDataBase = new HashMap<>();
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private static ArrayList<Thread> runningThreads = new ArrayList<>();
    private Thread serverCommandThread;

    public Server() throws IOException{

        //Creating Suto DataBase
        //Temp DataBase Makes Acc from dai19000 - dai19199 || pass 19000 - 19199
        for(int i = 0; i < 200; i++)
            credentialsDataBase.put("dai" + ((Integer)(19000 + i)).toString(), ((Integer)(19000 + i)).toString());


        this.serverSocket = new ServerSocket(5000);                // Creating server socket
        serverCommandThread = new Thread(new ServerCommandHandler());   // New Thread to wait for commands while the server is running
        serverCommandThread.start(); 
        this.startServer();


        //SERVER GUI STARTING...

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

    public void registerNewAccount(String name, String pass){
        credentialsDataBase.put(name, pass);
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
