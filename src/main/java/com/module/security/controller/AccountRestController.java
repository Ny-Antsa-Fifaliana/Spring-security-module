package com.module.security.controller;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.module.security.service.AccountService;
import com.module.security.JWTUtil;
import com.module.security.bean.AppRole;
import com.module.security.bean.AppUser;
import com.module.security.bean.RoleUserBean;
import com.module.security.exception.EmptyException;
import com.module.security.exception.ResponseMessage;


@CrossOrigin(origins="http://localhost:4200")
@RestController
public class AccountRestController {
 
	@Autowired
	private AccountService accountService;
	
	
	// Lister User -----------------------
	@GetMapping("/users")
	public List<AppUser> appUsers(){
		return accountService.listUsers();
	}
	
	
	
	// find User -----------------------
	@GetMapping("/user")
	public Optional<AppUser> getUserById(@RequestParam long id){
		return accountService.getUserById(id);
	}
	
	
	
	// Add User -----------------------
	@PostMapping("/addusers")
	public ResponseEntity<?> saveUser(@RequestBody AppUser appUser) {
		try {
			AppUser newAppUser=accountService.addNewUser(appUser);
			return new ResponseEntity<>(new ResponseMessage(true,appUser.getUserName()+" est enregistré avec succès!",newAppUser),HttpStatus.OK);
		} catch (EmptyException e) {
			return new ResponseEntity<>(new ResponseMessage(false,e.getMessage()),HttpStatus.OK);
		}
	}
	
	
	
	// Modifier User -----------------------
	@PutMapping("/users")
	public ResponseEntity<?> updateUser(@RequestBody AppUser appUser) {
		try {
			AppUser newAppUser=accountService.updateUser(appUser);
			return new ResponseEntity<>(new ResponseMessage(true,"Modifié avec succès!",newAppUser),HttpStatus.OK);
		} catch (EmptyException e) {
			return new ResponseEntity<>(new ResponseMessage(false,e.getMessage()),HttpStatus.OK);
		}
	}
	
	
	// update MdpUser -----------------------
	@PutMapping("/mdpusers")
	public ResponseEntity<?> updateMdpUser(@RequestBody AppUser appUser) {
		try {
			AppUser newAppUser=accountService.updateMdpUser(appUser);
			return new ResponseEntity<>(new ResponseMessage(true,"Modifié avec succès!",newAppUser),HttpStatus.OK);
		} catch (EmptyException e) {
			return new ResponseEntity<>(new ResponseMessage(false,e.getMessage()),HttpStatus.OK);
		}
	}
	
	
	// delete User --------------------------
	@DeleteMapping("/users")
	public void deleteUser(@RequestParam long id) {
		accountService.deleteUser(id);
	}
	
	
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	
	
	// Lister Role -----------------------
	@GetMapping("/roles")
	public List<AppRole> appRoles(){
		return accountService.listRoles();
	}
	
	
	// find Role -------------------------
	@GetMapping("/role")
	public Optional<AppRole> getRoleById(@RequestParam long id){
		return accountService.getRoleById(id);
	}
	
	
	// add Role -----------------------
	@PostMapping("/roles")
	public ResponseEntity<?> saveRole(@RequestBody AppRole appRole) {
		try {
			AppRole newAppRole=accountService.addNewRole(appRole);
			return new ResponseEntity<>(new ResponseMessage(true,"Le rôle "+appRole.getRoleName()+" est enregistré avec succès!",newAppRole),HttpStatus.OK);
		} catch (EmptyException e) {
			return new ResponseEntity<>(new ResponseMessage(false,e.getMessage()),HttpStatus.OK);
		}
	}
	
	
	// update Role -----------------------
	@PutMapping("/roles")
	public ResponseEntity<?> updateRole(@RequestBody AppRole appRole) {
		try {
			AppRole newAppRole=accountService.updateRole(appRole);
			return new ResponseEntity<>(new ResponseMessage(true,"Modifié avec succès!",newAppRole),HttpStatus.OK);
		} catch ( EmptyException e) {
			return new ResponseEntity<>(new ResponseMessage(false,e.getMessage()),HttpStatus.OK);
		}
	}
	
	
	
	// delete Role -----------------------
	@DeleteMapping("/roles")
	public void deleteRole(@RequestParam long id) {
		accountService.deleteRole(id);
	}
	
	
	// Liste des roles qui ne sont pas dans AppUserRole ---------------------
	@GetMapping("/rolesByAppUserRole")
	public Iterable<AppRole> listeRoleNotInAppUserRole(@RequestParam int id){
		return accountService.listeRoleNotInAppUserRole(id);
	}
	
	
	
	
	//----------------------------------------------------------------------------------------------------------------------------
	
	
	
	// add Role to User -------------------------------
	@PostMapping("/addRoleToUser")
	public void addRoleToUser(@RequestBody RoleUserBean roleUser) {
		accountService.addRoleToUser(roleUser.getUserName(), roleUser.getRoleName());
	}
	
	
	// remove Role to User -------------------------------
	@PostMapping("/removeRoleToUser")
	public void removeRoleToUser(@RequestBody RoleUserBean roleUser) {
		accountService.removeRoleToUser(roleUser.getUserName(), roleUser.getRoleName());
	}
	
	
	// get profile ----------------------------------------
	@GetMapping("/profile")
	public AppUser profile(Principal principal) {
		return accountService.loadUserByUserName(principal.getName()); 
	}
	
	
	// Request refreshToken -------------------------------
	@PostMapping("/refreshToken")
	public void refreshToken(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String authToken= request.getHeader(JWTUtil.AUTH_HEADER);
	//	System.out.println("appelé soa amantsara");
	//	System.out.println("refressh token fonctionnnnnnnnn");
	//	System.out.println(authToken);
		if (authToken!=null && authToken.startsWith(JWTUtil.PREFIX)) {
			try {
				String jwt= authToken.substring(JWTUtil.PREFIX.length());
				Algorithm algorithm= Algorithm.HMAC256(JWTUtil.SECRET);
				JWTVerifier jwtVerifier= JWT.require(algorithm).build();
				DecodedJWT decodedJWT= jwtVerifier.verify(jwt);
				String userName= decodedJWT.getSubject();
				AppUser appUser= accountService.loadUserByUserName(userName);
				String JwtAccessToken= JWT.create()
						.withSubject(appUser.getUserName())
						.withExpiresAt(new Date(System.currentTimeMillis()+JWTUtil.EXPIRE_ACCESS_TOKEN))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", appUser.getAppRoles().stream().map(r->r.getRoleName()).collect(Collectors.toList()))
						.sign(algorithm);
				
				String JwtRefreshToken= JWT.create()
						.withSubject(appUser.getUserName())
						.withExpiresAt(new Date(System.currentTimeMillis()+JWTUtil.EXPIRE_REFRESH_TOKEN))
						.withIssuer(request.getRequestURL().toString())
						.sign(algorithm);
				
				Map<String, String> idToken=new HashMap<>();
				idToken.put("access_token",JwtAccessToken);
				idToken.put("refresh_token", JwtRefreshToken);
				response.setContentType("application/json");

				new ObjectMapper().writeValue(response.getOutputStream(), idToken);
				
				System.out.println("terminer refressh ");
				
			} catch (Exception e) {
				throw e;
			}	
		}
		else {
			throw new RuntimeException("RefreshToken required !!!");	
		}
	}
	
	
	
	
	
	
} 

