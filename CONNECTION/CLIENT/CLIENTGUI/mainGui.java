package CONNECTION.CLIENT.CLIENTGUI;

import USER.*;
import MENU.Menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
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
    private JLabel totalPrice;
    private ButtonGroup buttonsGroup;

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
        this.setSize(1000, 700);
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
        JLabel infoLabel = new JLabel("StudentID: " + this.profile.getStudentId() + (this.profile.getFree_meal_provision() ? "\n, Has Free Meal Provision, " : "\n, Doesn't Free Meal Provision, ")
                                         + "Email: " + this.profile.getEmail());
        JButton submitButton = new JButton("Submit");
        this.totalPrice = new JLabel("Total: 0.0€");
        JLabel Dishes = new JLabel("Dishes:");
        JPanel radioPanel = new JPanel((new FlowLayout(FlowLayout.LEFT, 2, 2)));
        // headerP.setPreferredSize(new Dimension(this.basketPanel.getWidth(), 10));
        // this.basketPanel.add(basketDishes, BorderLayout.CENTER);
        // this.basketPanel.add(bottomP, BorderLayout.SOUTH);
        
        // header template
        headerP.setLayout(new BorderLayout());
        headerP.add(basketLabel, BorderLayout.CENTER);
        headerP.add(infoLabel, BorderLayout.SOUTH);
        radioPanel.setPreferredSize(new Dimension(100, 40));
        buttonsGroup = new ButtonGroup();
        JRadioButton takeAway = new JRadioButton("Take-away");
        takeAway.setActionCommand("Take-Away");
        JRadioButton dineIn = new JRadioButton("Dine-In");
        dineIn.setActionCommand("Dine-In");
        buttonsGroup.add(takeAway);
        buttonsGroup.add(dineIn);
        radioPanel.add(takeAway);
        radioPanel.add(dineIn);
        headerP.add(radioPanel, FlowLayout.RIGHT);

        // central template
        centralP.setBackground(Color.GRAY);
        centralP.setLayout(new BorderLayout());
        centralP.add(Dishes, BorderLayout.NORTH);
        new JScrollPane(basketDishes);
        basketDishes.setLayout(new BoxLayout(basketDishes, BoxLayout.Y_AXIS));
        basketDishes.setBackground(Color.PINK);
        centralP.add(basketDishes, BorderLayout.CENTER);
        this.basketDishes.setLayout(new BoxLayout(basketDishes, BoxLayout.Y_AXIS));

        // bottom template
        bottomP.setLayout(new BoxLayout(bottomP, BoxLayout.X_AXIS));
        bottomP.add(totalPrice);
        bottomP.add(Box.createRigidArea(new Dimension(330, 0)));
        submitButton.addActionListener(new SubmitButton());
        bottomP.add(submitButton);
        
        

        this.basketPanel.add(headerP, BorderLayout.NORTH);
        this.basketPanel.add(centralP, BorderLayout.CENTER);
        this.basketPanel.add(bottomP, BorderLayout.SOUTH);
    }

    private void addDishToBacket(Dish dish){

        JPanel panelToAdd = new JPanel();

        GridBagLayout gbl = new GridBagLayout();
        panelToAdd.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 0;
        gbc.gridx = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;

        DishPanel dishPanel = new DishPanel(dish);
        dishPanel.setLayout(new GridBagLayout());
        // dishPanel.setLayout(new BorderLayout());

        dishPanel.setBackground(Color.CYAN);
        
        dishPanel.add(new JLabel(dish.name() + "     x" + dish.quantity()));
        dishPanel.add(new JLabel("Price: " + String.valueOf(dish.price()) + "€"));
        
        JButton minusButton = new JButton("-");
        minusButton.addActionListener(new MinusButtonListener());
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


            dishPanel.add(new JLabel(dish.name() + "               "), BorderLayout.NORTH);
            dishPanel.add(new JLabel("Price: " + String.valueOf(dish.price()) + "€"), BorderLayout.SOUTH);

            JButton addButton = new JButton("+");
            addButton.addActionListener(new AddButtonListener());
            dishPanel.add(addButton, BorderLayout.WEST);
            
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

                for(Component dishPanel: basketDishes.getComponents())
                    if(dishToAdd.name().equals(((DishPanel)dishPanel).getName()))
                        basketDishes.remove((DishPanel)dishPanel);       

                addDishToBacket(dishToAdd);
            }

            Order tmpOrder = new Order(profile.getStudentId(), profile.getFree_meal_provision(), true, new ArrayList<Dish>(dishesForOrder.values()));

            // TODO: Change takeAway to getButtonChoice
            totalPrice.setText("Total: " + String.valueOf(tmpOrder.getOrderTotalPrice()) + "€");
            
            
            SwingUtilities.updateComponentTreeUI(contentPane);
        }

    }

    public class MinusButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            DishPanel tmpDishP = basketButtonsToPanel.get(e.getSource());

            if(tmpDishP.getDish().quantity() == 1){
                basketDishes.remove(tmpDishP);
                dishesForOrder.remove(tmpDishP.getName());
            }
            else {

                Dish dishToAdd = new Dish(tmpDishP.getDish().name(), tmpDishP.getDish().price(),
                                            dishesForOrder.get(tmpDishP.getDish().name()).quantity() - 1,
                                            tmpDishP.getDish().dishCatagory());

                dishesForOrder.put(tmpDishP.getDish().name(), dishToAdd);

                for(Component dishPanel: basketDishes.getComponents())
                    if(dishToAdd.name().equals(((DishPanel)dishPanel).getName()))
                        basketDishes.remove((DishPanel)dishPanel);       

                addDishToBacket(dishToAdd);
            }

            Order tmpOrder = new Order(profile.getStudentId(), profile.getFree_meal_provision(), true, new ArrayList<Dish>(dishesForOrder.values()));

            // TODO: Change takeAway to getButtonChoice
            totalPrice.setText("Total: " + String.valueOf(tmpOrder.getOrderTotalPrice()) + "€");


            SwingUtilities.updateComponentTreeUI(contentPane);
        }

    }


    public class SubmitButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            ArrayList<Dish> dishes = new ArrayList<>();
            
            // Inserting dishes for order in ArrayList
            for(Dish dish: dishesForOrder.values())
                if (dish == null) 
                    continue;
                else
                    dishes.add(dish);
            
            if(!dishes.isEmpty()){

                if(!(buttonsGroup.getSelection() == null)){
                    // Removing dishPanels from basket
                    for(Component dishPanel: basketDishes.getComponents())
                        basketDishes.remove((DishPanel)dishPanel);  
                        
                    JOptionPane.showMessageDialog(contentPane, app.sendOrder(new Order(profile.getStudentId(), profile.getFree_meal_provision(),
                                                    (buttonsGroup.getSelection()).getActionCommand().equals("Take-Away") ? true : false, dishes)) ? "Order Accepted ! Your order should be ready in a bit. " : "Your order has been declined. ");

                    SwingUtilities.updateComponentTreeUI(contentPane);

                    dishesForOrder.clear();
                }
                else
                    JOptionPane.showMessageDialog(contentPane, "Select TakeAway or Dine-In to be able to complete your order ! ");
            }
            else
                JOptionPane.showMessageDialog(contentPane, "Select an item to be able to submit your order ! ");
                // System.out.println("Select an item to be able to submit you order ! ");



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