package dev.antimoxs.hyplus.modules.quests;

import dev.antimoxs.hyplus.HyPlus;
import net.labymod.ingamegui.Module;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleConfigElement;
import net.labymod.ingamegui.ModuleGui;
import net.labymod.ingamegui.enums.EnumDisplayType;
import net.labymod.ingamegui.enums.EnumModuleRegion;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;

import java.util.*;

public class HyTrackboxGUI extends Module {

    HyQuestTracker tracker;
    boolean lastSort;

    public HyTrackboxGUI() {

        this.tracker = HyPlus.getInstance().hyQuestTracker;
        this.lastSort = tracker.HYPLUS_CTR_SORTORDER.getDefaultBoolean();

    }

    @Override
    public void draw(double x, double y, double rightX) {

        /*
        int index = 1;
        for (HyTrackboxBox box : challenges) {

            HyTrackboxBox draw = new HyTrackboxBox(1,(int)x,(int)y,256,"Kill 5 players");
            draw.drawButton(Minecraft.getMinecraft(), 0,0);

        }*/

        double posX = x;

        if (this.lastSort != tracker.HYPLUS_CTR_SORTORDER.getValueBoolean()) {

            tracker.updateChallanges(HyPlus.getInstance().hyLocationDetector.getCurrentLocation().rawloc.toLowerCase());
            this.lastSort = tracker.HYPLUS_CTR_SORTORDER.getValueBoolean();
            return;

        }


        if (tracker.active && HyPlus.getInstance().hypixel.checkOnServer()) {

            try {

                int count = 0;
                if (HyPlus.getInstance().hyQuestTracker.dspc2.isEmpty()) return;
                for (QuestData data : HyPlus.getInstance().hyQuestTracker.dspc2.values()) {

                    int boxW = tracker.HYPLUS_CTR_SORTORDER.getValueBoolean() ? -data.getL(data.percent == 100) : data.getL(data.percent == 100);
                    if (this.isLastRightBound()) posX = rightX - boxW;

                    if (data.id.equals("empty")) {

                        String text = "No quests available.";
                        int len = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 10;
                        HyTrackboxBox draw = new HyTrackboxBox(1, (int) posX, (int) y + count, len, text);
                        //HyPlus.displayIgMessage("trackbox", k.i + "");
                        draw.drawButton(Minecraft.getMinecraft(), 0, 0, 100);
                        count = count + 20;

                    }
                    else {

                        HyTrackboxBox draw = new HyTrackboxBox(1, (int) posX, (int) y + count, boxW, data.getText(data.percent == 100));
                        //HyPlus.displayIgMessage("trackbox", k.i + "");
                        draw.drawButton(Minecraft.getMinecraft(), 0, 0, data.percent);
                        count = count + 20;

                    }


                }

            } catch (ConcurrentModificationException e) {}

        }
        else {



        }

    }

    @Override
    protected boolean supportsRescale() {
        return false;
    }

