package USER;

import java.io.Serializable;
import java.util.ArrayList;

import MENU.Dishes;

public class Order implements Serializable{
    
    private String studentId;
    private float finalPrice;
    private boolean free_meal_provision;
    private ArrayList<Dishes> dishes;


    public Order(String studentId, boolean fmp, ArrayList<Dishes> dishes){

        this.studentId = studentId;
        this.free_meal_provision = fmp;
        this.dishes = dishes;

    }


}
