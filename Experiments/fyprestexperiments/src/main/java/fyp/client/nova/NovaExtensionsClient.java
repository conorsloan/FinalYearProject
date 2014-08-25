package fyp.client.nova;

import java.util.List;

import fyp.client.nova.data.Host;
import fyp.client.nova.data.HostInfo;

public interface NovaExtensionsClient {

	public void setAuthToken(String authToken);

	public void pauseServer(String serverId);

	public void unpauseServer(String serverId);

	public void suspendServer(String serverId);

	public void resumeServer(String serverId);

	public void migrateServer(String serverId);

	public void lockServer(String serverId);

	public void unlockServer(String serverId);

	public void migrateServer(String serverId, String hostId, boolean blockMigration, boolean diskOvercommit);

	public List<HostInfo> getHostInfo();

	public Host getHostByHostName(String hostName);

	// public void startHost(String hostId);

	// public void shutdownHost(String hostId);

	// public void rebootHost(String hostId);
}
