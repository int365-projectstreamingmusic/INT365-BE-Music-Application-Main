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
		SAVE_IS_NULL(2004), // ---------
		SAVE_FORBIDDEN(2005), // ---------

		// Authentication
		AUTHEN_BAD_CREDENTIALS(101), // Username or password is incorrect. Or both!
		// AUTHEN_USERNAME_ALREADY_EXISTED(102), // When registering the username but
		// someone already took it.
		// AUTHEN_REGISTERATION_FAILED(103), // Maybe the user forgot to create their
		// password.
		// AUTHEN_PHONE_NUMBER_ALREADY_EXISTED(104), // Someone owned this phone number.
		AUTHEN_TOKEN_MALFUNCTION(105), // Token is broken.
		// AUTHEN_ILLEGAL_CHAR(106), // This character of the string is not allowed.
		// AUTHEN_NOT_ALLOWED(107), // When the account is suspended.
		// AUTHEN_HORRIBLE_TOKEN(108), // This token is invalid, nothing can be found
		// here.
		// AUTHEN_EMAIL_ALREADY_EXIST(109), // Email is taken
		// AUTHEN_IS_TAKEN(110), AUTHEN_PASSWORD_MISSMATCH(111),
		// AUTHEN_NOT_SUFFICIENT_PRIVILEGE(112),

		// User application
		SHOP_NOT_ON_STORE(6001), // Sorry! they don't want to sell this product!
		SHOP_NOT_ENOUGH_GOODS_FOR_SELL(6002), // Have 3 but order 4.
		SHOP_NOT_ALLOW_TO_CANCLE(6003), // You paid for your product but want to cancle.
		SHOP_NOT_ALLOW_TO_CHANGE_STATUS(6004), // You paid for your product but want to cancle.
		SHOP_THIS_IS_YOUR_PRODUCT(6005), // What is the reason you buy an item that you are currently selling.

		// MINIO SERVER
		MINIO_BUCKET_UNREACHABLE(1001), // Minio bucket is not reachable.
		MINIO_OBJECT_UNREACHABLE(1002), // Minio object is unreachable.
		MINIO_OBJECT_INVALID(1003), // What they have and what we need are not the same.
		MINIO_OBJECT_FORMAT_NOT_SUPPORT(1004),
		MINIO_OBJECT_DOES_NOT_EXIST(1005),

		// User

		// Others
		DEAD(9999), // Just stupidly died with an unknown reason.

		// User registeration
		REGISTERATION_INVALID_EMAIL_FORMAT(10001), // ---------
		REGISTERATION_INVALID_PASSWORD_FORMAT(10002), // ---------
		REGISTERATION_INVALID_USERNAME_FORMAT(10003), // ---------
		REGISTERATION_TAKEN_EMAIL(10004), // ---------
		REGISTERATION_TAKEN_USERNAME(10005), // ---------
		REGISTERATION_PASSWORD_MISMATCH(10006), // ---------

		// User Authentication and authorization
		AUTHEN_INCORRECT_CREDENTIALS(20001), // ---------
		AUTHEN_ACCOUNT_SUSPENDED(20002), // ---------
		AUTHEN_INSUFFICIENT_PRIVILEGES(20003), // ---------
		AUTHEN_NOT_THE_OWNER(20004), // ---------
		AUTHEN_NOT_FOUND(20005), // ---------
		AUTHEN_BAD_TOKEN(20006), // ---------

		AUTHEN_PROVIDER_NOT_FOUND(20007),

		// User action
		
		// Database
		INVALID_DATABASE_INIT(10001),
		
		FILE_INVALID_TYPE(10001),

		// Browsing
		BROWSE_NO_RECORD_EXISTS(40001), // ---------
		BROWSE_IMPOSSIBLE(40002), // ---------
		BROWSE_FORBIDDEN(40003), // ---------

		// Deleting
		RECORD_ALREADY_GONE(50001), // ---------
		RECORE_ALREADY_EXIST(50002), // ---------
		RECORD_INVALID_STATUS(50003), // ---------
		RECORD_HAS_REPLY(50004), // ---------
		RECORD_HAS_REPORT(50006), // ---------

		// Unavailable, at least for now.
		FEATURE_NOT_IMPLEMENTED(60001), // Not yet done.
		FEATURE_KNOWN_BUG(60002), // We just know that it is going to be an error.
		FEATURE_TOO_DANGEROUS_TO_HAVE_IT(60003), // We won't let you use it...
		FEATURE_MISS_USED(60004), // ---------

		// Application core infrastructure
		CORE_INIT_FAILED(70001), // File initialization failed.
		CORE_METHOD_FAILED(70002), // ---------
		CORE_FILE_DUPLICATED(70003), // File duplicated.
		CORE_COMPRESSION_FAILURE(70004), // ---------
		CORE_MINIO_NOT_FOUND(70005), // ---------
		CORE_NOT_IMPLEMENTED(70006), // ---------
		CORE_INTERNAL_SERVER_ERROR(70007), // ---------

		// REPORTS
		REPORT_ALREADY_MADE(80001), REPORT_INVALID_TYPE(80002), REPORT_INVALID_STATUS(80003),
		REPORT_INVALID_GENRE(80004),

		// Related to role browsing
		ROLE_NOT_FOUND(11001), // ---------
		ROLE_INSUFFICIENT_PRIVILEGE(11002), // ---------
		ROLE_FORBIDDEN_STRENGTH(11003), // ---------
		ROLE_ALREADY_EXIST(11004), // ---------
		ROLE_ALREADY_GONE(11005), // ---------
		ROLE_FORBIDDEN(11006), // ---------

		USER_ACCOUNT_NOT_FOUND(12001), // ---------
		USER_SEARCH_NOT_FOUND(12002), // ---------
		USER_SAVE_REJECTED(12003), // ---------
		USER_ILLEGAL_NAME(12004), // ---------
		USER_TOKEN_NOT_FOUND(12005), // ---------
		USER_NO_SELF_SUSPEND(12006); // ---------

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
