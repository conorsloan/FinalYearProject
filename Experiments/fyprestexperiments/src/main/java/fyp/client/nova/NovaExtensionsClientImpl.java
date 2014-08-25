package fyp.client.nova;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import fyp.client.OpenstackRestClient;
import fyp.client.nova.data.Host;
import fyp.client.nova.data.HostInfo;
import fyp.client.nova.request.LockServerRequest;
import fyp.client.nova.request.PauseServerRequest;
import fyp.client.nova.request.ResumeServerRequest;
import fyp.client.nova.request.SuspendServerRequest;
import fyp.client.nova.request.UnlockServerRequest;
import fyp.client.nova.request.UnpauseServerRequest;
import fyp.client.nova.response.GetHostInfoResponse;
import fyp.client.nova.response.GetHostResponse;
import fyp.config.OpenstackConfig;
import fyp.utils.WebServiceUtils;

public class NovaExtensionsClientImpl extends OpenstackRestClient implements NovaExtensionsClient {

	private static final Logger logger = LogManager.getLogger(NovaExtensionsClientImpl.class.getName());

	private final static String HOSTS = "os-hosts";
	private final static String SERVERS = "servers/";
	private final static String ACTION = "action";
	private final static String STARTUP = "startup";
	private final static String SHUTDOWN = "shutdown";
	private final static String REBOOT = "reboot";

	private String authToken;

	public NovaExtensionsClientImpl(OpenstackConfig config, RestTemplate restTemplate) {
		super(config, restTemplate);
	}

	@Override
	public boolean validateClient() {
		return true;
	}

	@Override
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	@Override
	public void pauseServer(String serverId) {
		String serverActionUrl = config.getNovaEndpoint() + config.getTenantId() + "/" + SERVERS + serverId + "/" + ACTION;
		HttpEntity<PauseServerRequest> request = new HttpEntity<>(new PauseServerRequest(), WebServiceUtils.getAuthHeaders(authToken));
		restTemplate.exchange(serverActionUrl, HttpMethod.POST, request, String.class);
	}

	@Override
	public void unpauseServer(String serverId) {
		String serverActionUrl = config.getNovaEndpoint() + config.getTenantId() + "/" + SERVERS + serverId + "/" + ACTION;
		HttpEntity<UnpauseServerRequest> request = new HttpEntity<>(new UnpauseServerRequest(), WebServiceUtils.getAuthHeaders(authToken));
		restTemplate.exchange(serverActionUrl, HttpMethod.POST, request, String.class);
	}

	@Override
	public void suspendServer(String serverId) {
		String serverActionUrl = config.getNovaEndpoint() + config.getTenantId() + "/" + SERVERS + serverId + "/" + ACTION;
		HttpEntity<SuspendServerRequest> request = new HttpEntity<>(new SuspendServerRequest(), WebServiceUtils.getAuthHeaders(authToken));
		restTemplate.exchange(serverActionUrl, HttpMethod.POST, request, String.class);
	}

	@Override
	public void resumeServer(String serverId) {
		String serverActionUrl = config.getNovaEndpoint() + config.getTenantId() + "/" + SERVERS + serverId + "/" + ACTION;
		HttpEntity<ResumeServerRequest> request = new HttpEntity<>(new ResumeServerRequest(), WebServiceUtils.getAuthHeaders(authToken));
		restTemplate.exchange(serverActionUrl, HttpMethod.POST, request, String.class);
	}

	@Override
	public void migrateServer(String serverId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void lockServer(String serverId) {
		String serverActionUrl = config.getNovaEndpoint() + config.getTenantId() + "/" + SERVERS + serverId + "/" + ACTION;
		HttpEntity<LockServerRequest> request = new HttpEntity<>(new LockServerRequest(), WebServiceUtils.getAuthHeaders(authToken));
		restTemplate.exchange(serverActionUrl, HttpMethod.POST, request, String.class);
	}

	@Override
	public void unlockServer(String serverId) {
		String serverActionUrl = config.getNovaEndpoint() + config.getTenantId() + "/" + SERVERS + serverId + "/" + ACTION;
		HttpEntity<UnlockServerRequest> request = new HttpEntity<>(new UnlockServerRequest(), WebServiceUtils.getAuthHeaders(authToken));
		restTemplate.exchange(serverActionUrl, HttpMethod.POST, request, String.class);
	}

	@Override
	public void migrateServer(String serverId, String hostId, boolean blockMigration, boolean diskOvercommit) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<HostInfo> getHostInfo() {
		String hostsUrl = config.getNovaEndpoint() + config.getTenantId() + "/" + HOSTS;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate.exchange(hostsUrl, HttpMethod.GET, request, GetHostInfoResponse.class).getBody().getHosts();
	}

	@Override
	public Host getHostByHostName(String hostId) {
		String hostUrl = config.getNovaEndpoint() + config.getTenantId() + "/" + HOSTS + "/" + hostId;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return new Host(restTemplate.exchange(hostUrl, HttpMethod.GET, request, GetHostResponse.class).getBody().getHost());
	}

	// @Override
	// public void startHost(String hostId) {
	// // NOTE this is wholly inconsistent with other actions using the ACTION
	// // URL.. why?
	// String startHostUrl = config.getNovaEndpoint() + config.getTenantId() +
	// "/" + HOSTS + "/" + hostId + "/" + STARTUP;
	// HttpEntity<Void> request = new HttpEntity<>(null,
	// WebServiceUtils.getAuthHeaders(authToken));
	// restTemplate.exchange(startHostUrl, HttpMethod.GET, request,
	// HostActionResponse.class);
	// }
	//
	// @Override
	// public void shutdownHost(String hostId) {
	// String stopHostUrl = config.getNovaEndpoint() + config.getTenantId() +
	// "/" + HOSTS + "/" + hostId + "/" + SHUTDOWN;
	// HttpEntity<Void> request = new HttpEntity<>(null,
	// WebServiceUtils.getAuthHeaders(authToken));
	// restTemplate.exchange(stopHostUrl, HttpMethod.GET, request,
	// HostActionResponse.class);
	// }
	//
	// @Override
	// public void rebootHost(String hostId) {
	// String startHostUrl = config.getNovaEndpoint() + config.getTenantId() +
	// "/" + HOSTS + "/" + hostId + "/" + REBOOT;
	// HttpEntity<Void> request = new HttpEntity<>(null,
	// WebServiceUtils.getAuthHeaders(authToken));
	// restTemplate.exchange(startHostUrl, HttpMethod.GET, request,
	// HostActionResponse.class);
	// }

}
