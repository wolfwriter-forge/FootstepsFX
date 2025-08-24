package wolfwriter.footsteps_fx.footstepsFX;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.EventHandler;

public class PlayerTrailListener implements Listener {

    private final TrailManager manager;

    public PlayerTrailListener(TrailManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!manager.isEnabled(player)) return;

        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() &&
                from.getBlockY() == to.getBlockY() &&
                from.getBlockZ() == to.getBlockZ()) return;

        Particle particle;
        try {
            particle = Particle.valueOf(manager.getEffectType(player).toUpperCase());
        } catch (IllegalArgumentException e) {
            particle = Particle.FLAME;
        }

        for (int i = 0; i < manager.getIntensity(player); i++) {
            player.getWorld().spawnParticle(particle, player.getLocation(), 1, 0.2, 0.2, 0.2, 0);
        }
    }
}
