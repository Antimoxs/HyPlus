package dev.antimoxs.hypixelapiHP.objects.friends;

import dev.antimoxs.hypixelapiHP.exceptions.UnknownValueException;

public class Friend {

    public String _id = "No data for id";
    public String uuidSender = "No data for sender";
    public String uuidReceiver = "No data for receiver";
    public Long started = 0L;

    public Friend() {}

    public String getFriendUUID(String myUUID) throws dev.antimoxs.hypixelapiHP.exceptions.UnknownValueException {

        if (myUUID.equals(uuidSender)) {

            return uuidReceiver;

        }
        else if (myUUID.equals(uuidReceiver)) {

            return uuidSender;

        }
        else {

            throw new UnknownValueException("myUUID matches neither sender nor receiver.");

        }

    }

}
