package dev.antimoxs.hypixelapi.objects.resource;

import java.util.HashMap;

public class ResourceGame {

    private HashMap<String, String> modeNames = new HashMap<>();
    public boolean legacy = false;
    public boolean retired = false;
    public String databaseName = "";
    public String name = "";
    public int id = 0;

    public HashMap<String, String> getModes() {

        if (modeNames.isEmpty()) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(databaseName.toLowerCase(), name);
            return map;

        }
        return modeNames;

    }

}
