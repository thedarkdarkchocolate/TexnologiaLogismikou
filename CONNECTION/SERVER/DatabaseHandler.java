package CONNECTION.SERVER;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {
    
    private Connection connection;

    //  main used for easier testing 
    public static void main(String args[]){

        DatabaseHandler db = new DatabaseHandler();
        db.closeDB();
    }


    public DatabaseHandler(){

        String jdbcUrl = "jdbc:sqlite:src/CONNECTION/SERVER/DB/eLesxh.db";
        try {
            connection = DriverManager.getConnection(jdbcUrl);
            
        } catch (SQLException e) {
            System.out.println("DATABASE:Error connecting to database");
            e.printStackTrace();
        }
    }


    public void closeDB(){

        try {
            this.connection.close();
        } catch (SQLException e) {
            System.out.println("DATABASE:Error closing the database");
            e.printStackTrace();
        }
    }




}
