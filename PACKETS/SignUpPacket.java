package PACKETS;

import java.util.HashMap;

public class SignUpPacket extends Packet<HashMap<String, String>> {

    private String studentNumber, password, email;

    public SignUpPacket(String studentNumber, String password, String email){
        super("SIGNUP");
        this.studentNumber = studentNumber;
        this.password = password;
        this.email = email;
    }

    @Override
    public HashMap<String, String> getPacketData() {
        
        HashMap<String, String> dict = new HashMap<>();
        dict.put("username", this.studentNumber);
        dict.put("password", this.password);
        dict.put("email", this.email);

        return dict;
    }
    
}
