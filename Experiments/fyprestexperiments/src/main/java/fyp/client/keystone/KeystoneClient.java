package fyp.client.keystone;

import java.util.List;

import fyp.client.keystone.response.Tenant;

public interface KeystoneClient {

	public String authenticate(String username, String password, String tenantName);

	List<Tenant> listTokenTenants(String authToken);

}
