package PACKETS;

import USER.Order;

public class OrderPacket extends Packet<Order>{
    
    private Order order;
    
    public OrderPacket(Order order){
        super("ORDER");
        this.order = order;

    }

    @Override
    public Order getPacketData() {
        
        return this.order;
    }

}
