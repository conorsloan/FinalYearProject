package fyp.experiment.nova;

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

public class MultiServerStartupExperiment extends Experiment {

	private static final Logger logger = LogManager.getLogger(MultiServerStartupExperiment.class
			.getName());
	private final static int REPETITIONS = 2;
	private final static int MAX_INSTANCES = 2;
	private final static String SERVER_NAME = "testServer";

	KeystoneClient keystoneClient;
	NovaClient novaClient;

	@Override
	public String getName() {
		return "Multi VM Startup Experiment - a performance analysis of OpenStack's Instance launching capabilities";
	}

	@Override
	public String getDescription() {
		return "This experiment, given a max number of instances & number of repetitions, starts up a number of VMs simultaneously from 1 to "
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

	@Override
	public void execute() throws Exception {
		// Always use same image
		String imageRef = novaClient.getImageInfo().get(0).getLinks().get(0).getHref();

		// MODIFIED VERSION FOR SPECIFIC OPENSTACK CONFIG
		// Perform experiment once per flavor
		List<FlavorInfo> flavors = novaClient.getFlavorInfo();
		for (FlavorInfo flavor : flavors) {
			performServerStartupExperiment(flavor, MAX_INSTANCES, imageRef);
		}
	}

	/**
	 * Performs the Multi startup experiment
	 * 
	 * @param flavor
	 * @param maxInstances
	 * @throws Exception
	 */
	public void performServerStartupExperiment(FlavorInfo flavor, int maxInstances, String imageRef)
			throws Exception {
		logger.info("Starting experiment for flavor: " + flavor.getName());
		String flavorRef = flavor.getLinks().get(0).getHref();
		NovaUtils.deleteAllServers(novaClient);

		// From 1 to max instances, start up that many at once and time it
		long sum = 0;
		for (int i = 1; i <= maxInstances; i++) {
			Thread.sleep(10000);
			logger.info(String.format("Starting %d VMs", i));

			// Perform this experiment a number of times for increased
			// accuracy
			for (int r = 1; r <= REPETITIONS; r++) {
				long startTime = System.nanoTime();

				// Start a number of VMs
				for (int j = 0; j < i; j++) {
					new Thread(new VMCreator(flavorRef, imageRef)).start();
				}
				// Wait for all specified VMs to build, then get time &
				// delete VMs for next repetition
				NovaUtils.waitForMultipleServerBuild(novaClient, i);
				sum += (System.nanoTime() - startTime);
				NovaUtils.deleteAllServers(novaClient);
				Thread.sleep(3000);
			}
			long average = sum / REPETITIONS;
			sum = 0;
			logger.info(String.format("Experiment flavor %s, %d VMs, Average Time: %s",
					flavor.getName(), i, Long.toString(average)));
		}
	}

	@Override
	public void tearDown() throws Exception {
		NovaUtils.deleteAllServers(novaClient);
	}

	private class VMCreator implements Runnable {
		String flavorRef;
		String imageRef;

		public VMCreator(String flavorRef, String imageRef) {
			this.flavorRef = flavorRef;
			this.imageRef = imageRef;
		}

		@Override
		public void run() {
			novaClient.createServerFromImage(SERVER_NAME, flavorRef, imageRef);
		}
	}

	/**
	 * Below is the hard-coded version of this experiment created solely as a
	 * time saving measure based on the limited resources of the test
	 * environment. In any real environment, running tests as small as these
	 * would not be a problem in terms of resources.
	 */

	// @Override
	// public void execute() throws Exception {
	// // Always use same image
	// String imageRef =
	// novaClient.getImageInfo().get(0).getLinks().get(0).getHref();
	//
	// // MODIFIED VERSION FOR SPECIFIC OPENSTACK CONFIG
	// // Perform experiment once per flavor
	// List<FlavorInfo> flavors = novaClient.getFlavorInfo();
	// // m1.tiny
	// performServerStartupExperiment(flavors.get(0), 10, imageRef);
	// // m1.small
	// performServerStartupExperiment(flavors.get(1), 3, imageRef);
	// // m1.medium
	// performServerStartupExperiment(flavors.get(2), 2, imageRef);
	// }

}
