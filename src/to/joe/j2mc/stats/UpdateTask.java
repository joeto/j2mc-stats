package to.joe.j2mc.stats;

import java.sql.PreparedStatement;
import java.util.Iterator;

import to.joe.j2mc.stats.util.StatsObject;

public class UpdateTask implements Runnable {

    private J2MC_Stats plugin;

    public UpdateTask(J2MC_Stats stats) {
        this.plugin = stats;
    }

    @Override
    public void run() {
        Iterator<StatsObject> iterator = (plugin.statObjects).values().iterator();
        while (iterator.hasNext()) {
            final StatsObject stat = iterator.next();
            try {
                PreparedStatement query = stat.getQuery();
                query.executeUpdate();
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to update stats for " + stat.user + ": " + e.getMessage());
            }
        }
    }

}
