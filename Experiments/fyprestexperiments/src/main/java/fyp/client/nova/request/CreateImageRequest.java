package fyp.client.nova.request;

import java.util.HashMap;
import java.util.Map;

public class CreateImageRequest {

	public ImageParams createImage;

	public CreateImageRequest(String name, Map<String, String> metadata) {
		this.createImage = new ImageParams(name, metadata);
	}

	public CreateImageRequest(String name) {
		this.createImage = new ImageParams(name, new HashMap<String, String>());
	}

	public static class ImageParams {
		public ImageParams(String name, Map<String, String> metadata) {
			this.setName(name);
			this.setMetadata(metadata);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Map<String, String> getMetadata() {
			return metadata;
		}

		public void setMetadata(Map<String, String> metadata) {
			this.metadata = metadata;
		}

		private String name;
		private Map<String, String> metadata;
	}

}
