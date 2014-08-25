package fyp.client.glance.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import fyp.client.glance.data.ImageInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetImageInfoResponse {

	private List<ImageInfo> images;
	private String schema;
	private String first;

	public List<ImageInfo> getImages() {
		return images;
	}

	public void setImages(List<ImageInfo> images) {
		this.images = images;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
}
