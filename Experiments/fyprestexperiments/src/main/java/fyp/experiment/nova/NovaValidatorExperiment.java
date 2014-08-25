package fyp.experiment.nova;

import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import fyp.client.OpenstackRestClient;
import fyp.client.keystone.KeystoneClient;
import fyp.client.keystone.KeystoneClientImpl;
import fyp.client.nova.NovaClient;
import fyp.client.nova.NovaClientImpl;
import fyp.client.nova.data.Flavor;
import fyp.client.nova.data.Image;
import fyp.client.nova.data.Server;
import fyp.client.nova.response.GetFlavorInfoResponse.FlavorInfo;
import fyp.client.nova.response.GetImageInfoResponse.ImageInfo;
import fyp.client.nova.response.GetServerInfoResponse.ServerInfo;
import fyp.experiment.Experiment;
import fyp.utils.NovaUtils;
import fyp.utils.WebServiceUtils;
import fyp.utils.NovaUtils.ServerStatus;

public class NovaValidatorExperiment extends Experiment {

	private static final Logger logger = LogManager.getLogger(NovaValidatorExperiment.class.getName());

	private NovaClient novaClient;
	private KeystoneClient keystoneClient;

	@Override
	public String getName() {
		return "Nova Validator";
	}

	@Override
	public String getDescription() {
		return "Validates Nova Functionality";
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

	@Override
	public void execute() throws Exception {

		// Authenticate session with Identity Service
		String authToken = keystoneClient.authenticate(config.getUsername(), config.getPassword(), config.getTenantName());
		novaClient.setAuthToken(authToken);

		// Perform experiments
		createServerExperiment();
		logger.info("\n");
		getServerExperiment();
		logger.info("\n");
		serverMetadataExperiment();
		logger.info("\n");
		getFlavorExperiment();
		logger.info("\n");
		getImageExperiment();
		logger.info("\n");
		deleteServerExperiment();
		logger.info("\n");
		logger.info("Finished");
	}

	private void getServerExperiment() throws Exception {

		logger.info("Server getters test");
		// Get Server Info in 3 different ways
		List<ServerInfo> serverInfo = novaClient.getServerInfo();
		List<Server> servers = novaClient.getServers();
		Server server = novaClient.getServerById(serverInfo.get(0).getId());

		// Print info about all these
		logger.info("Printing basic server info");
		for (ServerInfo si : serverInfo) {
			logger.info(si);
		}
		logger.info("Printing detailed server info");
		for (Server s : servers) {
			logger.info(s);
		}
		logger.info("Printing server 1");
		logger.info(server);

	}

	private void serverMetadataExperiment() throws Exception {

		logger.info("Server metadata test");
		// Get a server
		Server server = novaClient.getServerById(novaClient.getServerInfo().get(0).getId());
		logger.info("Got server: " + server);
		Map<String, String> metadataFromServerDefinition = server.getMetadata();
		Map<String, String> metadataFromRequest = novaClient.getServerMetadata(server.getId());
		logger.info("Got metadata from server definition and from request");
		logger.info("From server def: " + metadataFromServerDefinition.toString());
		logger.info("From req: " + metadataFromRequest.toString());

		// Set new metadata
		metadataFromRequest.put("foo", "bar");
		logger.info("Setting new metadata.." + WebServiceUtils.writeObjectToJson(metadataFromRequest));
		Thread.sleep(2000);

		novaClient.setServerMetadata(server.getId(), metadataFromRequest);

		// Get md again and compare

		// Check 3 time if it has been deleted
		for (int i = 1; i <= 3; i++) {
			Map<String, String> newMD = novaClient.getServerMetadata(server.getId());
			logger.info("Metadata attempt" + i);
			if (newMD.containsKey("foo") && newMD.get("foo").equals("bar")) {
				logger.info("Experiment passed!");
				return;
			}
			Thread.sleep(5000);
		}
		// If all 3 attempts fail
		throw new Exception("Experiment failed - metadata was not updated");
	}

	private void getFlavorExperiment() throws Exception {

		logger.info("Flavor getters test");
		// Get Server Info in 3 different ways
		List<FlavorInfo> flavorInfo = novaClient.getFlavorInfo();
		List<Flavor> flavors = novaClient.getFlavors();
		Flavor flavor = novaClient.getFlavorById(flavorInfo.get(0).getId());

		// Print info about all these
		logger.info("Printing basic Flavor info");
		for (FlavorInfo fi : flavorInfo) {
			logger.info(fi);
		}
		logger.info("Printing detailed Flavor info");
		for (Flavor f : flavors) {
			logger.info(f);
		}
		logger.info("Printing Flavorr 1");
		logger.info(flavor);
	}

	private void getImageExperiment() throws Exception {

		logger.info("Image getters test");
		// Get Server Info in 3 different ways
		List<ImageInfo> imageInfo = novaClient.getImageInfo();
		List<Image> images = novaClient.getImages();
		Image image = novaClient.getImageById(imageInfo.get(0).getId());

		// Print info about all these
		logger.info("Printing basic Image info");
		for (ImageInfo ii : imageInfo) {
			logger.info(ii);
		}
		logger.info("Printing detailed Image info");
		for (Image i : images) {
			logger.info(i);
		}
		logger.info("Printing Image 1");
		logger.info(image);
	}

	private void createServerExperiment() throws Exception {
		logger.info("Create a server experiment");
		String flavorRef = novaClient.getFlavorInfo().get(0).getLinks().get(0).getHref();
		String imageRef = novaClient.getImageInfo().get(0).getLinks().get(0).getHref();
		String createdServerId = novaClient.createServerFromImage("test_server", flavorRef, imageRef).getId();
		
		// Wait for the server to build
		NovaUtils.waitForServerState(novaClient, createdServerId, ServerStatus.ACTIVE);
		
		// Get the server
		Server server = novaClient.getServerById(createdServerId);
		logger.info("Got the server: " + server.getName());
	}

	private void deleteServerExperiment() throws Exception {
		logger.info("Deleting server test");
		List<ServerInfo> serverInfo = novaClient.getServerInfo();
		// Get number of servers
		int countBefore = serverInfo.size();
		logger.info("Number of servers before: " + countBefore);

		// Delete the first server
		String serverId = serverInfo.get(0).getId();
		novaClient.deleteServer(serverId);
		NovaUtils.waitForServerDeleteById(novaClient, serverId);
		
		// Check it is gone
		boolean found = false;
		for(ServerInfo si : novaClient.getServerInfo()) {
			if(si.getId().equals(serverId)) {
				found = true;
			}
		}
		
		// It should not be found, so if it is, throw an exception
		if(found) {
			throw new Exception("Experiment failed - server was not deleted");
		} 
		
		int countAfter = novaClient.getServerInfo().size();
		logger.info("Number of servers after: "+countAfter);
		logger.info("Experiment was successful, server was deleted");
	}

	/**
	 * TODO: - Create image - Image metadata - Server actions, e.g. reboot,
	 * change password
	 */

	@Override
	public void tearDown() throws Exception {
		// TODO Auto-generated method stub

	}

}
