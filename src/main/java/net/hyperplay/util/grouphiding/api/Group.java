package net.hyperplay.util.grouphiding.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Group {

    private Set<Player> players = new HashSet<>();
    private Set<Player> spectators = new HashSet<>();

    /**
     * Create a new empty group.
     */
    public Group() {
    }

    /**
     * Create a new group with Players.
     *
     * @param players players for the new group
     */
    public Group(Player... players) {
        for (Player player : players) {
            addPlayer(player);
        }
    }

    /**
     * Add Player from Group.
     *
     * @param player player to be added
     */
    public void addPlayer(Player player) {
        if (this.spectators.contains(player)) {
            remove(player);
        }
        if (!this.players.contains(player)) {
            if (GroupHidingAPI.hasGroup(player)) {
                GroupHidingAPI.getGroup(player).remove(player);
            }
            this.players.add(player);
            GroupHidingAPI.playerToGroup.put(player, this);

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (player != p) {
                    if (players.contains(p)) {
                        player.showPlayer(p);
                        p.showPlayer(player);
                    } else if (spectators.contains(p)) {
                        player.hidePlayer(p);
                        p.showPlayer(player);
                    } else {
                        player.hidePlayer(p);
                        p.hidePlayer(player);
                    }
                }
            }
        }
    }

    /**
     * Add Spectator to Group.
     *
     * @param player spectator to be added
     */
    public void addSpectator(Player player) {
        if (this.players.contains(player)) {
            remove(player);
        }
        if (!this.spectators.contains(player)) {
            if (GroupHidingAPI.hasGroup(player)) {
                GroupHidingAPI.getGroup(player).remove(player);
            }
            this.spectators.add(player);
            GroupHidingAPI.playerToGroup.put(player, this);

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (player != p) {
                    if (players.contains(p)) {
                        player.showPlayer(p);
                        p.hidePlayer(player);
                    } else if (spectators.contains(p)) {
                        player.hidePlayer(p);
                        p.hidePlayer(player);
                    } else {
                        player.hidePlayer(p);
                        p.hidePlayer(player);
                    }
                }
            }
        }
    }

    /**
     * Remove Player from Group.
     *
     * @param player player to be removed
     */
    public void removePlayer(Player player) {
        if (remove(player)) {
            GroupHidingAPI.remove(player);
        }
    }

    protected boolean remove(Player player) {
        boolean existed = false;

        if (this.players.contains(player)) {
            players.remove(player);
            existed = true;
        }

        if (this.spectators.contains(player)) {
            spectators.remove(player);
            existed = true;
        }

        return existed;
    }

    /**
     * Kills the Group (removes it).
     */
    public void kill() {
        for (Player player : players) {
            GroupHidingAPI.reset(player);
        }
        for (Player player : spectators) {
            GroupHidingAPI.reset(player);
        }
        players = null;
        spectators = null;
    }

    /**
     * Check if Player is in Group.
     *
     * @param player player to be checked
     * @return is player in group?
     */
    public boolean hasPlayer(Player player) {
        return players.contains(player);
    }

    /**
     * Check if Spectator is in Group.
     *
     * @param player spectator to be checked
     * @return is spectator in group?
     */
    public boolean hasSpectator(Player player) {
        return spectators.contains(player);
    }

    /**
     * Get all Players in Group.
     *
     * @return players in group
     */
    public Set<Player> getPlayers() {
        return new HashSet<>(players);
    }

    /**
     * Get all Spectators in Group.
     *
     * @return spectators in group
     */
    public Set<Player> getSpectators() {
        return new HashSet<>(spectators);
    }

}