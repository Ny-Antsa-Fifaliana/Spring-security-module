package com.module.security.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.module.security.bean.AppRole;
import com.module.security.bean.AppUser;
import com.module.security.exception.EmptyException;
import com.module.security.repository.AppRoleRepository;
import com.module.security.repository.AppUserRepository;


@Service
@Transactional
public class AccountServiceImpl implements AccountService{

	@Autowired
	private AppUserRepository appUserRepository;
	
	@Autowired
	private AppRoleRepository appRoleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private PasswordEncoder passwordEncoderMdp;
	
	private Optional<AppUser> userActuel;
	private Optional<AppUser> userActuelMdp;
	private Optional<AppRole> roleActuel;
	
	
	@Override
	public AppUser addNewUser(AppUser appUser) throws EmptyException {
		
		if(appUser.getUserName().isBlank() || appUser.getPassword().isBlank()) {
			throw new EmptyException("Verifier les champs !!");
		}
		else {
			AppUser duplicated= appUserRepository.findByUserName(appUser.getUserName());
			if(duplicated==null) {
				String pw= appUser.getPassword();
				appUser.setPassword(passwordEncoder.encode(pw));
				return appUserRepository.save(appUser);
			}
			else {
				throw new EmptyException("Existe déjà !!");
			}
		}
	}

	@Override
	public AppRole addNewRole(AppRole appRole) throws EmptyException {
		
		if(appRole.getRoleName().isBlank()) {
			throw new EmptyException("Verifier les champs !!");
		}
		
		else {
			AppRole duplicated= appRoleRepository.findByroleName(appRole.getRoleName());
			if(duplicated==null) {
				return appRoleRepository.save(appRole);
			}
			else {
				throw new EmptyException("Existe déjà !!");
			}
		}
	}

	@Override
	public void addRoleToUser(String userName, String roleName) {
		AppUser appUser= appUserRepository.findByUserName(userName);
		AppRole appRole= appRoleRepository.findByRoleName(roleName);
		
		appUser.getAppRoles().add(appRole);	
	}
	
	@Override
	public void removeRoleToUser(String userName, String roleName) {
		AppUser appUser= appUserRepository.findByUserName(userName);
		AppRole appRole= appRoleRepository.findByRoleName(roleName);
		
		appUser.getAppRoles().remove(appRole);	
	}

	@Override
	public AppUser loadUserByUserName(String userName) {
		return appUserRepository.findByUserName(userName);
	}

	@Override
	public List<AppUser> listUsers() {
		return appUserRepository.findAll();
	}
	
	@Override
	public List<AppRole> listRoles() {
		return appRoleRepository.findAll();
	}

	@Override
	public AppUser updateUser(AppUser appUser) throws EmptyException {
		if(appUser.getUserName().isBlank()) {
			throw new EmptyException("Verifier les champs !!");
		}
		else {
			AppUser duplicated= appUserRepository.findByUserName(appUser.getUserName());
			if(duplicated==null) {
				this.userActuel=appUserRepository.findById(appUser.getId());
				this.userActuel.get().setUserName(appUser.getUserName());;
				return appUserRepository.save(this.userActuel.get());
			}
			else {
				throw new EmptyException("Existe déjà, utiliser un autre nom !!");
			}
		}
		
	}
	
	@Override
	public AppUser updateMdpUser(AppUser appUser) throws EmptyException {
		if(appUser.getPassword().isBlank()) {
			throw new EmptyException("Verifier les champs !!");
		}
		else {
			this.userActuelMdp=appUserRepository.findById(appUser.getId());
			this.userActuelMdp.get().setPassword(passwordEncoderMdp.encode(appUser.getPassword()));;
			return appUserRepository.save(this.userActuelMdp.get());	
		}
	}

	@Override
	public void deleteUser(long id) {
		appUserRepository.deleteById(id);
		
	}

	@Override
	public AppRole updateRole(AppRole appRole) throws EmptyException {
		
		if(appRole.getRoleName().isBlank()) {
			throw new EmptyException("Verifier les champs !!");
		}
		else {
			AppRole duplicated= appRoleRepository.findByRoleName(appRole.getRoleName());
			if(duplicated==null) {
				this.roleActuel=appRoleRepository.findById(appRole.getId());
				this.roleActuel.get().setRoleName(appRole.getRoleName());;
				return appRoleRepository.save(this.roleActuel.get());
			}
			else {
				throw new EmptyException("Existe déjà, utiliser un autre nom !!");
			}
		}
	}

	@Override
	public void deleteRole(long id) {
		appRoleRepository.deleteById(id);
		
	}

	@Override
	public Optional<AppUser> getUserById(long id) {
		return appUserRepository.findById(id);	
	}
	@Override
	public Optional<AppRole> getRoleById(long id) {
		return appRoleRepository.findById(id);	
	}

	@Override
	public Iterable<AppRole> listeRoleNotInAppUserRole(int id) {
		return appRoleRepository.listeRoleNotInAppUserRole(id);
	}

}
