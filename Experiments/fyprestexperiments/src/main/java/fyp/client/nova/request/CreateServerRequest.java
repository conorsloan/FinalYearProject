package fyp.client.nova.request;

public class CreateServerRequest {

	private ServerParams server;

	public CreateServerRequest(ServerParams server) {
		this.server = server;
	}

	public ServerParams getServer() {
		return server;
	}

	public void setServer(ServerParams server) {
		this.server = server;
	}

	public static class ServerParams {

		private String flavorRef;
		private String imageRef;
		private String name;

		public ServerParams() {

		}

		public ServerParams(String flavorRef, String imageRef, String name) {
			this.flavorRef = flavorRef;
			this.imageRef = imageRef;
			this.name = name;
		}

		public String getFlavorRef() {
			return flavorRef;
		}

		public void setFlavorRef(String flavorRef) {
			this.flavorRef = flavorRef;
		}

		public String getImageRef() {
			return imageRef;
		}

		public void setImageRef(String imageRef) {
			this.imageRef = imageRef;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
