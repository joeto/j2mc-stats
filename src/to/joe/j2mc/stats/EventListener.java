package to.joe.j2mc.stats;

import java.sql.SQLException;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import to.joe.j2mc.stats.util.StatsObject;

public class EventListener implements Listener{

    J2MC_Stats plugin;

    public EventListener(J2MC_Stats stats) {
        this.plugin = stats;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        this.plugin.statObjects.put(event.getPlayer().getName(), new StatsObject(event.getPlayer().getName()));
        this.plugin.addToQueue(event.getPlayer().getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        try {
            this.plugin.statObjects.get(event.getPlayer().getName()).getQuery().execute();
            this.plugin.addToQueue(event.getPlayer().getName());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.plugin.statObjects.remove(event.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        this.plugin.statObjects.get(event.getPlayer().getName()).blocksPlaced++;
        this.plugin.addToQueue(event.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        this.plugin.statObjects.get(event.getPlayer().getName()).blocksBroken++;
        this.plugin.addToQueue(event.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        this.plugin.statObjects.get(event.getEntity().getName()).timesDied++;
        this.plugin.addToQueue(event.getEntity().getName());
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            this.plugin.statObjects.get(killer.getName()).playersKilled++;
            this.plugin.addToQueue(killer.getName());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            return;
        }
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            this.plugin.statObjects.get(killer.getName()).mobsKilled++;
            this.plugin.addToQueue(killer.getName());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        this.plugin.statObjects.get(event.getPlayer().getName()).timesChatted++;
        this.plugin.addToQueue(event.getPlayer().getName());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        this.plugin.statObjects.get(event.getPlayer().getName()).distanceMoved += event.getFrom().distanceSquared(event.getTo());
        this.plugin.addToQueue(event.getPlayer().getName());
    }

}
