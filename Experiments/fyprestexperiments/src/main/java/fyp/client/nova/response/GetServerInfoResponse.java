package fyp.client.nova.response;

import java.util.List;

import fyp.client.nova.data.Link;

public class GetServerInfoResponse {
	List<ServerInfo> servers;

	public List<ServerInfo> getServers() {
		return servers;
	}

	public void setServers(List<ServerInfo> servers) {
		this.servers = servers;
	}
	
	public static class ServerInfo {
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public List<Link> getLinks() {
			return links;
		}
		public void setLinks(List<Link> links) {
			this.links = links;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return String.format("Server name: %s, ID: %s", getName(), getId());
		}
		
		private String id;
		List<Link> links;
		String name;
	}

}
