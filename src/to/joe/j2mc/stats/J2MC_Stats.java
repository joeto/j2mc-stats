package to.joe.j2mc.stats;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import to.joe.j2mc.stats.util.StatsObject;

public class J2MC_Stats extends JavaPlugin {
    
    public Map<String, StatsObject> statObjects;
    private UpdateTask updater;

    @Override
    public void onEnable() {
        this.getLogger().info("Stats module enabled");
        
        this.statObjects = new HashMap<String, StatsObject>();
        
        this.updater = new UpdateTask(this);
        this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, this.updater, 1200, 1200);
    }
    
    @Override
    public void onDisable() {
        this.updater.run();
        
        this.getLogger().info("Stats module disabled");
    }
    
}
