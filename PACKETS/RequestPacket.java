package PACKETS;

public class RequestPacket extends Packet<String>{

    private String requestType;

    public RequestPacket(String type) {
        super("REQUEST");
        this.requestType = type;
    }

    @Override
    public String getPacketData() {
        return this.requestType;
    }
    
}
