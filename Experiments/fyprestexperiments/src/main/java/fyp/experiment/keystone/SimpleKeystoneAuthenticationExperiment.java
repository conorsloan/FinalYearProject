package fyp.experiment.keystone;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import fyp.client.OpenstackRestClient;
import fyp.client.keystone.KeystoneClient;
import fyp.client.keystone.KeystoneClientImpl;
import fyp.experiment.Experiment;

public class SimpleKeystoneAuthenticationExperiment extends Experiment {

	private static final Logger logger = LogManager.getLogger(SimpleKeystoneAuthenticationExperiment.class.getName());
	private KeystoneClient keystoneClient;

	@Override
	public String getName() {
		return "A simple experiment to authenticate with the Keystone Identity Service";
	}

	@Override
	public String getDescription() {
		return "Authenticates with Keystone, then prints out the token received";
	}

	@Override
	public void setUp() throws Exception {
		keystoneClient = new KeystoneClientImpl(config, restTemplate);
		// Validate keystone client
		if (!((OpenstackRestClient) keystoneClient).validateClient()) {
			throw new Exception("Experiment setup failed: One of openstack clients invalid");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Finished setup.");
		}
	}

	@Override
	public void execute() throws Exception {
		// Authenticate
		String token = keystoneClient.authenticate(config.getUsername(), config.getPassword(), config.getTenantName());
		logger.info("Token is: " + token);
	}

	@Override
	public void tearDown() throws Exception {
		return;
	}

}
