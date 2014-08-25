package fyp.client.glance;

import java.util.List;

import fyp.client.glance.data.Image;
import fyp.client.glance.data.ImageInfo;

public interface GlanceClient {

	public void setAuthToken(String authToken);

	public List<ImageInfo> getImageInfo();

	public Image getImageById(String imageId);

	public Image createImage(String id, String name, boolean publicVisibility, String... tags);
}
