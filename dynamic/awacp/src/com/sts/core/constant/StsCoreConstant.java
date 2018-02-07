package com.sts.core.constant;

public class StsCoreConstant {
	public static final String SUCCESS = "success";
	public static final String UNKNOWN_SERVICE_PROVIDER = "unknown_service_provider";
	public static final String ERROR = "error";
	public static final String NO_STATUS = "no_status";
	public static final String SHIPMENT_TRACKING_ERROR = "shipment_tracking_error";
	public static final String NO_TRACKING_SEARCH_FORM = "no_tracking_search_form";
	public static final String UNABLE_TO_CONNECT = "unable_to_connect";
	public static final String UNABLE_TO_PARSE_CONTENT = "unable_to_parse_content";
	public static final String UNABLE_TO_CONNECT_POD = "unable_to_connect_pod";
	public static final String UNABLE_TO_PARSE_POD_CONTENT = "unable_to_parse_pod_content";
	public static final String INV_MODE_INV = "inv";
	public static final String INV_MODE_BILL = "bill";
	public static final String INV_MODE_PTL = "ptl";
	public static final String INV_SHIPMENT_TYPE_PARTIAL = "PARTIAL";
	public static final String INV_SHIPMENT_TYPE_FULL = "FULL";
	public static final String INV_AW_IMAGE_DOC = "INV_AW_IMAGE_DOC";
	public static final String INV_AWF_IMAGE_DOC = "INV_AWF_IMAGE_DOC";
	public static final String INV_SPL_IMAGE_DOC = "INV_SPL_IMAGE_DOC";
	public static final String INV_SBC_IMAGE_DOC = "INV_SBC_IMAGE_DOC";
	public static final String INV_J_IMAGE_DOC = "INV_J_IMAGE_DOC";
	public static final String DOC_TAKEOFF = "TAKEOFF_DOC";
	public static final String DOC_QUOTE_DOC = "QUOTE_DOC";
	public static final String DOC_QUOTE_PDF = "QUOTE_PDF_DOC";
	public static final String DOC_QUOTE_XLS = "QUOTE_XLS_DOC";
	public static final String DOC_QUOTE_VIBRO = "QUOTE_VIBRO_DOC";

	public static final String DOC_OB_A_DOC = "OB_A_DOC";
	public static final String DOC_OB_Y_XLS = "OB_Y_XLS";
	public static final String DOC_OB_ACK_PDF = "OB_ACK_PDF";
	public static final String DOC_OB_FRT_PDF = "OB_FRT_PDF";

	public static final String DOC_JO_ONE = "JO_ONE"; // A
	public static final String DOC_JO_TWO = "JO_TWO"; // V
	public static final String DOC_JO_THREE = "JO_THREE"; // E
	public static final String DOC_JO_FOUR = "JO_FOUR"; // T
	public static final String DOC_JO_FIVE = "JO_FIVE"; // Y
	public static final String DOC_JO_SIX = "JO_SIX"; // X
	public static final String DOC_JO_XLS = "JO_XLS";
	public static final String DOC_JO_DOC = "JO_DOC";
	public static final String DOC_JO_TAX = "JO_TAX";
	public static final String DOC_JO_PO = "JO_PO";
	public static final String DOC_JO_IU = "JO_IU";
	
	public static final String DOC_TC_PDF = "TC_PDF";
	public static final String DOC_FC_PDF = "FC_PDF";
	

