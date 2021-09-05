package dev.antimoxs.hypixelapi.response;


import dev.antimoxs.hypixelapi.objects.guild.Guild;

public class GuildResponse {

    public boolean success = false;
    private Guild guild = null;
    public String cause = "";

    public GuildResponse() {



    }

    public Guild getGuild() {

        return guild == null ? new Guild() : guild;

    }


}
