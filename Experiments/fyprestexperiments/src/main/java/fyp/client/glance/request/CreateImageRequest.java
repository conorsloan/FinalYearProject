package fyp.client.glance.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateImageRequest {
	private String id;
	private String name;
	private String visibility;
	private List<String> tags;

	private final static String PUBLIC = "public";
	private final static String PRIVATE = "private";

	public CreateImageRequest(String id, String name, boolean publicVisibility, String... tags) {
		this.id = id;
		this.name = name;
		if (tags != null && tags.length > 0) {
			this.tags = Arrays.asList(tags);
		} else {
			this.tags = new ArrayList<>();
		}
		if (publicVisibility) {
			this.visibility = PUBLIC;
		} else {
			this.visibility = PRIVATE;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
