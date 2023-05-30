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
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import CONNECTION.CLIENT.*;

public class mainGui extends JFrame {

    private final static String dishCategories[] = { "MAIN_DISH", "GARNISH", "SALAD", "DESERT", "SPECIAL_MENU" };
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

    public static void main(String args[]) {
        SwingUtilities.invokeLater(mainGui::new);
    }

    public mainGui(Menu menu, Profile profile, App app) {

        this.app = app;
        this.profile = profile;

        // TODO: get current service menu
        menuItems = menu.getBreakfastMenu();

        this.dishesForOrder = new ArrayList<>();

        this.startMenuGui();

    }

    public mainGui() {
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

        // basketPanel.setBackground(Color.black);

        for (String dishC : new String[] { "Main Dishes", "Garnish", "Salad", "Desert", "Special Menu" }) {
            JPanel tmpPanel = new JPanel();
            this.tabPanels.add(tmpPanel);
            new BoxLayout(tmpPanel, BoxLayout.X_AXIS);
            TabPane.add(dishC, tmpPanel);
        }

        mainPanel.add(TabPane);
        // mainPanel.add(Box.createRigidArea(new Dimension(-100, 0)));
        // mainPanel.add(new Box.Filler(new Dimension(100, 0), new Dimension(100, 0),
        // new Dimension(100, 0)));
        basketPanel.add(Box.createRigidArea(new Dimension(-100, 0)));
        // TabPane.add(Box.createRigidArea(new Dimension(+100, 0)));
        mainPanel.add(new Box.Filler(new Dimension(100, 0), new Dimension(100, 0), new Dimension(100, 0)));
        createBasketTemplate();
        mainPanel.add(basketPanel);

        this.addMenuItems();

        this.contentPane.add(mainPanel, BorderLayout.CENTER);

    }

    private void createBasketTemplate() {
        this.basketPanel.setLayout(new BorderLayout());
        JPanel headerP = new JPanel();
        JPanel centralP = new JPanel();
        JPanel bottomP = new JPanel();
        JLabel basketLabel = new JLabel();
        JLabel infoLabel = new JLabel();
        JButton tmpB = new JButton("Submit");

        // headerP.setPreferredSize(new Dimension(this.basketPanel.getWidth(), 10));
        // this.basketPanel.add(centralP, BorderLayout.CENTER);
        // this.basketPanel.add(bottomP, BorderLayout.SOUTH);
        // header template
        headerP.setLayout(new BorderLayout());
        headerP.add(basketLabel, BorderLayout.CENTER);
        headerP.add(infoLabel, BorderLayout.SOUTH);
        JRadioButton r1 = new JRadioButton("A) Dine-in");
        JRadioButton r2 = new JRadioButton("B) Take-away");
        // r1.setBounds(75, 50, 100, 30);
        // r2.setBounds(75, 100, 100, 30);
        ButtonGroup bg = new ButtonGroup();
        bg.add(r1);
        bg.add(r2);
        headerP.add(r1, BorderLayout.WEST);
        headerP.add(r2, BorderLayout.WEST);
        // bottom template
        // tmpB.addActionListener(new SubmitButton());
        // bottomP.add(tmpB, BorderLayout.PAGE_END);
        this.basketPanel.add(headerP, BorderLayout.NORTH);
    }

    private void addMenuItems() {
        int counter = 0;
        for (String dishCategory : dishCategories) {
            this.addDishesToPanel(menuItems.get(dishCategory), dishCategory, counter);
            counter++;
        }

    }

    private void addDishesToPanel(ArrayList<Dish> dishes, String dishCategory, int c) {

        for (Dish dish : dishes) {

            // TODO: create dish panel and add to corresponding Menu Category
            // need to add buttons
            JPanel panelToAdd = this.tabPanels.get(c);

            new BoxLayout(panelToAdd, BoxLayout.Y_AXIS);

            panelToAdd.setBackground(Color.GREEN);

            DishPanel dishPanel = new DishPanel(dish);

            dishPanel.add(new JLabel(dish.name() + "               "));
            dishPanel.add(new JLabel(String.valueOf(dish.price())));

            JButton addButton = new JButton("+");
            addButton.addActionListener(new ButtonListener());
            dishPanel.add(addButton);

            this.btnsToDishesList.put(addButton, dishPanel);

            panelToAdd.add(dishPanel);

        }

    }

    public void close() {
        this.dispose();
    }

    public class DishPanel extends JPanel {

        private Dish dish;
        private String name;
        private float dishPrice;

        public DishPanel(Dish dish) {
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

    public class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            DishPanel tmpDishP = btnsToDishesList.get(e.getSource());
            for (Dish dish : dishesForOrder) {
                if (dish.equals(tmpDishP.getDish())) {
                    Dish mergedDish = new Dish(dish.name(), dish.price(),
                            dish.quantity() + tmpDishP.getDish().quantity(), getName());
                    dishesForOrder.remove(dish);
                    dishesForOrder.add(mergedDish);
                    break;
                }
            }

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