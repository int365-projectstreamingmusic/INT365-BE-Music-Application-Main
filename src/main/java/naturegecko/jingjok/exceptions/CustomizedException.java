package naturegecko.jingjok.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomizedException {

	@ExceptionHandler(ExceptionFoundation.class)
	public ResponseEntity<ExceptionResponseModel> handleException(ExceptionFoundation exc, WebRequest request) {
		ExceptionResponseModel response = new ExceptionResponseModel(exc.getExceptionCode(), new Date(),
				exc.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(response, HttpStatus.I_AM_A_TEAPOT);
	}

}
