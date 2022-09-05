package com.application.exceptons;

import java.util.Date;
import org.springframework.http.HttpStatus;
import lombok.Data;

@Data
public class ExceptionResponseModel {
	public enum EXCEPTION_CODES {
		// Searching
		SEARCH_NOT_FOUND(1001), // Resource not found.
		SEARCH_CAN_NOT_READ(1002), // Cab't read, for some reason...
		SEARCH_NO_PAGE_HERE(1003), // When there are 5 pages available but user goes for 6.
		SEARCH_SIZE_INVALID(1004), // Limit size request for a page, preventing our database on fire.

		// Saving
		SAVE_FILE_FAILED(2001), // Can't save image or file.
		SAVE_FILE_INVALID(2002), // Wrong format
		SAVE_EXISTS(2003), // Already exist
		SAVE_IS_NULL(2004),

		// Authentication
		AUTHEN_BAD_CREDENTIALS(3001), // Username or password is incorrect. Or both!
		AUTHEN_USERNAME_ALREADY_EXISTED(3002), // When registering the username but someone already took it.
		AUTHEN_REGISTERATION_FAILED(3003), // Maybe the user forgot to create their password.
		AUTHEN_PHONE_NUMBER_ALREADY_EXISTED(3004), // Someone owned this phone number.
		AUTHEN_TOKEN_MALFUNCTION(3005), // Token is broken.
		AUTHEN_ILLEGAL_CHAR(3006), // This character of the string is not allowed.
		AUTHEN_NOT_ALLOWED(3007), // When the account is suspended.
		AUTHEN_HORRIBLE_TOKEN(3008), // This token is invalid, nothing can be found here.
		AUTHEN_EMAIL_ALREADY_EXIST(3009), // Email is taken
		AUTHEN_IS_TAKEN(3010), AUTHEN_PASSWORD_MISSMATCH(3011),

		// Unavailable, at least for now.
		FEATURE_NOT_IMPLEMENTED(4001), // Not yet done.
		FEATURE_KNOWN_BUG(4002), // We just know that it is going to be an error. At least the application
									// doesn't rest in peace.
		FEATURE_TOO_DANGEROUS_TO_HAVE_IT(4003), // We won't let you use it...
		FEATURE_MISS_USED(4004),

		// Application core infrastructure
		CORE_INIT_FAILED(5001), // File initialization failed.
		CORE_METHOD_FAILED(5002), CORE_FILE_DUPLICATED(5003), // File duplicated.
		CORE_COMPRESSION_FAILURE(5004), CORE_MINIO_NOT_FOUND(5005), CORE_NOT_IMPLEMENTED(5006),
		CORE_INTERNAL_SERVER_ERROR(5007),

		// User application
		SHOP_NOT_ON_STORE(6001), // Sorry! they don't want to sell this product!
		SHOP_NOT_ENOUGH_GOODS_FOR_SELL(6002), // Have 3 but order 4.
		SHOP_NOT_ALLOW_TO_CANCLE(6003), // You paid for your product but want to cancle.
		SHOP_NOT_ALLOW_TO_CHANGE_STATUS(6004), // You paid for your product but want to cancle.
		SHOP_THIS_IS_YOUR_PRODUCT(6005), // What is the reason you buy an item that you are currently selling.

		// MINIO SERVER
		MINIO_BUCKET_UNREACHABLE(7001), // Minio bucket is not reachable.
		MINIO_OBJECT_UNREACHABLE(7002), // Minio object is unreachable.
		MINIO_OBJECT_INVALID(7003), // What they have and what we need are not the same.

		// User

		// Others
		DEAD(9999); // Just stupidly died with an unknown reason.

		private final int codeValue;

		private EXCEPTION_CODES(int codeValue) {
			this.codeValue = codeValue;
		}

		public int getCodeValue() {
			return codeValue;
		}
	}

	private int exceptionCode;
	private EXCEPTION_CODES reason;
	private Date timeStamp;
	private String message;
	private String details;
	private HttpStatus httpStatus;

	public ExceptionResponseModel(EXCEPTION_CODES reason, Date timeStamp, String message, String details,
			HttpStatus httpStatus) {
		this.reason = reason;
		this.exceptionCode = reason.codeValue;
		this.timeStamp = timeStamp;
		this.message = message;
		this.details = details;
		this.httpStatus = httpStatus;
	}
}
