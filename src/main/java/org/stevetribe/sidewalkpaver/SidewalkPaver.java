package org.stevetribe.sidewalkpaver;

import org.bukkit.plugin.java.JavaPlugin;

public final class SidewalkPaver extends JavaPlugin {

    @Override
    public void onEnable() {
        // register commands
        this.getCommand("swp").setExecutor(new PaverCommandExecutor());

        // register events
        getServer().getPluginManager().registerEvents(new PaverListener(), this);

        getLogger().info("SidewalkPaver now enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SidewalkPaver now disabled.");
    }
}
