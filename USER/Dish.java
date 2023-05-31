package USER;

import java.io.Serializable;
import java.util.Arrays;

public record Dish (String name, float price, int quantity, String dishCatagory) implements Serializable{

    private final static String dishCategories [] = {"MAIN_DISH", "GARNISH", "SALAD", "DESERT", "SPECIAL_MENU"};

    //  Checking if the dish category is correct and if it isn't then raises an exception
    public Dish {
        if(!Arrays.stream(dishCategories).anyMatch(dishCatagory::equals)){
            throw new IllegalArgumentException("Invalid Dish Catagory");
        }
    }

    public float getTotalPrice(){
        return price * (float)quantity;
    }

    public void printDishInfo(){
        System.out.println("DishName: " + name + ", Price: " + price);
    }

    @Override
    public boolean equals(Object obj){
        Dish ds = (Dish)obj;
        return this.name().equals(ds.name());
    }

}