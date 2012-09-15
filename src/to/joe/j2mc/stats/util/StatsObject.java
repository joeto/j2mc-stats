package to.joe.j2mc.stats.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import to.joe.j2mc.core.J2MC_Manager;

public class StatsObject {

    public String user;
    private int id;

    public int blocksPlaced;
    public int blocksBroken;
    public int playersKilled;
    public int mobsKilled;
    public int timesDied;
    public int timesChatted;
    public double distanceMoved; // For performance reasons this is the squared
                                 // value of the distance, needs to be square
                                 // rooted to obtain the proper distance

    public StatsObject(String user) {
        try {

            PreparedStatement idQuery = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT `id` FROM `users` WHERE `name`=?");
            idQuery.setString(1, user);
            ResultSet idResult = idQuery.executeQuery();

            if (!idResult.next()) {
                throw new IllegalArgumentException("Invalid player name: " + user + "!");
            } else {
                this.id = idResult.getInt("id");
            }

            PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT * FROM `stats_" + J2MC_Manager.getServerID() + "` WHERE `id`=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                // No existing row for the user in table
                PreparedStatement insert = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("INSERT INTO `stats_" + J2MC_Manager.getServerID() + "` (`id`) VALUES (?)");
                insert.setInt(1, id);
                insert.executeUpdate();

                this.blocksPlaced = 0;
                this.blocksBroken = 0;
                this.playersKilled = 0;
                this.mobsKilled = 0;
                this.timesDied = 0;
                this.timesChatted = 0;
                this.distanceMoved = 0;

                this.user = user;

            } else {
                // Row found, read from there
                this.user = user;

                this.blocksPlaced = rs.getInt("blocks_placed");
                this.blocksBroken = rs.getInt("blocks_broken");
                this.playersKilled = rs.getInt("players_killed");
                this.mobsKilled = rs.getInt("mobs_killed");
                this.timesDied = rs.getInt("times_died");
                this.timesChatted = rs.getInt("times_chatted");
                this.distanceMoved = Math.pow(rs.getDouble("distance_moved"), 2); // needs to be distance ^ 2 to conform with standard set here
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int hashCode() {
        return user.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof StatsObject) {
            return ((StatsObject) obj).user.equalsIgnoreCase(this.user);
        }
        if (obj instanceof String) {
            return ((String) obj).equalsIgnoreCase(this.user);
        }
        return false;
    }

    @Override
    public String toString() {
        return "[ " + user + ";" + id + ": " + blocksPlaced + ", " + blocksBroken + ", " + playersKilled + ", " + mobsKilled + ", " + timesDied + ", " + timesChatted + ", " + Math.sqrt(distanceMoved) + "]";
    }

    public PreparedStatement getQuery() {
        try {
            String sql = "Update `stats_" + J2MC_Manager.getServerID() + "` SET `blocks_placed`=?, `blocks_broken`=?, `players_killed`=?, mobs_killed=?, `times_died`=?, `times_chatted`=?, `distance_moved`=? WHERE `id`=?";
            PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven(sql);
            ps.setInt(1, blocksPlaced);
            ps.setInt(2, blocksBroken);
            ps.setInt(3, playersKilled);
            ps.setInt(4, mobsKilled);
            ps.setInt(5, timesDied);
            ps.setInt(6, timesChatted);
            ps.setDouble(7, Math.sqrt(distanceMoved));
            ps.setInt(8, id);
            return ps;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PreparedStatement addToTimeline() {
        try {
            String sql = "INSERT INTO `stats_timeline_" + J2MC_Manager.getServerID() + "` VALUES (? , ? , ? , ? , ? , ? , ? , ?, ?)";
            PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven(sql);
            ps.setInt(1, id);
            ps.setInt(2, blocksPlaced);
            ps.setInt(3, blocksBroken);
            ps.setInt(4, playersKilled);
            ps.setInt(5, mobsKilled);
            ps.setInt(6, timesDied);
            ps.setInt(7, timesChatted);
            ps.setDouble(8, Math.sqrt(distanceMoved));
            ps.setLong(9, (System.currentTimeMillis() / 1000));
            return ps;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