    @Override
    public String getName() {
        return "QuestTracker";
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public EnumDisplayType[] getDisplayTypes() {
        return super.getDisplayTypes();
    }

    @Override
    public void createSettingElement() {

        String displayName = this.getSettingName();
        this.rawBooleanElement = new BooleanElement(this, this.getIconData(), displayName, "enabled");
        if (this.rawBooleanElement.getDisplayName() == null || !this.rawBooleanElement.getDisplayName().equals(displayName)) {
            this.rawBooleanElement.setDisplayName(displayName);
        }

        this.rawBooleanElement.setSortingId(this.getSortingId());
        ArrayList<SettingsElement> subList = new ArrayList();
        this.fillSubSettings(subList);
        this.rawBooleanElement.getSubSettings().addAll(subList);

    }

    @Override
    public String getControlName() {
        return super.getControlName();
    }

    @Override
    public void settingUpdated(boolean enabled) {
        super.settingUpdated(enabled);
    }

    @Override
    public void fillSubSettings(List<SettingsElement> settingsElements) {

        settingsElements.addAll(tracker.getSubSettings());

    }

    @Override
    public String getAttribute(String attribute, String defaultValue) {
        return super.getAttribute(attribute, defaultValue);
    }

    @Override
    public void setAttribute(String attribute, String value) {
        super.setAttribute(attribute, value);
    }

    @Override
    public BooleanElement getBooleanElement() {
        return super.getBooleanElement();
    }

    @Override
    public void setBooleanElement(BooleanElement booleanElement) {
        super.setBooleanElement(booleanElement);
    }

    @Override
    protected void applyScale(boolean enabled) {
        super.applyScale(enabled);
    }

    @Override
    protected double scaleModuleSize(double value, boolean divide) {
        return super.scaleModuleSize(value, divide);
    }

    @Override
    public boolean isShown() {
        return super.isShown();
    }

    @Override
    public boolean isMovable(int mouseX, int mouseY) {
        return super.isMovable(mouseX, mouseY);
    }

    @Override
    public void initModuleGui(ModuleGui moduleGui) {
        super.initModuleGui(moduleGui);
    }

    @Override
    public ModuleCategory getCategory() {
        return HyPlus.getInstance().hyModuleCategory;
    }

    @Override
    public ModuleConfigElement getModuleConfigElement() {
        return super.getModuleConfigElement();
    }

    @Override
    public ModuleConfigElement setModuleConfigElement(ModuleConfigElement moduleConfigElement) {
        return super.setModuleConfigElement(moduleConfigElement);
    }

    @Override
    public Set<EnumDisplayType> getEnabled() {
        return super.getEnabled();
    }

    @Override
    public void setEnabled(Set<EnumDisplayType> enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled(EnumDisplayType displayType) {
        return super.isEnabled(displayType);
    }

    @Override
    public boolean isRightBound(EnumDisplayType displayType) {
        return super.isRightBound(displayType);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
        super.onMouseClick(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onMouseMove(int mouseX, int mouseY, int mouseButton) {
        super.onMouseMove(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onMouseRelease(int mouseX, int mouseY, int mouseButton) {
        super.onMouseRelease(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onMouseScroll(int mouseX, int mouseY) {
        super.onMouseScroll(mouseX, mouseY);
    }

    @Override
    public void onKeyType(char charType, int keyCode) {
        super.onKeyType(charType, keyCode);
    }

    @Override
    public String getListedAfter() {
        return super.getListedAfter();
    }

    @Override
    public Map<String, String> getAttributes() {
        return super.getAttributes();
    }

    @Override
    public EnumModuleRegion getRegion(int index) {
        return super.getRegion(index);
    }

    @Override
    public double getX(int index) {
        return super.getX(index);
    }

    @Override
    public double getY(int index) {
        return super.getY(index);
    }

    @Override
    public void setListedAfter(String listedAfter) {
        super.setListedAfter(listedAfter);
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        super.setAttributes(attributes);
    }

    @Override
    public void setRegion(int index, EnumModuleRegion region) {
        super.setRegion(index, region);
    }

    @Override
    public void setRegions(EnumModuleRegion[] regions) {
        super.setRegions(regions);
    }

    @Override
    public void setX(int index, double x) {
        super.setX(index, x);
    }

    @Override
    public void setX(double[] x) {
        super.setX(x);
    }

    @Override
    public void setY(int index, double y) {
        super.setY(index, y);
    }

    @Override
    public void setY(double[] y) {
        super.setY(y);
    }

    @Override
    public void onResize(double width, double height, double prevWidth, double prevHeight) {
        super.onResize(width, height, prevWidth, prevHeight);
    }

    @Override
    protected ControlElement.IconData getModuleIcon(String name) {
        return super.getModuleIcon(name);
    }

    @Override
    protected ControlElement.IconData getModuleIcon(String name, String subSetting) {
        return super.getModuleIcon(name, subSetting);
    }

    @Override
    protected int getSpacing() {
        return super.getSpacing();
    }

    @Override
    public double getLastX() {
        return super.getLastX();
    }

    @Override
    public void setLastX(double lastX) {
        super.setLastX(lastX);
    }

    @Override
    public double getLastY() {
        return super.getLastY();
    }

    @Override
    public void setLastY(double lastY) {
        super.setLastY(lastY);
    }

    @Override
    public boolean isLastCenter() {
        return super.isLastCenter();
    }

    @Override
    public void setLastCenter(boolean lastCenter) {
        super.setLastCenter(lastCenter);
    }

    @Override
    public boolean isLastRightBound() {
        return super.isLastRightBound();
    }

    @Override
    public void setLastRightBound(boolean lastRightBound) {
        super.setLastRightBound(lastRightBound);
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    @Override
    public void setRawBooleanElement(BooleanElement rawBooleanElement) {
        super.setRawBooleanElement(rawBooleanElement);
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.MAP);
    }

    @Override
    public double getHeight() {

        if (tracker.dspc2.isEmpty()) return 20;
        return tracker.dspc2.size()*20;

    }

    @Override
    public double getWidth() {
        return 256;
    }

    @Override
    public void loadSettings() {

    }

    @Override
    public String getSettingName() {
        return "Quest Progress";
    }

    @Override
    public String getDescription() {
        return "Display current Quest progress";
    }

    @Override
    public int getSortingId() {
        return 0;
    }
}






