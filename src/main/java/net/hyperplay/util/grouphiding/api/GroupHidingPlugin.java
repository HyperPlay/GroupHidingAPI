package net.hyperplay.util.grouphiding.api;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class GroupHidingPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        GroupHidingAPI.register(this);
    }

}