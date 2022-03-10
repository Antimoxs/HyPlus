package dev.antimoxs.hypixelapiHP.objects.resource;

import java.util.ArrayList;

public class ResourceQuests {

    public ResourceQuest[] quake = new ResourceQuest[0];
    public ResourceQuest[] walls = new ResourceQuest[0];
    public ResourceQuest[] paintball = new ResourceQuest[0];
    public ResourceQuest[] hungergames = new ResourceQuest[0];
    public ResourceQuest[] tntgames = new ResourceQuest[0];
    public ResourceQuest[] vampirez = new ResourceQuest[0];
    public ResourceQuest[] walls3 = new ResourceQuest[0];
    public ResourceQuest[] arcade = new ResourceQuest[0];
    public ResourceQuest[] arena = new ResourceQuest[0];
    public ResourceQuest[] uhc = new ResourceQuest[0];
    public ResourceQuest[] mcgo = new ResourceQuest[0];
    public ResourceQuest[] battleground = new ResourceQuest[0];
    public ResourceQuest[] supersmash = new ResourceQuest[0];
    public ResourceQuest[] gingerbread = new ResourceQuest[0];
    public ResourceQuest[] skywars = new ResourceQuest[0];
    public ResourceQuest[] truecombat = new ResourceQuest[0];
    public ResourceQuest[] skyclash = new ResourceQuest[0];
    public ResourceQuest[] prototype = new ResourceQuest[0];
    public ResourceQuest[] bedwars = new ResourceQuest[0];
    public ResourceQuest[] murdermystery = new ResourceQuest[0];
    public ResourceQuest[] buildbattle = new ResourceQuest[0];
    public ResourceQuest[] duels = new ResourceQuest[0];

    public ResourceQuest[] getQuestsForMode(String mode) {

        switch (mode) {

            case "quakecraft":
            case "quake": return quake;
            case "walls": return walls;
            case "paintball": return paintball;
            case "survival_games":
            case "hungergames": return hungergames;
            case "tntgames": return tntgames;
            case "vampirez": return vampirez;
            case "walls3": return walls3;
            case "arcade": return arcade;
            case "arena": return arena;
            case "speed_uhc":
            case "uhc": return uhc;
            case "cops_and_crims":
            case "mcgo": return mcgo;
            case "battleground": return battleground;
            case "super_smash":
            case "supersmash": return supersmash;
            case "gingerbread": return gingerbread;
            case "skywars": return skywars;
            case "crazy_walls":
            case "true_combat":
            case "truecombat": return truecombat;
            case "skyclash": return skyclash;
            case "pit":
            case "prototype": return prototype;
            case "bedwars": return bedwars;
            case "murder_mystery":
            case "murdermystery": return murdermystery;
            case "build_battle":
            case "buildbattle": return buildbattle;
            case "duels": return duels;
            default: return new ResourceQuest[0];

        }

    }

    public ArrayList<String> getAllQuestsIds() {

        ArrayList<String> re = new ArrayList<>();

        for (ResourceQuest q : quake) { re.add(q.id); }
        for (ResourceQuest q : walls) { re.add(q.id); }
        for (ResourceQuest q : paintball) { re.add(q.id); }
        for (ResourceQuest q : hungergames) { re.add(q.id); }
        for (ResourceQuest q : tntgames) { re.add(q.id); }
        for (ResourceQuest q : vampirez) { re.add(q.id); }
        for (ResourceQuest q : walls3) { re.add(q.id); }
        for (ResourceQuest q : arcade) { re.add(q.id); }
        for (ResourceQuest q : uhc) { re.add(q.id); }
        for (ResourceQuest q : mcgo) { re.add(q.id); }
        for (ResourceQuest q : battleground) { re.add(q.id); }
        for (ResourceQuest q : supersmash) { re.add(q.id); }
        for (ResourceQuest q : gingerbread) { re.add(q.id); }
        for (ResourceQuest q : skywars) { re.add(q.id); }
        for (ResourceQuest q : truecombat) { re.add(q.id); }
        for (ResourceQuest q : skyclash) { re.add(q.id); }
        for (ResourceQuest q : prototype) { re.add(q.id); }
        for (ResourceQuest q : bedwars) { re.add(q.id); }
        for (ResourceQuest q : murdermystery) { re.add(q.id); }
        for (ResourceQuest q : buildbattle) { re.add(q.id); }
        for (ResourceQuest q : duels) { re.add(q.id); }

        return re;

    }


}
