package net.hyperplay.util.grouphiding.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class GroupHidingAPI implements Listener {

    private static final String errMsg = "Please use GroupHidingAPI#register(JavaPlugin) before using the API!";

    static Map<Player, Group> playerToGroup;

    /**
     * Register GroupHidingAPI.
     *
     * @param plugin your plugin instance
     */
    public static void register(JavaPlugin plugin) {
        if (playerToGroup != null) throw new IllegalStateException("GroupHidingAPI has already been registered!");
        Bukkit.getPluginManager().registerEvents(new GroupHidingAPI(), plugin);
        playerToGroup = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        GroupHidingAPI.reset(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        GroupHidingAPI.remove(event.getPlayer());
    }

    /**
     * Reset/Show Player
     *
     * @param player player to reset
     */
    public static void reset(Player player) {
        if (playerToGroup == null) throw new IllegalStateException(errMsg);

        remove(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (player != p) {
                if (playerToGroup.containsKey(p)) {
                    player.hidePlayer(p);
                    p.hidePlayer(player);
                } else {
                    player.showPlayer(p);
                    p.showPlayer(player);
                }
            }
        }
    }

    protected static void remove(Player player) {
        if (playerToGroup == null) throw new IllegalStateException(errMsg);

        if (playerToGroup.containsKey(player)) {
            Group group = playerToGroup.remove(player);
            if (group != null) group.remove(player);
        }
    }

    /**
     * Check if Player has Group.
     *
     * @param player player to check
     * @return does player have group?
     */
    public static boolean hasGroup(Player player) {
        if (playerToGroup == null) throw new IllegalStateException(errMsg);

        return playerToGroup.containsKey(player);
    }

    /**
     * Get a Player's Group
     *
     * @param player player to get group of
     * @return player's group
     */
    public static Group getGroup(Player player) {
        if (playerToGroup == null) throw new IllegalStateException(errMsg);

        return playerToGroup.get(player);
    }

    /**
     * Create a new empty Group.
     *
     * @return player's group
     */
    public static Group newGroup() {
        if (playerToGroup == null) throw new IllegalStateException(errMsg);

        return new Group();
    }

    /**
     * Create a new Group with Players.
     *
     * @param players players for the new group
     * @return player's group
     */
    public static Group newGroup(Player... players) {
        if (playerToGroup == null) throw new IllegalStateException(errMsg);

        return new Group(players);
    }

}