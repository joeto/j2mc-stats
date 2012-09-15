package to.joe.j2mc.stats;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitWorker;

import to.joe.j2mc.stats.util.StatsObject;

public class J2MC_Stats extends JavaPlugin {

    public Map<String, StatsObject> statObjects;
    private UpdateTask updater;
    private int taskId;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);

        this.statObjects = new ConcurrentHashMap<String, StatsObject>();

        this.updater = new UpdateTask(this);
        this.taskId = getServer().getScheduler().scheduleAsyncRepeatingTask(this, this.updater, 1200, 1200);
        
        this.getLogger().info("Stats module enabled");
    }

    @Override
    public void onDisable() {
        for (BukkitWorker worker : this.getServer().getScheduler().getActiveWorkers()) {
            if (worker.getThread().isAlive()) {
                continue;
            }
            if (worker.getTaskId() == taskId) {
                worker.getThread().run();
            }
        }
        this.getServer().getScheduler().cancelTask(taskId);

        this.getLogger().info("Stats module disabled");
    }

}
