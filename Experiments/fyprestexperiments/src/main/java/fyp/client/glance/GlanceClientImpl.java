package fyp.client.glance;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import fyp.client.OpenstackRestClient;
import fyp.client.glance.data.Image;
import fyp.client.glance.data.ImageInfo;
import fyp.client.glance.request.CreateImageRequest;
import fyp.client.glance.response.GetImageInfoResponse;
import fyp.config.OpenstackConfig;
import fyp.utils.WebServiceUtils;

public class GlanceClientImpl extends OpenstackRestClient implements GlanceClient {

	private static final Logger logger = LogManager.getLogger(GlanceClientImpl.class.getName());

	private static final String IMAGES = "/images";

	private String authToken;

	public GlanceClientImpl(OpenstackConfig config, RestTemplate restTemplate) {
		super(config, restTemplate);
	}

	@Override
	public boolean validateClient() {
		return true; // TODO
	}

	@Override
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	@Override
	public List<ImageInfo> getImageInfo() {
		String imageUrl = config.getGlanceEndpoint() + IMAGES;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate.exchange(imageUrl, HttpMethod.GET, request, GetImageInfoResponse.class).getBody().getImages();
	}

	@Override
	public Image getImageById(String imageId) {
		String imageUrl = config.getGlanceEndpoint() + IMAGES + "/" + imageId;
		HttpEntity<Void> request = new HttpEntity<>(null, WebServiceUtils.getAuthHeaders(authToken));
		return restTemplate.exchange(imageUrl, HttpMethod.GET, request, Image.class).getBody(); // This
																								// is
																								// awkward!
	}

	@Override
	public Image createImage(String id, String name, boolean publicVisibility, String... tags) {
		String imageUrl = config.getGlanceEndpoint() + IMAGES;

		HttpEntity<CreateImageRequest> request = new HttpEntity<>(new CreateImageRequest(id, name, publicVisibility, tags),
				WebServiceUtils.getAuthHeaders(authToken));

		CreateImageRequest x = new CreateImageRequest(id, name, publicVisibility, tags);
		try {
			logger.info(WebServiceUtils.writeObjectToJson(x));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseEntity<Image> response = restTemplate.exchange(imageUrl, HttpMethod.POST, request, Image.class);
		logger.info("HELLO" + response.getBody());
		return response.getBody();
	}

}
