package USER;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;



public class Order implements Serializable{
    
    private final String studentId;
    private final boolean free_meal_provision;
    private final String orderID;
    private final boolean takeAway;
    private final static String dishCategories [] = {"MAIN_DISH", "GARNISH", "SALAD", "DESERT", "SPECIAL_MENU"};
    private float price;
    private ArrayList<Dish> dishes;
    // Holds the dish catagories and a boolean value 
    // It is used to calculate the final price when the student has free meal provision 
    // If a student orders an dish from a catagory the the boolean value changes to false so if he orders another dish from the same
    // dish catagory then it will be added to the final price 
    private HashMap<String, Boolean> boolCatagories;

    
    public Order(String studentId, boolean fmp, boolean takeAway, ArrayList<Dish> dishes){

        this.studentId = studentId;
        this.free_meal_provision = fmp;
        this.dishes = dishes;
        this.takeAway = takeAway;
        this.orderID = calcOrderID();
        this.boolCatagories = new HashMap<>();
        this.calculateOrderTotalPrice();
    }

    // Testing main 
    // public static void main(String args[]){

    //     Order or = new Order("dai19159", true, new ArrayList<>(Arrays.asList(new Dish("kappa", 4, 3, "MAIN_DISH"), new Dish("kappa", 4, 2, "DESERT"))));

    //     System.out.println(or.getOrderTotalPrice());

    //     System.out.println(or.getOrderID());
        
    //     System.out.println(or.dishes);
    // }

    private String calcOrderID() {

        String id = this.studentId;
        String fmp = this.free_meal_provision ? "T" : "F";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String dateTime = now.format(formatter);
        
        return id + ":" + fmp + ":" + dateTime;
    }


    private void calculateOrderTotalPrice(){

        if(!this.free_meal_provision){
            
            for(Dish dish: this.dishes)
            this.price += dish.price();
            
        }
        else {

            // Initializing dictionary value to true for each catagories to indicate that
            // this item should't be added to the price because the student has free meal provision
            for(String catagory: Order.dishCategories)
                this.boolCatagories.put(catagory, true);

            for(Dish dish: this.dishes){

                if(!this.boolCatagories.get(dish.dishCatagory()))
                    this.price += dish.price();
                else
                    this.boolCatagories.put(dish.dishCatagory(), false);
                    this.price += dish.price() * (dish.quantity() - 1);
            }
        }
    }


    public String getStudentId(){
        return this.studentId;
    }

    public boolean hasFreeMealProvison(){
        return this.free_meal_provision;
    }

    public boolean isOrderTakeAway(){
        return this.takeAway;
    }

    public ArrayList<Dish> getDishes(){
        return this.dishes;
    }

    public String getOrderID(){
        return this.orderID;
    }

    public float getOrderTotalPrice(){
        return this.price;
    }

    public void printOrderInfo(){

        System.out.println(this.studentId);
        System.out.println(this.orderID);
        System.out.println(this.free_meal_provision);

    }


}
