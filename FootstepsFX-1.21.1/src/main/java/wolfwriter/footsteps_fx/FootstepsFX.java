package wolfwriter.footsteps_fx;

import org.bukkit.plugin.java.JavaPlugin;
import wolfwriter.footsteps_fx.footstepsFX.CommandHandler;
import wolfwriter.footsteps_fx.footstepsFX.PlayerTrailListener;
import wolfwriter.footsteps_fx.footstepsFX.TrailManager;
import wolfwriter.footsteps_fx.footstepsFX.storage.PlayerDataStorage;
import wolfwriter.footsteps_fx.footstepsFX.storage.TrailData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FootstepsFX extends JavaPlugin {

    private static FootstepsFX instance;
    private TrailManager trailManager;
    private PlayerDataStorage storage;
    private Map<UUID, TrailData> playerDataMap = new HashMap<>();

    @Override
    public void onEnable() {
        storage = new PlayerDataStorage(this);
        playerDataMap = storage.load();

        instance = this;
        trailManager = new TrailManager(storage);
        getServer().getPluginManager().registerEvents(new PlayerTrailListener(trailManager), this);
        getCommand("trailon").setExecutor(new CommandHandler(trailManager));
        getCommand("trailoff").setExecutor(new CommandHandler(trailManager));
        getCommand("trailtype").setExecutor(new CommandHandler(trailManager));
        getCommand("trailintensity").setExecutor(new CommandHandler(trailManager));
        getCommand("trailtypelist").setExecutor(new CommandHandler(trailManager));

        getLogger().info("------------------------------------------------");
        getLogger().info("|    FootstepsFX has been successfully started  |");
        getLogger().info("|                                               |");
        getLogger().info("|  Author:        Wolfwriter                    |");
        getLogger().info("|  Version:       " + getDescription().getVersion() + "                       |");
        getLogger().info("|  Server:        " + getServer().getName() + " (" + getServer().getVersion() + ") |");
        getLogger().info("|  Minecraft:     " + getServer().getMinecraftVersion() + "                  |");
        getLogger().info("------------------------------------------------");
    }

    @Override
    public void onDisable() {
        if (trailManager != null) {
            PlayerDataStorage storage = new PlayerDataStorage(this);
            storage.save(trailManager.getPlayerDataMap());
        }

        getLogger().info("--------------------------------------------------------------");
        getLogger().info("|    FootstepsFX plugin has been disabled                     |");
        getLogger().info("|                                                              |");
        getLogger().info("|  Plugin Name:   FootstepsFX                                  |");
        getLogger().info("|  Author:         Wolfwriter                                  |");
        getLogger().info("|  Version:        " + getDescription().getVersion() + "                                |");
        getLogger().info("|  Server Type:    " + getServer().getName() + "                                    |");
        getLogger().info("|  Server Version: " + getServer().getVersion() + "                   |");
        getLogger().info("|  Minecraft:      " + getServer().getMinecraftVersion() + "                           |");
        getLogger().info("|                                                              |");
        getLogger().info("|  All trail effects have been stopped.                        |");
        getLogger().info("|  Thanks for using FootstepsFX – see you next time!           |");
        getLogger().info("--------------------------------------------------------------");
        // Plugin shutdown logic
    }

    public static FootstepsFX getInstance() {
        return instance;
    }
}