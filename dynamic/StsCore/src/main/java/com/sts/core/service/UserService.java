package com.sts.core.service;

import java.util.List;

import com.sts.core.dto.StsCoreResponse;
import com.sts.core.dto.UserDTO;
import com.sts.core.entity.Address;
import com.sts.core.entity.PasswordResetHistory;
import com.sts.core.entity.User;

public interface UserService {

	/**
	 * Return true on authenticate success, false otherwise.
	 * 
	 * @param userName
	 *            unique user ID
	 * @param password
	 *            Password of the user trying to login
	 * @param loginType
	 *            Login type i.e 'manual', 'facebook', 'twitter', 'linkedin'
	 *            etc.
	 * @return true on authenticate success, false otherwise.
	 */

	public boolean authenticate(String userName, String password, String loginType);

	/**
	 * Return User if found based on email provided as parameter, null otherwise
	 * 
	 * @param email
	 *            email id of the user based on which user will be searched.
	 * @return User if found based on email provided as parameter, null
	 *         otherwise
	 */
	public User getUserDetails(String email);

	/**
	 * Return User if found, based on email id and user id, null otherwise.
	 * 
	 * @param email
	 *            Email id of the user
	 * @param userName
	 *            unique user ID
	 * @return User based on email id and user id.
	 */
	public User getUserDetails(String email, String userName);

	/**
	 * Return User based on user ID if found, null otherwise
	 * 
	 * @param userId
	 *            unique user ID based on which search will be performed.
	 * @return User based on user ID if found, null otherwise
	 */
	public User findUser(Long userId);

	/**
	 * Return list of users if available, empty list otherwise
	 * 
	 * @return List of users if available, empty list otherwise
	 */
	public List<User> listUser();

	
	public User saveUser(User user);

	public void removeUser(Long userId);

	/**
	 * Return User detail if found based on userName or email, password, and
	 * loginType, null otherwise
	 * 
	 * @param userNameOrEmail
	 *            Either userName or email id
	 * @param loginType
	 *            User login type
	 * @param verified
	 *            True or false
	 * @return User detail based on userName or email, password, and loginType
	 */
	public User getUserDetail(String userNameOrEmail, String loginType, boolean verified);

	/**
	 * Return User detail if found based on email id or userName passed in as
	 * parameter, null otherwise.
	 * 
	 * @param userNameOrEmail
	 * @return User detail if found based on email or userName passed in as
	 *         parameter, null otherwise.
	 */
	public User getUserByUserNameOrEmail(String userNameOrEmail);

	/**
	 * Return updated user with profile
	 * 
	 * @param user
	 *            User detail
	 * @return Updated user with profile
	 */
	public User updateUser(User user);
	
	public Long recordPasswordResetInfo(PasswordResetHistory prh); 
	
	public boolean checkOTP(String otp);
	
	public StsCoreResponse resetPassword(String email, String otp, String password);
	
	public StsCoreResponse setPassword(String email, String otp, String password);
	
	/**
	 * Return signup status
	 * 
	 * @param user
	 *            sign up detail of the user
	 * @return Return signup status i.e 'signup_success' or
	 *         'user_already_exists'
	 */
	public StsCoreResponse doSignup(User user);

	/**
	 * Return JykraSocialStatus consist of user account activation status.
	 * 
	 * @param verificationCode
	 *            The account will be activated based on the unique code
	 * @return JykraSocialStatus consist of user account activation status.
	 */
	public StsCoreResponse activateAccount(String verificationCode);
	
	public List<UserDTO> searchUserByFilter(String filterString);
	
	public List<User> listUser(String keyword);
	
	public Address getAddress(Long userId);
	
	public Address saveAddress(Address address);
}
