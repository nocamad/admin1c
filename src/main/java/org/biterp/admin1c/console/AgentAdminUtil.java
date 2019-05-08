package org.biterp.admin1c.console;

import java.util.List;
import java.util.UUID;

import com._1c.v8.ibis.admin.*;
import com._1c.v8.ibis.admin.client.IAgentAdminConnector;
import com._1c.v8.ibis.admin.client.IAgentAdminConnectorFactory;

/**
 * Utility class for interaction with the administration server of 1C:Enterprise
 * server cluster
 */
public  final  class AgentAdminUtil {
    private final IAgentAdminConnectorFactory factory;

    private IAgentAdminConnector connector;
    private IAgentAdminConnection connection;

    public AgentAdminUtil(IAgentAdminConnectorFactory factory) {
        this.factory = factory;
    }

    /**
     * Establishes connection with the administration server of 1C:Enterprise
     * server cluster
     *
     * @param address server address
     * @param port    IP port
     * @param timeout connection timeout (in milliseconds)
     * @throws AgentAdminException in the case of errors.
     */
    public synchronized void connect(String address, int port, long timeout) {
        if (connection != null) {
            throw new IllegalStateException("The connection is already established.");
        }

        connector = factory.createConnector(timeout);
        connection = connector.connect(address, port);
    }

    /**
     * Checks whether connection to the administration server is established
     *
     * @return {@code true} if connected, {@code false} overwise
     */
    public synchronized boolean  isConnected() {
        return connection != null;
    }

    /**
     * Terminates connection to the administration server
     *
     * @throws AgentAdminException in the case of errors.
     */
    public synchronized void disconnect() {
        if (connection == null) {
            throw new IllegalStateException("The connection is not established.");
        }

        try {
            connector.shutdown();
        } finally {
            connection = null;
            connector = null;
        }
    }

    /**
     * Performs cluster authentication
     *
     * @param clusterId cluster ID
     * @param userName  cluster administrator name
     * @param password  cluster administrator password
     */
    public synchronized void authenticateCluster(UUID clusterId, String userName, String password) {
        if (connection == null) {
            throw new IllegalStateException("The connection is not established.");
        }

        connection.authenticate(clusterId, userName, password);
    }

    /**
     * Adds infobase authentication parameters to the context
     * of the current administration server connection
     *
     * @param clusterId cluster ID
     * @param userName  infobase administrator name
     * @param password  infobase administrator password
     */
    public synchronized void addInfoBaseCredentials(UUID clusterId, String userName,
                                       String password) {
        if (connection == null) {
            throw new IllegalStateException("The connection is not established.");
        }

        connection.addAuthentication(clusterId, userName, password);
    }

    /**
     * Gets the list of cluster descriptions
     *
     * @return list of cluster descriptions
     */
    public synchronized List<IClusterInfo> getClusterInfoList() {
        if (connection == null) {
            throw new IllegalStateException("The connection is not established.");
        }

        return connection.getClusters();
    }

    /**
     * Gets the list of short descriptions of cluster infobases
     *
     * @param clusterId cluster ID
     * @return list of short descriptions of cluster infobases
     */
    public synchronized List<IInfoBaseInfoShort> getInfoBaseShortInfos(UUID clusterId) {
        if (connection == null) {
            throw new IllegalStateException("The connection is not established.");
        }
        return connection.getInfoBasesShort(clusterId);
    }

    /**
     * Gets the full infobase description
     *
     * @param clusterId  cluster ID
     * @param infoBaseId infobase ID
     * @return infobase full infobase description
     */
    public synchronized IInfoBaseInfo getInfoBaseInfo(UUID clusterId, UUID infoBaseId) {
        if (connection == null) {
            throw new IllegalStateException("The connection is not established.");
        }

        return connection.getInfoBaseInfo(clusterId, infoBaseId);
    }

    public synchronized IInfoBaseInfoShort getInfoBaseInfoShort(UUID clusterId, UUID infoBaseId) {
        if (connection == null) {
            throw new IllegalStateException("The connection is not established.");
        }

        return connection.getInfoBaseShortInfo(clusterId, infoBaseId);
    }

    /**
     * Updates infobase parameters
     *
     * @param clusterId cluster ID
     * @param info      infobase parameters
     */
    public synchronized void updateInfoBase(UUID clusterId, IInfoBaseInfo info) {
        if (connection == null) {
            throw new IllegalStateException("The connection is not established.");
        }

        connection.updateInfoBase(clusterId, info);
    }

    /**
     * Terminates all sessions for all infobases in the cluster
     *
     * @param clusterId cluster ID
     */
    public synchronized void terminateAllSessions(UUID clusterId) {
        if (connection == null) {
            throw new IllegalStateException("The connection is not established.");
        }

        List<ISessionInfo> sessions = getSessions(clusterId);
        for (ISessionInfo session : sessions) {
            connection.terminateSession(clusterId, session.getSid());
        }
    }

    public synchronized List<ISessionInfo> getSessions(UUID clusterId) {
        return connection.getSessions(clusterId);
    }
}
