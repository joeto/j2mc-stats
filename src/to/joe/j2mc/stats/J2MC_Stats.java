package to.joe.j2mc.stats;

import java.io.File;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitWorker;


import to.joe.j2mc.core.J2MC_Manager;
import to.joe.j2mc.stats.util.StatsObject;

public class J2MC_Stats extends JavaPlugin {

    public Map<String, StatsObject> statObjects;
    public ConcurrentLinkedQueue<StatsObject> timeLineQueue;
    public boolean timelineEnabled;
    
    private UpdateTask updater;
    private int taskId;
    private int timeLineTaskId;
    
    public void addToQueue(String player) {
        if (this.timelineEnabled && this.timeLineQueue != null) {
            StatsObject stat = statObjects.get(player);
            if (stat != null && !this.timeLineQueue.contains(statObjects.get(player))) {
                this.timeLineQueue.add(statObjects.get(player));
            }
        }
    }

    @Override
    public void onEnable() {
        if (!new File(getDataFolder() + File.separator + "config.yml").exists()) {
            try {
                getConfig().save(new File(getDataFolder() + File.separator + "config.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        this.timelineEnabled = this.getConfig().getBoolean("enabletimeline");
        this.statObjects = new ConcurrentHashMap<String, StatsObject>();
        
        try {
            DatabaseMetaData dbmd = J2MC_Manager.getMySQL().getConnection().getMetaData();
            if (!dbmd.getTables(null, null, "stats_" + J2MC_Manager.getServerID(), null).next()) {
                this.getLogger().severe("Stats table not found");
                this.getServer().getPluginManager().disablePlugin(this);
            }
            if (timelineEnabled && !dbmd.getTables(null, null, "stats_timeline_" + J2MC_Manager.getServerID(), null).next()) {
                this.getLogger().severe("Stats timeline table not found");
                this.getServer().getPluginManager().disablePlugin(this);                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if (timelineEnabled) {
            this.timeLineQueue = new ConcurrentLinkedQueue<StatsObject>();
            this.timeLineTaskId = getServer().getScheduler().scheduleAsyncRepeatingTask(this, new TimeLineUpdateTask(this), 36000, 36000); // Run every half an hour
        }

        this.updater = new UpdateTask(this);
        this.taskId = getServer().getScheduler().scheduleAsyncRepeatingTask(this, this.updater, 1200, 1200); // Run every minute
        
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        
        this.getLogger().info("Stats module enabled");
    }

    @Override
    public void onDisable() {
        for (BukkitWorker worker : this.getServer().getScheduler().getActiveWorkers()) {
            if (worker.getThread().isAlive()) {
                continue;
            }
            if (worker.getTaskId() == taskId || worker.getTaskId() == timeLineTaskId) {
                worker.getThread().run();
            }
        }
        this.getServer().getScheduler().cancelTasks(this);

        this.getLogger().info("Stats module disabled");
    }

}
