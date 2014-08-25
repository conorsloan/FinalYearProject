package fyp.experiment.glance;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import fyp.client.OpenstackRestClient;
import fyp.client.glance.GlanceClient;
import fyp.client.glance.GlanceClientImpl;
import fyp.client.glance.data.Image;
import fyp.client.glance.data.ImageInfo;
import fyp.client.keystone.KeystoneClient;
import fyp.client.keystone.KeystoneClientImpl;
import fyp.experiment.Experiment;

public class GlanceValidatorExperiment extends Experiment {

	private static final Logger logger = LogManager.getLogger(GlanceValidatorExperiment.class.getName());

	private GlanceClient glanceClient;
	private KeystoneClient keystoneClient;

	@Override
	public String getName() {
		return "Glance Validator Experiment";
	}

	@Override
	public String getDescription() {
		return "Validates functionality of the Glance service & APIs with a real endpoint. ";
	}

	@Override
	public void setUp() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Setting up - retrieving and validating nova & keystone clients.");
		}

		glanceClient = new GlanceClientImpl(config, restTemplate);
		keystoneClient = new KeystoneClientImpl(config, restTemplate);

		// Validate nova & keystone clients
		if (!((OpenstackRestClient) glanceClient).validateClient() || !((OpenstackRestClient) keystoneClient).validateClient()) {
			throw new Exception("Experiment setup failed: One of openstack clients invalid");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Finished setup.");
		}
	}

	@Override
	public void execute() throws Exception {

		// Authenticate
		glanceClient.setAuthToken(keystoneClient.authenticate(config.getUsername(), config.getPassword(), config.getTenantName()));

		// Run experiments
		logger.info("\n");
		getImagesExperiment();
		logger.info("\n");
		createImageExperiment();
		logger.info("\n");
		logger.info("Finished");
	}

	private void getImagesExperiment() throws Exception {
		logger.info("Printing information about Images");
		for (ImageInfo i : glanceClient.getImageInfo()) {
			logger.info(i);
		}

		logger.info("Getting image by id");
		Image image = glanceClient.getImageById(glanceClient.getImageInfo().get(0).getId());
		logger.info(image);
		logger.info("Finished");
	}

	private void createImageExperiment() throws Exception {
		logger.info("Creating an image");
		Image createdImage = glanceClient.createImage(null, "conorOS", true, "hello", "world");
		logger.info("Created image: " + createdImage);
		logger.info("Finished");
	}

	@Override
	public void tearDown() throws Exception {
		// TODO Auto-generated method stub

	}

}
