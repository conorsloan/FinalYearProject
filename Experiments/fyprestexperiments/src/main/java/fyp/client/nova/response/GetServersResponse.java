package fyp.client.nova.response;

import java.util.List;

import fyp.client.nova.data.Server;

public class GetServersResponse {

	private List<Server> servers;

	public List<Server> getServers() {
		return servers;
	}

	public void setServers(List<Server> servers) {
		this.servers = servers;
	}

}
