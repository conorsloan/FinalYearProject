package fyp.client.nova;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import fyp.client.OpenstackRestClient;
import fyp.client.nova.data.Flavor;
import fyp.client.nova.data.Image;
import fyp.client.nova.data.Server;
import fyp.client.nova.request.ChangeServerPasswordRequest;
import fyp.client.nova.request.CreateServerRequest;
import fyp.client.nova.request.CreateServerRequest.ServerParams;
import fyp.client.nova.request.RebootServerRequest;
import fyp.client.nova.request.RebootServerRequest.RebootType;
import fyp.client.nova.request.SetMetadataRequest;
import fyp.client.nova.response.CreateServerResponse;
import fyp.client.nova.response.CreateServerResponse.CreatedServerInfo;
import fyp.client.nova.response.GetFlavorInfoResponse;
import fyp.client.nova.response.GetFlavorInfoResponse.FlavorInfo;
import fyp.client.nova.response.GetFlavorResponse;
import fyp.client.nova.response.GetFlavorsResponse;
import fyp.client.nova.response.GetImageInfoResponse;
import fyp.client.nova.response.GetImageInfoResponse.ImageInfo;
import fyp.client.nova.response.GetImageResponse;
import fyp.client.nova.response.GetImagesResponse;
import fyp.client.nova.response.GetMetadataResponse;
import fyp.client.nova.response.GetServerInfoResponse;
import fyp.client.nova.response.GetServerInfoResponse.ServerInfo;
import fyp.client.nova.response.GetServerResponse;
import fyp.client.nova.response.GetServersResponse;
import fyp.config.OpenstackConfig;
import fyp.utils.WebServiceUtils;

public class NovaClientImpl extends OpenstackRestClient implements NovaClient {

	private static final Logger logger = LogManager.getLogger(NovaClientImpl.class.getName());
	private final String SERVER_URL = "/servers";
	private final String FLAVOR_URL = "/flavors";
	private final String IMAGE_URL = "/images";
	private final String DETAILS = "/detail";
	private final String MD = "/metadata";
	private final String IPS = "/ips";
	private final String ACTION = "/action";

	private String authToken = null;

	public NovaClientImpl(OpenstackConfig config, RestTemplate restTemplate) {
		super(config, restTemplate);
	}

	@Override
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	@Override
	public List<ServerInfo> getServerInfo() {
		String serversUrl = config.getNovaEndpoint() + config.getTenantId() + SERVER_URL;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(serversUrl, HttpMethod.GET, request, GetServerInfoResponse.class)
				.getBody().getServers();
	}

