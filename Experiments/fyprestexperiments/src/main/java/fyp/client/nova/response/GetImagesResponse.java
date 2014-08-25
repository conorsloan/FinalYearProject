package fyp.client.nova.response;

import java.util.List;

import fyp.client.nova.data.Image;

public class GetImagesResponse {

	private List<Image> images;

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

}
