package fyp.client.nova.response;

import java.util.List;

import fyp.client.nova.data.Link;

public class GetFlavorInfoResponse {

	List<FlavorInfo> flavors;

	public List<FlavorInfo> getFlavors() {
		return flavors;
	}

	public void setFlavors(List<FlavorInfo> flavors) {
		this.flavors = flavors;
	}
	
	public static class FlavorInfo {

		private String id;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getRam() {
			return ram;
		}
		public void setRam(String ram) {
			this.ram = ram;
		}
		public int getDisk() {
			return disk;
		}
		public void setDisk(int disk) {
			this.disk = disk;
		}
		public int getVcpus() {
			return vcpus;
		}
		public void setVcpus(int vcpus) {
			this.vcpus = vcpus;
		}
		public List<Link> getLinks() {
			return links;
		}
		public void setLinks(List<Link> links) {
			this.links = links;
		}
		private String name;
		private String ram;
		private int disk;
		private int vcpus;
		private List<Link> links;
		
		@Override
		public String toString() {
			return String.format("Name: %s, Ram: %s, Disks: %d, VCPUS: %d", name, ram, disk, vcpus);
		}
	}

}
