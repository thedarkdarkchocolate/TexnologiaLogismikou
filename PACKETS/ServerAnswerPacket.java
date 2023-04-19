package PACKETS;


public class ServerAnswerPacket extends Packet<Boolean> {

    private boolean serverAnswer;

    public ServerAnswerPacket(boolean answer) {
        super("SERVER_ANSWER");
        this.serverAnswer = answer;        
    }

    @Override
    public Boolean getPacketData() {
        return this.serverAnswer;
    }

	

}
