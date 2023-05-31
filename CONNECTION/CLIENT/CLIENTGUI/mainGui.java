package CONNECTION.CLIENT.CLIENTGUI;

import USER.*;
import MENU.Menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import CONNECTION.CLIENT.*;
import CONNECTION.SERVER.SERVERGUI.serverGui.OrderJPanel;

public class mainGui extends JFrame {

    private final static String dishCategories [] = {"MAIN_DISH", "GARNISH", "SALAD", "DESERT", "SPECIAL_MENU"};
    private ArrayList<JPanel> tabPanels;
    private HashMap<JButton, DishPanel> menuButtonsToPanel = new HashMap<>();
    private HashMap<JButton, DishPanel> basketButtonsToPanel = new HashMap<>();
    private HashMap<String, ArrayList<Dish>> menuItems;
    private HashMap<String, Dish> dishesForOrder;

    private JPanel contentPane;
    private JPanel headerPanel;
    private JTabbedPane TabPane;
    private JPanel mainPanel;
    private JPanel basketPanel;
    private JPanel basketDishes;

    private App app;
    private Profile profile;

    public static void  main(String args[]){
        SwingUtilities.invokeLater(mainGui::new);
    }


    public mainGui(Menu menu, Profile profile, App app){

        this.app = app;
        this.profile = profile;
        
        // TODO: get current service menu
        menuItems = menu.getBreakfastMenu();
        
        this.startMenuGui();
        
        
    }

    public mainGui(){
        startMenuGui();
    }
    
    
    private void startMenuGui() {
        
        this.setTitle("Bite-Byte-UoM");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setVisible(true);

        // this.setLocationRelativeTo(true);
        this.dishesForOrder = new HashMap<String, Dish>();
        
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        createHeaderPanel();
        createMainPanel();
        
        this.setContentPane(contentPane);

    }

    private void createHeaderPanel() {

        this.headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add icon
        ImageIcon icon = new ImageIcon("src/ASSETS/logo-new.png");
        Image image = icon.getImage();
        JLabel iconLabel = new JLabel(new ImageIcon(image.getScaledInstance(100, -100, Image.SCALE_SMOOTH)));
        headerPanel.add(iconLabel, BorderLayout.WEST);

        // Add app name label
        JLabel appNameLabel = new JLabel("Bite-Byte-UoM");
        appNameLabel.setFont(appNameLabel.getFont().deriveFont(Font.BOLD, 18f));
        appNameLabel.setBorder(new EmptyBorder(0, 100, 0, 0));
        headerPanel.add(appNameLabel, BorderLayout.CENTER);

        this.contentPane.add(headerPanel, BorderLayout.NORTH);
    }


    private void createMainPanel() {

        this.tabPanels = new ArrayList<>();
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(contentPane.getWidth(), contentPane.getHeight()));
        mainPanel.setBackground(Color.BLUE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        basketPanel = new JPanel();
        TabPane = new JTabbedPane();
        // TabPane.setSize(new Dimension(this.mainPanel.getWidth()/3 * 2, this.mainPanel.getHeight()));
        // new BoxLayout(TabPane., BoxLayout.Y_AXIS);
        
        basketPanel.setBackground(Color.black);
        
        for(String dishC: new String[]{"Main Dishes", "Garnish", "Salad", "Desert", "Special Menu"}){
            JPanel tmpPanel = new JPanel();
            this.tabPanels.add(tmpPanel);
            TabPane.add(dishC, tmpPanel);
        }

        createBasketTemplate();
            
        mainPanel.add(TabPane);
        // mainPanel.add(Box.createRigidArea(new Dimension(-50, 0)));
        // mainPanel.add(new Box.Filler(new Dimension(50, 0), new Dimension(50, 0), new Dimension(50, 0)));
        mainPanel.add(basketPanel);

        this.addMenuItems();


        this.contentPane.add(mainPanel, BorderLayout.CENTER);

    }



    private void addMenuItems(){
        int counter = 0;
        for(String dishCategory: dishCategories){
            this.addDishesToPanel(menuItems.get(dishCategory), dishCategory, counter);
            counter++;
        }

    }

