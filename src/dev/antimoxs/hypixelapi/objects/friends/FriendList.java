package dev.antimoxs.hypixelapi.objects.friends;

import dev.antimoxs.hypixelapi.exceptions.UnknownValueException;
import dev.antimoxs.hypixelapi.requests.MojangRequest;

import java.util.ArrayList;

public class FriendList {

    String uuid = "";
    ArrayList<Friend> friends = new ArrayList<>();

    public FriendList() {}

    public void addFriend(Friend friend) {

        friends.add(friend);

    }

    public ArrayList<Friend> getFriends() {

        return friends;

    }

    public Friend getFriendByName(String name) throws UnknownValueException {

        String uuidIn = MojangRequest.getUUID(name);
        return getFriendByUUID(uuidIn);

    }

    public Friend getFriendByUUID(String uuid) throws UnknownValueException {

        if (this.uuid == null) throw new UnknownValueException("no uuid.");
        for (Friend f : friends) {

            if (uuid.equals(f.getFriendUUID(this.uuid))) {

                return f;

            }

        }

        return new Friend();

    }

}
