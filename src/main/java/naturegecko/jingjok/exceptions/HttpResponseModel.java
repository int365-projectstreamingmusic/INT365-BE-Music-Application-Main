package naturegecko.jingjok.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unused")
@Data
@Getter
@Setter
public class HttpResponseModel {
	private int responseCode;
	private HttpStatus httpStatus;
	private String details;
	private String mssg;
}
