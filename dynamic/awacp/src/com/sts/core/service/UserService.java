package com.sts.core.service;

import java.util.List;

import com.sts.core.dto.Menu;
import com.sts.core.dto.PermissionGroup;
import com.sts.core.dto.StsCoreResponse;
import com.sts.core.dto.StsResponse;
import com.sts.core.dto.UserDTO;
import com.sts.core.entity.Address;
import com.sts.core.entity.PasswordResetHistory;
import com.sts.core.entity.Permission;
import com.sts.core.entity.User;
import com.sts.core.exception.StsCoreException;

public interface UserService {

	public User getByUserCode(final String code);

	public UserDTO getUserMinimumInfo(final String userNameOrEmail);

	public UserDTO getUserMinimumInfo(final Long userId);

	public User getUserDetails(String email);

	public User getUserDetails(String email, String userName);

	public User findUser(Long userId);

	public StsResponse<User> listUser(int pageNumber, int pageSize);
	
	public StsResponse<User> listArchivedUser(int pageNumber, int pageSize);

	public User saveUser(User user) throws StsCoreException;

	public String removeUser(Long userId) throws StsCoreException;
	
	public String archiveUser(Long userId) throws StsCoreException;
	
	public String activateUser(Long userId) throws StsCoreException;

	public User getUserDetail(String userNameOrEmail, String loginType, boolean verified);

	public User getUserByUserNameOrEmail(String userNameOrEmail);

	public User updateUser(User user) throws StsCoreException;

	public Long recordPasswordResetInfo(PasswordResetHistory prh);

	public boolean checkOTP(String otp);

	public StsCoreResponse resetPassword(String email, String otp, String password);

	public StsCoreResponse setPassword(String email, String otp, String password);

	public StsCoreResponse doSignup(User user);

	public StsCoreResponse activateAccount(String verificationCode);

	public List<UserDTO> searchUserByFilter(String filterString);

	public List<User> listUser(String keyword);

	public Address getAddress(Long userId);

	public Address saveAddress(Address address);

	public String getUserCode(final String userNameOrEmail);

	public List<Permission> listPermissions();

	public List<PermissionGroup> groupPermissionsGroup();

	public Permission getPermission(String permissionName);

	public List<Object[]> getUniquePermissionGroups();

	public List<Permission> getAllMatchingPermissions(String keyword);

	public List<Menu> getUserMenu(Long userId);

	public List<Menu> getUserMenu(String userNameOrEmail);

	public User getUserWithPermissions(Long userId);

	public List<User> filterUsers(String name, String userId, String userCode, String email, String status);

	public List<User> filterByName(String name); // match name

	public List<User> filterByCode(String code); // match code
	
	public List<UserDTO> listUsersByOnlineStatus(String onlineStatus);
	
	public int updateUserOnlineStatus(Long userId, boolean status);
}
