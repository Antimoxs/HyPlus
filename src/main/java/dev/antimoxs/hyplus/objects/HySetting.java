package dev.antimoxs.hyplus.objects;

import dev.antimoxs.hyplus.HyPlus;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class HySetting<T> {

    private final String configname;
    private final T defaultValue;
    private final ControlElement.IconData icon;
    private final String displayName;
    private final String description;

    private T value;

    public HySetting(String configname, String displayName, String desc, T defaultValue, String iconPath) {

        this.configname = configname;
        this.displayName = displayName;
        this.description = desc;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.icon = new ControlElement.IconData(iconPath);

    }

    public HySetting(String configname, String displayName, String desc, T defaultValue, Material material) {

        this.configname = configname;
        this.displayName = displayName;
        this.description = desc;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.icon = new ControlElement.IconData(material);

    }

    public void changeValue(T value) {

        this.value = value;

    }

    public T getValue() {

        return this.value;

    }


    public void reset() {

        this.value = defaultValue;

    }

    public T getDefault() {

        return this.defaultValue;

    }


    public String getConfigName() {

        return this.configname;

    }

    public ControlElement.IconData getIcon() {

        return this.icon;

    }
    public String getDisplayName() {

        return this.displayName;

    }
    public String getDescription() {

        return this.description;

    }

    public HySettingType getType() {

        Class<?> clazz = this.defaultValue.getClass();
        if (clazz.equals(String.class)) return HySettingType.STRING;
        if (clazz.equals(Boolean.class)) return HySettingType.BOOLEAN;
        if (clazz.equals(Integer.class)) return HySettingType.INT;
        if (clazz.equals(Character.class)) return HySettingType.STRING;
        return HySettingType.OTHER;

    }

    private void changeConfigValueO(HyPlus hyPlus, Object value) {

        hyPlus.hyConfigManager.changeConfigValue(this.configname, value);

    }
    public void changeConfigValue(HyPlus hyPlus, String value) { changeConfigValueO(hyPlus, value); }
    public void changeConfigValue(HyPlus hyPlus, int value) { changeConfigValueO(hyPlus, value); }
    public void changeConfigValue(HyPlus hyPlus, boolean value) { changeConfigValueO(hyPlus, value); }
    public void changeConfigValue(HyPlus hyPlus, char value) { changeConfigValueO(hyPlus, value); }

}
