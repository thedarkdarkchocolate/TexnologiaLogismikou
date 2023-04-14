package PACKETS;
import java.util.HashMap;

public class SignInPacket extends Packet<HashMap<String, String>>{
    
    private String studentNumber, password;

    public SignInPacket(String username, String password) {
        super("SIGNIN");
        this.studentNumber = username;
        this.password = password;
    }

    @Override
    public HashMap<String, String> getPacketData() {
        
        HashMap<String, String> dict = new HashMap<>();
        dict.put("username", studentNumber);
        dict.put("password", password);

        return dict;
    }

   
    
}
