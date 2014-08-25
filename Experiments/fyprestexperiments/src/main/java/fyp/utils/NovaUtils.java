package fyp.utils;

import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import fyp.client.nova.NovaClient;
import fyp.client.nova.data.Server;
import fyp.client.nova.response.GetServerInfoResponse.ServerInfo;

public class NovaUtils {

	private static final Logger logger = LogManager.getLogger(NovaUtils.class.getName());
	private static final long DEFAULT_TIMEOUT = 25000;

	public static void deleteAllServers(NovaClient nova) {
		// logger.info("Deleting all VMs");
		for (ServerInfo s : nova.getServerInfo()) {
			nova.deleteServer(s.getId());
		}
		while (nova.getServerInfo().size() != 0) {

		}
		// logger.info("Deleted? " + nova.getServerInfo().size());
	}

	public static void waitForMultipleServerBuild(NovaClient novaClient, int numberOfVms) {
		List<Server> servers;
		boolean VMStillBuilding;
		boolean serverInErrorState = false;
		while (true) {
			servers = novaClient.getServers();
			VMStillBuilding = false;
			// If we don't have the right no. VMs, definitely not ready
			if (servers.size() != numberOfVms) {
				continue;
			}
			// Check if any are building
			for (Server server : servers) {
				if (server.getStatus().equals("BUILD")) {
					VMStillBuilding = true;
				} else if (server.getStatus().equals("ERROR")) {
					serverInErrorState = true;
				}
			}
			// If none are building, we are done
			if (!VMStillBuilding) {
				if (serverInErrorState) {
					logger.info("One or more servers was in Error state");
				}
				return;
			}
		}
	}

	public static void waitForServerBuildById(NovaClient novaClient, String serverId) {
		Server server;
		while (true) {
			server = novaClient.getServerById(serverId);
			ServerStatus status = ServerStatus.valueOf(server.getStatus());
			if (status == ServerStatus.ACTIVE || status == ServerStatus.ERROR) {
				return;
			}
		}
	}

	public static void waitForServerDeleteById(NovaClient novaClient, String serverId) {
		boolean found;
		while (true) {
			found = false;
			for (ServerInfo server : novaClient.getServerInfo()) {
				if (server.getId().equals(serverId)) {
					found = true;
				}
			}
			if (!found) {
				return;
			}
		}
	}

	public static void deleteServerAndWait(NovaClient novaClient, String serverId) {
		novaClient.deleteServer(serverId);
		waitForServerDeleteById(novaClient, serverId);
	}

	public static void waitForServerState(NovaClient novaClient, String serverId,
			ServerStatus status) {
		waitForServerState(novaClient, serverId, status, DEFAULT_TIMEOUT);
	}

	public static void waitForServerState(NovaClient novaClient, String serverId,
			ServerStatus status, long timeout) {
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0L;
		Server server;
		ServerStatus currentStatus;
		while (true) {
			server = novaClient.getServerById(serverId);
			currentStatus = ServerStatus.valueOf(server.getStatus());
			if (currentStatus == status) {
				return;
			}

			// perform time check, return if past the timeout
			elapsedTime = (new Date()).getTime() - startTime;
			if (elapsedTime > timeout) {
				return;
			}
		}

	}

	public enum ServerStatus {
		ERROR, ACTIVE, BUILD, PAUSED, SUSPENDED
	}

}
