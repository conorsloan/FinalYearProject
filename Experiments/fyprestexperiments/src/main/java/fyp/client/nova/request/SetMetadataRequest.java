package fyp.client.nova.request;

import java.util.Map;

public class SetMetadataRequest {

	private Map<String, String> metadata;

	public SetMetadataRequest(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

}
