package fyp.client.nova.response;

import fyp.client.nova.data.Addresses;

public class GetServerAddressResponse {

	private Addresses addresses;

	public Addresses getAddresses() {
		return addresses;
	}

	public void setAddresses(Addresses addresses) {
		this.addresses = addresses;
	}
}
