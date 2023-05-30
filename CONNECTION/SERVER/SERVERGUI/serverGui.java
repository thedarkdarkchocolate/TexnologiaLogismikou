package CONNECTION.SERVER.SERVERGUI;

import java.util.ArrayList;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import CONNECTION.SERVER.Server;
import USER.*;

// ServerGui --> Emfanizei tis eiserxomenes parageleies 
//               Uparxei input box gia eisagwgh tou pasou h studentId
//               afou o perastoun ta stoixeia tou foithth theloume na emfanizetai analutika h parageleia kai to poso plhrwmhs  
//               extra: na exoume kai sxolia parageleias 

public class serverGui extends JFrame{

    private Server server;
    // Button dictionaries with key(Button) and value the OrderJPanel class (that extends JPanel and hold the order) in the list that corresponds to that Button
    private HashMap<JButton, OrderJPanel> confirmationListButtons = new HashMap<>();
    private HashMap<JButton, OrderJPanel> onGoingListButtons = new HashMap<>();
    private HashMap<JButton, OrderJPanel> completedListButtons = new HashMap<>();

    private JFrame frame;
    private JPanel confirmedListPanel;
    private JPanel onGoingListPanel;
    private JPanel completedListPanel;

    private File soundFile;
    private AudioInputStream audioStream;
    private Clip clip;

    
    
