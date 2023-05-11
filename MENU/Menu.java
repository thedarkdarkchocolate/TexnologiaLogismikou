package MENU;

import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;


public class Menu implements Serializable{
    
    private HashMap<String, ArrayList<String>> breakfastMenu;
    private HashMap<String, ArrayList<String>> launchMenu;
    private HashMap<String, ArrayList<String>> dinnerMenu;

    private final static String dishCategories [] = {"MAIN_DISH", "GARNISH", "SALAD", "DESERT", "SPECIAL_MENU"};
    
    public Menu(){

        this.breakfastMenu = new HashMap<String, ArrayList<String>>();
        this.launchMenu = new HashMap<String, ArrayList<String>>();
        this.dinnerMenu = new HashMap<String, ArrayList<String>>();

        for(String dishCat: dishCategories){
            this.breakfastMenu.put(dishCat, new ArrayList<String>());
            this.launchMenu.put(dishCat, new ArrayList<String>());
            this.dinnerMenu.put(dishCat, new ArrayList<String>());
        }

    }

    public void insertBreakfastDish(String category, String dish){

        if (Arrays.stream(dishCategories).anyMatch(category::equals)){
            this.breakfastMenu.get(category).add(dish);
        }

    }

    public void insertLaunchDish(String category, String dish){

        if (Arrays.stream(dishCategories).anyMatch(category::equals)){
            this.launchMenu.get(category).add(dish);
        }

    }

    public void insertDinnerDish(String category, String dish){

        if (Arrays.stream(dishCategories).anyMatch(category::equals)){
            this.dinnerMenu.get(category).add(dish);
        }

    }
    
    public static String[] getDishCategories(){
        return dishCategories;
    }

    public HashMap<String, ArrayList<String>> getbreakfastMenu(){
        return breakfastMenu;
    }

    public HashMap<String, ArrayList<String>> getlaunchMenu(){
        return launchMenu;
    }

    public HashMap<String, ArrayList<String>> getdinnerMenu(){
        return dinnerMenu;
    }

    public void printMenuByTimeOfService(int time){

        switch(time){

            case 1:
                System.out.println("-------------BREAKFAST-------------");
                System.out.println();
                for(String dishCatgory: dishCategories){
                    System.out.println("       -------" + dishCatgory + "-------");
                    for(String dish: breakfastMenu.get(dishCatgory)){
                        System.out.println("              " + dish);   
                    }
                }
                System.out.println();
                break;
            case 2:
                System.out.println("-------------LAUNCH-------------");
                System.out.println();
                for(String dishCatgory: dishCategories){
                    System.out.println("       -------" + dishCatgory + "-------");                    for(String dish: launchMenu.get(dishCatgory)){
                        System.out.println("           " + dish);   
                    }
                }
                System.out.println();
                break;
            case 3:
                System.out.println("-------------DINNER-------------");
                System.out.println();
                for(String dishCatgory: dishCategories){
                    System.out.println("       -------" + dishCatgory + "-------");                    for(String dish: dinnerMenu.get(dishCatgory)){
                        System.out.println("           " + dish);   
                    }
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
        for(String dishCatgory: dishCategories){
            System.out.println("       -------" + dishCatgory + "-------");
            for(String dish: breakfastMenu.get(dishCatgory)){
                System.out.println("           " + dish);   
            }
            
        }
        System.out.println();System.out.println();

        System.out.println("-------------LAUNCH-------------");
        System.out.println();
        for(String dishCatgory: dishCategories){
            System.out.println("       -------" + dishCatgory + "-------");
            for(String dish: launchMenu.get(dishCatgory)){
                System.out.println("           " + dish);   
            }
        }
        System.out.println();System.out.println();

        System.out.println("-------------DINNER-------------");
        System.out.println();
        for(String dishCatgory: dishCategories){
            System.out.println("       -------" + dishCatgory + "-------");
            for(String dish: dinnerMenu.get(dishCatgory)){
                System.out.println("           " + dish);   
            }
        }
        System.out.println();System.out.println();
    }



}
