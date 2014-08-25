package fyp.client.nova.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fyp.client.nova.data.Flavor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetFlavorsResponse {

	public List<Flavor> flavors;

	public List<Flavor> getFlavors() {
		return flavors;
	}

	public void setFlavors(List<Flavor> flavors) {
		this.flavors = flavors;
	}

}
