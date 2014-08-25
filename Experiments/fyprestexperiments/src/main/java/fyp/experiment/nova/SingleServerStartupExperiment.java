package fyp.experiment.nova;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import fyp.client.OpenstackRestClient;
import fyp.client.keystone.KeystoneClient;
import fyp.client.keystone.KeystoneClientImpl;
import fyp.client.nova.NovaClient;
import fyp.client.nova.NovaClientImpl;
import fyp.client.nova.response.GetFlavorInfoResponse.FlavorInfo;
import fyp.experiment.Experiment;
import fyp.utils.NovaUtils;
import fyp.utils.NovaUtils.ServerStatus;

public class SingleServerStartupExperiment extends Experiment {

	private static final Logger logger = LogManager.getLogger(SingleServerStartupExperiment.class
			.getName());
	private final static int REPETITIONS = 2;
	private final static int MAX_INSTANCES = 5;
	private final static String SERVER_NAME = "testServer";

	KeystoneClient keystoneClient;
	NovaClient novaClient;

	@Override
	public String getName() {
		return "Single VM Startup Experiment - a performance analysis of OpenStack's Instance launching capabilities";
	}

	@Override
	public String getDescription() {
		return "This experiment, given a max number of instances & number of repetitions, starts up a number of VMs from 1 to "
				+ "max instances and times how long this takes. The requests are performed in parallel, so as to minimise serial cost "
				+ "differences.";
	}

	@Override
	public void setUp() throws Exception {
		novaClient = new NovaClientImpl(config, restTemplate);
		keystoneClient = new KeystoneClientImpl(config, restTemplate);

		// Validate nova & keystone clients
		if (!((OpenstackRestClient) novaClient).validateClient()
				|| !((OpenstackRestClient) keystoneClient).validateClient()) {
			throw new Exception("Experiment setup failed: One of openstack clients invalid");
		}

		// Authenticate
		novaClient.setAuthToken(keystoneClient.authenticate(config.getUsername(),
				config.getPassword(), config.getTenantName()));
		NovaUtils.deleteAllServers(novaClient);

		if (logger.isDebugEnabled()) {
			logger.debug("Finished setup.");
		}
	}

//	@Override
//	public void execute() throws Exception {
//
//		// Always use same image
//		List<FlavorInfo> flavors = novaClient.getFlavorInfo();
//		String imageRef = novaClient.getImageInfo().get(0).getLinks().get(0).getHref();
//		String sharedFlavorRef = novaClient.getFlavorInfo().get(0).getLinks().get(0).getHref();
//
//		// From 1 to max instances, start up that many at once and time it
//		for (int i = 0; i < MAX_INSTANCES; i++) {
//			performAddInstanceExperiment(flavors, i, sharedFlavorRef, imageRef);
//		}
//	}

	public void performAddInstanceExperiment(List<FlavorInfo> flavors, int numberOfServers,
			String sharedFlavorRef, String imageRef) throws Exception {
		String flavorRef;
		long sum;
		
		// Delete all vms, then start up X number of the first flavor,
		// waiting for them
		NovaUtils.deleteAllServers(novaClient);
		for (int j = 0; j < numberOfServers; j++) {
			novaClient.createServerFromImage(SERVER_NAME, sharedFlavorRef, imageRef);
		}
		NovaUtils.waitForMultipleServerBuild(novaClient, numberOfServers);

		// Now, for each flavor, for num of repetitions, time starting a vm
		for (FlavorInfo flavor : flavors) {
			logger.info("Starting experiment for flavor: " + flavor.getName()+" after "+numberOfServers+" VMs: ");
			flavorRef = flavor.getLinks().get(0).getHref();

			// Perform experiment a number of times
			sum = 0;
			for (int r = 1; r <= REPETITIONS; r++) {
				long timeBefore = System.nanoTime();
				String id = novaClient.createServerFromImage(SERVER_NAME, flavorRef, imageRef)
						.getId();
				// NovaUtils.waitForServerBuildById(novaClient, id);
				NovaUtils.waitForServerState(novaClient, id, ServerStatus.ACTIVE);
				long result = System.nanoTime() - timeBefore;
				//logger.debug(String.format("Repetition %d result: " + result, r));
				sum += result;
				novaClient.deleteServer(id);
				NovaUtils.waitForServerDeleteById(novaClient, id);
				//logger.debug("Number of servers currently: " + novaClient.getServerInfo().size());
				Thread.sleep(3000);
			}
			long average = sum / REPETITIONS;
			logger.info(String.format("Flavor: %s Number of VMs: %d Average Time: %s",
					flavor.getName(), numberOfServers, Long.toString(average)));
		}
		Thread.sleep(10000);
	}

	@Override
	public void tearDown() throws Exception {
		NovaUtils.deleteAllServers(novaClient);
	}

	/**
	 * Below is the hard-coded version of this experiment created solely as a
	 * time saving measure based on the limited resources of the test
	 * environment. In any real environment, running tests as small as these
	 * would not be a problem in terms of resources.
	 */

	 @Override
	 public void execute() throws Exception {
	 // Always use same image
	 List<FlavorInfo> flavors = new ArrayList<>();
	 flavors.add(novaClient.getFlavorInfo().get(0));
	 flavors.add(novaClient.getFlavorInfo().get(1));
	 String imageRef =
	 novaClient.getImageInfo().get(0).getLinks().get(0).getHref();
	 String flavorRef;
	 String sharedFlavorRef =
	 novaClient.getFlavorInfo().get(0).getLinks().get(0).getHref();
	
	 // From 1 to max instances, start up that many at once and time it
	 for (int i = 0; i < MAX_INSTANCES; i++) {
	 performAddInstanceExperiment(flavors, i, sharedFlavorRef, imageRef);
	 NovaUtils.deleteAllServers(novaClient);
	 Thread.sleep(5000);
	 }
	
	 }

}
