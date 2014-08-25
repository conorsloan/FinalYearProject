package fyp.experiment.warmup;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import fyp.client.OpenstackRestClient;
import fyp.client.keystone.KeystoneClient;
import fyp.client.keystone.KeystoneClientImpl;
import fyp.client.nova.NovaClient;
import fyp.client.nova.NovaClientImpl;
import fyp.client.nova.response.CreateServerResponse.CreatedServerInfo;
import fyp.client.nova.response.GetFlavorInfoResponse.FlavorInfo;
import fyp.client.nova.response.GetImageInfoResponse.ImageInfo;
import fyp.client.nova.response.GetServerInfoResponse.ServerInfo;
import fyp.config.OpenstackConfig;
import fyp.experiment.Experiment;

/**
 * Created by Conor on 18/03/14.
 */
public class LaunchInstanceExperiment extends Experiment {

	private final static String SERVER_NAME = "testServer";
	private static final Logger logger = LogManager.getLogger(LaunchInstanceExperiment.class.getName());

	private NovaClient novaClient;
	private KeystoneClient keystoneClient;

	public LaunchInstanceExperiment(OpenstackConfig config, RestTemplate restTemplate) {
		super(config, restTemplate);
	}

	public LaunchInstanceExperiment() {

	}

	@Override
	public void setUp() throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Setting up - retrieving and validating nova & keystone clients.");
		}

		novaClient = new NovaClientImpl(config, restTemplate);
		keystoneClient = new KeystoneClientImpl(config, restTemplate);

		// Validate nova & keystone clients
		if (!((OpenstackRestClient) novaClient).validateClient() || !((OpenstackRestClient) keystoneClient).validateClient()) {
			throw new Exception("Experiment setup failed: One of openstack clients invalid");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Finished setup.");
		}
	}

	/**
	 * In this experiment, the keystone API is used to authenticate a session,
	 * and following this, the nova API is used to list flavours, images and
	 * servers, and to create a new server instance.
	 */
	@Override
	public void execute() throws Exception {

		// Authenticate session with Identity Service
		String authToken = keystoneClient.authenticate(config.getUsername(), config.getPassword(), config.getTenantName());
		novaClient.setAuthToken(authToken);

		// Get servers & print details
		logger.info("Servers:");
		List<ServerInfo> servers = novaClient.getServerInfo();
		for (ServerInfo s : servers) {
			logger.info(s);
		}
		logger.info("Number of Servers Before: " + servers.size());
		int serversBefore = servers.size();

		// Now for the flavors
		logger.info("Flavors:");
		List<FlavorInfo> flavors = novaClient.getFlavorInfo();
		for (FlavorInfo f : flavors) {
			logger.info(f);
		}

		// And now images
		logger.info("Images:");
		List<ImageInfo> images = novaClient.getImageInfo();
		for (ImageInfo i : images) {
			logger.info(i);
		}

		// Create a server instance
		CreatedServerInfo createdServer = novaClient.createServerFromImage(SERVER_NAME, flavors.get(0).getLinks().get(0).getHref(), images
				.get(0).getLinks().get(0).getHref());

		logger.info("Admin Pass: " + createdServer.getAdminPass());

		// Retrieve servers again
		List<ServerInfo> serversNow = novaClient.getServerInfo();
		logger.info("Number of Servers After: " + serversNow.size());

		if (serversNow.size() == serversBefore + 1) {
			logger.info("Success - One server instance was created");
		} else {
			throw new Exception("Experiment failed - no server instance was created");
		}
	}

	@Override
	public void tearDown() {
		// TODO delete the image
	}

	@Override
	public String getName() {
		return "Warm Up Exercise - Launching an Instance";
	}

	@Override
	public String getDescription() {
		return "In this experiment, a new server instance is launched and checked, using" + "the Nova and Keystone APIs.";
	}
}
