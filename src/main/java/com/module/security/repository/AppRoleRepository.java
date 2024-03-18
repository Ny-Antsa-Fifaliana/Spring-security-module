package com.module.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.module.security.bean.AppRole;


@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
	public AppRole findByRoleName(String roleName);
	
	@Query(value = " select * from app_role where id not in (select app_user_app_roles.app_roles_id from app_user_app_roles where (app_user_app_roles.app_user_id=:id)); ", nativeQuery = true)
	public Iterable<AppRole> listeRoleNotInAppUserRole( @Param("id") int id);
	
	public AppRole findByroleName(String name);

}
