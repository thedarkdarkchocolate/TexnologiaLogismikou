package PACKETS;
import java.util.HashMap;

public class SignInPacket extends Packet<HashMap<String, String>>{
    
    private String studentId, password;

    public SignInPacket(String username, String password) {
        super("SIGNIN");
        this.studentId = username;
        this.password = password;
    }

    @Override
    public HashMap<String, String> getPacketData() {
        
        HashMap<String, String> dict = new HashMap<>();
        dict.put("username", studentId);
        dict.put("password", password);

        return dict;
    }

    public String getStudentId(){return this.studentId;}
    public String getPassword(){return this.password;}

   
    
}
