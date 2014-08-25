package fyp.experiment.keystone;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import fyp.client.OpenstackRestClient;
import fyp.client.keystone.KeystoneClient;
import fyp.client.keystone.KeystoneClientImpl;
import fyp.client.keystone.response.Tenant;
import fyp.experiment.Experiment;

public class KeystoneValidatorExperiment extends Experiment {

	private static final Logger logger = LogManager.getLogger(KeystoneValidatorExperiment.class.getName());

	private KeystoneClient keystoneClient;

	@Override
	public String getName() {
		return "Keystone Validator Experiment";
	}

	@Override
	public String getDescription() {
		return "This experiment validates a subset of the functionality of the OpenStack identity service, keystone";
	}

	@Override
	public void setUp() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Setting up - retrieving and validating nova & keystone clients.");
		}

		keystoneClient = new KeystoneClientImpl(config, restTemplate);

		// Validate keystone client
		if (!((OpenstackRestClient) keystoneClient).validateClient()) {
			throw new Exception("Experiment setup failed: Openstack keystone client invalid");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Finished setup.");
		}
	}

	@Override
	public void execute() throws Exception {

		logger.info("\n");
		String token = authTokenExperiment();
		logger.info("\n");
		listTenantExperiment(token);
		logger.info("Finished");

	}

	public String authTokenExperiment() throws Exception {
		logger.info("Starting authentication experiment");
		String user = config.getUsername();
		String pass = config.getPassword();
		String name = config.getTenantName();

		logger.info(String.format("Attempting to retrieve an auth token with config: user: %s pass: %s name: %s ", user, pass, name));
		String token = keystoneClient.authenticate(config.getUsername(), config.getPassword(), config.getTenantName());
		logger.info("Got an Auth Token! - " + token);
		return token;
	}

	public void listTenantExperiment(String token) throws Exception {
		logger.info("Starting tenant list experiment");
		logger.info("Sending token to keystone..");
		List<Tenant> tenants = keystoneClient.listTokenTenants(token);
		logger.info("Got a list of tenants");
		for (Tenant t : tenants) {
			logger.info(t);
		}
	}

	@Override
	public void tearDown() throws Exception {
		// TODO Auto-generated method stub

	}

}
