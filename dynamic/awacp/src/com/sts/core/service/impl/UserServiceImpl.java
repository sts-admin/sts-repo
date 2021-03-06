/**
 * 
 */
package com.sts.core.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.StringUtils;
import com.sts.core.config.AppPropConfig;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.Menu;
import com.sts.core.dto.MenuItem;
import com.sts.core.dto.PermissionGroup;
import com.sts.core.dto.StsCoreResponse;
import com.sts.core.dto.StsResponse;
import com.sts.core.dto.UserDTO;
import com.sts.core.entity.Address;
import com.sts.core.entity.File;
import com.sts.core.entity.PasswordResetHistory;
import com.sts.core.entity.Permission;
import com.sts.core.entity.User;
import com.sts.core.exception.StsCoreException;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.ChatMessageService;
import com.sts.core.service.UserService;
import com.sts.core.util.ConversionUtil;
import com.sts.core.util.MenuItemComparator;
import com.sts.core.util.SecurityEncryptor;

public class UserServiceImpl extends CommonServiceImpl<User> implements UserService {

	@Autowired
	ChatMessageService chatService;

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	@SuppressWarnings("unchecked")
	public User getUserDetails(String email, String userName) {
		User user = null;
		Query userQuery = entityManager.createNamedQuery("User.findUserByNameOrEmail");
		userQuery.setParameter("email", email.toLowerCase());
		userQuery.setParameter("userName", userName.toLowerCase());
		List<User> users = userQuery.getResultList();
		if (users != null && !users.isEmpty()) {
			user = users.get(0);
			setUserPhotoAndName(user);
		}
		return user;
	}

	@Override
	@SuppressWarnings("unchecked")
	public User getUserDetails(String email) {
		User user = null;
		Query userQuery = entityManager.createNamedQuery("User.findUserByEmail");
		userQuery.setParameter("email", email.toLowerCase());
		List<User> users = userQuery.getResultList();
		if (users != null && !users.isEmpty()) {
			user = users.get(0);
			setUserPhotoAndName(user);
		}
		return user;
	}

	@Override
	public User findUser(Long userId) {
		User user = getEntityManager().find(User.class, userId);
		setUserPhotoAndName(user);
		return user;
	}

	@Override
	public StsResponse<User> listUser(int pageNumber, int pageSize) {
		StsResponse<User> response = listAll(pageNumber, pageSize, User.class.getSimpleName(), getEntityManager());
		if (response.getResults() != null && !response.getResults().isEmpty()) {
			for (User user : response.getResults()) {
				setUserPhotoAndName(user);
			}
		}
		return response;
	}

	@Override
	@Transactional
	public User updateUser(User user) throws StsCoreException {
		User existingUser = findUser(user.getId());
		if (existingUser.getId() != user.getId() && !existingUser.isDeleted()) {
			if (existingUser.getUserName().equalsIgnoreCase(user.getUserName())) {
				throw new StsDuplicateException("duplicate_username");
			}
			if (existingUser.getEmail().equalsIgnoreCase(user.getEmail())) {
				throw new StsDuplicateException("duplicate_email");
			}
			if (existingUser.getUserCode().equalsIgnoreCase(user.getUserCode())) {
				throw new StsDuplicateException("duplicate_code");
			}
		}

		String[] permissionArray = user.getPermissionArray();
		if (permissionArray != null && permissionArray.length > 0) {
			Set<Permission> permissions = new HashSet<Permission>();
			for (String permissionName : permissionArray) {
				Permission permission = getPermission(permissionName);
				if (permission != null) {
					permissions.add(permission);
				}
			}
			user.setPermissions(permissions);
		} else {
			user.getPermissions().clear();
		}
		user.setPermissionChanged(!user.getPermissions().equals(existingUser.getPermissions()));
		user.setVersion(existingUser.getVersion());
		if (existingUser.getPhoto() != null) {
			user.setPhoto(existingUser.getPhoto());
		}

		getEntityManager().merge(user);
		getEntityManager().flush();
		return user;
	}

