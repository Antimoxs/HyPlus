package dev.antimoxs.hypixelapiHP.objects.resource;

import dev.antimoxs.hypixelapiHP.util.hypixelFetcher;

import java.util.HashMap;

public class ResourceGame {

    private HashMap<String, String> modeNames = new HashMap<>();
    public boolean legacy = false;
    public boolean retired = false;
    public String databaseName = "";
    public String name = "";
    public int id = 0;

    public ResourceGame() {}
    public ResourceGame(String name) {

        this.name = name;
        this.databaseName = name.replaceAll(" ", "");

    }

    public HashMap<String, String> getModes() {

        if (modeNames.isEmpty()) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(databaseName.toLowerCase(), name);
            return map;

        }
        return modeNames;

    }

    public String fetchMode(String mode) {

        if (getModes().containsKey(mode.toUpperCase())) {

            return getModes().get(mode.toUpperCase());

        }
        else {

            for (String g : getModes().keySet()) {

                if (g.equalsIgnoreCase(mode)) return getModes().get(g);

            }

            return hypixelFetcher.fetchMode(mode);

        }


    }

}
