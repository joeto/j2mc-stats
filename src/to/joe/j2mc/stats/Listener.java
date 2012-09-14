package to.joe.j2mc.stats;

import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import to.joe.j2mc.stats.util.StatsObject;

public class Listener {
    
    J2MC_Stats plugin;
    
    public Listener (J2MC_Stats stats) {
        this.plugin = stats;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        this.plugin.statObjects.put(event.getPlayer().getName(), new StatsObject(event.getPlayer().getName()));
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        try {
            this.plugin.statObjects.get(event.getPlayer().getName()).getQuery().execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.plugin.statObjects.remove(event.getPlayer().getName());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        this.plugin.statObjects.get(event.getPlayer().getName()).blocksPlaced++;
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        this.plugin.statObjects.get(event.getPlayer().getName()).blocksBroken++;
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        this.plugin.statObjects.get(event.getEntity().getName()).timesDied++;
    }

}
