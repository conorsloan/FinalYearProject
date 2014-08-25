package fyp.client.nova.response;

import java.util.List;

import fyp.client.nova.data.Link;

public class GetImageInfoResponse {

	private List<ImageInfo> images;

	public List<ImageInfo> getImages() {
		return images;
	}

	public void setImages(List<ImageInfo> images) {
		this.images = images;
	}
	
	public static class ImageInfo {

		private String name;
		private String id;
		private List<Link> links;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public List<Link> getLinks() {
			return links;
		}
		public void setLinks(List<Link> links) {
			this.links = links;
		}
		
		@Override
		public String toString() {
			return String.format("Name: %s, Id: %s", name, id);
		}		
	}
}
