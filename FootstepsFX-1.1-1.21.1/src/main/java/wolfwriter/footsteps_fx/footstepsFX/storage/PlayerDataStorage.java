package wolfwriter.footsteps_fx.footstepsFX.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataStorage {

    private final File file;
    private final Gson gson;
    private final JavaPlugin plugin;
    private final Map<UUID, TrailData> dataMap = new HashMap<>();

    public PlayerDataStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "footstepsfx_playerdata.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void save(Map<UUID, TrailData> dataMap) {
        plugin.getDataFolder().mkdirs();

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(dataMap, writer);
            plugin.getLogger().info("Player data saved to footstepsfx_playerdata.json");
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save player data: " + e.getMessage());
        }

    }

    public Map<UUID, TrailData> load() {
        plugin.getDataFolder().mkdirs();

        if (!file.exists()) {
            plugin.getLogger().info("No existing player data found. A new file will be created.");
            return new HashMap<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<UUID, TrailData>>() {}.getType();
            Map<UUID, TrailData> dataMap = gson.fromJson(reader, type);
            plugin.getLogger().info("Player data loaded from footstepsfx_playerdata.json");
            return dataMap;
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to load player data: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public TrailData get(UUID uuid) {
        return dataMap.computeIfAbsent(uuid, k -> new TrailData(false, "flame", 8));
    }
}
