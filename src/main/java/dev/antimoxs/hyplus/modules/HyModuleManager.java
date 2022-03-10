package dev.antimoxs.hyplus.modules;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.events.IHyPlusEvent;

import java.util.ArrayList;

public class HyModuleManager {

    private final ArrayList<IHyPlusModule> modules = new ArrayList<>();


    public void registerModule(IHyPlusModule module) {

        if (!modules.contains(module)) {

            // Add module to module list
            modules.add(module);

        }

        if (module instanceof IHyPlusEvent) {

            // register module events
            HyPlus.getInstance().hyEventManager.register((IHyPlusEvent) module);

        }

    }

    public ArrayList<IHyPlusModule> getModules() { return this.modules; }


}
