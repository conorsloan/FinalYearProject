package fyp.client.keystone;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import fyp.client.OpenstackRestClient;
import fyp.client.keystone.request.AuthTokenRequest;
import fyp.client.keystone.response.AuthTokenResponse;
import fyp.client.keystone.response.GetTokenTenantsResponse;
import fyp.client.keystone.response.Tenant;
import fyp.config.OpenstackConfig;
import fyp.utils.WebServiceUtils;

public class KeystoneClientImpl extends OpenstackRestClient implements KeystoneClient {

	private final static String TOKEN_URL = "/tokens";
	private final static String TENANT_URL = "/tenants";

	public KeystoneClientImpl(OpenstackConfig config, RestTemplate restTemplate) {
		super(config, restTemplate);
	}

	@Override
	public boolean validateClient() {
		return true; // TODO
	}

	public String authenticate(String username, String password, String tenantName) {
		String tokenAuthUrl = config.getKeystoneEndpoint() + TOKEN_URL;
		AuthTokenRequest request = new AuthTokenRequest(username, password, tenantName);
		return restTemplate.postForObject(tokenAuthUrl, request, AuthTokenResponse.class, new Object()).getAccess().getToken().getId();
	}

	@Override
	public List<Tenant> listTokenTenants(String authToken) {
		String tenantsUrl = config.getKeystoneEndpoint() + TENANT_URL;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate.exchange(tenantsUrl, HttpMethod.GET, request, GetTokenTenantsResponse.class).getBody().getTenants();
	}

}
