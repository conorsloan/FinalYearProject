package fyp.client.nova.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fyp.client.nova.data.Host.Resource;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetHostResponse {
	private List<Resource> host;

	public List<Resource> getHost() {
		return host;
	}

	public void setHost(List<Resource> host) {
		this.host = host;
	}
}
