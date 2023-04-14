package PACKETS;


public class ServerAnswer extends Packet<Boolean> {

    private boolean serverAnswer;

    public ServerAnswer(boolean answer) {
        super("SERVER_ANSWER");
        this.serverAnswer = answer;        
    }

    @Override
    public Boolean getPacketData() {

       
        return this.serverAnswer;
    }

	

}
