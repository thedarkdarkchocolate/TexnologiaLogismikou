package USER;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import MENU.Menu;


public class Order implements Serializable{
    
    private final String studentId;
    private final boolean free_meal_provision;
    private final static String dishCategories [] = {"MAIN_DISH", "GARNISH", "SALAD", "DESERT", "SPECIAL_MENU"};
    private float price;
    private ArrayList<Dish> dishes;

    // TODO: additions: time, date to log the order
    public Order(String studentId, boolean fmp, ArrayList<Dish> dishes){

        this.studentId = studentId;
        this.free_meal_provision = fmp;
        this.dishes = dishes;
        this.calculateOrderTotalPrice();
    }

    
    private void calculateOrderTotalPrice(){

        if(!this.free_meal_provision){
            
            for(Dish dish: this.dishes)
            this.price += dish.price();
            
        }
        else {

            HashMap<String, Boolean> tmpCat = new HashMap<>();
            // Initializing dictionary value to true for each catagories to indicate that
            // this item should't be added to the price because the student has free meal provision 
            for(String catagory: Order.dishCategories)
                tmpCat.put(catagory, true);

            for(Dish dish: this.dishes){

                if(!tmpCat.get(dish.dishCatagory()))
                    this.price += dish.price();
                else
                    tmpCat.put(dish.dishCatagory(), false);
            }
        }
    }

    public String getStudentId(){
        return this.studentId;
    }

    public Boolean hasFreeMealProvison(){
        return this.free_meal_provision;
    }

    public ArrayList<Dish> getDishes(){
        return this.dishes;
    }


}
