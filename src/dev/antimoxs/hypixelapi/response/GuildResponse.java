package dev.antimoxs.hypixelapi.response;


import dev.antimoxs.hypixelapi.objects.guild.Guild;

public class GuildResponse extends BaseResponse {

    private Guild guild = null;

    public GuildResponse() {



    }

    public Guild getGuild() {

        return guild == null ? new Guild() : guild;

    }


}
