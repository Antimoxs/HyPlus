package dev.antimoxs.hypixelapiHP.response;


import dev.antimoxs.hypixelapiHP.objects.guild.Guild;

public class GuildResponse extends BaseResponse {

    private dev.antimoxs.hypixelapiHP.objects.guild.Guild guild = null;

    public GuildResponse() {



    }

    public dev.antimoxs.hypixelapiHP.objects.guild.Guild getGuild() {

        return guild == null ? new Guild() : guild;

    }


}
