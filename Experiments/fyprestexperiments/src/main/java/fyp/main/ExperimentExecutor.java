package fyp.main;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fyp.experiment.Experiment;

public class ExperimentExecutor {

	private static List<Experiment> experiments;
	private static final Logger logger = LogManager
			.getLogger(ExperimentExecutor.class.getName());

	@SuppressWarnings({ "unchecked", "resource" })
	public static void main(String[] args) {

		ApplicationContext context;
		// Default
		if(args.length == 0) {
			context = new ClassPathXmlApplicationContext(
					"fyp/beans/Experiments.xml", "fyp/beans/Beans.xml");
		} else {
			// Arguments
			context = new ClassPathXmlApplicationContext(args);
		}
		
		// Check if these actually exist
		if(!context.containsBean("Experiments") || !context.containsBean("openstackConfigBean")) {
			System.err.print("Error: Bean definitions were not provided. Please see README for details.");
		}
		
		// Get experiments to run
		experiments = (List<Experiment>) context.getBean("Experiments");
		logger.info(String.format(
				"Starting Experiment Executor - %d Experiments",
				experiments.size()));

		// Run each experiment
		for (Experiment e : experiments) {
			try {
				logger.info(String.format("Experiment: %s, Details: %s",
						e.getName(), e.getDescription()));
				logger.info("Setting up experiment..");
				e.setUp();
				logger.info("Executing Experiment");
				e.execute();
				logger.info("Tearing down Experiment");
				e.tearDown();
			} catch (Exception ex) {
				logger.error(String.format(
						"Experiment %s failed with Exception: %s", e.getName(),
						ex.getMessage()));
			}
		}

		// Exit
		logger.info("Finished running experiments - exiting");
	}
}