    private void createBasketTemplate() {

        this.basketPanel.setLayout(new BorderLayout());
        // this.basketPanel.setMaximumSize(new Dimension(this.mainPanel.getWidth()/3, this.mainPanel.getHeight()));
        this.basketPanel.setMaximumSize(new Dimension((int)this.getSize().getWidth()/3, 500));
        this.basketDishes = new JPanel();

        JPanel headerP = new JPanel();
        JPanel centralP = new JPanel();
        JPanel bottomP = new JPanel();
        JLabel basketLabel = new JLabel("Kalathi");
        JLabel infoLabel = new JLabel("Info:");
        JButton submitButton = new JButton("Submit");
        JLabel totalLabel = new JLabel("total");
        JLabel Dishes = new JLabel("Dishes");
        JPanel radioPanel = new JPanel((new FlowLayout(FlowLayout.LEFT, 2, 2)));
        // headerP.setPreferredSize(new Dimension(this.basketPanel.getWidth(), 10));
        // this.basketPanel.add(basketDishes, BorderLayout.CENTER);
        // this.basketPanel.add(bottomP, BorderLayout.SOUTH);
        
        // header template
        headerP.setLayout(new BorderLayout());
        headerP.add(basketLabel, BorderLayout.CENTER);
        headerP.add(infoLabel, BorderLayout.SOUTH);
        radioPanel.setPreferredSize(new Dimension(100, 40));
        radioPanel.add(new JRadioButton("A) Dine-in"));
        radioPanel.add(new JRadioButton("B) Take-away"));
        headerP.add(radioPanel, FlowLayout.RIGHT);

        // central template
        centralP.setBackground(Color.GRAY);
        centralP.setLayout(new BorderLayout());
        centralP.add(Dishes, BorderLayout.NORTH);
        basketDishes.setLayout(new BoxLayout(basketDishes, BoxLayout.Y_AXIS));
        basketDishes.setBackground(Color.PINK);
        centralP.add(basketDishes, BorderLayout.CENTER);
        this.basketDishes.setLayout(new BoxLayout(basketDishes, BoxLayout.Y_AXIS));

        // bottom template
        bottomP.setLayout(new BoxLayout(bottomP, BoxLayout.X_AXIS));
        bottomP.add(totalLabel);
        bottomP.add(Box.createRigidArea(new Dimension(330, 0)));
        submitButton.addActionListener(new SubmitButton());
        bottomP.add(submitButton);
        
        

        this.basketPanel.add(headerP, BorderLayout.NORTH);
        this.basketPanel.add(centralP, BorderLayout.CENTER);
        this.basketPanel.add(bottomP, BorderLayout.SOUTH);
    }

    private void addDishToBacket(Dish dish){

        // JPanel panelToAdd = new JPanel();

        // GridBagLayout gbl = new GridBagLayout();
        // panelToAdd.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 0;
        gbc.gridx = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;

        DishPanel dishPanel = new DishPanel(dish);
        dishPanel.setLayout(new GridBagLayout());

        dishPanel.setBackground(Color.CYAN);
        
        dishPanel.add(new JLabel(dish.name() + "               "));
        dishPanel.add(new JLabel(String.valueOf(dish.price())));

        JButton minusButton = new JButton("-");
        minusButton.addActionListener(new AddButtonListener());
        dishPanel.add(minusButton);
        
        this.basketButtonsToPanel.put(minusButton, dishPanel);

        this.basketDishes.add(dishPanel);

    }

    private void addDishesToPanel(ArrayList<Dish> dishes, String dishCategory, int c){

        JPanel panelToAdd = this.tabPanels.get(c);
        GridBagLayout gbl = new GridBagLayout();
        panelToAdd.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 0;
        gbc.gridx = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        
        for(Dish dish: dishes){

            DishPanel dishPanel = new DishPanel(dish);


            dishPanel.add(new JLabel(dish.name() + "               "));
            dishPanel.add(new JLabel(String.valueOf(dish.price())));

            JButton addButton = new JButton("+");
            addButton.addActionListener(new AddButtonListener());
            dishPanel.add(addButton);
            
            this.menuButtonsToPanel.put(addButton, dishPanel);

          

            panelToAdd.add(dishPanel, gbc);

            // Adding dish names into 
            this.dishesForOrder.put(dish.name(), null);
            
        }


    }
    

    

    public class AddButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            Dish tmpDish = menuButtonsToPanel.get(e.getSource()).getDish();

            if(dishesForOrder.get(tmpDish.name()) == null){
                dishesForOrder.put(tmpDish.name(), tmpDish);
                addDishToBacket(tmpDish);
            }
            else {

                Dish dishToAdd = new Dish(tmpDish.name(), tmpDish.price(),
                                            tmpDish.quantity() + dishesForOrder.get(tmpDish.name()).quantity(),
                                            tmpDish.dishCatagory());

                dishesForOrder.put(tmpDish.name(), dishToAdd);
                addDishToBacket(dishToAdd);
            }
            
            SwingUtilities.updateComponentTreeUI(contentPane);
        }

    }


    public class SubmitButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            

            // Get order from basket Panel and send it to the app 
            // Before sending start in a new thread the loading animation
            System.out.println(dishesForOrder);
            ArrayList<Dish> dishes = new ArrayList<>();

            for(Dish dish: dishesForOrder.values())
                if (dish == null) 
                    continue;
                else
                    dishes.add(dish);
                
                
            app.sendOrder(new Order(profile.getStudentId(), profile.getFree_meal_provision(), true, dishes));

            dishesForOrder.clear();
            
        }


    }


    public void close(){
        this.dispose();
    }

    public class DishPanel extends JPanel{

        private Dish dish;
        private String name;
        private float dishPrice;

        public DishPanel(Dish dish){
            super();
            this.dish = dish;
            this.name = dish.name();
            this.dishPrice = dish.getTotalPrice();

        }

        public float getDishPrice() {
            return dishPrice;
        }

        public String getName() {
            return name;
        }

        public Dish getDish() {
            return dish;
        }


    }

}