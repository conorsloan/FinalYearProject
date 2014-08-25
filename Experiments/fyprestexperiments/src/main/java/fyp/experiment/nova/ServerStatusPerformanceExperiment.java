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
import fyp.client.nova.data.Server;
import fyp.client.nova.response.GetFlavorInfoResponse.FlavorInfo;
import fyp.experiment.Experiment;
import fyp.utils.NovaUtils;
import fyp.utils.NovaUtils.ServerStatus;

public class ServerStatusPerformanceExperiment extends Experiment {

	private static final Logger logger = LogManager
			.getLogger(ServerStatusPerformanceExperiment.class.getName());
	private final static int REPETITIONS = 2;
	private final static String SERVER_NAME = "testServer";
	private NovaClient novaClient;
	private KeystoneClient keystoneClient;
	private NovaExtensionsClient novaExtensionsClient;
	private String imageRef;
	private String experimentServerId;

	@Override
	public String getName() {
		return "Server Staus Performance Experiment";
	}

	@Override
	public String getDescription() {
		return "This experiment times the changing of state of different sized servers, e.g. locking/suspending";
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
		imageRef = novaClient.getImageInfo().get(0).getLinks().get(0).getHref();

		if (logger.isDebugEnabled()) {
			logger.debug("Finished setup.");
		}
	}

	@Override
	public void execute() throws Exception {

		// Get all flavors:
		List<FlavorInfo> flavors = novaClient.getFlavorInfo();
		for (FlavorInfo flavor : flavors) {
			logger.info("Starting for Flavor: " + flavor.getName());
			logger.info("Creating Server..");

			// Create one server to work with
			experimentServerId = novaClient.createServerFromImage(SERVER_NAME,
					flavor.getLinks().get(0).getHref(), imageRef).getId();
			NovaUtils.waitForServerState(novaClient, experimentServerId, ServerStatus.ACTIVE);
			// Check created server is in ACTIVE state, if it isn't this won't
			// work
			Server server = novaClient.getServerById(experimentServerId);
			if (ServerStatus.valueOf(server.getStatus()) != ServerStatus.ACTIVE) {
				throw new Exception("Newly created server was not in active state");
			}
			logger.info("Created Server Successfully");

			// Now, perform experiments
			timePauseUnpause(server);
			timeSuspendResume(server);
			timeLockUnlock(server);

			// Delete all servers
			NovaUtils.deleteAllServers(novaClient);
		}
	}

	private void timePauseUnpause(Server server) {

		long pauseSum = 0L;
		long unpauseSum = 0L;
		long startTime = 0L;
		String serverId = server.getId();

		for (int i = 1; i <= REPETITIONS; i++) {
			// Pause
			startTime = System.nanoTime();
			novaExtensionsClient.pauseServer(serverId);
			NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.PAUSED);
			pauseSum += (System.nanoTime() - startTime);

			// Unpause
			startTime = System.nanoTime();
			novaExtensionsClient.unpauseServer(serverId);
			NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.ACTIVE);
			unpauseSum += (System.nanoTime() - startTime);
		}

		logger.info("Average Pause Time: " + (pauseSum / REPETITIONS));
		logger.info("Average Unpause Time: " + (unpauseSum / REPETITIONS));
	}

	private void timeSuspendResume(Server server) {

		long suspendSum = 0L;
		long resumeSum = 0L;
		long startTime = 0L;
		String serverId = server.getId();

		for (int i = 1; i <= REPETITIONS; i++) {
			// Suspend
			startTime = System.nanoTime();
			novaExtensionsClient.suspendServer(serverId);
			NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.SUSPENDED);
			suspendSum += (System.nanoTime() - startTime);

			// Resume
			startTime = System.nanoTime();
			novaExtensionsClient.resumeServer(serverId);
			NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.ACTIVE);
			resumeSum += (System.nanoTime() - startTime);
		}

		logger.info("Average Suspend Time: " + (suspendSum / REPETITIONS));
		logger.info("Average Resume Time: " + (resumeSum / REPETITIONS));
	}

	private void timeLockUnlock(Server server) {
		long lockSum = 0L;
		long unlockSum = 0L;
		long startTime = 0L;
		String serverId = server.getId();

		for (int i = 1; i <= REPETITIONS; i++) {
			// Lock
			startTime = System.nanoTime();
			novaExtensionsClient.pauseServer(serverId);
			NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.PAUSED);
			lockSum += (System.nanoTime() - startTime);

			// Unlock
			startTime = System.nanoTime();
			novaExtensionsClient.unpauseServer(serverId);
			NovaUtils.waitForServerState(novaClient, serverId, ServerStatus.ACTIVE);
			unlockSum += (System.nanoTime() - startTime);
		}

		logger.info("Average Lock Time: " + (lockSum / REPETITIONS));
		logger.info("Average Unlock Time: " + (unlockSum / REPETITIONS));
	}

	@Override
	public void tearDown() throws Exception {
		NovaUtils.deleteAllServers(novaClient);
	}
}
