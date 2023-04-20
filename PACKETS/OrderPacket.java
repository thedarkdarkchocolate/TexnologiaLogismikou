package PACKETS;

import USER.Order;

public class OrderPacket extends Packet<Order>{
    
    private Order order;
    
    public OrderPacket(Order order){ // args: students_id, dishes...
        super("CLIENT_ORDER");
        this.order = order;

    }

    @Override
    public Order getPacketData() {
        
        return this.order;
    }

}
