package wolfwriter.footsteps_fx.footstepsFX.storage;

import org.bukkit.Particle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TrailData {

    private boolean enabled;
    private String effectType;
    private int intensity;

    private Set<String> lockedEffects = new HashSet<>();
    private final Set<String> unlockedEffects = new HashSet<>();
    private transient boolean locked = false;

    public TrailData(boolean enabled, String effectType, int intensity) {
        this.enabled = enabled;
        this.effectType = effectType;
        this.intensity = intensity;

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEffectType() {
        return effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }




    public void lockEffect(String effect) {
        effect = effect.toLowerCase();
        final String targetEffect = effect;

        if (unlockedEffects.contains("*")) {
            unlockedEffects.clear();
            getAllValidEffects().stream()
                    .filter(e -> !e.equals(targetEffect))
                    .forEach(unlockedEffects::add);
        }

        lockedEffects.add(effect);
    }

    public void unlockEffect(String effect) {
        effect = effect.toLowerCase();
        final String targetEffect = effect;

        if (lockedEffects.contains("*")) {
            lockedEffects.clear();
            getAllValidEffects().stream()
                    .filter(e -> !e.equals(targetEffect))
                    .forEach(lockedEffects::add);
        }

        unlockedEffects.add(effect);
    }

    public void lockAllEffects() {
        lockedEffects.clear();
        lockedEffects.add("*");
        unlockedEffects.clear(); // optional: reset freigaben
    }

    public void unlockAllEffects() {
        unlockedEffects.clear();
        unlockedEffects.add("*");
        lockedEffects.clear(); // optional: reset sperren
    }

    public boolean isEffectLocked(String effect) {
        effect = effect.toLowerCase();
        return lockedEffects.contains("*") || lockedEffects.contains(effect);
    }

    public boolean isEffectUnlocked(String effect) {
        effect = effect.toLowerCase();
        return unlockedEffects.contains("*") || unlockedEffects.contains(effect);
    }




    public static boolean isValidEffect(String effect) {
        return getAllValidEffects().contains(effect.toLowerCase());
    }

    public static List<String> getAllValidEffects() {
        return Arrays.stream(Particle.values())
                .filter(p -> p.getDataType() == Void.class)
                .map(p -> p.name().toLowerCase())
                .collect(Collectors.toList());
    }

    public Set<String> getLockedEffects() {
        return lockedEffects;
    }

    public Set<String> getUnlockedEffects() {
        return unlockedEffects;
    }





    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }
}
