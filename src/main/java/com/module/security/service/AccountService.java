package com.module.security.service;

import java.util.List;
import java.util.Optional;

import com.module.security.bean.AppRole;
import com.module.security.bean.AppUser;
import com.module.security.exception.EmptyException;



public interface AccountService {

	AppUser addNewUser(AppUser appUser) throws EmptyException;
	AppRole addNewRole(AppRole appRole) throws EmptyException;
	void addRoleToUser(String userName, String roleName);
	void removeRoleToUser(String userName, String roleName);
	AppUser loadUserByUserName(String userName);
	List<AppUser> listUsers();
	List<AppRole> listRoles();
	AppUser updateUser(AppUser appUser) throws EmptyException;
	AppUser updateMdpUser(AppUser appUser) throws EmptyException;
	void deleteUser(long id);
	AppRole updateRole(AppRole appRole) throws EmptyException;
	void deleteRole(long id);
	Optional<AppUser> getUserById(long id);
	Optional<AppRole> getRoleById(long id);
	Iterable<AppRole> listeRoleNotInAppUserRole(int id);
}
 