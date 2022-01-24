package dev.antimoxs.hypixelapi.util;

import org.jetbrains.annotations.NotNull;

public class kvp implements Comparable<kvp> {

    public String string;
    public int i;

    public kvp(String s, Integer i) {

        this.string = s;
        this.i = i;

    }

    @Override
    public int compareTo(@NotNull kvp o) {
        return (int)this.string.length() - o.string.length();
    }
}
