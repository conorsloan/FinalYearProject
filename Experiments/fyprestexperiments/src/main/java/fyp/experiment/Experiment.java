package fyp.experiment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import fyp.config.OpenstackConfig;

public abstract class Experiment {

	@Autowired
    protected OpenstackConfig config; // Automatically made available by XML
	@Autowired
    protected RestTemplate restTemplate;
    
    public Experiment(OpenstackConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }
    
    public Experiment() {
    	
    }
    
    public abstract String getName();
    
    public abstract String getDescription();

	public abstract void setUp() throws Exception;
	
	public abstract void execute() throws Exception;
	
	public abstract void tearDown() throws Exception;
}
