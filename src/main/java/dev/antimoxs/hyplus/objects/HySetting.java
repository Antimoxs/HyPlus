package dev.antimoxs.hyplus.objects;

import dev.antimoxs.hyplus.HyPlus;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class HySetting {

    private final HySettingType type;
    private final String configname;
    private final Object defaultValue;
    private final ControlElement.IconData icon;
    private final String displayName;
    private final String description;

    private Object value;

    public HySetting(HySettingType type, String configname, String displayName, String desc, Object value, Object defaultValue, String iconPath) {

        this.configname = configname;
        this.type = type;
        this.displayName = displayName;
        this.description = desc;
        this.value = value;
        this.defaultValue = defaultValue;
        this.icon = new ControlElement.IconData(iconPath);

    }

    public HySetting(HySettingType type, String configname, String displayName, String desc, Object value, Object defaultValue, Material material) {

        this.configname = configname;
        this.type = type;
        this.displayName = displayName;
        this.description = desc;
        this.value = value;
        this.defaultValue = defaultValue;
        this.icon = new ControlElement.IconData(material);

    }

    public void changeValue(Object value) {

        this.value = value;

    }

    public int getValueInt() {

        if (type == HySettingType.INT) return (int) value;
        return -1;

    }
    public String getValueString() {

        if (type == HySettingType.STRING) return (String) value;
        return value.toString();

    }
    public boolean getValueBoolean() {

        if (type == HySettingType.BOOLEAN) return (boolean) value;
        return false;

    }
    public char getValueChar() {

        if (type == HySettingType.CHAR) return (char) value;
        return 'x';

    }
    public Object getValue(HySettingType type) {

        if (this.type == type) {

            return value;

        }

        return new Object();

    }


    public void reset() {

        this.value = defaultValue;

    }

    public int getDefaultInt() {

        if (type == HySettingType.INT) return (int) defaultValue;
        return -1;

    }
    public String getDefaultString() {

        if (type == HySettingType.STRING) return (String) defaultValue;
        return defaultValue.toString();

    }
    public boolean getDefaultBoolean() {

        if (type == HySettingType.BOOLEAN) return (boolean) defaultValue;
        return false;

    }
    public char getDefaultChar() {

        if (type == HySettingType.CHAR) return (char) defaultValue;
        return 'x';

    }
    public Object getDefault(HySettingType type) {

        if (this.type == type) {

            return defaultValue;

        }

        return new Object();

    }

    public String getConfigName() {

        return this.configname;

    }
    public HySettingType getType() {

        return this.type;

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

    private void changeConfigValueO(HyPlus hyPlus, Object value) {

        hyPlus.hyConfigManager.changeConfigValue(this.configname, value);

    }
    public void changeConfigValue(HyPlus hyPlus, String value) { changeConfigValueO(hyPlus, value); }
    public void changeConfigValue(HyPlus hyPlus, int value) { changeConfigValueO(hyPlus, value); }
    public void changeConfigValue(HyPlus hyPlus, boolean value) { changeConfigValueO(hyPlus, value); }
    public void changeConfigValue(HyPlus hyPlus, char value) { changeConfigValueO(hyPlus, value); }

}
