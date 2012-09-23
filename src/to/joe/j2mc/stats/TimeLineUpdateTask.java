package to.joe.j2mc.stats;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import to.joe.j2mc.core.Debug;
import to.joe.j2mc.stats.util.StatsObject;

public class TimeLineUpdateTask implements Runnable {
    
    private J2MC_Stats plugin;

    public TimeLineUpdateTask(J2MC_Stats stats) {
        this.plugin = stats;
    }

    @Override
    public void run() {
        StatsObject stat;
        while ((stat = this.plugin.timeLineQueue.poll()) != null ) {
            try {
                PreparedStatement query = stat.getQuery();
                Debug.log("Processing update for: " + stat.toString() + ", query: " + query.toString());
                query.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
