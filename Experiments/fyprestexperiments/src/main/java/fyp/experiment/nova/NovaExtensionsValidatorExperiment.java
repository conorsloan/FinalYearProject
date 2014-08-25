package fyp.experiment.nova;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import fyp.client.OpenstackRestClient;
import fyp.client.keystone.KeystoneClient;
import fyp.client.keystone.KeystoneClientImpl;
import fyp.client.nova.NovaClient;
import fyp.client.nova.NovaClientImpl;
import fyp.client.nova.NovaExtensionsClient;
import fyp.client.nova.data.Host;
import fyp.client.nova.data.HostInfo;
import fyp.client.nova.data.Server;
import fyp.experiment.Experiment;
import fyp.utils.NovaUtils;
import fyp.utils.NovaUtils.ServerStatus;

public class NovaExtensionsValidatorExperiment extends Experiment {

	private static final Logger logger = LogManager
			.getLogger(NovaExtensionsValidatorExperiment.class.getName());

	private NovaClient novaClient;
	private KeystoneClient keystoneClient;
	private NovaExtensionsClient novaExtensionsClient;

	private String flavorRef;

	private String imageRef;

	@Override
	public String getName() {
		return "Nova Extensions Validator";
	}

	@Override
	public String getDescription() {
		return "Validates Nova Extensions Functionality";
	}