	public static final String DOC_TAKEOFF_DRAWING = "TAKEOFF_DRAWING_DOC";
	public static final String DOC_TAKEOFF_VIBRO = "TAKEOFF_VIBRO_DOC";
	public static final String DUPLICATE_EMAIL = "duplicate_email";
	public static final String DUPLICATE_NAME = "duplicate_name";
	public static final String DUPLICATE_TEMPLATE = "duplicate_template";
	public static final String DUPLICATE_MULTIPLIER = "duplicate_multiplier";
	public static final String DUPLICATE_USERNAME = "duplicate_username";
	public static final String DUPLICATE_CODE = "duplicate_code";
	public static final String SINGLE_USER = "single_user";
	public static final String ACCOUNT_NOT_ACTIVE = "account_not_active";
	public static final String MULTIPLE_ACCOUNT_FOUND = "multiple_account_found";
	public static final String ACCOUNT_ACTIVATION_SUCCESS = "account_activation_success";
	public static final String MANUAL_SIGNUP_EMAIL_EVENT = "sendSignUpVerificationMail";
	public static final String PASSWORD_RESET_EMAIL_EVENT = "sendPasswordResetMail";
	public static final String NOT_IMPLEMENTED_YET = "not_implemented_yet";
	public static final String PROFILE_TYPE_MANUAL = "manual";
	public static final String PROFILE_TYPE_LINKEDIN = "linkedin";
	public static final String PROFILE_TYPE_FACEBOOK = "facebook";
	public static final String PROFILE_TYPE_TWITTER = "twitter";
	public static final String NO_CREDENTIAL = "no_credential_provided";
	public static final String NO_PASSWORD_RESET_MAIL_FOUND = "no_password_reset_history_found";
	public static final String PASSWORD_RESET_SUCCESS = "password_reset_success";
	public static final String PASSWORD_SET_SUCCESS = "password_set_success";
	public static final String OTP_MISMATCH = "otp_mismatch_error";
	public static final String NO_DETAIL_PROVIDED = new StringBuilder("no_ %s,").append("_detail_provided").toString();
	public static final String USER_ALREADY_EXISTS = "user_already_exists";
	public static final String USER_PROFILE_ALREADY_EXISTS = "user_profile_already_exists";
	public static final String USER_CREATE_SUCCESS = "user_created_successfully";
	public static final String USER_NOT_FOUND = "user_not_found";
	public static final String USER_PROFILE_NOT_FOUND = "user_profile_not_found";
	public static final String MAIL_SEND_SUCCESS = "mail_send_success";
	public static final String MAIL_SEND_FAIL = "mail_send_fail";
	public static final String USER_PROFILE_MERGE_SUCCESS = "user_profile_merge_success";
	public static final String SIGNUP_SUCCESS = "signup_success";
	public static final String USER_UNAUTHORIZED = "user_unauthorized";
	public static final String PROFILE_CREATE_SUCCESS = "profile_create_success";
	public static final String PROFILE_MERGE_SUCCESS = "profile_merged_success";
	// 1- $s(User Name), 2- $s (email link), 3- $s(Project or Company Name), 4-
	// $s(Project Team or CompanyName)
	public static final String SIGNUP_SUCCESS_MAIL_MESSAGE = new StringBuilder("Dear %s,")
			.append(System.lineSeparator()).append(System.lineSeparator())
			.append(" Please activate your account with %s by clicking the following link: ")
			.append(System.lineSeparator()).append(System.lineSeparator()).append(" %s ").append(System.lineSeparator())
			.append(System.lineSeparator()).append(" --- ").append(System.lineSeparator()).append(" With Regards. ")
			.append(System.lineSeparator()).append(" %s Team.").toString();
	// 1- $s(User Name), 2- $s (Project Name), 3- $s(write us email), 4-
	// $s(Project Team or CompanyName)
	public static final String FACEBOOK_SIGNUP_MESSAGE = new StringBuilder("Dear %s,").append(System.lineSeparator())
			.append(System.lineSeparator())
			.append(" This is to inform you that the Facebook account associated with this email was used to signup into %s. ")
			.append(System.lineSeparator()).append(System.lineSeparator())
			.append(" If this is not you, please write us at ").append(" %s ").append(System.lineSeparator())
			.append(System.lineSeparator()).append(" --- ").append(System.lineSeparator()).append(" With Regards. ")
			.append(System.lineSeparator()).append(" %s Team.").toString();
	// 1- $s(User name), 2- $s(Project or Company Name), 3- $s(OTP), 4- $s(Team
	// name)
	public static final String PASSWORD_RESET_MAIL_MESSAGE = new StringBuilder("Dear %s,")
			.append(System.lineSeparator()).append(System.lineSeparator())
			.append("This is to inform you that; we at %s have received password reset request for the account associated with this email.")
			.append(System.lineSeparator())
			.append("Please verify your email by providing OTP(One Time Password) attached below.")
			.append(System.lineSeparator()).append(System.lineSeparator()).append("OTP:%s")
			.append(System.lineSeparator()).append(System.lineSeparator()).append(System.lineSeparator())
			.append(" --- ").append(System.lineSeparator()).append(" With Regards. ").append(System.lineSeparator())
			.append(" %s Team.").toString();

	// 1- $s(User name), 2- $s(Project or Company Name), 3- $s(OTP), 4- $s(Team
	// name)
	public static final String ACCOUNT_VERIFICATION_MAIL = new StringBuilder("Dear %s,").append(System.lineSeparator())
			.append(System.lineSeparator())
			.append("Thanks for Signing Up with %s. Please activate your accout using verification code given below.")
			.append(System.lineSeparator()).append("%s").append(System.lineSeparator()).append(System.lineSeparator())
			.append("Enjoy Your Social Connection!").append(System.lineSeparator()).append(System.lineSeparator())
			.append(" --- ").append(System.lineSeparator()).append(" With Regards. ").append(System.lineSeparator())
			.append(" %s Team.").toString();

	public static final String SIGNUP_SUCCESS_OTP_MESSAGE = new StringBuilder("Dear %s,").append(System.lineSeparator())
			.append(System.lineSeparator())
			.append(" Please activate your account with %s by entering the following code: ")
			.append(System.lineSeparator()).append(System.lineSeparator()).append(" %s ").append(System.lineSeparator())
			.append(System.lineSeparator()).append(" --- ").append(System.lineSeparator()).append(" With Regards. ")
			.append(System.lineSeparator()).append(" %s Team.").toString();

}
