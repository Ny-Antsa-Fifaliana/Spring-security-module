package com.module.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.module.security.bean.AppUser;


@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	
	public AppUser findByUserName(String userName);

}
