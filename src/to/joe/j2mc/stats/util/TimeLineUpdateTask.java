package to.joe.j2mc.stats.util;

import java.sql.SQLException;

import to.joe.j2mc.stats.J2MC_Stats;

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
                stat.addToTimeline().executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
