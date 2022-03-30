package naturegecko.jingjok.models.entities;

import javax.persistence.Id;

import lombok.Data;

@Data
public class ImagesListModels {
	
	@Id
	private String imageId;
	private String belongTo;
	private String type;
}
