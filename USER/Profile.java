package USER;

import java.io.Serializable;
import java.util.ArrayList;

public class Profile implements Serializable{

    private String studentId;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private boolean free_meal_provision;
    private ArrayList<Order> prevOrders;
    

    
    // on cred[] || 0 --> studentId, 1 --> password, 2 --> e-mail, 3 --> firstName, 4 --> lastName ||
    public Profile(String[] cred, boolean free_meal_provision){
        
        this.studentId = cred[0];
        this.password = cred[1];
        this.email = cred[2];
        this.firstName = cred[3];
        this.lastName = cred[4];
        this.free_meal_provision = free_meal_provision;
        
    }

    public void setPrevOrders(ArrayList<Order> orders){
        this.prevOrders = orders;
    }

    
    public String getStudentId() {
        return studentId;
    }
    
    public String getPassword() {
        return password;
    }
    
    public ArrayList<Order> getPrevOrders() {
        return prevOrders;
    }

    public String getFirstname(){
        return this.firstName;
    }
    
    public String getLastname(){
        return this.lastName;
    }

    public String getEmail() {
        return email;
    }
    
    public boolean getFree_meal_provision() {
        return free_meal_provision;
    }

    public void printProfileInfo(){

        System.out.println("------Profile-Info------");
        System.out.println("StudentId: "+this.studentId);
        System.out.println("Password: "+this.password);
        System.out.println("E-mail: "+this.email);
        System.out.println("Firstname: "+this.firstName);
        System.out.println("Lastname: "+this.lastName);
        System.out.println("Free email provision: "+this.free_meal_provision);
        System.out.println("---------PrevOrders-----------");
        for(Order order: this.prevOrders)
            System.out.println(order);
        
    }
    
}
