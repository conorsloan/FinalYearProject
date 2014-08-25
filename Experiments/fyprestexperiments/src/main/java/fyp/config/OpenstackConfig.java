package fyp.config;

public class OpenstackConfig {

	private String novaEndpoint;
	private String glanceEndpoint;
	private String keystoneEndpoint;
	private String username;
	private String password;
	private String tenantName;
	private String tenantId;
	
	public String getNovaEndpoint() {
		return novaEndpoint;
	}
	public void setNovaEndpoint(String novaEndpoint) {
		this.novaEndpoint = novaEndpoint;
	}
	public String getGlanceEndpoint() {
		return glanceEndpoint;
	}
	public void setGlanceEndpoint(String glanceEndpoint) {
		this.glanceEndpoint = glanceEndpoint;
	}
	public String getKeystoneEndpoint() {
		return keystoneEndpoint;
	}
	public void setKeystoneEndpoint(String keystoneEndpoint) {
		this.keystoneEndpoint = keystoneEndpoint;
	}
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
}
