package PACKETS;

import USER.Profile;

public class ProfilePacket extends Packet<Profile> {

    private Profile profile;

    public ProfilePacket(Profile profile) {
        super("PROFILE");
        this.profile = profile;
    }

    @Override
    public Profile getPacketData() {
       return this.profile;
    }
    
}
