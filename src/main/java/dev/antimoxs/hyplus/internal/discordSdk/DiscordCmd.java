package dev.antimoxs.hyplus.internal.discordSdk;

public class DiscordCmd {

    public String cmd;
    public Object args;

    public DiscordCmd(){};

    public DiscordCmd(String cmd, Object args) {

        this.cmd = cmd;
        this.args = args;

    }

}
