package CONNECTION.SERVER;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import USER.Profile;

public class DatabaseHandler {
    
    private Connection connection;
    private final HashMap<String, PreparedStatement> preparedStatements;
    private static ReentrantLock insertToUserDataBaseLock = new ReentrantLock(true);
    private static ReentrantLock insertToLogInCredentialsDataBaseLock = new ReentrantLock(true);

    //  main used for easier testing 
    public static void main(String args[]) throws SQLException{

        DatabaseHandler db = new DatabaseHandler();
        db.closeDB();

        System.out.println();

    }

    public DatabaseHandler() throws SQLException{

        String jdbcUrl = "jdbc:sqlite:src/CONNECTION/SERVER/DB/eLesxh.db";

        try {
            this.connection = DriverManager.getConnection(jdbcUrl);
            
        } catch (SQLException e) {
            System.out.println("DATABASE: Error connecting to database");
            e.printStackTrace();
        }

        // initializing preparedStatements Dictionary
        // to add or delete key and values modify the initialization bellow  
        
        preparedStatements = new HashMap<>(){{  

            put("GET_ID_PASS", connection.prepareStatement("select Id, Password from logInCredentials where Id = ? and Password = ?"));
            put("GET_ID", connection.prepareStatement("select Id from logInCredentials where Id = ?"));           
            put("GET_PROFILE_BY_ID", connection.prepareStatement("select * from userProfileData where id = ?"));
            put("INSERT_TO_LOG_IN_CREDENTIALS", connection.prepareStatement("insert into logInCredentials values (?, ?)"));
            put("INSERT_TO_USER_PROFILE", connection.prepareStatement("insert into userProfileData values (?, ?, ?, ?, ?, ?)"));

        }};
    }



    public boolean logInAuth(String studentId, String password) throws SQLException{


        this.preparedStatements.get("GET_ID_PASS").setString(1, studentId);
        this.preparedStatements.get("GET_ID_PASS").setString(2, password);

        ResultSet rs = this.preparedStatements.get("GET_ID_PASS").executeQuery();

        return rs.getString(1).equals(studentId) && rs.getString(2).equals(password); 
    }
    
    
    public void insertToLogInCredDataBase(String studentId, String password) throws SQLException{

        this.preparedStatements.get("INSERT_TO_LOG_IN_CREDENTIALS").setString(1, studentId);
        this.preparedStatements.get("INSERT_TO_LOG_IN_CREDENTIALS").setString(2, password);

        insertToLogInCredentialsDataBaseLock.lock();
        this.preparedStatements.get("INSERT_TO_LOG_IN_CREDENTIALS").executeUpdate();
        insertToLogInCredentialsDataBaseLock.unlock();
    }


    public void insertToUserProfileDataBase(Profile studentProfile) throws SQLException{

        this.preparedStatements.get("INSERT_TO_USER_PROFILE").setString(1, studentProfile.getStudentId());
        this.preparedStatements.get("INSERT_TO_USER_PROFILE").setString(2, studentProfile.getPassword());
        this.preparedStatements.get("INSERT_TO_USER_PROFILE").setString(3, studentProfile.getEmail());
        this.preparedStatements.get("INSERT_TO_USER_PROFILE").setString(4, studentProfile.getFirstname());
        this.preparedStatements.get("INSERT_TO_USER_PROFILE").setString(5, studentProfile.getLastname());
        this.preparedStatements.get("INSERT_TO_USER_PROFILE").setByte(6, (byte) (studentProfile.getFree_meal_provision() ? 1 : 0));

        insertToUserDataBaseLock.lock();
        this.preparedStatements.get("INSERT_TO_USER_PROFILE").executeUpdate();
        insertToUserDataBaseLock.unlock();
    }


    public Profile getStudentProfileById(String studentId) throws Exception{

        this.preparedStatements.get("GET_PROFILE_BY_ID").setString(1, studentId);

        ResultSet rs = this.preparedStatements.get("GET_PROFILE_BY_ID").executeQuery();

        String cred[] = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)};

        return new Profile(cred, rs.getByte(6) != 0);

    }

    public boolean checkIfUserExists(String studentId) throws SQLException{
        this.preparedStatements.get("GET_ID").setString(1, studentId);

        try {
            return this.preparedStatements.get("GET_ID").executeQuery().getString(1).equals(studentId);
        } catch (Exception e) {
            return false;
        }
    }

    public void closeDB(){

        try {
            this.connection.close();
        } catch (SQLException e) {
            System.out.println("DATABASE: Error closing the database");
            e.printStackTrace();
        }
    }




}
