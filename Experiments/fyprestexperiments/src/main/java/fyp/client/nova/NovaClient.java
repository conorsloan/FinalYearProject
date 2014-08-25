package fyp.client.nova;

import java.util.List;
import java.util.Map;

import fyp.client.nova.data.Flavor;
import fyp.client.nova.data.Image;
import fyp.client.nova.data.Server;
import fyp.client.nova.request.RebootServerRequest.RebootType;
import fyp.client.nova.response.CreateServerResponse.CreatedServerInfo;
import fyp.client.nova.response.GetFlavorInfoResponse.FlavorInfo;
import fyp.client.nova.response.GetImageInfoResponse.ImageInfo;
import fyp.client.nova.response.GetServerInfoResponse.ServerInfo;

public interface NovaClient {

	/**
	 * Sets session token to be used for authorising requests to the API. NOTE:
	 * MUST BE SET TO PERFORM MOST ACTIONS
	 * 
	 * @param authToken
	 *            The auth token from keystone service.
	 */
	public void setAuthToken(String authToken);

	public List<ServerInfo> getServerInfo();

	public CreatedServerInfo createServerFromImage(String serverName, String flavorRef,
			String imageRef);

	public Server getServerById(String serverId);

	public List<Server> getServers();

	public void deleteServer(String serverId);

	public List<FlavorInfo> getFlavorInfo();

	public List<Flavor> getFlavors();

	public Flavor getFlavorById(String flavorId);

	public List<ImageInfo> getImageInfo();

	public List<Image> getImages();

	public Image getImageById(String imageId);

	public void deleteImage(String imageId);

	public Map<String, String> getServerMetadata(String serverId);

	public Map<String, String> setServerMetadata(String serverId, Map<String, String> metadata);

	public Map<String, String> updateServerMetadata(String serverId, Map<String, String> metadata);

	public Map<String, String> updateServerMetadata(String serverId, String key, String value);

	public void deleteServerMetadata(String serverId, String key);

	public Map<String, String> getImageMetadata(String imageId);

	public Map<String, String> setImageMetadata(String imageId, Map<String, String> metadata);

	public Map<String, String> updateImageMetadata(String imageId, Map<String, String> metadata);

	public Map<String, String> updateImageMetadata(String imageId, String key, String value);

	public void deleteImageMetadata(String imageId, String key);

	/* Server Actions */

	public void changeServerPassword(String serverId, String newPassword);

	public void rebootServer(String serverId, RebootType rebootType);

	public NovaExtensionsClient getExtensionsClient();

}
