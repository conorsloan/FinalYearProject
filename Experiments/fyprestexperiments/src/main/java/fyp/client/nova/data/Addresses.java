package fyp.client.nova.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Addresses {

	@JsonProperty("private")
	private List<Address> privateAddresses;

	@JsonProperty("public")
	private List<Address> publicAddresses;

	public List<Address> getPrivateAddresses() {
		return privateAddresses;
	}

	public void setPrivateAddresses(List<Address> privateAddresses) {
		this.privateAddresses = privateAddresses;
	}

	public List<Address> getPublicAddresses() {
		return publicAddresses;
	}

	public void setPublicAddresses(List<Address> publicAddresses) {
		this.publicAddresses = publicAddresses;
	}

}
