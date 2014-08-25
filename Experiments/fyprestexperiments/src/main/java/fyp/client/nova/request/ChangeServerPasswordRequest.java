package fyp.client.nova.request;

public class ChangeServerPasswordRequest {

	private Password password;

	public ChangeServerPasswordRequest(String password) {
		this.password = new Password(password);
	}

	public static class Password {

		private String adminPass;

		public Password(String pass) {
			this.adminPass = pass;
		}

		public String getAdminPass() {
			return adminPass;
		}

		public void setAdminPass(String adminPass) {
			this.adminPass = adminPass;
		}
	}

	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}

}