	@Override
	public CreatedServerInfo createServerFromImage(String serverName, String flavorRef,
			String imageRef) {
		String serversUrl = config.getNovaEndpoint() + config.getTenantId() + SERVER_URL;
		CreateServerRequest r = new CreateServerRequest(new ServerParams(flavorRef, imageRef,
				serverName));
		HttpEntity<CreateServerRequest> request = new HttpEntity<>(r,
				WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(serversUrl, HttpMethod.POST, request, CreateServerResponse.class)
				.getBody().getServer();
	}

	@Override
	public Server getServerById(String serverId) {
		String serverUrl = config.getNovaEndpoint() + config.getTenantId() + SERVER_URL + "/"
				+ serverId;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate.exchange(serverUrl, HttpMethod.GET, request, GetServerResponse.class)
				.getBody().getServer();
	}

	@Override
	public List<Server> getServers() {
		String serverDetailsUrl = config.getNovaEndpoint() + config.getTenantId() + SERVER_URL
				+ DETAILS;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(serverDetailsUrl, HttpMethod.GET, request, GetServersResponse.class)
				.getBody().getServers();
	}

	@Override
	public void deleteServer(String serverId) {
		String serverUrl = config.getNovaEndpoint() + config.getTenantId() + SERVER_URL + "/"
				+ serverId;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		restTemplate.exchange(serverUrl, HttpMethod.DELETE, request, Object.class);
	}

	@Override
	public List<FlavorInfo> getFlavorInfo() {
		String flavorsUrl = config.getNovaEndpoint() + config.getTenantId() + FLAVOR_URL;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(flavorsUrl, HttpMethod.GET, request, GetFlavorInfoResponse.class)
				.getBody().getFlavors();
	}

	@Override
	public List<Flavor> getFlavors() {
		String flavorDetailsUrl = config.getNovaEndpoint() + config.getTenantId() + FLAVOR_URL
				+ DETAILS;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(flavorDetailsUrl, HttpMethod.GET, request, GetFlavorsResponse.class)
				.getBody().getFlavors();
	}

	@Override
	public Flavor getFlavorById(String flavorId) {
		String flavorUrl = config.getNovaEndpoint() + config.getTenantId() + FLAVOR_URL + "/"
				+ flavorId;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate.exchange(flavorUrl, HttpMethod.GET, request, GetFlavorResponse.class)
				.getBody().getFlavor();
	}

	@Override
	public List<ImageInfo> getImageInfo() {
		String imagesUrl = config.getNovaEndpoint() + config.getTenantId() + IMAGE_URL;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(imagesUrl, HttpMethod.GET, request, GetImageInfoResponse.class).getBody()
				.getImages();
	}

	@Override
	public List<Image> getImages() {
		String imageDetailsUrl = config.getNovaEndpoint() + config.getTenantId() + IMAGE_URL
				+ DETAILS;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(imageDetailsUrl, HttpMethod.GET, request, GetImagesResponse.class)
				.getBody().getImages();
	}

	@Override
	public Image getImageById(String imageId) {
		String imageUrl = config.getNovaEndpoint() + config.getTenantId() + IMAGE_URL + "/"
				+ imageId;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate.exchange(imageUrl, HttpMethod.GET, request, GetImageResponse.class)
				.getBody().getImage();
	}

	@Override
	public void deleteImage(String imageId) {
		String imageUrl = config.getNovaEndpoint() + config.getTenantId() + IMAGE_URL + "/"
				+ imageId;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		restTemplate.exchange(imageUrl, HttpMethod.DELETE, request, Object.class);
	}

	@Override
	public Map<String, String> getServerMetadata(String serverId) {
		String serverMdUrl = config.getNovaEndpoint() + config.getTenantId() + SERVER_URL + "/"
				+ serverId + MD;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(serverMdUrl, HttpMethod.GET, request, GetMetadataResponse.class)
				.getBody().getMetadata();
	}

	@Override
	public Map<String, String> setServerMetadata(String serverId, Map<String, String> metadata) {
		String serverMdUrl = config.getNovaEndpoint() + config.getTenantId() + SERVER_URL + "/"
				+ serverId + MD;
		HttpEntity<SetMetadataRequest> request = new HttpEntity<>(new SetMetadataRequest(metadata),
				WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(serverMdUrl, HttpMethod.PUT, request, GetMetadataResponse.class)
				.getBody().getMetadata();
	}

	public Map<String, String> updateServerMetadata(String serverId, Map<String, String> metadata) {
		String serverMdUrl = config.getNovaEndpoint() + config.getTenantId() + SERVER_URL + "/"
				+ serverId + MD;
		HttpEntity<SetMetadataRequest> request = new HttpEntity<>(new SetMetadataRequest(metadata),
				WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(serverMdUrl, HttpMethod.POST, request, GetMetadataResponse.class)
				.getBody().getMetadata();
	}

	@Override
	public Map<String, String> updateServerMetadata(String serverId, String key, String value) {
		String serverMdUrl = config.getNovaEndpoint() + config.getTenantId() + SERVER_URL + "/"
				+ serverId + MD;
		Map<String, String> md = new HashMap<>();
		md.put(key, value);
		HttpEntity<SetMetadataRequest> request = new HttpEntity<>(new SetMetadataRequest(md),
				WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(serverMdUrl, HttpMethod.POST, request, GetMetadataResponse.class)
				.getBody().getMetadata();
	}

	@Override
	public void deleteServerMetadata(String serverId, String key) {
		String serverMdUrl = config.getNovaEndpoint() + config.getTenantId() + SERVER_URL + "/"
				+ serverId + MD + "/" + key;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		restTemplate.exchange(serverMdUrl, HttpMethod.DELETE, request, Object.class);
	}

	@Override
	public boolean validateClient() {
		HttpEntity<Void> request = new HttpEntity<>(null, null);
		try {
			logger.debug("Validating client");
			String baseUrl = config.getNovaEndpoint().substring(0,
					config.getNovaEndpoint().length() - 2);
			restTemplate.exchange(baseUrl, HttpMethod.GET, request, GetFlavorsResponse.class);
			return true;
		} catch (Exception e) {
			logger.debug("Validation failed: Error: " + e.getMessage());
			return false;
		}
	}

	@Override
	public Map<String, String> getImageMetadata(String imageId) {
		String imageMdUrl = config.getNovaEndpoint() + config.getTenantId() + IMAGE_URL + "/"
				+ imageId + MD;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(imageMdUrl, HttpMethod.GET, request, GetMetadataResponse.class).getBody()
				.getMetadata();
	}

	@Override
	public Map<String, String> setImageMetadata(String imageId, Map<String, String> metadata) {
		String imageMdUrl = config.getNovaEndpoint() + config.getTenantId() + IMAGE_URL + "/"
				+ imageId + MD;
		HttpEntity<SetMetadataRequest> request = new HttpEntity<>(new SetMetadataRequest(metadata),
				WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(imageMdUrl, HttpMethod.PUT, request, GetMetadataResponse.class).getBody()
				.getMetadata();
	}

	@Override
	public Map<String, String> updateImageMetadata(String imageId, Map<String, String> metadata) {
		String imageMdUrl = config.getNovaEndpoint() + config.getTenantId() + IMAGE_URL + "/"
				+ imageId + MD;
		HttpEntity<SetMetadataRequest> request = new HttpEntity<>(new SetMetadataRequest(metadata),
				WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(imageMdUrl, HttpMethod.POST, request, GetMetadataResponse.class)
				.getBody().getMetadata();
	}

	@Override
	public Map<String, String> updateImageMetadata(String imageId, String key, String value) {
		String imageMdUrl = config.getNovaEndpoint() + config.getTenantId() + IMAGE_URL + "/"
				+ imageId + MD;
		Map<String, String> md = new HashMap<>();
		md.put(key, value);
		HttpEntity<SetMetadataRequest> request = new HttpEntity<>(new SetMetadataRequest(md),
				WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate
				.exchange(imageMdUrl, HttpMethod.PUT, request, GetMetadataResponse.class).getBody()
				.getMetadata();
	}

	public void deleteImageMetadata(String imageId, String key) {
		String serverMdUrl = config.getNovaEndpoint() + config.getTenantId() + IMAGE_URL + "/"
				+ imageId + MD + "/" + key;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		restTemplate.exchange(serverMdUrl, HttpMethod.DELETE, request, Object.class);
	}

	@Override
	public void changeServerPassword(String serverId, String newPassword) {
		String serverActionUrl = config.getNovaEndpoint() + config.getTenantId() + SERVER_URL + "/"
				+ serverId + ACTION;
		HttpEntity<ChangeServerPasswordRequest> request = new HttpEntity<>(
				new ChangeServerPasswordRequest(newPassword),
				WebServiceUtils.getAuthHeaders(authToken));
		restTemplate.exchange(serverActionUrl, HttpMethod.POST, request, Object.class);
	}

	@Override
	public void rebootServer(String serverId, RebootType rebootType) {
		String serverActionUrl = config.getNovaEndpoint() + config.getTenantId() + SERVER_URL + "/"
				+ serverId + ACTION;
		HttpEntity<RebootServerRequest> request = new HttpEntity<>(new RebootServerRequest(
				rebootType), WebServiceUtils.getAuthHeaders(authToken));
		restTemplate.exchange(serverActionUrl, HttpMethod.POST, request, Object.class);
	}

	@Override
	public NovaExtensionsClient getExtensionsClient() {
		return new NovaExtensionsClientImpl(config, restTemplate);
	}
}
