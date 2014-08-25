package fyp.client.nova.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fyp.client.nova.data.Flavor;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetFlavorResponse {

	private Flavor flavor;

	public Flavor getFlavor() {
		return flavor;
	}

	public void setFlavor(Flavor flavor) {
		this.flavor = flavor;
	}

}
