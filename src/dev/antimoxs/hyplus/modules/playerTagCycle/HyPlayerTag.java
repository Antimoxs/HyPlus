package dev.antimoxs.hyplus.modules.playerTagCycle;

import dev.antimoxs.hypixelapi.util.kvp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class HyPlayerTag {

    UUID uuid;
    ArrayList<kvp> values = new ArrayList<>();
    double size = 1.0d;
    int maxValues = 0;
    int currentValue = 0;

    public HyPlayerTag(UUID uuid,  double size, kvp... value) {

        this.uuid = uuid;

        values.addAll(Arrays.asList(value));

        this.size = size;
        this.maxValues = values.size();

    }

    public UUID getUUID() {

        return uuid;

    }

    public ArrayList<kvp> getValues() {

        return values;

    }

    public double getSize() {

        return size;

    }

    public void setSize(double size) {

        this.size = size;

    }

    public void addValue(kvp value) {

        for (kvp k : values) {

            if (k.i == value.i) {

                updateValue(value);
                return;

            }

        }
        values.add(value);
        maxValues = values.size();

    }

    public void updateValue(kvp value) {

        for (kvp k : values) {

            if (k.i == value.i) {

                values.remove(k);
                values.add(value);

            }

        }

    }

    public void setValue(kvp value) {

        removeValue(value);
        addValue(value);

    }

    public void removeValue(kvp value) {

        if (values.isEmpty()) return;

        values.remove(value);
        maxValues = values.size();

    }

    public void removeValueById(int i) {

        if (values.isEmpty()) return;

        values.removeIf(k -> k.i == i);
        maxValues = values.size();


    }

    public String getNextValue() {

        if (values.isEmpty()) return null;

        if (currentValue >= maxValues) {

            currentValue = 0;

        }

        //System.out.println("PlayerTag: " + currentValue + " / " + maxValues);
        String re = values.get(currentValue).string;

        currentValue++;

        return re;

    }

    public String getStaticValue() {

        if (values.isEmpty()) return null;

        return values.get(0).string;

    }

    public void clearValues() {

        if (!values.isEmpty()) {

            values.clear();
            maxValues = 0;

        }

    }

}