    // Main for testing
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(serverGui::new);
    }

    // Constructor for testing with main
    public serverGui(){
        
        this.startServerGui();
        ArrayList<Dish> d = new ArrayList<>();
        // for(int i = 0; i < 10; i++)
        d.add(new Dish("fasolakia", 3, 2, "MAIN_DISH"));

        this.insertIncomingOrder(new Order("dai19159", false, false, d));
    }


    // Constructor called by server
    public serverGui(Server server){

        this.server = server;
        this.soundFile = new File("src/ASSETS/short-success-sound-glockenspiel-treasure-video-game-6346.wav");
        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            this.playSound();

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }



        this.startServerGui();
        
        
    }

    private void startServerGui(){

        frame = new JFrame("Bite-Byte-UoM");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create and add the header panel
        JPanel headerPanel = createHeaderPanel();
        frame.add(headerPanel, BorderLayout.NORTH);

        // Create and add the scroll panes for the three lists
        confirmedListPanel = createOrderList("Incoming Orders",300, 400);
        onGoingListPanel = createOrderList("Ongoing Orders",300, 400);
        completedListPanel = createOrderList("Completed Orders",300, 400);

        JPanel listsPanel = createListsPanel();
        frame.add(listsPanel, BorderLayout.CENTER);

        // Set the frame to be visible
        frame.setVisible(true);
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

    private JPanel createListsPanel() {

        JPanel listsPanel = new JPanel(new GridLayout(1, 3, 20, 10));
        listsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        listsPanel.add(new JScrollPane(confirmedListPanel));
        listsPanel.add(new JScrollPane(onGoingListPanel));
        listsPanel.add(new JScrollPane(completedListPanel));
        
        return listsPanel;
    }
    

    
    private JPanel createOrderList(String title, int width, int height) {
        
        JPanel panel = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        panel.setBorder(titledBorder);
        
        GridBagLayout gBagLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gBagLayout.setConstraints(panel, gbc);
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }


    // panelCode --> 1: confirmationPanel, 2: On-goingPanel, 3: completedOrderPanel
    private void addOrderToPanel(Order order, JPanel panel, int panelCode) {

        // Create a container panel
        OrderJPanel orderPanel = new OrderJPanel(order);
        orderPanel.setLayout(new GridBagLayout());

        Color backgroundColor = Color.gray;
        
        Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GREEN, Color.CYAN);
        orderPanel.setBorder(border);
        
        orderPanel.setBackground(backgroundColor);
        orderPanel.setToolTipText("dwefw");
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;

        // Add order details to the orderPanel
        JLabel studentIdLabel = new JLabel("Student ID: " + order.getStudentId());
        orderPanel.add(studentIdLabel, gbc);
    
        JLabel freeMealLabel = new JLabel("Free Meal Provision: " + order.hasFreeMealProvison());
        gbc.gridy += 1;
        orderPanel.add(freeMealLabel, gbc);
        
        JLabel takeawayLabel = new JLabel(order.isOrderTakeAway() ? "--TakeAway--" : "--Dine-In--");
        gbc.gridy += 1;
        orderPanel.add(takeawayLabel, gbc);
        
        
        JLabel dishesLabel = new JLabel("Dishes: ");
        gbc.gridy += 1;
        orderPanel.add(dishesLabel, gbc);
        
        ArrayList<Dish> dishes = order.getDishes();
        for (Dish dish : dishes) {
            JLabel dishLabel = new JLabel(" - " + dish.name() + " x" + dish.quantity());
            gbc.gridy += 1;
            orderPanel.add(dishLabel, gbc);
        }
        
        JLabel priceLabel = new JLabel("Total Price: " + order.getOrderTotalPrice() + "€");
        gbc.gridy += 1;
        orderPanel.add(priceLabel, gbc);
        
        gbc.gridy += 1;

        switch(panelCode){

            case 1:

                JButton tmpA = new JButton("Accept");
                JButton tmpD = new JButton("Decline");
                JPanel buttonsPanel = new JPanel();

                buttonsPanel.setBackground(backgroundColor);

                buttonsPanel.add(tmpA);
                buttonsPanel.add(tmpD);

                orderPanel.add(buttonsPanel);

                this.confirmationListButtons.put(tmpA, orderPanel);
                this.confirmationListButtons.put(tmpD, orderPanel);

                tmpA.addActionListener(new confirmationListButtons());
                tmpD.addActionListener(new confirmationListButtons());


                break;

            case 2:

                JButton tmpB = new JButton("Order Ready");
                orderPanel.add(tmpB);
                this.onGoingListButtons.put(tmpB, orderPanel);
                tmpB.addActionListener(new onGoingListButtons());

                break;

            case 3:

                JButton tmpC = new JButton("Picked-Up");
                orderPanel.add(tmpC);
                this.completedListButtons.put(tmpC, orderPanel);
                tmpC.addActionListener(new completedListButtons());

                break;

            default:
                System.out.println("SERVER_GUI: ADDING ORDER FAILED");

        }
        
        panel.add(orderPanel);
    }

    // Order should be added to the waitingConfirmationOrders dict and TODO: add order to the UI list waiting for requests
    // add new button to confirmationListButton
    public void insertIncomingOrder(Order order){

        int panelCode = 1;

        // Inserted Order to dict
        // this.waitingConfirmationOrders.put(order.getOrderID(), order);
        this.playSound();

        this.addOrderToPanel(order, this.confirmedListPanel, panelCode);
        // Refreshes GUI
        SwingUtilities.updateComponentTreeUI(frame);
    }

    void playSound() {
        clip.start();
    }

    public class confirmationListButtons implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            boolean accepted = ((AbstractButton) e.getSource()).getText().equals("Accept") ? true : false;

            int panelCode = 2;
            
            // Retriving order from dict
            Order tmpOrder = confirmationListButtons.get(e.getSource()).getOrder();

            if(accepted){
                

                // TODO: Send client confirmation resault

                // Removing JPanel from frame
                confirmedListPanel.remove(confirmationListButtons.get(e.getSource()));
                // Removing JButton and JPanel from dict
                confirmationListButtons.remove(e.getSource());

                addOrderToPanel(tmpOrder, onGoingListPanel, panelCode);

                // TODO: change status of order to on-Going

            }else{

                // TODO: Send client confirmation resault
                
                // Removing JPanel from frame
                confirmedListPanel.remove(confirmationListButtons.get(e.getSource()));
                // Removing JButton and JPanel from dict
                confirmationListButtons.remove(e.getSource());

                // TODO: change status of order to Declined
            }

            // Send Update To client
            server.sendOrderStatusUpdateToClient(tmpOrder.getStudentId(), accepted);
            // Update Order Status
            server.updateOrderStatus(tmpOrder.getOrderID(), accepted ? "ACCEPTED" : "DECLINED");
            // Refreshes GUI
            SwingUtilities.updateComponentTreeUI(frame);

        }

    }

    public class onGoingListButtons implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            int panelCode = 3;

            // Retriving order from dict
            Order tmpOrder = onGoingListButtons.get(e.getSource()).getOrder();

            // Removing JPanel from frame
            onGoingListPanel.remove(onGoingListButtons.get(e.getSource()));
            // Removing JButton and JPanel from dict
            onGoingListButtons.remove(e.getSource());

            addOrderToPanel(tmpOrder, completedListPanel, panelCode);
            
            // TODO: change status of order to ReadyToPickUp
            server.updateOrderStatus(tmpOrder.getOrderID(), "READY_FOR_PICK_UP");

            // Refreshes GUI
            SwingUtilities.updateComponentTreeUI(frame);

        }

    }
    
    public class completedListButtons implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            // Retriving order from dict
            Order tmpOrder = completedListButtons.get(e.getSource()).getOrder();
            
            // Removing JPanel from frame
            completedListPanel.remove(completedListButtons.get(e.getSource()));
            // Removing JButton and JPanel from dict
            completedListButtons.remove(e.getSource());

            server.updateOrderStatus(tmpOrder.getOrderID(), "ORDER_COMPLETED");

            SwingUtilities.updateComponentTreeUI(frame);
        }

    }


    public void close(){
        this.dispose();
    }
    
    public class OrderJPanel extends JPanel{

        private Order order;
    
        public OrderJPanel(Order order){
            super();
            this.order = order;
        }
    
        public Order getOrder(){
            return this.order;
        }
    
    
        
    }
    


}
