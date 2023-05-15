package PACKETS;

import java.util.HashMap;

public class SignUpPacket extends Packet<HashMap<String, String>> {

    private String studentId, password, email, firstName, lastName;

    public SignUpPacket(String studentId, String password, String email, String firstName, String lastName){
        super("SIGNUP");
        this.studentId = studentId;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public HashMap<String, String> getPacketData() {
        
        HashMap<String, String> dict = new HashMap<>();
        dict.put("username", this.studentId);
        dict.put("password", this.password);
        dict.put("email", this.email);
        dict.put("firstName", this.firstName);
        dict.put("lastName", this.lastName);

        return dict;
    }
    
}