	@Override
	@Transactional
	public User saveUser(User user) throws StsDuplicateException {
		User existingUser = getUserDetails(user.getEmail(), user.getUserName());
		if (existingUser != null && !existingUser.isDeleted()) {
			if (existingUser.getUserName().equalsIgnoreCase(user.getUserName())) {
				throw new StsDuplicateException("duplicate_username");
			}
			if (existingUser.getEmail().equalsIgnoreCase(user.getEmail())) {
				throw new StsDuplicateException("duplicate_email");
			}
		}
		if (getByUserCode(user.getUserCode()) != null && !existingUser.isDeleted()) {
			throw new StsDuplicateException("duplicate_code");
		}

		if (user.getPassword() != null) {
			user.setPassword(SecurityEncryptor.encrypt(user.getPassword()));
		}
		String[] permissionArray = user.getPermissionArray();
		if (permissionArray != null && permissionArray.length > 0) {
			Set<Permission> permissions = new HashSet<Permission>();
			for (String permissionName : permissionArray) {
				Permission permission = getPermission(permissionName);
				if (permission != null) {
					permissions.add(permission);
				}
			}
			user.setPermissions(permissions);
		} else {
			user.getPermissions().clear();
		}
		user.setAvtarImage("avatar.png");
		user.setVerified(true);
		user.setVerificationCode("DEFAULT");
		File userAvatarImage = new File();
		userAvatarImage.setCreatedName("avatar");
		userAvatarImage.setOriginalName("avatar");
		userAvatarImage.setExtension(".png");
		userAvatarImage.setContentType("image/png");
		getEntityManager().persist(userAvatarImage);
		user.setPhoto(userAvatarImage);
		getEntityManager().persist(user);
		getEntityManager().flush();
		return user;
	}

	@Override
	@Transactional
	public String removeUser(Long userId) throws StsCoreException {
		int total = getTotalRecords(User.class.getSimpleName(), "id", getEntityManager());
		if (total <= 1) {
			throw new StsCoreException("single_user");
		}
		User user = findUser(userId);
		if (user != null) {
			user.setArchived(true);
			user.setDeleted(true);
			getEntityManager().merge(user);
			return "success";
		}
		return "fail";
	}

	@Override
	@Transactional
	public String archiveUser(Long userId) throws StsCoreException {
		int total = getTotalRecords(User.class.getSimpleName(), "id", getEntityManager());
		if (total <= 1) {
			throw new StsCoreException("single_user");
		}

		User user = findUser(userId);
		if (user != null) {
			user.setArchived(true);
			getEntityManager().merge(user);
			return "success";
		}
		return "fail";
	}

	@Override
	@Transactional
	public String activateUser(Long userId) throws StsCoreException {
		User user = findUser(userId);
		if (user != null) {
			user.setArchived(false);
			user.setDeleted(false);
			getEntityManager().merge(user);
			return "success";
		}
		return "fail";
	}

