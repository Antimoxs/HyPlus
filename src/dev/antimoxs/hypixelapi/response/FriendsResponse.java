package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.exceptions.UnknownValueException;
import dev.antimoxs.hypixelapi.objects.friends.Friend;
import dev.antimoxs.hypixelapi.objects.friends.FriendList;
import dev.antimoxs.hypixelapi.requests.MojangRequest;

public class FriendsResponse {

    public boolean success = false;
    public String uuid = "No data for uuid";
    private Friend[] records = null;
    public String cause = "";

    public FriendsResponse() {



    }

    public Friend[] getFriends() {

        return records == null ? new Friend[0] : records;

    }

    public FriendList getAsFriendlist() {

        FriendList fl = new FriendList();
        for (Friend f : records) {

            fl.addFriend(f);

        }
        return fl;

    }

    public Friend getFriendByName(String name) throws UnknownValueException {

        String uuidIn = MojangRequest.getUUID(name);
        return getFriendByUUID(uuidIn);

    }

    public Friend getFriendByUUID(String uuid) throws UnknownValueException {

        if (this.uuid == null) throw new UnknownValueException("no uuid.");
        for (Friend f : records) {

            if (uuid.equals(f.getFriendUUID(this.uuid))) {

                return f;

            }

        }

        return new Friend();

    }

}
