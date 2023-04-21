package USER;

import java.io.Serializable;
import java.util.ArrayList;

public class Profile implements Serializable{

    private String studentId;
    private String password;
    private String email;
    private ArrayList<Order> prevOrders;
    private boolean free_meal_provision;
    

    
    // on cred[] || 0 --> studentId, 1 --> password, 2 --> e-mail, 3 --> firstName, 4 --> lastName ||
    public Profile(String[] cred, boolean free_meal_provision){
        
        this.studentId = cred[0];
        this.password = cred[1];
        this.email = cred[2];
        this.free_meal_provision = free_meal_provision;
        
    }

    public Profile(){}
    
    public String getStudentId() {
        return studentId;
    }
    
    public String getPassword() {
        return password;
    }
    
    public ArrayList<Order> getPrevOrders() {
        return prevOrders;
    }
    
    public String getEmail() {
        return email;
    }
    
    public boolean isFree_meal_provision() {
        return free_meal_provision;
    }

    public void printProfileInfo(){

        System.out.println("------Profile-Info------");
        System.out.println("StudentId: "+this.studentId);
        System.out.println("Password: "+this.password);
        System.out.println("E-mail: "+this.email);
        System.out.println("Free email provision: "+this.free_meal_provision);
        System.out.println("------------------------");
        
    }
    
}