	private void setUserPhotoAndName(User user) {
		if (user.getFirstName() == null) {
			user.setFirstName(" ");
		}
		if (user.getLastName() == null) {
			user.setLastName(" ");
		}
		String photoUrl = AppPropConfig.acBaseUrl + "/" + AppPropConfig.acImageDir;
		user.setPhotoUrl(photoUrl + "/" + user.getAvtarImage());

		if (user.getPhoto() != null) {
			user.setPhotoUrl(photoUrl + user.getPhoto().getCreatedName() + user.getPhoto().getExtension());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public User getUserByUserNameOrEmail(String userNameOrEmail) {
		User user = null;
		Query userQuery = entityManager.createNamedQuery("User.findUserByNameOrEmail");
		userQuery.setParameter("email", userNameOrEmail.toLowerCase());
		userQuery.setParameter("userName", userNameOrEmail.toLowerCase());
		List<User> users = userQuery.getResultList();
		if (users != null && !users.isEmpty()) {
			user = users.get(0);
			setUserPhotoAndName(user);
		}
		return user;
	}

	@Override
	public User getUserDetail(String userNameOrEmail, String loginType, boolean verified) {
		String queryHanlder = "User.Login";
		if (loginType.equalsIgnoreCase("facebook")) {
			queryHanlder = "FacebookProfile.Login";
		} else if (loginType.equalsIgnoreCase("twitter")) {
			queryHanlder = "TwitterProfile.Login";
		} else if (loginType.equalsIgnoreCase("linkedin")) {
			queryHanlder = "LinkedInProfile.Login";
		}
		Query userQuery = entityManager.createNamedQuery(queryHanlder);
		userQuery.setParameter("email", userNameOrEmail.toLowerCase());
		if ("manual".equals(loginType)) {
			userQuery.setParameter("userName", userNameOrEmail.toLowerCase());
		}
		userQuery.setParameter("verified", verified);
		@SuppressWarnings("unchecked")
		List<User> users = userQuery.getResultList();

		User user = null;
		if (users != null && !users.isEmpty()) {
			user = users.get(0);
			setUserPhotoAndName(user);
		}
		return user;
	}

	@Override
	@Transactional
	public Long recordPasswordResetInfo(PasswordResetHistory prh) {
		getEntityManager().persist(prh);
		getEntityManager().flush();
		return prh.getId();
	}

	@Override
	public boolean checkOTP(String otp) {
		Query query = entityManager.createNamedQuery("PasswordResetHistory.findByOTP");
		query.setParameter("otp", otp);
		query.setParameter("archived", false);
		@SuppressWarnings("unchecked")
		List<PasswordResetHistory> passwordResetHistorys = query.getResultList();
		return (passwordResetHistorys != null && !passwordResetHistorys.isEmpty());
	}

	@Override
	@Transactional
	public StsCoreResponse resetPassword(String email, String otp, String password) {
		StsCoreResponse status = new StsCoreResponse();
		User existingUser = getUserDetails(email);
		if (existingUser == null) {
			status.setStatusMessage(StsCoreConstant.USER_NOT_FOUND);
		} else if (!existingUser.isVerified()) {
			status.setStatusMessage(StsCoreConstant.ACCOUNT_NOT_ACTIVE);
		} else {
			Query query = entityManager.createNamedQuery("PasswordResetHistory.findByOTP");
			query.setParameter("otp", otp);
			query.setParameter("archived", false);

			@SuppressWarnings("unchecked")
			List<PasswordResetHistory> passwordResetHistorys = query.getResultList();
			if (passwordResetHistorys == null || passwordResetHistorys.isEmpty()) {
				status.setStatusMessage(StsCoreConstant.NO_PASSWORD_RESET_MAIL_FOUND);
			} else {
				PasswordResetHistory passwordResetHistory = passwordResetHistorys.get(0);
				passwordResetHistory.setNewPassword(SecurityEncryptor.encrypt(password));
				passwordResetHistory.setOldPassword(existingUser.getPassword());
				passwordResetHistory.setArchived(true);
				getEntityManager().merge(passwordResetHistory);
				existingUser.setPassword(SecurityEncryptor.encrypt(password));
				getEntityManager().merge(existingUser);

				status.setStatusMessage(StsCoreConstant.PASSWORD_RESET_SUCCESS);
				status.setUserName(email);
			}
		}

		return status;
	}

	@Override
	@Transactional
	public StsCoreResponse setPassword(String email, String otp, String password) {
		StsCoreResponse status = new StsCoreResponse();
		User existingUser = getUserDetails(email);
		if (existingUser == null) {
			status.setStatusMessage(StsCoreConstant.USER_NOT_FOUND);
		} else if (!existingUser.isVerified()) {
			status.setStatusMessage(StsCoreConstant.ACCOUNT_NOT_ACTIVE);
		} else {
			if (!existingUser.getVerificationCode().equals(otp)) {
				status.setStatusMessage(StsCoreConstant.OTP_MISMATCH);
			} else {
				existingUser.setPassword(SecurityEncryptor.encrypt(password));
				getEntityManager().merge(existingUser);
				status.setStatusMessage(StsCoreConstant.PASSWORD_SET_SUCCESS);
				status.setUserName(email);
			}
		}
		return status;
	}

	@Override
	@Transactional
	public StsCoreResponse doSignup(User user) {
		String userNameOrEmail = user.getEmail();
		if (userNameOrEmail == null || userNameOrEmail.isEmpty()) {
			userNameOrEmail = user.getUserName();
		}
		String userFullName = "";
		if (user.getFirstName() != null) {
			userFullName = userFullName + user.getFirstName();
		}
		if (user.getLastName() != null) {
			userFullName = userFullName + "\t" + user.getLastName();
		}

		String vCode = ConversionUtil.getAlphaNumeric(System.currentTimeMillis()).toUpperCase();
		user.setVerificationCode(vCode);
		user.setPassword(SecurityEncryptor.encrypt(user.getPassword().toString()));
		user.setVerified(false);

		StsCoreResponse status = new StsCoreResponse(userNameOrEmail, userFullName, vCode);

		// check user existence
		User existingUser = getUserByUserNameOrEmail(userNameOrEmail);

		File userAvatarImage = null;
		// No user found, save user with profile detail.
		if (existingUser == null) {
			existingUser = user;
			existingUser.setAvtarImage("avatar.png");
			userAvatarImage = new File();
			userAvatarImage.setCreatedName("avatar");
			userAvatarImage.setOriginalName("avatar");
			userAvatarImage.setExtension(".png");
			getEntityManager().persist(userAvatarImage);
			existingUser.setPhoto(userAvatarImage);
			getEntityManager().persist(existingUser);
			getEntityManager().flush();
			status.setSignedUpUserId("" + existingUser.getId());
			status.setStatusMessage(StsCoreConstant.SIGNUP_SUCCESS);
		} else {
			status.setStatusCode(existingUser.getVerificationCode());
			boolean accountAlreadyVerified = existingUser.isVerified();
			if (accountAlreadyVerified) {
				status.setStatusMessage(StsCoreConstant.USER_ALREADY_EXISTS);
			} else {// Manually send an email to user for account activation
				status.setStatusMessage(StsCoreConstant.ACCOUNT_NOT_ACTIVE);
			}
		}
		Address address = new Address();
		address.setUserId(existingUser.getId());
		this.saveAddress(address);
		return status;
	}

	@Override
	@Transactional
	public StsCoreResponse activateAccount(String verificationCode) {
		Query query = entityManager.createNamedQuery("User.findByVerificationCode");
		query.setParameter("verificationCode", verificationCode);
		@SuppressWarnings("unchecked")
		List<User> users = query.getResultList();
		StsCoreResponse status = new StsCoreResponse();
		// There should be only one record based on verification code.
		if (users != null && !users.isEmpty()) {
			if (users.size() > 1) {
				status.setStatusMessage(StsCoreConstant.MULTIPLE_ACCOUNT_FOUND);
			} else {
				User user = users.get(0);
				user.setVerified(true);
				getEntityManager().merge(user);
				getEntityManager().flush();
				status.setStatusMessage(StsCoreConstant.ACCOUNT_ACTIVATION_SUCCESS);
			}
		} else {
			status.setStatusMessage(StsCoreConstant.USER_NOT_FOUND);
		}
		return status;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserDTO> searchUserByFilter(String filterString) {
		if (filterString == null || filterString.trim().length() <= 0)
			return null;
		String queryString = "SELECT u.USERID, u.USERNAME, CONCAT_WS(' ', u.FIRSTNAME, u.MIDDLENAME, u.LASTNAME) As FULLNAME FROM USER u WHERE LOWER(CONCAT_WS('\t', u.USERNAME, u.FIRSTNAME, u.MIDDLENAME, u.LASTNAME)) LIKE '%"
				+ filterString.toLowerCase() + "%'";
		Query query = getEntityManager().createNativeQuery(queryString, "User.Filter");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> listUser(String keyword) {
		if (keyword == null || keyword.trim().length() <= 0)
			return null;
		String query = "SELECT * FROM (SELECT * FROM USER u WHERE LOWER(u.USERNAME) LIKE '" + keyword
				+ "%' UNION SELECT * FROM USER u WHERE LOWER(u.FIRSTNAME) LIKE '" + keyword
				+ "%') AS a ORDER BY a.CREATIONDATE DESC";
		return getEntityManager().createNativeQuery(query, User.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Address getAddress(Long userId) {
		List<Address> address = getEntityManager().createNamedQuery("Address.findByUserId")
				.setParameter("userId", userId).getResultList();
		return address != null && !address.isEmpty() ? address.get(0) : null;
	}

	@Override

	@Transactional
	public Address saveAddress(Address address) {
		if (address.getId() == null) {
			getEntityManager().persist(address);
			getEntityManager().flush();
		} else {
			getEntityManager().merge(address);
		}
		return address;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User getByUserCode(String code) {
		List<User> users = getEntityManager().createNamedQuery("User.findUserCode")
				.setParameter("userCode", code.toLowerCase()).getResultList();
		return users == null || users.isEmpty() ? null : users.get(0);
	}

	@Override
	public String getUserCode(String userNameOrEmail) {
		return (String) getEntityManager().createNamedQuery("User.getCode").setParameter("email", userNameOrEmail)
				.setParameter("userName", userNameOrEmail).getSingleResult();

	}

	@Override
	public UserDTO getUserMinimumInfo(String userNameOrEmail) {
		return setMinimumUserInfo(getUserDetails(userNameOrEmail, userNameOrEmail));
	}

	@Override
	public UserDTO getUserMinimumInfo(Long userId) {
		return setMinimumUserInfo(findUser(userId));
	}

	private UserDTO setMinimumUserInfo(User user) {
		UserDTO dto = null;
		if (user != null) {
			dto = new UserDTO();
			dto.setId(user.getId());
			dto.setUserCode(user.getUserCode());
			StringBuffer name = new StringBuffer(user.getFirstName());
			if (user.getMiddleName() != null) {
				name.append(" ").append(user.getMiddleName());
			}
			if (user.getLastName() != null) {
				name.append(" ").append(user.getLastName());
			}
			dto.setFullName(name.toString());
			if (user.getUserName() != null) {
				dto.setUserName(user.getUserName());
			} else {
				dto.setUserName(user.getEmail());
			}

		}
		return dto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> listPermissions() {
		return getEntityManager().createNamedQuery("Permission.listAll").getResultList();
	}

	@Override
	public List<PermissionGroup> groupPermissionsGroup() {
		List<Object[]> uniquePermissions = getUniquePermissionGroups();
		List<PermissionGroup> groups = null;
		if (uniquePermissions != null && !uniquePermissions.isEmpty()) {
			groups = new ArrayList<PermissionGroup>();
			for (Object[] permission : uniquePermissions) {
				PermissionGroup pg = new PermissionGroup();
				pg.setGroupName(permission[1].toString());
				String keyword = permission[0].toString();
				pg.setPermissions(getAllMatchingPermissions(keyword));
				groups.add(pg);

			}
		}

		return groups;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getUniquePermissionGroups() {
		String query = "SELECT DISTINCT(SUBSTRING_INDEX(AUTHORITY,'_',1)) AS authority, label FROM PERMISSION";
		return getEntityManager().createNativeQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> getAllMatchingPermissions(String keyword) {
		return getEntityManager().createNamedQuery("Permission.getAllMatchingPermissions")
				.setParameter("exp", keyword.toLowerCase() + "\\_%").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Permission getPermission(String permissionName) {
		List<Permission> permissions = getEntityManager().createNamedQuery("Permission.getByName")
				.setParameter("permissionName", permissionName).getResultList();
		return permissions == null || permissions.isEmpty() ? null : permissions.get(0);
	}

	@Override
	public List<Menu> getUserMenu(String userNameOrEmail) {
		return getMenu(getUserByUserNameOrEmail(userNameOrEmail));
	}

	@Override
	public List<Menu> getUserMenu(Long userId) {
		return getMenu(getEntityManager().find(User.class, userId));
	}

	private List<Menu> getMenu(User user) {
		List<Menu> menus = new ArrayList<Menu>();
		List<MenuItem> items = null;
		Menu aMenu = null;
		// Iterate over each permission group
		for (PermissionGroup group : groupPermissionsGroup()) {
			aMenu = new Menu(group.getGroupName(), group.getGroupName());
			// Create a menu per permission group
			if (group.getPermissions() != null && !group.getPermissions().isEmpty()) {
				// Retain all permissions of this role only.
				group.getPermissions().retainAll(user.getPermissions());
				if (group.getPermissions() == null || group.getPermissions().isEmpty()) {
					continue;
				}
				items = new ArrayList<MenuItem>();
				for (Permission permission : group.getPermissions()) {
					if (permission.getUrl() == null || permission.getUrl().isEmpty()) {
						continue;
					}
					items.add(new MenuItem(permission.getDescription(), permission.getUrl(),
							Double.valueOf(permission.getDisplayOrder()), permission.getHierarchy()));
					items.add(new MenuItem("divider", "#", Double.valueOf(permission.getDisplayOrder() + 0.1),
							permission.getHierarchy()));
				}
				if (items != null && !items.isEmpty()) {
					Collections.sort(items, new MenuItemComparator());
					int index = items.size() - 1;
					if (items.get(index).getName().equalsIgnoreCase("divider")) {
						items.remove(index);
					}
					aMenu.setItems(items);
				}

			}
			if (items != null && !items.isEmpty()) {
				menus.add(aMenu);
			}
		}
		return menus;

	}

	/*
	 * private List<MenuItem> getBbtItems(List<MenuItem> items) { items.add(new
	 * MenuItem("Manage Engineer", "engineers")); items.add(new MenuItem("divider",
	 * "#"));
	 * 
	 * items.add(new MenuItem("Manage Contractor", "contractors")); items.add(new
	 * MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Manage Architect", "architects")); items.add(new
	 * MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Manage Bidder", "bidders")); items.add(new
	 * MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Manage Ship To", "ships")); items.add(new
	 * MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Manage Trucker", "truckers")); items.add(new
	 * MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Manage PDNI", "pndis")); items.add(new
	 * MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Manage Quote Notes", "qnotes")); items.add(new
	 * MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Manage Manufacture & Description", "manufactures"));
	 * items.add(new MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Manage Item Shipped", "iships")); items.add(new
	 * MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Manage Shipped Via", "vships")); items.add(new
	 * MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Delete File", "deletefiles")); items.add(new
	 * MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Manage Specification", "specifications"));
	 * items.add(new MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Manage Product", "products")); items.add(new
	 * MenuItem("divider", "#"));
	 * 
	 * items.add(new MenuItem("Manage GCs", "gcs")); return items; }
	 */

	@Override
	public User getUserWithPermissions(Long userId) {
		User user = findUser(userId);
		if (user != null) {
			user.getPermissions();
		}
		if (user.getPermissions() != null && !user.getPermissions().isEmpty()) {
			String[] permissions = new String[user.getPermissions().size()];
			int i = 0;
			for (Permission permission : user.getPermissions()) {
				permissions[i++] = permission.getAuthority();
			}
			user.setPermissionArray(permissions);
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> filterUsers(String name, String userId, String userCode, String email, String role) {
		StringBuffer query = new StringBuffer(
				"SELECT new com.sts.core.entity.User(u.id, u.firstName, u.lastName, u.userName, u.userCode, u.password, u.email, u.role, u.dateCreated) FROM User u WHERE u.archived = 'false'");
		if (!StringUtils.isNullOrEmpty(name)) {
			query.append(" AND u.firstName LIKE %").append(name).append("%").append(" OR u.lastName LIKE %")
					.append(name).append("%");
		}
		if (!StringUtils.isNullOrEmpty(userId)) {
			query.append(" AND u.userName LIKE %").append(userId).append("%");
		}
		if (!StringUtils.isNullOrEmpty(userCode)) {
			query.append(" AND u.userCode LIKE %").append(userCode).append("%");
		}

		if (!StringUtils.isNullOrEmpty(email)) {
			query.append(" AND u.email LIKE %").append(email).append("%");
		}

		if (!StringUtils.isNullOrEmpty(role)) {
			query.append(" AND u.role LIKE %").append(role).append("%");
		}
		System.err.println("filterUsers, query = " + query.toString());
		return getEntityManager().createQuery(query.toString()).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> filterByName(String name) {
		if (name == null || name.isEmpty())
			return null;
		return getEntityManager().createNamedQuery("Engineer.filterByNameMatch")
				.setParameter("keyword", "%" + name.toLowerCase() + "%").getResultList();

	}

	@Override
	public List<User> filterByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StsResponse<User> listArchivedUser(int pageNumber, int pageSize) {
		StsResponse<User> response = listAllArchived(pageNumber, pageSize, User.class.getSimpleName(),
				getEntityManager());
		if (response.getResults() != null && !response.getResults().isEmpty()) {
			for (User user : response.getResults()) {
				setUserPhotoAndName(user);
			}
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserDTO> listUsersByOnlineStatus(String onlineStatus) {
		String query = "User.listOnlineUsers";
		if (onlineStatus != null && onlineStatus.trim().equalsIgnoreCase("offline")) {
			query = "User.listOfflineUsers";
		}
		List<UserDTO> users = getEntityManager().createNamedQuery(query).getResultList();
		enrichUserDTO(users);
		return users;
	}

	@Override
	@Transactional
	public int updateUserOnlineStatus(Long userId, boolean status) {
		User user = findUser(userId);
		user.setOnline(status);
		if (status) {
			user.setOnlineTime(Calendar.getInstance());
			user.setOfflineTime(null);
		} else {
			user.setOfflineTime(Calendar.getInstance());
		}

		getEntityManager().merge(user);
		getEntityManager().flush();
		return status ? 1 : 0;
	}

	private void enrichUserDTO(List<UserDTO> users) {
		if (users == null || users.isEmpty())
			return;
		for (UserDTO user : users) {
			File userImage = getEntityManager().find(File.class, user.getPhotoId());
			String photoUrl = userImage != null
					? AppPropConfig.acBaseUrl + "/" + AppPropConfig.acImageDir + "/" + userImage.getCreatedName() + userImage.getExtension()
					: AppPropConfig.acBaseUrl + "/" + AppPropConfig.acImageDir + "/" +  user.getAvtarImage();
			user.setPhotoUrl(photoUrl);
			/*user.setUnreadMessageCount(chatService.getMyUnreadMessagesCount(user.getId()));*/

		}
	}

	@Override
	public StsResponse<User> listDeletedUser(int pageNumber, int pageSize) {
		StringBuffer selectQuery = new StringBuffer("SELECT entity FROM ").append(User.class.getSimpleName()).append(
				" entity WHERE entity.archived = 'true' AND  entity.deleted = 'true' ORDER BY entity.dateCreated DESC");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(entity.id) FROM ").append(User.class.getSimpleName())
				.append(" entity WHERE entity.archived = 'true' AND entity.deleted = 'true'");
		StsResponse<User> response = listAll(pageNumber, pageSize, selectQuery.toString(), countQuery.toString(),
				User.class.getSimpleName(), "id", getEntityManager());
		if (response.getResults() != null && !response.getResults().isEmpty()) {
			for (User user : response.getResults()) {
				setUserPhotoAndName(user);
			}
		}
		return response;
	}
}
