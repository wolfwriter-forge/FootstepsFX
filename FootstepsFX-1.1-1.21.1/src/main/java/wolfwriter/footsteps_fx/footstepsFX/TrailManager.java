package wolfwriter.footsteps_fx.footstepsFX;

import org.bukkit.entity.Player;
import wolfwriter.footsteps_fx.footstepsFX.storage.PlayerDataStorage;
import wolfwriter.footsteps_fx.footstepsFX.storage.TrailData;

import java.util.*;

public class TrailManager {

    private final Map<UUID, TrailData> playerDataMap = new HashMap<>();
    private final PlayerDataStorage storage;

    public TrailManager(PlayerDataStorage storage) {
        this.storage = storage;
        this.playerDataMap.putAll(storage.load()); // Lade beim Start
    }

    public boolean isEnabled(Player player) {
        return getData(player).isEnabled();
    }

    public void setEnabled(Player player, boolean value) {
        TrailData data = getData(player);
        data.setEnabled(value);
        playerDataMap.put(player.getUniqueId(), data);
        storage.save(playerDataMap);
    }

    public String getEffectType(Player player) {
        return getData(player).getEffectType();
    }

    public void setEffectType(Player player, String type) {
        TrailData data = getData(player);
        data.setEffectType(type);
        playerDataMap.put(player.getUniqueId(), data);
        storage.save(playerDataMap);
    }

    public int getIntensity(Player player) {
        return getData(player).getIntensity();
    }

    public void setIntensity(Player player, int value) {
        TrailData data = getData(player);
        data.setIntensity(Math.max(1, Math.min(100, value)));
        playerDataMap.put(player.getUniqueId(), data);
        storage.save(playerDataMap);
    }

    private TrailData getData(Player player) {
        return playerDataMap.getOrDefault(player.getUniqueId(), new TrailData(false, "flame", 8));
    }

    public Map<UUID, TrailData> getPlayerDataMap() {
        return playerDataMap;
    }

    public void lock(UUID uuid) {
        TrailData data = playerDataMap.getOrDefault(uuid, new TrailData(false, "flame", 8));
        data.setLocked(true);
        playerDataMap.put(uuid, data);
        storage.save(playerDataMap);
    }

    public void unlock(UUID uuid) {
        TrailData data = playerDataMap.getOrDefault(uuid, new TrailData(false, "flame", 8));
        data.setLocked(false);
        playerDataMap.put(uuid, data);
        storage.save(playerDataMap);
    }

    public void lockAll() {
        for (TrailData data : playerDataMap.values()) {
            data.setLocked(true);
        }
        storage.save(playerDataMap);
    }

    public void unlockAll() {
        for (TrailData data : playerDataMap.values()) {
            data.setLocked(false);
        }
        storage.save(playerDataMap);
    }

    public boolean isLocked(Player player) {
        return getData(player).isLocked();
    }

    public void reloadConfig() {
        playerDataMap.clear();
        playerDataMap.putAll(storage.load());
    }

    public PlayerDataStorage getStorage() {
        return storage;
    }


}
