package fyp.client.nova.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HostActionResponse {
	private String host;
	@JsonProperty("power_action")
	private String powerAction;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPowerAction() {
		return powerAction;
	}

	public void setPowerAction(String powerAction) {
		this.powerAction = powerAction;
	}

}
