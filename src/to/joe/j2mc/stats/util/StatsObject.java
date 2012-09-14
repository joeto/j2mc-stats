package to.joe.j2mc.stats.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import to.joe.j2mc.core.J2MC_Manager;

public class StatsObject {
    
    public String user;
    
    public int blocksPlaced;
    public int blocksBroken;
    public int playersKilled;
    public int timesDied;

    public StatsObject(String user) {
        try {

            PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("SELECT * FROM `stats` WHERE `user`=?");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                // No existing row for the user in table
                PreparedStatement insert = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("INSERT INTO `stats` (`user`) VALUES (?)");
                insert.setString(1, user);
                insert.executeUpdate();

                this.blocksPlaced = 0;
                this.blocksBroken = 0;
                this.playersKilled = 0;
                this.timesDied = 0;

                this.user = user;

            } else {
                // Row found, read from there
                this.user = user;
                this.blocksPlaced = rs.getInt("blocks_placed");
                this.blocksBroken = rs.getInt("blocks_broken");
                this.playersKilled = rs.getInt("players_killed");
                this.timesDied = rs.getInt("times_died");
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
        if (obj instanceof StatsObject) {
            return ((StatsObject) obj).user.equalsIgnoreCase(this.user);
        }
        if (obj instanceof String) {
            return ((String) obj).equalsIgnoreCase(this.user);
        }
        return false;
    }

    public PreparedStatement getQuery() {
        //TODO: return a prepared statement to update the user's row
        try {
            PreparedStatement ps = J2MC_Manager.getMySQL().getFreshPreparedStatementHotFromTheOven("");
            return ps;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
