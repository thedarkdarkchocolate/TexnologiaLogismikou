package PACKETS;

import MENU.Menu;

public class MenuPacket extends Packet<Menu> {

    private Menu todaysMenu;

    public MenuPacket(Menu menu) {
        super("MENU");
        this.todaysMenu = menu;
    }

    @Override
    public Menu getPacketData() {
        return this.todaysMenu;        
    }
    
}
