package dev.antimoxs.hyplus.modules.quickplay;

import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.ModTextField;
import net.labymod.main.LabyMod;
import net.labymod.main.ModTextures;
import net.labymod.splash.SplashLoader;
import net.labymod.splash.dailyemotes.DailyEmote;
import net.labymod.user.User;
import net.labymod.user.UserManager;
import net.labymod.user.emote.EmoteRegistry;
import net.labymod.user.emote.EmoteRenderer;
import net.labymod.user.emote.keys.EmoteKeyFrame;
import net.labymod.user.emote.keys.EmotePose;
import net.labymod.user.emote.keys.provider.KeyFrameStorage;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QuickplayMenu extends Gui {
    private static final int ANIMATION_SPEED = 100;
    private static Field fieldPressTime;
    private static Field fieldLeftClickCounter;
    private static long emoteCooldownEnd = 0L;
    private long lastOpened;
    private int selectedItemIndex;
    private boolean open = false;
    private float lockedYaw = 0.0F;
    private float lockedPitch = 0.0F;
    private boolean prevCrosshairState;
    private short lastHoveredEmoteId = -1;
    private boolean emotesLocked = false;
    private boolean emotesOnCooldown = false;
    private int page = 0;
    private int acceptedPage = 0;
    private int animationState = 0;
    private long pageAnimation = 0L;
    private int scrollSelectedEmote = -1;
    private List<Short> filteredEmotes;
    private boolean searchOpened = false;
    private boolean dailyEmotes = false;
    private int searchMouseX;
    private int searchMouseY;

    public QuickplayMenu() {
    }

    public void open() {
        if (!this.open && !Minecraft.getMinecraft().gameSettings.hideGUI) {
            EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
            if (player != null) {
                System.out.println("player");
                if (fieldLeftClickCounter == null) {
                    try {
                        fieldLeftClickCounter = ReflectionHelper.findField(Minecraft.class, LabyModCore.getMappingAdapter().getLeftClickCounterMappings());
                        fieldLeftClickCounter.setAccessible(true);
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }
                }

                this.page = 0;
                this.open = true;
                this.selectedItemIndex = LabyModCore.getMinecraft().getPlayer().inventory.currentItem;
                this.scrollSelectedEmote = -1;
                this.lockedYaw = player.rotationYaw;
                this.lockedPitch = player.rotationPitch;
                this.prevCrosshairState = LabyMod.getInstance().getLabyModAPI().isCrosshairHidden();
                LabyMod.getInstance().getLabyModAPI().setCrosshairHidden(true);
                if (fieldPressTime == null) {
                    try {
                        fieldPressTime = ReflectionHelper.findField(KeyBinding.class, LabyModCore.getMappingAdapter().getPressTimeMappings());
                        fieldPressTime.setAccessible(true);
                    } catch (Exception var3) {
                        var3.printStackTrace();
                    }
                }

                this.emotesOnCooldown = LabyMod.getInstance().isServerHasEmoteSpamProtection();
                this.dailyEmotes = this.hasDailyEmotes(player);
                if (this.isEmotePlaying(player)) {
                    this.emotesLocked = true;
                }

                UserManager userManager = LabyMod.getInstance().getUserManager();
                if (System.currentTimeMillis() - this.lastOpened < 300L && userManager.getGroupManager().hasPermissionOf(userManager.getUser(player.getUniqueID()), (short)10)) {
                    Minecraft.getMinecraft().displayGuiScreen(new QuickplayMenu.SearchGui(this));
                    this.searchOpened = true;
                    this.animationState = 0;
                } else {
                    this.searchOpened = false;
                }

                this.lastOpened = System.currentTimeMillis();
                this.filter("");
            }
        }
    }

    public void close() {
        if (this.open) {
            this.open = false;
            LabyMod.getInstance().getLabyModAPI().setCrosshairHidden(this.prevCrosshairState);
            this.updateScrollLock(false);
            EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
            if (player != null) {
                player.rotationYaw = this.lockedYaw;
                player.rotationPitch = this.lockedPitch;
                if (this.lastHoveredEmoteId != -1) {
                    emoteCooldownEnd = System.currentTimeMillis() + 5000L;
                    LabyMod.getInstance().getEmoteRegistry().playEmote(this.lastHoveredEmoteId);
                }

            }
        }
    }

    public void pointSearchMouse(int mouseX, int mouseY) {
        this.searchMouseX = mouseX;
        this.searchMouseY = mouseY;
    }

    public void filter(String searchString) {
        System.out.println("filter");
        EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
        User user = LabyMod.getInstance().getUserManager().getUser(player.getUniqueID());
        if (searchString.isEmpty()) {
            this.filteredEmotes = user.getEmotes();
        } else {
            searchString = searchString.toLowerCase();
            Map<Short, KeyFrameStorage> sources = LabyMod.getInstance().getEmoteRegistry().getEmoteSources();
            List<Short> filteredEmotes = new ArrayList();
            Iterator var6 = user.getEmotes().iterator();

            while(var6.hasNext()) {
                Short id = (Short)var6.next();
                KeyFrameStorage storage = (KeyFrameStorage)sources.get(id);
                if (storage != null && storage.getName().toLowerCase().contains(searchString)) {
                    filteredEmotes.add(id);
                }
            }

            this.filteredEmotes = filteredEmotes;
        }

    }

    private boolean hasDailyEmotes(EntityPlayerSP player) {
        UserManager userManager = LabyMod.getInstance().getUserManager();
        User user = userManager.getUser(player.getUniqueID());
        if (SplashLoader.getLoader() != null && SplashLoader.getLoader().getEntries() != null) {
            DailyEmote[] dailyEmotes = SplashLoader.getLoader().getEntries().getDailyEmotes();
            return dailyEmotes != null && dailyEmotes.length != 0 && user.isDailyEmoteFlat();
        } else {
            return false;
        }
    }

    private boolean isEmotePlaying(EntityPlayerSP player) {
        EmoteRenderer renderer = LabyMod.getInstance().getEmoteRegistry().getEmoteRendererFor(player);
        return renderer != null && renderer.isVisible() && !renderer.isStream();
    }

    public void render() {
        if (this.open) {
            DrawUtils draw = LabyMod.getInstance().getDrawUtils();
            EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
            if (player != null && player.getUniqueID() != null && player.hurtTime == 0) {
                try {
                    if (fieldLeftClickCounter != null) {
                        fieldLeftClickCounter.setInt(Minecraft.getMinecraft(), 2);
                    }
                } catch (Exception var44) {
                    var44.printStackTrace();
                }

                if (this.emotesLocked && !this.isEmotePlaying(player)) {
                    this.emotesLocked = false;
                }

                double radiusMouseBorder = (double)draw.getHeight() / 4.0D / 3.0D;
                double midX = (double)draw.getWidth() / 2.0D;
                double midY = (double)draw.getHeight() / 2.0D;
                double lockedX = (double)this.lockedYaw;
                double lockedY = (double)this.lockedPitch;
                if (lockedY + radiusMouseBorder > 90.0D) {
                    lockedY = (double)((float)(90.0D - radiusMouseBorder));
                }

                if (lockedY - radiusMouseBorder < -90.0D) {
                    lockedY = (double)((float)(-90.0D + radiusMouseBorder));
                }

                long timePassed = System.currentTimeMillis() - this.lastOpened;
                if (timePassed > 2000L) {
                    timePassed = 2000L;
                }

                double radius = (double)draw.getHeight() / 4.0D - Math.exp((double)(-timePassed) / 100.0D) * 10.0D;
                double offsetX = lockedX - (double)player.rotationYaw;
                double offsetY = lockedY - (double)player.rotationPitch;
                double distance = Math.sqrt(offsetX * offsetX + offsetY * offsetY);
                double cursorX = midX - offsetX * 1.5D;
                double cursorY = midY - offsetY * 1.5D;
                if (this.searchOpened) {
                    cursorX = (double)this.searchMouseX;
                    cursorY = (double)this.searchMouseY;
                    offsetX = (double)(this.searchMouseX - draw.getWidth() / 2);
                    offsetY = (double)(this.searchMouseY - draw.getHeight() / 2);
                    distance = Math.sqrt(offsetX * offsetX + offsetY * offsetY);
                }

                GlStateManager.pushMatrix();
                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && !this.searchOpened) {
                    midX += offsetX;
                    midY += offsetY;
                }

                int totalEmotes = 10;
                int amount = 6;
                int emoteIndex = this.page * amount;
                int maxPages = (int)Math.ceil((double)totalEmotes / (double)amount);
                boolean cooldown = this.emotesOnCooldown ? emoteCooldownEnd > System.currentTimeMillis() : false;
                boolean emotesEnabled = LabyMod.getSettings().emotes;
                String localeKey = emotesEnabled ? (this.emotesLocked ? "emote_status_already_playing" : (totalEmotes == 0 ? (this.searchOpened ? "emote_status_not_found" : "emote_status_no_emotes") : (cooldown ? "emote_status_cooldown" : "emote_status_select"))) : "emote_status_disabled";
                String title = (!this.emotesLocked && emotesEnabled ? "" : ModColor.cl('c')) + LabyMod.getMessage(localeKey, new Object[0]);
                draw.drawCenteredString(title, midX, midY - radius - 5.0D);
                System.out.println("drawing?");
                if (this.page == -1) {
                    draw.drawCenteredString(ModColor.cl('b') + ModColor.cl('o') + "labymod.net/shop", midX, midY + radius + 6.0D);
                    draw.drawCenteredString(ModColor.cl('6') + LabyMod.getMessage("emote_daily", new Object[0]), midX, midY + radius - 5.0D, 0.7D);
                } else {
                    if (totalEmotes == 0) {
                        draw.drawCenteredString(ModColor.cl('b') + ModColor.cl('o') + "labymod.net/shop", midX, midY + radius - 5.0D);
                    } else if (maxPages > 1 && !this.searchOpened) {
                        String keyName = "?";

                        try {
                            keyName = Keyboard.getKeyName(LabyMod.getSettings().keyEmote).toLowerCase();
                        } catch (Exception var43) {
                        }

                        draw.drawCenteredString(LabyMod.getMessage("emote_selector_page", new Object[]{this.page + 1, maxPages}), midX, midY + radius - 5.0D, 0.7D);
                        draw.drawCenteredString(ModColor.cl('7') + LabyMod.getMessage("emote_doubletap", new Object[]{keyName}), midX, midY + radius + 6.0D, 0.7D);
                    }

                    if (maxPages == 1 && !this.searchOpened && this.dailyEmotes) {
                        draw.drawCenteredString(LabyMod.getMessage("emote_own", new Object[0]), midX, midY + radius - 5.0D, 0.7D);
                    }
                }

                double speed;
                double animation;
                if (!this.searchOpened) {
                    animation = 9.0D;
                    speed = 6.0D;
                    double arrowAnimation = Math.abs(Math.sin((double)(System.currentTimeMillis() - this.pageAnimation) / 28.274333882308138D) / 2.0D);
                    double scale;
                    if (this.acceptedPage > (this.dailyEmotes ? -1 : 0)) {
                        if (this.acceptedPage <= 0) {
                            GL11.glColor4f(1.0F, 0.7F, 0.0F, 1.0F);
                        } else {
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        }

                        scale = this.animationState == -1 ? arrowAnimation + 1.0D : 1.0D;
                        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.MISC_ARROW);
                        draw.drawTexture(midX - animation * scale / 2.0D - radius / 2.0D, midY + radius - 2.0D - speed * scale / 2.0D, 0.0D, 0.0D, 127.5D, 255.0D, animation * scale, speed * scale, 1.1F);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.MISC_MOUSE);
                        draw.drawTexture(midX - animation * scale / 2.0D - radius / 2.0D + 10.0D + 2.0D, midY + radius - 4.0D - speed * scale / 2.0D, 127.0D, 0.0D, 127.0D, 255.0D, 7.0D * scale, 10.0D * scale, 1.1F);
                    }

                    if (this.acceptedPage < maxPages - 1) {
                        scale = this.animationState == 1 ? arrowAnimation + 1.0D : 1.0D;
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.MISC_ARROW);
                        draw.drawTexture(midX - animation * scale / 2.0D + radius / 2.0D, midY + radius - 2.0D - speed * scale / 2.0D, 127.5D, 0.0D, 127.5D, 255.0D, animation * scale, speed * scale, 1.1F);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.MISC_MOUSE);
                        draw.drawTexture(midX - animation * scale / 2.0D + radius / 2.0D - 10.0D, midY + radius - 4.0D - speed * scale / 2.0D, 0.0D, 0.0D, 127.0D, 255.0D, 7.0D * scale, 10.0D * scale, 1.1F);
                    }
                }

                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                this.lastHoveredEmoteId = -1;
                if (this.animationState != 0) {
                    animation = (double)(System.currentTimeMillis() - this.pageAnimation) * 0.01D * radius;
                    speed = 1.0D * radius;
                    if (this.animationState != 1 && this.animationState != -1) {
                        if (this.animationState == 2) {
                            midX += speed - animation;
                        } else {
                            midX -= speed - animation;
                        }
                    } else if (this.animationState == -1) {
                        midX += animation;
                    } else {
                        midX -= animation;
                    }
                }

                Map<Short, KeyFrameStorage> sources = LabyMod.getInstance().getEmoteRegistry().getEmoteSources();
                if (this.page == -1) {
                    DailyEmote[] dailyEmotes = SplashLoader.getLoader().getEntries().getDailyEmotes();
                    emoteIndex = 0;

                    for(int index = amount; index >= 1; --index) {
                        DailyEmote dailyEmote = emoteIndex >= dailyEmotes.length ? null : dailyEmotes[emoteIndex];
                        KeyFrameStorage emote = dailyEmote == null ? null : (KeyFrameStorage)sources.get(dailyEmote.getId());
                        this.drawUnit(midX, midY, radius, amount, index, cursorX, cursorY, distance, emote, player);
                        ++emoteIndex;
                    }
                } else {
                    for(int index = amount - 1; index >= 0; --index) {
                        Short emoteId = emoteIndex >= totalEmotes ? null : (Short)this.filteredEmotes.get(emoteIndex);
                        KeyFrameStorage emote = emoteId == null ? null : (KeyFrameStorage)sources.get(emoteId);
                        this.drawUnit(midX, midY, radius, amount, index, cursorX, cursorY, distance, emote, player);
                        ++emoteIndex;
                    }
                }

                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                if (offsetX == 0.0D && offsetY == 0.0D) {
                    cursorX = (double)((int)cursorX);
                    cursorY = (double)((int)cursorY);
                }

                if (!this.searchOpened) {
                    draw.drawRect(cursorX, cursorY - 4.0D, cursorX + 1.0D, cursorY + 5.0D, 2147483647);
                    draw.drawRect(cursorX - 4.0D, cursorY, cursorX + 5.0D, cursorY + 1.0D, 2147483647);
                    this.handleMouseInput(maxPages - 1);
                }

            } else {
                this.close();
            }
        }
    }

    private void handleMouseInput(int maxPages) {
        double scroll = (double) Mouse.getDWheel();
        boolean moveUp = scroll > 0.0D;
        boolean moveDown = scroll < 0.0D;
        if (moveUp || moveDown) {
            int value = this.scrollSelectedEmote + (moveUp ? 1 : -1);
            this.scrollSelectedEmote = value < 0 ? 5 : value % 6;
        }

        try {
            int amount = 6;

            for(int i = 0; i < amount; ++i) {
                int code = Minecraft.getMinecraft().gameSettings.keyBindsHotbar[i].getKeyCode();
                if (code >= 0 && Keyboard.isKeyDown(code)) {
                    this.scrollSelectedEmote = amount - 1 - i;
                }
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        if (this.acceptedPage == this.page && this.animationState == 0) {
            if (Mouse.isButtonDown(0) && this.page > (this.dailyEmotes ? -1 : 0)) {
                --this.page;
                this.animationState = -1;
                this.pageAnimation = System.currentTimeMillis();
            }

            if (Mouse.isButtonDown(1) && this.page < maxPages) {
                ++this.page;
                this.animationState = 1;
                this.pageAnimation = System.currentTimeMillis();
            }
        } else if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1) && this.animationState != -1 && this.animationState != 1) {
            this.acceptedPage = this.page;
        }

        if ((this.animationState == -1 || this.animationState == 1) && this.pageAnimation + 100L < System.currentTimeMillis()) {
            this.animationState *= 2;
            this.pageAnimation = System.currentTimeMillis();
        }

        if ((this.animationState == -2 || this.animationState == 2) && this.pageAnimation + 100L < System.currentTimeMillis()) {
            this.animationState = 0;
            this.pageAnimation = System.currentTimeMillis();
        }

    }

    private void drawUnit(double midX, double midY, double radius, int amount, int index, double cursorX, double cursorY, double distance, KeyFrameStorage emote, EntityPlayerSP player) {
        double tau = 6.283185307179586D;
        double unitGap = 0.02D;
        double idleGap = 1.0D;
        double shift = tau / 3.0D + 3.141592653589793D;
        double outsideX = midX + radius * Math.cos((double)index * tau / (double)amount + unitGap + shift);
        double outsideY = midY + radius * Math.sin((double)index * tau / (double)amount + unitGap + shift);
        double outsideXNext = midX + radius * Math.cos((double)(index + 1) * tau / (double)amount - unitGap + shift);
        double outsideYNext = midY + radius * Math.sin((double)(index + 1) * tau / (double)amount - unitGap + shift);
        double radiusInside = radius / 5.0D;
        double insideX = midX + radiusInside * Math.cos((double)index * tau / (double)amount + unitGap + shift);
        double insideY = midY + radiusInside * Math.sin((double)index * tau / (double)amount + unitGap + shift);
        double insideXNext = midX + radiusInside * Math.cos((double)(index + 1) * tau / (double)amount - unitGap + shift);
        double insideYNext = midY + radiusInside * Math.sin((double)(index + 1) * tau / (double)amount - unitGap + shift);
        double idleRadius = radius / 5.0D - idleGap;
        double idleX = midX + idleRadius * Math.cos((double)index * tau / (double)amount + shift);
        double idleY = midY + idleRadius * Math.sin((double)index * tau / (double)amount + shift);
        double idleXNext = midX + idleRadius * Math.cos((double)(index + 1) * tau / (double)amount + shift);
        double idleYNext = midY + idleRadius * Math.sin((double)(index + 1) * tau / (double)amount + shift);
        double staticMidX = (double)(LabyMod.getInstance().getDrawUtils().getWidth() / 2);
        double switchPageBrightness = this.animationState == 0 ? 1.0D : 1.0D - 1.0D / radius * Math.abs(staticMidX - midX);
        boolean validEmote = LabyMod.getSettings().emotes && !this.emotesLocked && emote != null;
        boolean scrollSelected = this.scrollSelectedEmote != -1 && this.scrollSelectedEmote == index - (this.page == -1 ? 1 : 0);
        boolean hoverOutside = validEmote && (this.isInside(cursorX, cursorY, outsideX, outsideY, midX, midY, outsideXNext, outsideYNext) && Math.abs(distance) > 6.0D || scrollSelected && Math.abs(distance) < 6.0D);
        double outsideBrightness = (hoverOutside ? 0.5D : 0.1D) * switchPageBrightness;
        double outsideAlpha = (hoverOutside ? 0.6D : 0.5D) * switchPageBrightness;
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(2.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.VOID);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GL11.glBegin(7);
        GL11.glColor4d(outsideBrightness, outsideBrightness, outsideBrightness, outsideAlpha);
        GL11.glVertex3d(insideX, insideY, 0.0D);
        GL11.glVertex3d(insideXNext, insideYNext, 0.0D);
        GL11.glVertex3d(outsideXNext, outsideYNext, 0.0D);
        GL11.glVertex3d(outsideX, outsideY, 0.0D);
        GL11.glEnd();
        GL11.glBegin(2);
        GL11.glColor4d(0.0D, 0.0D, 0.0D, 0.5D * switchPageBrightness);
        GL11.glVertex3d(insideX, insideY, 0.0D);
        GL11.glVertex3d(insideXNext, insideYNext, 0.0D);
        GL11.glVertex3d(outsideXNext, outsideYNext, 0.0D);
        GL11.glVertex3d(outsideX, outsideY, 0.0D);
        GL11.glEnd();
        GL11.glBegin(4);
        GL11.glColor4d(0.3D, 0.3D, 0.3D, 0.5D * switchPageBrightness);
        GL11.glVertex3d(idleXNext, idleYNext, 0.0D);
        GL11.glVertex3d(idleX, idleY, 0.0D);
        GL11.glVertex3d(midX, midY, 0.0D);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glColor4d(0.2D, 0.2D, 0.2D, 0.9D * switchPageBrightness);
        GL11.glVertex3d(idleXNext, idleYNext, 0.0D);
        GL11.glVertex3d(idleX, idleY, 0.0D);
        GL11.glEnd();
        boolean cooldown = this.emotesOnCooldown ? emoteCooldownEnd > System.currentTimeMillis() : false;
        double middleOutsideX;
        double middleOutsideY;
        if (emote != null && !this.emotesLocked && !cooldown) {
            EmoteRegistry registry = LabyMod.getInstance().getEmoteRegistry();
            EmoteRenderer emoteRenderer = registry.getEmoteRendererFor(player);
            if (hoverOutside) {
                boolean moving = player.prevPosX != player.posX || player.prevPosY != player.posY || player.prevPosZ != player.posZ;
                if (moving) {
                    EmoteKeyFrame[] var68 = emote.getKeyframes();
                    int var69 = var68.length;

                    for(int var70 = 0; var70 < var69; ++var70) {
                        EmoteKeyFrame keyframe = var68[var70];
                        if (keyframe != null) {
                            EmotePose[] var72 = keyframe.getEmotePoses();
                            int var73 = var72.length;

                            for(int var74 = 0; var74 < var73; ++var74) {
                                EmotePose emotePose = var72[var74];
                                if (emotePose != null && emotePose.isBlockMovement()) {
                                    hoverOutside = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (!hoverOutside) {
                if (emoteRenderer != null && emoteRenderer.getEmoteId() == emote.getId()) {
                    registry.handleEmote(player.getUniqueID(), (short)-1);
                    emoteRenderer = null;
                }
            } else {
                if (emoteRenderer == null || emoteRenderer.isStream()) {
                    emoteRenderer = registry.handleEmote(player.getUniqueID(), emote.getId());
                    if (emoteRenderer != null) {
                        emoteRenderer.setVisible(false);
                    }
                }

                this.lastHoveredEmoteId = emote.getId();
            }

            middleOutsideX = radius / 1.7D;
            middleOutsideX = midX + middleOutsideX * Math.cos(((double)index + 0.5D) * tau / (double)amount + shift);
            middleOutsideY = midY + middleOutsideX * Math.sin(((double)index + 0.5D) * tau / (double)amount + shift);
            if (emoteRenderer != null && emoteRenderer.getEmoteId() == emote.getId()) {
                emoteRenderer.setVisible(true);
            }

            double size = radius / 70.0D;
            if (switchPageBrightness < 0.5D) {
                size = 0.0D;
            }

            GlStateManager.pushMatrix();
            GlStateManager.scale(size, size, size);
            drawEntityOnScreen(player, middleOutsideX / size, middleOutsideY / size + 8.0D, 13.0D, middleOutsideX - cursorX, middleOutsideY - cursorY, hoverOutside);
            GlStateManager.popMatrix();
            if (emoteRenderer != null && emoteRenderer.getEmoteId() == emote.getId()) {
                emoteRenderer.setVisible(false);
            }
        }

        double emoteRadius;
        if (emote != null && (this.emotesLocked || cooldown)) {
            emoteRadius = radius / 1.7D;
            middleOutsideX = midX + emoteRadius * Math.cos(((double)index + 0.5D) * tau / (double)amount + shift);
            middleOutsideX = midY + emoteRadius * Math.sin(((double)index + 0.5D) * tau / (double)amount + shift);
            middleOutsideY = radius / 4.0D;
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.MISC_BLOCKED);
            LabyMod.getInstance().getDrawUtils().drawTexture(middleOutsideX - middleOutsideY / 2.0D, middleOutsideX - middleOutsideY / 2.0D, 255.0D, 255.0D, middleOutsideY, middleOutsideY);
        }

        if (emote != null) {
            emoteRadius = radius / 1.7D;
            middleOutsideX = midX + emoteRadius * Math.cos(((double)index + 0.5D) * tau / (double)amount + shift);
            middleOutsideX = midY + emoteRadius * Math.sin(((double)index + 0.5D) * tau / (double)amount + shift);
            DrawUtils draw = LabyMod.getInstance().getDrawUtils();
            double fontSize = 0.5D;
            String prefix = this.page == -1 ? ModColor.cl('6') : "";
            List<String> lines = draw.listFormattedStringToWidth(prefix + emote.getName(), (int)(idleRadius * 2.0D / fontSize), 2);
            int lineY = 0;

            for(Iterator var77 = lines.iterator(); var77.hasNext(); lineY += 6) {
                String line = (String)var77.next();
                draw.drawCenteredString(line, middleOutsideX, middleOutsideX + (double)lineY + emoteRadius / 4.0D, radius / 70.0D * fontSize * switchPageBrightness);
            }
        }

    }

    private void updateScrollLock(boolean locked) {
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        if (gameSettings != null) {
            KeyBinding keyBinding = gameSettings.keyBindsHotbar[this.selectedItemIndex];
            if (keyBinding != null) {
                int keyCode = keyBinding.getKeyCode();
                KeyBinding.setKeyBindState(keyCode, locked);
                if (locked) {
                    KeyBinding.onTick(keyCode);
                } else {
                    try {
                        if (fieldPressTime != null) {
                            fieldPressTime.setInt(keyBinding, 0);
                        }
                    } catch (Exception var6) {
                        var6.printStackTrace();
                    }
                }

            }
        }
    }

    public void lockMouseMovementInCircle() {
        if (this.open) {
            EntityPlayerSP player = LabyModCore.getMinecraft().getPlayer();
            if (player != null) {
                double radius = (double)LabyMod.getInstance().getDrawUtils().getHeight() / 4.0D / 3.0D;
                float centerX = this.lockedYaw;
                float centerY = this.lockedPitch;
                if ((double)centerY + radius > 90.0D) {
                    centerY = (float)(90.0D - radius);
                }

                if ((double)centerY - radius < -90.0D) {
                    centerY = (float)(-90.0D + radius);
                }

                float newX = player.rotationYaw;
                float newY = player.rotationPitch;
                double distanceX = (double)(centerX - newX);
                double distanceY = (double)(centerY - newY);
                double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                if (distance > radius) {
                    double fromOriginToObjectX = (double)(newX - centerX);
                    double fromOriginToObjectY = (double)(newY - centerY);
                    double multiplier = radius / distance;
                    fromOriginToObjectX *= multiplier;
                    fromOriginToObjectY *= multiplier;
                    centerX = (float)((double)centerX + fromOriginToObjectX);
                    centerY = (float)((double)centerY + fromOriginToObjectY);
                    player.rotationYaw = centerX;
                    player.prevRotationYaw = centerX;
                    player.rotationPitch = centerY;
                    player.prevRotationPitch = centerY;
                }

                this.updateScrollLock(true);
            }
        }
    }

    public static void drawEntityOnScreen(EntityPlayerSP entity, double x, double y, double size, double mouseX, double mouseY, boolean hover) {
        GlStateManager.pushMatrix();
        GlStateManager.enableColorMaterial();
        GlStateManager.translate(x, y, 0.0D);
        GlStateManager.scale(-size, size, size);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float var6 = entity.renderYawOffset;
        float var7 = entity.rotationYaw;
        float var8 = entity.rotationPitch;
        float var9 = entity.prevRotationYawHead;
        float var10 = entity.rotationYawHead;
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableNormalize();
        GlStateManager.disableLighting();
        if (hover) {
            GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
        } else {
            GL11.glColor4d(0.5D, 0.5D, 0.5D, 1.0D);
        }

        entity.renderYawOffset = (float)Math.atan(mouseX / 40.0D) * 20.0F;
        entity.rotationYaw = (float)Math.atan(mouseX / 40.0D) * 40.0F;
        entity.rotationPitch = -((float)Math.atan(mouseY / 40.0D)) * 20.0F;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        double lastTickPosX = entity.lastTickPosX;
        double lastTickPosY = entity.lastTickPosY;
        double lastTickPosZ = entity.lastTickPosZ;
        double posX = entity.posX;
        double posY = entity.posY;
        double posZ = entity.posZ;
        double prevPosX = entity.prevPosX;
        double prevPosY = entity.prevPosY;
        double prevPosZ = entity.prevPosZ;
        double chasingPosX = entity.chasingPosX;
        double chasingPosY = entity.chasingPosY;
        double chasingPosZ = entity.chasingPosZ;
        double prevChasingPosX = entity.prevChasingPosX;
        double prevChasingPosY = entity.prevChasingPosY;
        double prevChasingPosZ = entity.prevChasingPosZ;
        entity.lastTickPosX = 0.0D;
        entity.lastTickPosY = 0.0D;
        entity.lastTickPosZ = 0.0D;
        entity.posX = 0.0D;
        entity.posY = 0.0D;
        entity.posZ = 0.0D;
        entity.prevPosX = 0.0D;
        entity.prevPosY = 0.0D;
        entity.prevPosZ = 0.0D;
        entity.chasingPosX = 0.0D;
        entity.chasingPosY = 0.0D;
        entity.chasingPosZ = 0.0D;
        entity.prevChasingPosX = 0.0D;
        entity.prevChasingPosY = 0.0D;
        entity.prevChasingPosZ = 0.0D;
        GlStateManager.translate(0.0F, 0.0F, 10.0F);
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.setPlayerViewY(90.0F);
        LabyModCore.getRenderImplementation().renderEntity(renderManager, entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        entity.lastTickPosX = lastTickPosX;
        entity.lastTickPosY = lastTickPosY;
        entity.lastTickPosZ = lastTickPosZ;
        entity.posX = posX;
        entity.posY = posY;
        entity.posZ = posZ;
        entity.prevPosX = prevPosX;
        entity.prevPosY = prevPosY;
        entity.prevPosZ = prevPosZ;
        entity.chasingPosX = chasingPosX;
        entity.chasingPosY = chasingPosY;
        entity.chasingPosZ = chasingPosZ;
        entity.prevChasingPosX = prevChasingPosX;
        entity.prevChasingPosY = prevChasingPosY;
        entity.prevChasingPosZ = prevChasingPosZ;
        entity.renderYawOffset = var6;
        entity.rotationYaw = var7;
        entity.rotationPitch = var8;
        entity.prevRotationYawHead = var9;
        entity.rotationYawHead = var10;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.popMatrix();
    }

    private double sign(double px1, double py1, double px2, double py2, double px3, double py3) {
        return (px1 - px3) * (py2 - py3) - (px2 - px3) * (py1 - py3);
    }

    private boolean isInside(double pointX, double pointY, double px1, double py1, double px2, double py2, double px3, double py3) {
        boolean b1 = this.sign(pointX, pointY, px1, py1, px2, py2) < 0.0D;
        boolean b2 = this.sign(pointX, pointY, px2, py2, px3, py3) < 0.0D;
        boolean b3 = this.sign(pointX, pointY, px3, py3, px1, py1) < 0.0D;
        return b1 == b2 && b2 == b3;
    }

    public boolean isOpen() {
        return this.open;
    }

    public static class SearchGui extends GuiScreen {
        private QuickplayMenu emoteSelectorGui;
        private ModTextField textField;

        public SearchGui(QuickplayMenu emoteSelectorGui) {
            this.emoteSelectorGui = emoteSelectorGui;
        }

        public void initGui() {
            super.initGui();
            this.textField = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), this.width / 2 - 50, this.height / 4 - 30, 100, 20);
            this.textField.setFocused(true);
            this.textField.setBlackBox(false);
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.textField.drawTextBox();
            this.emoteSelectorGui.pointSearchMouse(mouseX, mouseY);
        }

        public void updateScreen() {
            super.updateScreen();
            this.textField.updateCursorCounter();
        }

        protected void keyTyped(char typedChar, int keyCode) throws IOException {
            super.keyTyped(typedChar, keyCode);
            if (this.textField.textboxKeyTyped(typedChar, keyCode)) {
                this.emoteSelectorGui.filter(this.textField.getText());
            }

        }

        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            if (!this.textField.mouseClicked(mouseX, mouseY, mouseButton)) {
                this.emoteSelectorGui.pointSearchMouse(mouseX, mouseY);
                Minecraft.getMinecraft().displayGuiScreen((GuiScreen)null);
                this.emoteSelectorGui.close();
            }
        }

        protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
            super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
            this.emoteSelectorGui.pointSearchMouse(mouseX, mouseY);
        }
    }
}

