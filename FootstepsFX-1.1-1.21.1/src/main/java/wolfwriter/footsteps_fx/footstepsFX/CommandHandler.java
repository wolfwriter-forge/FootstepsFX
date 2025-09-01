package wolfwriter.footsteps_fx.footstepsFX;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import wolfwriter.footsteps_fx.footstepsFX.storage.PlayerDataStorage;
import wolfwriter.footsteps_fx.footstepsFX.storage.TrailData;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommandHandler implements CommandExecutor, TabCompleter {

    private TrailManager manager;
    private PlayerDataStorage dataManager;

    public CommandHandler(TrailManager manager, PlayerDataStorage dataManager) {
        this.manager = manager;
        this.dataManager = dataManager;
    }

    private boolean error(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        UUID uuid = null;
        TrailData data = null;
        Player player = null;


        if (sender instanceof Player) {
            player = (Player) sender;
            uuid = player.getUniqueId();
            data = dataManager.get(uuid);
        } else {
            return error(sender);
        }

        if (args.length == 0) {
            sender.sendMessage("§cUse §e/killeffect help §cfor a list of available commands.");
            return true;
        }

        if (!sender.hasPermission("trail.use")) {
            sender.sendMessage("§cYou don't have permission to use trail commands.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§cUse §e/trail help §cfor a list of available commands.");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "on" -> {
                data.setEnabled(true);
                sender.sendMessage("§aTrail effect enabled.");
            }

            case "off" -> {
                data.setEnabled(false);
                sender.sendMessage("§cTrail effect disabled.");
            }

            case "effect" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cPlease specify a trail effect.");
                    return true;
                }
                manager.setEffectType(player, args[1]);
                sender.sendMessage("§bEffect set to: " + args[1]);
            }

            case "intensity" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cPlease specify an intensity value.");
                    return true;
                }
                try {
                    int value = Integer.parseInt(args[1]);
                    manager.setIntensity(player, value);
                    sender.sendMessage("§eTrail intensity set to: " + value);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cInvalid number. Please enter a whole number between 1 and 100.");
                }
            }

            case "list" -> {
                int page = 1;
                if (args.length > 1) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("§cInvalid page number.");
                        return true;
                    }
                }

                List<String> particles = Arrays.stream(Particle.values())
                        .filter(p -> p.getDataType() == Void.class)
                        .map(p -> p.name().toLowerCase())
                        .sorted()
                        .toList();

                int pageSize = 20;
                int totalPages = (int) Math.ceil((double) particles.size() / pageSize);
                if (page < 1 || page > totalPages) {
                    sender.sendMessage("§cPage not found. There are §e" + totalPages + " §cpages.");
                    return true;
                }

                sender.sendMessage("§7Effects (Page §e" + page + "§7/§e" + totalPages + "§7):");
                particles.subList((page - 1) * pageSize, Math.min(page * pageSize, particles.size()))
                        .forEach(p -> sender.sendMessage("§a• " + p));
            }

            case "lock" -> {
                if (!sender.hasPermission("trail.lock")) {
                    sender.sendMessage("§cYou don't have permission to lock trail effects.");
                    return true;
                }

                // /trail lock player <name> effect <effect>
                if (args.length >= 5 && args[1].equalsIgnoreCase("player") && args[3].equalsIgnoreCase("effect")) {
                    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[2]);
                    UUID targetUUID = targetPlayer.getUniqueId();
                    TrailData targetData = manager.getPlayerDataMap().computeIfAbsent(targetUUID, id -> new TrailData(false, "flame", 8));

                    String effect = args[4].toLowerCase();
                    if (effect.equals("*") || effect.equals("all")) {
                        Arrays.stream(Particle.values())
                                .filter(p -> p.getDataType() == Void.class)
                                .map(p -> p.name().toLowerCase())
                                .forEach(targetData::lockEffect);

                        sender.sendMessage("§cAll effects have been locked for §e" + targetPlayer.getName());
                    } else if (isValidEffect(effect)) {
                        targetData.lockEffect(effect);
                        sender.sendMessage("§cEffect §e" + effect + " §chas been locked for §e" + targetPlayer.getName());
                    } else {
                        sender.sendMessage("§cInvalid effect name. Use §e/trail list §cfor available effects.");
                        return true;
                    }

                    manager.getPlayerDataMap().put(targetUUID, targetData);
                    manager.getStorage().save(manager.getPlayerDataMap());
                    return true;
                }

                // /trail lock effect <effect>
                if (args.length >= 3 && args[1].equalsIgnoreCase("effect")) {
                    String effect = args[2].toLowerCase();
                    if (effect.equals("*") || effect.equals("all")) {
                        data.lockAllEffects();
                        sender.sendMessage("§cAll effects have been locked.");
                    } else if (isValidEffect(effect)) {
                        data.lockEffect(effect);
                        sender.sendMessage("§cEffect §e" + effect + " §chas been locked.");
                    } else {
                        sender.sendMessage("§cInvalid effect name. Use §e/trail list §cfor available effects.");
                        return true;
                    }

                    manager.getPlayerDataMap().put(uuid, data);
                    manager.getStorage().save(manager.getPlayerDataMap());
                    return true;
                }

                sender.sendMessage("§cUsage: /trail lock effect <effect> or /trail lock player <name> effect <effect>");
                return true;
            }

            case "unlock" -> {
                if (!sender.hasPermission("trail.unlock")) {
                    sender.sendMessage("§cYou don't have permission to unlock trail effects.");
                    return true;
                }

                // /trail unlock player <name> effect <effect|all|*>
                if (args.length >= 5 && args[1].equalsIgnoreCase("player") && args[3].equalsIgnoreCase("effect")) {
                    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[2]);
                    UUID targetUUID = targetPlayer.getUniqueId();
                    TrailData targetData = manager.getPlayerDataMap().computeIfAbsent(targetUUID, id -> new TrailData(false, "flame", 8));

                    String effect = args[4].toLowerCase();

                    if (effect.equals("*") || effect.equals("all")) {
                        data.unlockAllEffects();
                        sender.sendMessage("§aAll effects have been unlocked for §e" + targetPlayer.getName());
                    } else if (isValidEffect(effect)) {
                        targetData.unlockEffect(effect);
                        sender.sendMessage("§aEffect §e" + effect + " §ahas been unlocked for §e" + targetPlayer.getName());
                    } else {
                        sender.sendMessage("§cInvalid effect name. Use §e/trail list §cfor available effects.");
                        return true;
                    }

                    manager.getPlayerDataMap().put(targetUUID, targetData);
                    manager.getStorage().save(manager.getPlayerDataMap());
                    return true;
                }

                // /trail unlock effect <effect|all|*>
                if (args.length >= 3 && args[1].equalsIgnoreCase("effect")) {
                    String effect = args[2].toLowerCase();

                    if (effect.equals("*") || effect.equals("all")) {
                        data.unlockAllEffects();
                        sender.sendMessage("§aAll effects have been unlocked.");
                    } else if (isValidEffect(effect)) {
                        data.unlockEffect(effect);
                        sender.sendMessage("§aEffect §e" + effect + " §ahas been unlocked.");
                    } else {
                        sender.sendMessage("§cInvalid effect name. Use §e/trail list §cfor available effects.");
                        return true;
                    }

                    manager.getPlayerDataMap().put(uuid, data);
                    manager.getStorage().save(manager.getPlayerDataMap());
                    return true;
                }

                sender.sendMessage("§cUsage: /trail unlock effect <effect> or /trail unlock player <name> effect <effect>");
                return true;
            }

            case "reload" -> {
                if (!sender.hasPermission("trail.reload")) {
                    sender.sendMessage("§cYou don't have permission to reload trail config.");
                    return true;
                }
                manager.reloadConfig();
                sender.sendMessage("§aTrail configuration reloaded.");
            }

            case "help" -> {

            sender.sendMessage("§e§lFootstepsFX Commands:");
            sender.sendMessage("§7/trail on §8– Enables kill effects");
            sender.sendMessage("§7/trail off §8– Disables kill effects");
            sender.sendMessage("§7/trail effect <effect> §8– Sets the particle effect");
            sender.sendMessage("§7/trail list [page] §8– Shows all available effects");

            if (sender.hasPermission("trail.unlock")) {
                sender.sendMessage("§7/trail unlock effect <name|all/*> §8– Unlocks effects");
                sender.sendMessage("§7/trail unlock player <player> effect <name|all/*> §8– Unlocks for another player");
            }

            if (sender.hasPermission("trail.lock")) {
                sender.sendMessage("§7/trail lock effect <name|all/*> §8– Locks effects");
                sender.sendMessage("§7/trail lock player <player> effect <name|all/*> §8– Locks for another player");
            }

            if (sender.hasPermission("trail.reload")) {
                sender.sendMessage("§7/trail reload §8– Reloads the JSON data");
            }
        }

            default -> sender.sendMessage("§cUnknown subcommand. Use §e/trail help §cfor help.");
        }

        manager.getPlayerDataMap().put(uuid, data);
        manager.getStorage().save(manager.getPlayerDataMap());
        return true;
    }


    private boolean noPerm(CommandSender sender) {
        sender.sendMessage("§cYou don't have permission to use this command.");
        return true;
    }

    private boolean usage(CommandSender sender, String usage) {
        sender.sendMessage("§cUsage: §e" + usage);
        return true;
    }

    private boolean handleUnlockOrLock(CommandSender sender, String[] args, boolean isOtherPlayer, boolean isUnlock) {
        String targetEffect = isOtherPlayer ? args[4].toLowerCase() : args[2].toLowerCase();

        TrailData targetData;
        UUID targetUUID;

        if (isOtherPlayer) {
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[2]);
            targetUUID = targetPlayer.getUniqueId();
            targetData = manager.getPlayerDataMap().computeIfAbsent(targetUUID, id -> new TrailData(false, "flame", 8));
        } else {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cOnly players can use this command.");
                return true;
            }
            targetUUID = player.getUniqueId();
            targetData = manager.getPlayerDataMap().computeIfAbsent(targetUUID, id -> new TrailData(false, "flame", 8));
        }

        if (targetEffect.equals("*") || targetEffect.equals("all")) {
            if (isUnlock) {
                targetData.unlockAllEffects(); // ← zentrale Methode
                sender.sendMessage("§aAll effects" + (isOtherPlayer ? " for §e" + args[2] : "") + " §ahave been unlocked.");
            } else {
                targetData.lockAllEffects(); // ← zentrale Methode
                sender.sendMessage("§cAll effects" + (isOtherPlayer ? " for §e" + args[2] : "") + " §chave been locked.");
            }

        } else if (isValidEffect(targetEffect)) {
            if (isUnlock) {
                targetData.unlockEffect(targetEffect);
                sender.sendMessage("§aEffect §e" + targetEffect + (isOtherPlayer ? " §afor §e" + args[2] : "") + " §ahas been unlocked.");
            } else {
                targetData.lockEffect(targetEffect);
                sender.sendMessage("§cEffect §e" + targetEffect + (isOtherPlayer ? " §cfor §e" + args[2] : "") + " §chas been locked.");
            }
        } else {
            sender.sendMessage("§cInvalid effect name. Use §e/trail list §cfor available effects.");
            return true;
        }

        manager.getPlayerDataMap().put(targetUUID, targetData);
        manager.getStorage().save(manager.getPlayerDataMap());
        return true;
    }

    private boolean isValidEffect(String effect) {
        return Arrays.stream(Particle.values())
                .filter(p -> p.getDataType() == Void.class)
                .anyMatch(p -> p.name().equalsIgnoreCase(effect));
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("trail")) return Collections.emptyList();

        if (args.length == 1) {
            return List.of("on", "off", "effect", "list", "intensity", "lock", "unlock", "reload", "help");
        }

        // /trail type <effect>
        if (args.length == 2 && args[0].equalsIgnoreCase("effect")) {
            return Arrays.stream(Particle.values())
                    .filter(p -> p.getDataType() == Void.class)
                    .map(p -> p.name().toLowerCase())
                    .sorted()
                    .collect(Collectors.toList());
        }

        // /trail intensity <value>
        if (args.length == 2 && args[0].equalsIgnoreCase("intensity")) {
            return IntStream.rangeClosed(1, 100)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList());
        }

        // /trail lock|unlock <effect|player>
        if (args.length == 2 && (args[0].equalsIgnoreCase("lock") || args[0].equalsIgnoreCase("unlock"))) {
            return List.of("effect", "player");
        }

        // /trail lock|unlock effect <name>
        if (args.length == 3 && args[1].equalsIgnoreCase("effect")) {
            return Arrays.stream(Particle.values())
                    .filter(p -> p.getDataType() == Void.class)
                    .map(p -> p.name().toLowerCase())
                    .sorted()
                    .collect(Collectors.toList());
        }

        // /trail lock|unlock player <name>
        if (args.length == 3 && args[1].equalsIgnoreCase("player")) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }

        // /trail lock|unlock player <name> effect
        if (args.length == 4 && args[1].equalsIgnoreCase("player") &&
                (args[0].equalsIgnoreCase("lock") || args[0].equalsIgnoreCase("unlock"))) {
            return List.of("effect");
        }

        // /trail lock|unlock player <name> effect <name>
        if (args.length == 5 && args[1].equalsIgnoreCase("player") && args[3].equalsIgnoreCase("effect") &&
                (args[0].equalsIgnoreCase("lock") || args[0].equalsIgnoreCase("unlock"))) {
            return Arrays.stream(Particle.values())
                    .filter(p -> p.getDataType() == Void.class)
                    .map(p -> p.name().toLowerCase())
                    .sorted()
                    .collect(Collectors.toList());
        }


        return Collections.emptyList();
    }
}