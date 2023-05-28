package CONNECTION.SERVER;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import USER.Dish;
import USER.Order;
import USER.Profile;

public class DatabaseHandler {
    
    private Connection connection;
    private HashMap<String, PreparedStatement> preparedStatements;

    private static ReentrantLock insertToUserDataBaseLock = new ReentrantLock(true);
    private static ReentrantLock insertToLogInCredentialsDataBaseLock = new ReentrantLock(true);
    private static ReentrantLock insertOrderLock = new ReentrantLock(true);
    private static ReentrantLock updateOrderStatus = new ReentrantLock(true);

    //  main used for easier testing 
    public static void main(String args[]) throws SQLException, ClassNotFoundException, IOException{

        DatabaseHandler db = new DatabaseHandler();

        ArrayList<Dish> d = new ArrayList<>();

        d.add(new Dish("fasolakia", 3, 2, "MAIN_DISH"));

        db.insertOrder(new Order("dai19159", false, false, d), "DECLINED");
        db.insertOrder(new Order("dai19159", true, false, d), "DECLINED");
        
        for(Order order: db.getOrderByID("dai19159")){
            order.printOrderInfo();
            System.out.println("--------------");
        }
        
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
        try {
            
            preparedStatements = new HashMap<>(){{  
    
                put("GET_ID_PASS", connection.prepareStatement("select Id, Password from logInCredentials where Id = ? and Password = ?"));
                put("GET_ID", connection.prepareStatement("select Id from logInCredentials where Id = ?"));           
                put("GET_PROFILE_BY_ID", connection.prepareStatement("select * from userProfileData where id = ?"));
                put("INSERT_TO_LOG_IN_CREDENTIALS", connection.prepareStatement("insert into logInCredentials values (?, ?)"));
                put("INSERT_TO_USER_PROFILE", connection.prepareStatement("insert into userProfileData values (?, ?, ?, ?, ?, ?)"));
                put("INSERT_ORDER", connection.prepareStatement("insert into Orders values (?, ?, ?, ?)"));
                put("GET_ORDERS_BY_ID", connection.prepareStatement("select OrderObj from Orders where studentID = ? group by OrderID"));
                put("UPDATE_ORDER_STATUS_BY_ORDER_ID", connection.prepareStatement("update Orders set Status = ? where OrderID = ?"));
    
            }};

        } catch (Exception e) {
            preparedStatements = new HashMap<>();
            e.printStackTrace();
            this.closeDB();
        }
    }

    public void insertOrder(Order order, String status) throws SQLException {
        
        this.preparedStatements.get("INSERT_ORDER").setString(1, order.getOrderID());
        this.preparedStatements.get("INSERT_ORDER").setString(2, order.getStudentId());
        // convert order to byte stream
        this.preparedStatements.get("INSERT_ORDER").setObject(3, this.orderToByte(order));
        this.preparedStatements.get("INSERT_ORDER").setString(4, status);

        insertOrderLock.lock();

        this.preparedStatements.get("INSERT_ORDER").executeUpdate();

        insertOrderLock.unlock();

    }

    public ArrayList<Order> getOrderByID(String studentID) throws SQLException, ClassNotFoundException, IOException{

        this.preparedStatements.get("GET_ORDERS_BY_ID").setString(1, studentID);

        ResultSet rs = this.preparedStatements.get("GET_ORDERS_BY_ID").executeQuery();

        ArrayList<Order> orders = new ArrayList<>();

        while(rs.next())
            orders.add(byteToOrder(rs.getBinaryStream(1)));

        return orders;
    }

    public void updateOrderStatus(String orderID, String status) throws SQLException{

        this.preparedStatements.get("UPDATE_ORDER_STATUS_BY_ORDER_ID").setString(1, status);
        this.preparedStatements.get("UPDATE_ORDER_STATUS_BY_ORDER_ID").setString(1, orderID);

        updateOrderStatus.lock();

        this.preparedStatements.get("UPDATE_ORDER_STATUS_BY_ORDER_ID").executeUpdate();

        updateOrderStatus.unlock();

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
        
        ResultSet rs1 = this.preparedStatements.get("GET_PROFILE_BY_ID").executeQuery();

        String cred[] = {rs1.getString(1), rs1.getString(2), rs1.getString(3), rs1.getString(4), rs1.getString(5)};

        Profile profile = new Profile(cred, rs1.getByte(6) != 0);

        profile.setPrevOrders(this.getOrderByID(studentId));

        return profile;

    }

    public boolean checkIfUserExists(String studentId) throws SQLException{
        this.preparedStatements.get("GET_ID").setString(1, studentId);
        try {
            return this.preparedStatements.get("GET_ID").executeQuery().getString(1).equals(studentId);
        } catch (Exception e) {
            return false;
        }
    }

    public byte[] orderToByte(Order order) {

        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(bytesOutputStream);
            objectOutputStream.writeObject(order);
            objectOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
        return bytesOutputStream.toByteArray();

    }

    public Order byteToOrder(InputStream byteStream) throws ClassNotFoundException, IOException{

        ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
        Order tmpOrder = (Order)objectInputStream.readObject();

        objectInputStream.close();
        
        return tmpOrder;
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
