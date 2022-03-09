package dev.antimoxs.hypixelapiHP.response;

import dev.antimoxs.hypixelapiHP.objects.friends.Friend;
import dev.antimoxs.hypixelapiHP.objects.friends.FriendList;
import dev.antimoxs.hypixelapiHP.requests.MojangRequest;
import dev.antimoxs.hypixelapiHP.exceptions.UnknownValueException;

public class FriendsResponse extends BaseResponse {

    private Friend[] records = null;

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

    public Friend getFriendByName(String name) throws dev.antimoxs.hypixelapiHP.exceptions.UnknownValueException {

        String uuidIn = MojangRequest.getUUID(name);
        return getFriendByUUID(uuidIn);

    }

    public Friend getFriendByUUID(String uuid) throws dev.antimoxs.hypixelapiHP.exceptions.UnknownValueException {

        if (this.uuid == null) throw new UnknownValueException("no uuid.");
        for (Friend f : records) {

            if (uuid.equals(f.getFriendUUID(this.uuid))) {

                return f;

            }

        }

        return new Friend();

    }

}
