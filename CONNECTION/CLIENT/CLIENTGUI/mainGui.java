package CONNECTION.CLIENT.CLIENTGUI;

import USER.*;
import MENU.Menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import CONNECTION.CLIENT.*;
import CONNECTION.SERVER.SERVERGUI.serverGui.OrderJPanel;

public class mainGui extends JFrame{

    private final static String dishCategories [] = {"MAIN_DISH", "GARNISH", "SALAD", "DESERT", "SPECIAL_MENU"};
    private ArrayList<JPanel> tabPanels;
    private HashMap<JButton, DishPanel> btnsToDishesList = new HashMap<>();
    private HashMap<String, ArrayList<Dish>> menuItems;
    private ArrayList<Dish> dishesForOrder;

    private JPanel contentPane;
    private JPanel headerPanel;
    private JTabbedPane TabPane;
    private JPanel mainPanel, basketPanel;

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
        
        this.dishesForOrder = new ArrayList<>();
        
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
        this.dishesForOrder = new ArrayList<>();
        
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        createHeaderPanel();
        createMainPanel();
        
        this.setContentPane(contentPane);


        // starting Gui
        // this.addMenuItems();

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
        // new BoxLayout(TabPane., BoxLayout.Y_AXIS);
        
        basketPanel.setBackground(Color.black);
        
        for(String dishC: new String[]{"Main Dishes", "Garnish", "Salad", "Desert", "Special Menu"}){
            JPanel tmpPanel = new JPanel();
            this.tabPanels.add(tmpPanel);
            TabPane.add(dishC, tmpPanel);
        }
            
        mainPanel.add(TabPane);
        mainPanel.add(Box.createRigidArea(new Dimension(-50, 0)));
        mainPanel.add(new Box.Filler(new Dimension(50, 0), new Dimension(50, 0), new Dimension(50, 0)));
        mainPanel.add(basketPanel);

        this.addMenuItems();

        JButton tmpB = new JButton("Submit");
        tmpB.addActionListener(new SubmitButton());
        basketPanel.add(tmpB);

        this.contentPane.add(mainPanel, BorderLayout.CENTER);

    }



    private void addMenuItems(){
        int counter = 0;
        for(String dishCategory: dishCategories){
            this.addDishesToPanel(menuItems.get(dishCategory), dishCategory, counter);
            counter++;
        }

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

            

            // TODO: create dish panel and add to corresponding Menu Category
            // need to add buttons 
            DishPanel dishPanel = new DishPanel(dish);

            // panelToAdd.setLayout(new BoxLayout(panelToAdd, BoxLayout.Y_AXIS));
            // panelToAdd.setLayout(new BoxLayout(panelToAdd, BoxLayout.PAGE_AXIS));

            panelToAdd.setBackground(Color.GREEN);

            // dishPanel.setPreferredSize(new Dimension(panelToAdd.getWidth() - 10, 100));
            // dishPanel.setSize(panelToAdd.getWidth(), 20);
            
            dishPanel.add(new JLabel(dish.name() + "               "));
            dishPanel.add(new JLabel(String.valueOf(dish.price())));

            JButton addButton = new JButton("+");
            addButton.addActionListener(new ButtonListener());
            dishPanel.add(addButton);
            
            this.btnsToDishesList.put(addButton, dishPanel);

          

            panelToAdd.add(dishPanel, gbc);
            // gbc.gridy += 1;
            
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

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DishPanel other = (DishPanel) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

        private mainGui getEnclosingInstance() {
            return mainGui.this;
        }

        

    }

    public class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            DishPanel tmpDishP = btnsToDishesList.get(e.getSource());
            if(dishesForOrder.contains(tmpDishP.getDish()))
                for(Dish dish: dishesForOrder){
                    if(dish.equals(tmpDishP.getDish())){
                        Dish mergedDish = new Dish(dish.name(), dish.price(), dish.quantity() + tmpDishP.getDish().quantity(), dish.dishCatagory());
                        dishesForOrder.remove(dish);
                        dishesForOrder.add(mergedDish);
                        break;
                    }
                }
            else
                dishesForOrder.add(tmpDishP.getDish());

        
        }

        
    }


    public class SubmitButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            

            // Get order from basket Panel and send it to the app 
            // Before sending start in a new thread the loading animation
            System.out.println(dishesForOrder);
            app.sendOrder(new Order(profile.getStudentId(), profile.getFree_meal_provision(), true, dishesForOrder));

            dishesForOrder.clear();
            
        }


    }

}