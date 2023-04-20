package PACKETS;

import java.util.HashMap;

public class SignUpPacket extends Packet<HashMap<String, String>> {

    private String studentId, password, email;

    public SignUpPacket(String studentId, String password, String email){
        super("SIGNUP");
        this.studentId = studentId;
        this.password = password;
        this.email = email;
    }

    @Override
    public HashMap<String, String> getPacketData() {
        
        HashMap<String, String> dict = new HashMap<>();
        dict.put("username", this.studentId);
        dict.put("password", this.password);
        dict.put("email", this.email);

        return dict;
    }
    
}
