package com.ullarah.tcgmcau.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.ullarah.tcgmcau.mConfig;
import com.ullarah.tcgmcau.mInit;

public class sFunction extends Thread {

    private final String sqlUsername = mConfig.getConfig().getString("connection.user");
    private final String sqlPassword = mConfig.getConfig().getString("connection.pass");

    private final String sqlHostName = mConfig.getConfig().getString("connection.host");
    private final String sqlPort     = mConfig.getConfig().getString("connection.port");

    private final String sqlDatabase = mConfig.getConfig().getString("connection.name");

    private BoneCP sqlConnectionPool = null;
    private Connection sqlConnection = null;

    public Connection sqlOpenConnection() {

        if ( sqlConnection == null ){

            try {

                BoneCPConfig bcpConfig = new BoneCPConfig();

                bcpConfig.setJdbcUrl( "jdbc:mysql://" + this.sqlHostName + ":" + this.sqlPort + "/" + this.sqlDatabase );

                bcpConfig.setUsername( this.sqlUsername );
                bcpConfig.setPassword( this.sqlPassword );

                bcpConfig.setMinConnectionsPerPartition(3);
                bcpConfig.setMaxConnectionsPerPartition(10);
                bcpConfig.setPartitionCount(1);

                bcpConfig.setIdleMaxAgeInSeconds(60);
                bcpConfig.setIdleConnectionTestPeriodInSeconds(30);

                sqlConnectionPool = new BoneCP(bcpConfig);
                sqlConnection = sqlConnectionPool.getConnection();

            } catch (CommunicationsException e) {

                sqlMaintenaceConnection();

            } catch (SQLException e) {

                mInit.getPlugin().getLogger().log( Level.SEVERE, "JDBC CONNECT ERROR: " + e.getMessage() );

            }

        }

        return sqlConnection;

    }

    boolean sqlCheckConnection() {

        return sqlConnection != null;

    }

    Connection sqlGetConnection() {

        return sqlConnection;

    }

    void sqlMaintenaceConnection() {

        mInit.setMaintenanceCheck(true);

        sqlCloseConnection();

        mConfig.getConfig().set("maintenance", true);
        mConfig.saveConfig();

    }

    public void sqlCloseConnection() {

        if (sqlConnection != null) try {

            sqlConnection.close();
            try {
                sqlConnectionPool.close();
            }
            catch (NoClassDefFoundError e) {
                // XXX: GROSS HACK
                /* Some Bukkit implementations use an old version of Guava where
                 * FinalizableReferenceQueue does not implement java.io.Closeable.
                 * BoneCP attempts to call .close() on its queue as the final act
                 * in its shutdown process. This throws a NoClassDefFoundError on
                 * the old Guava versions, which we catch and discard.
                 */
            }

        } catch (SQLException e) {

            mInit.getPlugin().getLogger().log( Level.SEVERE, "JDBC CLOSE ERROR: " + e.getMessage() );

        }

    }

    /**
     * Performs a prepared SQL query safely.
     * @param query Query string with parameters replaced with "?" placeholders.
     * @param params A list of string parameters.
     * @return The ResultSet.
     */
    public ResultSet sqlQuery(String query, String[] params) {

        Connection currentConnection;

        if( sqlCheckConnection() ) currentConnection = sqlGetConnection();
        else currentConnection = sqlOpenConnection();

        try {

            PreparedStatement s = currentConnection.prepareStatement(query);

            for (int param = 0; param < params.length; param++) s.setString(param + 1, params[param]);

            return s.executeQuery();

        } catch (CommunicationsException e) {

            sqlMaintenaceConnection();

        } catch (SQLException e) {

            mInit.getPlugin().getLogger().log(Level.SEVERE, "JDBC QUERY ERROR: " + e.getMessage());

            mInit.setMaintenanceCheck(true);

        }

        return null;

    }

    /**
     * Performs a prepared SQL update safely.
     * @param update Update string with parameters replaced with "?" placeholders.
     * @param params A list of string parameters.
     */
    public void sqlUpdate(String update, String[] params){

        Connection currentConnection;

        if( sqlCheckConnection() ) currentConnection = sqlGetConnection();
        else currentConnection = sqlOpenConnection();

        try {

            PreparedStatement s = currentConnection.prepareStatement(update);

            for (int param = 0; param < params.length; param++) s.setString(param + 1, params[param]);

        } catch (CommunicationsException e) {

            sqlMaintenaceConnection();

        } catch (SQLException e) {

            mInit.getPlugin().getLogger().log(Level.SEVERE, "JDBC UPDATE ERROR: " + e.getMessage());

            mInit.setMaintenanceCheck(true);

        }

    }

}
