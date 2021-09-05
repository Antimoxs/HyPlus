package dev.antimoxs.hyplus.modules.betterMsg;

public enum HyBetterMsgType {


    ARROW,
    SWITCH;

    public static HyBetterMsgType getByName(String name) {

        for (HyBetterMsgType type : HyBetterMsgType.values()) {

            if (type.name().equals(name.toUpperCase())) {

                return type;

            }

        }
        return SWITCH;

    }


}
