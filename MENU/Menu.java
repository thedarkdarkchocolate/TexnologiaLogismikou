package MENU;

import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;

import USER.Dish;

public class Menu implements Serializable{
    
    private HashMap<String, ArrayList<Dish>> breakfastMenu;
    private HashMap<String, ArrayList<Dish>> launchMenu;
    private HashMap<String, ArrayList<Dish>> dinnerMenu;

    private final static HashMap<String, Float> dishCategories = new HashMap<>() {{
        put("MAIN_DISH", (float)2.0);
        put("GARNISH", (float)0.3);
        put("SALAD", (float)0.7);
        put("DESERT", (float)0.5);
        put("SPECIAL_MENU", (float)2.0);
    }};
    
    public Menu(){

        this.breakfastMenu = new HashMap<String, ArrayList<Dish>>();
        this.launchMenu = new HashMap<String, ArrayList<Dish>>();
        this.dinnerMenu = new HashMap<String, ArrayList<Dish>>();

        for(String dishCat: dishCategories.keySet()){
            this.breakfastMenu.put(dishCat, new ArrayList<Dish>());
            this.launchMenu.put(dishCat, new ArrayList<Dish>());
            this.dinnerMenu.put(dishCat, new ArrayList<Dish>());
        }

    }

    public void insertBreakfastDish(String category, String dish){

        if (dishCategories.keySet().contains(category)){
            // Here from the dictionary we getting the ArrayList that is in the dict and adding the dish
            this.breakfastMenu.get(category).add(new Dish(dish, dishCategories.get(category), 1, category));
        }
        
    }
    
    public void insertLaunchDish(String category, String dish){
        
        if (dishCategories.keySet().contains(category)){
            // Here from the dictionary we getting the ArrayList that is in the dict and adding the dish
            this.launchMenu.get(category).add(new Dish(dish, dishCategories.get(category), 1, category));
        }
        
    }
    
    public void insertDinnerDish(String category, String dish){
        
        if (dishCategories.keySet().contains(category)){
            // Here from the dictionary we getting the ArrayList that is in the dict and adding the dish
            this.dinnerMenu.get(category).add(new Dish(dish, dishCategories.get(category), 1, category));
        }

    }
    
    public static String[] getDishCategories(){
        return dishCategories.keySet().stream().toArray(String[] ::new);
    }

    public HashMap<String, ArrayList<Dish>> getBreakfastMenu(){
        return breakfastMenu;
    }

    public HashMap<String, ArrayList<Dish>> getLaunchMenu(){
        return launchMenu;
    }

    public HashMap<String, ArrayList<Dish>> getDinnerMenu(){
        return dinnerMenu;
    }

    public void printMenuByTimeOfService(int time){

        switch(time){

            case 1:
                System.out.println("-------------BREAKFAST-------------");
                System.out.println();
                for(String dishCatgory: dishCategories.keySet()){
                    System.out.println("       -------" + dishCatgory + "-------");
                    for(Dish dish: breakfastMenu.get(dishCatgory)){
                        dish.printDishInfo();   
                    }
                }
                System.out.println();
                break;
            case 2:
                System.out.println("-------------LAUNCH-------------");
                System.out.println();
                for(String dishCatgory: dishCategories.keySet()){
                    System.out.println("       -------" + dishCatgory + "-------");
                    for(Dish dish: launchMenu.get(dishCatgory)){
                        dish.printDishInfo();                    }
                }
                System.out.println();
                break;
            case 3:
                System.out.println("-------------DINNER-------------");
                System.out.println();
                for(String dishCatgory: dishCategories.keySet()){
                    System.out.println("       -------" + dishCatgory + "-------");
                    for(Dish dish: dinnerMenu.get(dishCatgory)){
                        dish.printDishInfo();                    }
                }
                System.out.println();
                break;

            default:
                break;
        }

    }

    public void printWholeDayMenu(){

        System.out.println("-------------BREAKFAST-------------");
        System.out.println();
        for(String dishCatgory: dishCategories.keySet()){
            System.out.println("       -------" + dishCatgory + "-------");
            for(Dish dish: breakfastMenu.get(dishCatgory)){
                dish.printDishInfo();            }
            
        }
        System.out.println();System.out.println();

        System.out.println("-------------LAUNCH-------------");
        System.out.println();
        for(String dishCatgory: dishCategories.keySet()){
            System.out.println("       -------" + dishCatgory + "-------");
            for(Dish dish: launchMenu.get(dishCatgory)){
                dish.printDishInfo();            }
        }
        System.out.println();System.out.println();

        System.out.println("-------------DINNER-------------");
        System.out.println();
        for(String dishCatgory: dishCategories.keySet()){
            System.out.println("       -------" + dishCatgory + "-------");
            for(Dish dish: dinnerMenu.get(dishCatgory)){
                dish.printDishInfo();            }
        }
        System.out.println();System.out.println();
    }



}
