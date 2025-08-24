package wolfwriter.footsteps_fx.footstepsFX;

import org.bukkit.Particle;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private final TrailManager manager;

    public CommandHandler(TrailManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (!player.hasPermission("trail.use")) return true;

        switch (cmd.getName().toLowerCase()) {
            case "trailon" -> {
                manager.setEnabled(player, true);
                player.sendMessage("§aTrail effect enabled.");
            }
            case "trailoff" -> {
                manager.setEnabled(player, false);
                player.sendMessage("§cTrail effect disabled.");
            }
            case "trailtype" -> {
                if (args.length == 0) return false;
                manager.setEffectType(player, args[0]);
                player.sendMessage("§bEffect type set to: " + args[0]);
            }
            case "trailtypelist" -> {
                StringBuilder builder = new StringBuilder("§7Available trail effects:\n");
                for (Particle particle : Particle.values()) {
                    if (particle.getDataType() == Void.class) {
                        builder.append("§a• ").append(particle.name().toLowerCase()).append("\n");
                    }
                }
                player.sendMessage(builder.toString());
            }
            case "trailintensity" -> {
                if (args.length == 0) return false;
                try {
                    int value = Integer.parseInt(args[0]);
                    if (value < 1 || value > 100) {
                        player.sendMessage("§cPlease enter a number between 1 and 100.");
                        return true;
                    }
                    manager.setIntensity(player, value);
                    player.sendMessage("§eTrail intensity set to: " + value);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cInvalid number. Please enter a whole number between 1 and 100.");
                }
            }
        }
        return true;
    }
}