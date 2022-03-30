package naturegecko.jingjok.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
public class ExceptionFoundation extends RuntimeException {
	
	private ExceptionResponseModel.EXCEPTION_CODES excDetails;
	
	public ExceptionFoundation(ExceptionResponseModel.EXCEPTION_CODES excDetails, String exc) {
		super(exc);
		this.excDetails = excDetails;
	}
	
	public ExceptionResponseModel.EXCEPTION_CODES getExceptionCode(){
		return this.excDetails;
	}
}
