package dev.antimoxs.hypixelapiHP.objects.games;

import dev.antimoxs.hypixelapiHP.util.UnixConverter;

public class Game {

    public long date = 0L;
    public String gameType = "";
    public String mode = "";
    public String map = "";
    public long ended = 0L;

    public String getDateAsString() {

        return UnixConverter.toDate(date);

    }
    public String getEndedAsString() {

        return UnixConverter.toDate(ended);

    }
    public int getDurationSeconds() {

        return (int) (ended - date);

    }

}
