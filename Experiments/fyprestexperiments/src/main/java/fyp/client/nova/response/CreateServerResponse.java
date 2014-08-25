package fyp.client.nova.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fyp.client.nova.data.Link;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateServerResponse {

	private CreatedServerInfo server;

	public CreatedServerInfo getServer() {
		return server;
	}

	public void setServer(CreatedServerInfo server) {
		this.server = server;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CreatedServerInfo {

		private String adminPass;
		private String id;
		private List<Link> links;

		public String getAdminPass() {
			return adminPass;
		}

		public void setAdminPass(String adminPass) {
			this.adminPass = adminPass;
		}

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

		@Override
		public String toString() {
			return String.format("Id: %s, AdminPass: %s", id, adminPass);
		}

	}

}
