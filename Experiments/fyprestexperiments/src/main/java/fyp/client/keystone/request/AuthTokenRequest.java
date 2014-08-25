package fyp.client.keystone.request;

public class AuthTokenRequest {
	AuthorisationDetails auth;

	public AuthTokenRequest(String username, String password, String tenantName) {
		this.auth = new AuthorisationDetails(username, password, tenantName);
	}

	public AuthorisationDetails getAuth() {
		return auth;
	}

	public void setAuth(AuthorisationDetails auth) {
		this.auth = auth;
	}

	public static class AuthorisationDetails {

		PasswordCredentials passwordCredentials;
		String tenantName;
		String tenantId;

		public AuthorisationDetails(String username, String password, String tenantName) {
			this.passwordCredentials = new PasswordCredentials(username, password);
			this.tenantName = tenantName;
		}

		public PasswordCredentials getPasswordCredentials() {
			return passwordCredentials;
		}

		public void setPasswordCredentials(PasswordCredentials passwordCredentials) {
			this.passwordCredentials = passwordCredentials;
		}

		public String getTenantName() {
			return tenantName;
		}

		public void setTenantName(String tenantName) {
			this.tenantName = tenantName;
		}

		public String getTenantId() {
			return tenantId;
		}

		public void setTenantId(String tenantId) {
			this.tenantId = tenantId;
		}

		public static class PasswordCredentials {

			private String username;
			private String password;

			public String getUsername() {
				return username;
			}

			public void setUsername(String username) {
				this.username = username;
			}

			public String getPassword() {
				return password;
			}

			public void setPassword(String password) {
				this.password = password;
			}

			public PasswordCredentials() {
				// TODO Auto-generated constructor stub
			}

			public PasswordCredentials(String username, String password) {
				this.username = username;
				this.password = password;
			}
		}
	}
}
