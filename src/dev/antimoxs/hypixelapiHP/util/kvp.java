package dev.antimoxs.hypixelapiHP.util;


public class kvp implements Comparable<kvp> {

    public String string;
    public int i;

    public kvp(String s, Integer i) {

        this.string = s;
        this.i = i;

    }

    @Override
    public int compareTo(kvp o) {
        return (int)this.string.length() - o.string.length();
    }
}
