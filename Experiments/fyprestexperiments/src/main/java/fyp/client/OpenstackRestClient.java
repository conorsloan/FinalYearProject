package fyp.client;

import org.springframework.web.client.RestTemplate;

import fyp.config.OpenstackConfig;

public abstract class OpenstackRestClient {

	protected OpenstackConfig config;
	protected RestTemplate restTemplate;

	public OpenstackConfig getConfig() {
		return config;
	}

	public OpenstackRestClient(OpenstackConfig config, RestTemplate restTemplate) {
		this.config = config;
		this.restTemplate = restTemplate;
	}

    /**
     * Validates the given rest client, usually by sending a request to the root of the API
     * and returning anything that is not a 404.
     *
     * @return Boolean representing the validity of the given API endpoint.
     */
    public abstract boolean validateClient();
}
