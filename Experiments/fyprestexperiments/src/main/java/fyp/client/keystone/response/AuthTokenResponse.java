package fyp.client.keystone.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Simple Auth Token to be mapped to for authenticating 
 * @author Conor
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthTokenResponse {

	AccessResponse access;

	public AccessResponse getAccess() {
		return access;
	}

	public void setAccess(AccessResponse access) {
		this.access = access;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AccessResponse {

		TokenResponse token;

		public TokenResponse getToken() {
			return token;
		}

		public void setToken(TokenResponse token) {
			this.token = token;
		}
		
		
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class TokenResponse {
			String id;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}
		}
	}
	
}
