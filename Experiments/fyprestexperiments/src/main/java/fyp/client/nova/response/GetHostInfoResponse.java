package fyp.client.nova.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fyp.client.nova.data.HostInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetHostInfoResponse {

	private List<HostInfo> hosts;

	public List<HostInfo> getHosts() {
		return hosts;
	}

	public void setHosts(List<HostInfo> hosts) {
		this.hosts = hosts;
	}

}
