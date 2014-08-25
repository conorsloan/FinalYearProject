package fyp.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebServiceUtils {

	private WebServiceUtils() {
		// Prevents instantiation
	}

	/**
	 * Given a URL, normalises problems in the URL such as lack of / after port
	 * number, double slashes after the port number, which can be caused by
	 * problems with config files & cause problems
	 * 
	 * @param URL
	 *            The URL to be normalised
	 * @return Modified version of the given URL
	 */
	public static String normaliseURL(String URL) {
		return URL;
	}

	public static HttpHeaders getAuthHeaders(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Auth-Token", authToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	public static String writeObjectToJson(Object x) throws JsonProcessingException {
		ObjectMapper om = new MappingJackson2HttpMessageConverter().getObjectMapper();
		return om.writer().writeValueAsString(x);
	}

}
