package fyp.client.nova.request;

public class RebootServerRequest {

	private Reboot reboot;

	public enum RebootType {
		HARD, SOFT
	}

	public RebootServerRequest(RebootType type) {
		this.reboot = new Reboot(type);
	}

	public static class Reboot {

		public Reboot(RebootType type) {
			this.type = type;
		}

		private RebootType type;

		public RebootType getType() {
			return type;
		}

		public void setType(RebootType type) {
			this.type = type;
		}
	}

	public Reboot getReboot() {
		return reboot;
	}

	public void setReboot(Reboot reboot) {
		this.reboot = reboot;
	}
}
