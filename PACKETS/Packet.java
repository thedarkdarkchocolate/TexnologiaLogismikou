package PACKETS;

import java.io.Serializable;
import java.util.Arrays;

public abstract class Packet<T extends Serializable> implements Serializable {
    
    private String packetType;
    private final static String[] validTypes = new String[]{"SIGNIN", "SIGNUP", "SERVER_ANSWER", 
                                                            "ORDER", "PROFILE", "MENU", "REQUEST"};

    public Packet(String type){
        
        if (Arrays.stream(validTypes).anyMatch(type::equals))
            this.packetType = type;
        else
            try {
                throw new Exception("Invalid Packet Type");
            } catch (Exception e) {

                e.printStackTrace();

            }


    }


    public String getPacketType(){
        return this.packetType;
    }

    public abstract T getPacketData();

}