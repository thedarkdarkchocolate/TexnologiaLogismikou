package MENU;

import java.io.Serializable;

public class Menu implements Serializable{
    
    private int day;

    public Menu(int day, int timeOfday){       

    }

    public static class Dishes implements Serializable{


        private String dishName;

        public Dishes(){ // args: title, ingridients, price

        }

        public String getDishName(){
            return this.dishName;
        }

    }



}
