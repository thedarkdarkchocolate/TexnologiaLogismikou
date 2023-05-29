package CONNECTION.CLIENT.CLIENTGUI;

import USER.*;
import MENU.Menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import CONNECTION.CLIENT.*;

public class mainGui extends JFrame{

    private final static String dishCategories [] = {"MAIN_DISH", "GARNISH", "SALAD", "DESERT", "SPECIAL_MENU"};
    private HashMap<JButton, DishPanel> btnsToDishesList = new HashMap<>();
    private HashMap<String, ArrayList<Dish>> menuItems;

    private JPanel contentPane;
    private JPanel headerJPanel;
    private JTabbedPane TabPane;
    private JPanel mainPanel, menuPanel, basketPanel;
    private JPanel orderPanel;

    private App app;

    public static void  main(String args[]){
        SwingUtilities.invokeLater(mainGui::new);
    }


    public mainGui(Menu menu, Profile profile, App app){

        this.app = app;

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

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        headerJPanel = createHeaderPanel();
        contentPane.add(headerJPanel, BorderLayout.NORTH);
        createMainPanel();
        // createOrderPanel();
        this.setContentPane(contentPane);


        // starting Gui
        // this.addMenuItems();

    }

    private JPanel createHeaderPanel() {

        JPanel headerPanel = new JPanel(new BorderLayout());
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

        return headerPanel;
    }

    private void createMainPanel() {

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        // panel1.setBackground(Color.BLACK);
        // // Add the panels to the tabbed pane with titles
        // panel1.add(new JScrollPane());
        // panel2.add(new JScrollPane());
        // panel3.add(new JScrollPane());

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(contentPane.getWidth(), contentPane.getHeight()));
        mainPanel.setBackground(Color.BLUE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        basketPanel = new JPanel();
        TabPane = new JTabbedPane();
        
        // TabPane.setPreferredSize(new Dimension(mainPanel.getWidth()/3 * 2, 300));
        // basketPanel.setPreferredSize(new Dimension(mainPanel.getWidth()/3, 300));
        basketPanel.setBackground(Color.black);
        

        TabPane.addTab("Tab 1", panel1);
        TabPane.addTab("Tab 2", panel2);
        TabPane.addTab("Tab 3", panel3);

        mainPanel.add(TabPane);
        mainPanel.add(new Box.Filler(new Dimension(50, 0), new Dimension(50, 0), new Dimension(50, 0)));
        mainPanel.add(basketPanel);
        this.contentPane.add(mainPanel, BorderLayout.CENTER);

    }

    private void createOrderPanel() {
        orderPanel = new JPanel();
        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(orderPanel);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }


    private void addMenuItems(){

        for(String dishCategory: dishCategories){
            this.addDishesToPanel(menuItems.get(dishCategory), dishCategory, new JPanel());

        }

    }

    private void addDishesToPanel(ArrayList<Dish> dishes, String dishCategory, JPanel panelToAdd){

        for(Dish dish: dishes){

            // TODO: create dish panel and add to corresponding Menu Category
            // need to add buttons 

            DishPanel dishPanel = new DishPanel(dish);
            
            dishPanel.add(new JLabel(dish.name()));
            dishPanel.add(new JLabel(String.valueOf(dish.price())));

            JButton addButton = new JButton("+");
            addButton.addActionListener(new ButtonListener());
            dishPanel.add(addButton);
            
            this.btnsToDishesList.put(addButton, dishPanel);

            panelToAdd.add(dishPanel);
            
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

    public class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        }

        
    }


    public class SubmitButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            

            // Get order from basket Panel and send it to the app 
            // Before sending start in a new thread the loading animation
            // app.sendOrder(new Order())
            
        }


    }

}