	@Override
	public void setUp() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Setting up - retrieving and validating nova & keystone clients.");
		}

		novaClient = new NovaClientImpl(config, restTemplate);
		keystoneClient = new KeystoneClientImpl(config, restTemplate);
		novaExtensionsClient = novaClient.getExtensionsClient();

		// Validate nova & keystone clients
		if (!((OpenstackRestClient) novaClient).validateClient()
				|| !((OpenstackRestClient) keystoneClient).validateClient()
				|| !((OpenstackRestClient) novaExtensionsClient).validateClient()) {
			throw new Exception("Experiment setup failed: One of openstack clients invalid");
		}

		// Authenticate session with Identity Service
		String authToken = keystoneClient.authenticate(config.getUsername(), config.getPassword(),
				config.getTenantName());
		novaClient.setAuthToken(authToken);
		novaExtensionsClient.setAuthToken(authToken);
		NovaUtils.deleteAllServers(novaClient);

		// Get standard flavor & image links for creating servers
		flavorRef = novaClient.getFlavorInfo().get(0).getLinks().get(0).getHref();
		imageRef = novaClient.getImageInfo().get(0).getLinks().get(0).getHref();

		if (logger.isDebugEnabled()) {
			logger.debug("Finished setup.");
		}

	}

	@Override
	public void execute() throws Exception {

		// Perform experiments
		logger.info("\n");
		getHostsExperiment();
		logger.info("\n");
		serverPauseExperiment();
		logger.info("\n");
		serverSuspendExperiment();
		logger.info("\n");
		serverLockExperiment();
		logger.info("\n");
		logger.info("Finished");
	}

	public void getHostsExperiment() throws Exception {
		// Get hosts
		logger.info("Printing Hosts Info");
		List<HostInfo> hosts = novaExtensionsClient.getHostInfo();
		for (HostInfo host : hosts) {
			logger.info(host);
		}
		// Get a particular host by id
		String hostName = hosts.get(0).getHostName();
		logger.info("Retrieving a host by name: " + hostName);
		Host host = novaExtensionsClient.getHostByHostName(hostName);
		logger.info("Host: " + host);
		logger.info("Finished");
	}

	public void serverPauseExperiment() throws Exception {

		logger.info("Performing Server Pause & Unpause");

		// Create a Server
		logger.info("Creating a Server");
		String serverId = novaClient.getServerById(
				novaClient.createServerFromImage("test", flavorRef, imageRef).getId()).getId();
		logger.info("Got a server, ID: " + serverId);
		NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.ACTIVE);

		// Pause & Unpause
		logger.info("Pausing Server...");
		novaExtensionsClient.pauseServer(serverId);
		NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.PAUSED);
		Server server = novaClient.getServerById(serverId);
		if (ServerStatus.valueOf(server.getStatus()) == ServerStatus.PAUSED) {
			logger.info("Paused successfully!");
		} else {
			logger.info("Server did not pause successfully..");
			throw new Exception("Server did not pause before timeout");
		}

		// Unpause it, then check it is active again
		logger.info("Unpausing Server...");
		novaExtensionsClient.unpauseServer(serverId);
		NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.ACTIVE);
		server = novaClient.getServerById(serverId);
		if (ServerStatus.valueOf(server.getStatus()) == ServerStatus.ACTIVE) {
			logger.info("Unpaused successfully!");
		} else {
			logger.info("Server did not unpause");
			throw new Exception("Server did not unpause before timeout");
		}

		logger.info("Finished pause & unpause, deleting server..");
		NovaUtils.deleteAllServers(novaClient);
		logger.info("Server deleted successfully");
	}

	public void serverSuspendExperiment() throws Exception {
		logger.info("Performing Server Suspend & Resume");

		// Create a Server
		logger.info("Creating a Server");
		String serverId = novaClient.getServerById(
				novaClient.createServerFromImage("test", flavorRef, imageRef).getId()).getId();
		logger.info("Got a server, ID: " + serverId);
		NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.ACTIVE);

		// Pause & Unpause
		logger.info("Suspending Server...");
		novaExtensionsClient.suspendServer(serverId);
		NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.SUSPENDED);
		Server server = novaClient.getServerById(serverId);
		if (ServerStatus.valueOf(server.getStatus()) == ServerStatus.SUSPENDED) {
			logger.info("Suspended successfully!");
		} else {
			logger.info("Server did not suspend successfully..");
			throw new Exception("Server did not suspend before timeout");
		}

		// Unpause it, then check it is active again
		logger.info("Resuming Server...");
		novaExtensionsClient.resumeServer(serverId);
		NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.ACTIVE);
		server = novaClient.getServerById(serverId);
		if (ServerStatus.valueOf(server.getStatus()) == ServerStatus.ACTIVE) {
			logger.info("Resumed successfully!");
		} else {
			logger.info("Server did not resume..");
			throw new Exception("Server did not resume before timeout");
		}

		logger.info("Finished suspend & resume, deleting server..");
		NovaUtils.deleteAllServers(novaClient);
		logger.info("Server deleted successfully");
	}

	public void serverLockExperiment() throws Exception {
		logger.info("Performing Server Lock & Unlock");

		// Create a Server
		logger.info("Creating a Server");
		String serverId = novaClient.getServerById(
				novaClient.createServerFromImage("test", flavorRef, imageRef).getId()).getId();
		logger.info("Got a server, ID: " + serverId);
		NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.ACTIVE);

		// Pause & Unpause
		logger.info("Locking Server...");
		novaExtensionsClient.suspendServer(serverId);
		NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.SUSPENDED);
		Server server = novaClient.getServerById(serverId);
		if (ServerStatus.valueOf(server.getStatus()) == ServerStatus.SUSPENDED) {
			logger.info("Locked successfully!");
		} else {
			logger.info("Server did not lock successfully..");
			throw new Exception("Server did not lock before timeout");
		}

		// Unpause it, then check it is active again
		logger.info("Unlocking Server...");
		novaExtensionsClient.resumeServer(serverId);
		NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.ACTIVE);
		server = novaClient.getServerById(serverId);
		if (ServerStatus.valueOf(server.getStatus()) == ServerStatus.ACTIVE) {
			logger.info("Unlocked successfully!");
		} else {
			logger.info("Server did not unlock..");
			throw new Exception("Server did not unlock before timeout");
		}

		logger.info("Finished lock & unlock, deleting server..");
		NovaUtils.deleteAllServers(novaClient);
		logger.info("Server deleted successfully");
	}

	public void serverMigrateExperiment() throws Exception {
		// TODO
	}

	@Override
	public void tearDown() throws Exception {
		// TODO Auto-generated method stub

	}
}
