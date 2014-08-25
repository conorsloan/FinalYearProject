package fyp.client.nova.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Host {

	@JsonProperty("host")
	private List<Resource> resources;

	public Host(List<Resource> resources) {
		this.resources = resources;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Host Resources: \n");
		for (Resource r : resources) {
			sb.append(r);
			sb.append('\n');
		}
		return sb.toString();
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Resource {

		private String cpu;
		@JsonProperty("disk_gb")
		private int diskGb;
		private String host;
		@JsonProperty("memory_mb")
		private int memoryMb;
		private String project;

		@Override
		public String toString() {
			return String.format("Resource: cpu: %s diskGb: %d host: %s memory: %d project: %s", cpu, diskGb, host, memoryMb, project);
		}

		public String getCpu() {
			return cpu;
		}

		public void setCpu(String cpu) {
			this.cpu = cpu;
		}

		public int getDiskGb() {
			return diskGb;
		}

		public void setDiskGb(int diskGb) {
			this.diskGb = diskGb;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getMemoryMb() {
			return memoryMb;
		}

		public void setMemoryMb(int memoryMb) {
			this.memoryMb = memoryMb;
		}

		public String getProject() {
			return project;
		}

		public void setProject(String project) {
			this.project = project;
		}

	}
}
