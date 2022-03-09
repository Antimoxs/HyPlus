package dev.antimoxs.hyplus.objects;

public class HyGameStatus {

    public State state = State.UNDEFINED;
    public long endingTimestamp = 0L;
    public long startingTimestamp = 0L;

    public enum State {

        UNDEFINED("Undefined"),
        LOBBY("Lobby"),
        PREGAME("Pre-Game"),
        INGAME("Playing"),
        IDLING("Idling");

        public String name;

        State(String name) {

            this.name = name;

        }

    }

    @Override
    public String toString() {

        return state.name + "/" + ((int)endingTimestamp/10000) + "/" + ((int)startingTimestamp/10000);

    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof HyGameStatus) {

            return obj.toString().equals(this.toString());

        }
        return false;

    }
}
