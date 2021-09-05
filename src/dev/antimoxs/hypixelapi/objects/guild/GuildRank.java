package dev.antimoxs.hypixelapi.objects.guild;

public class GuildRank {

    private String name = "No data for 'rankName'";
    private Boolean isDefault = false;
    private String tag = "No data for 'rankTag'";
    private Long created = 0L;
    private Integer priority = -1;

    public GuildRank() {



    }

    public String getName()         { return this.name; }
    public String getTag()          { return this.tag; }
    public Boolean isDefault()      { return this.isDefault; }
    public Long getCreation()       { return this.created; }
    public Integer getPriority()    { return this.priority; }

}
