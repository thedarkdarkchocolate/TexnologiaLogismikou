package CONNECTION.CLIENT.CLIENTGUI;

import USER.*;
import MENU.Menu;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import CONNECTION.CLIENT.*;

public class mainGui extends JFrame{

    private final static String dishCategories [] = {"MAIN_DISH", "GARNISH", "SALAD", "DESERT", "SPECIAL_MENU"};
    private HashMap<String, ArrayList<String>> menuItems;


    public mainGui(Menu menu, Profile profile, App app){

        // TODO: get current service menu
        menuItems = menu.getBreakfastMenu();

        this.startMenuGui();


    }


    private void startMenuGui() {

        // starting

        this.addMenuItems();

    }


    private void addMenuItems(){

        for(String dishCategory: dishCategories){
            this.addDishesToPanel(menuItems.get(dishCategory), dishCategory);

        }


    }

    private void addDishesToPanel(ArrayList<String> dishes, String dishCategory){

        for(String dish: dishes){

            // TODO: create dish panel and add to corresponding Menu Category
            // need to add buttons 

            
            

        }


    }


    public void close(){
        this.dispose();
    }
}