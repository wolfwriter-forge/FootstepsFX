package wolfwriter.footsteps_fx.footstepsFX.storage;

public class TrailData {

    private boolean enabled;
    private String effectType;
    private int intensity;

    public TrailData() {}

    public TrailData(boolean enabled, String effectType, int intensity) {
        this.enabled = enabled;
        this.effectType = effectType;
        this.intensity = intensity;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getEffectType() {
        return effectType;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
}
