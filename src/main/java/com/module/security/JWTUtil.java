package com.module.security;

public class JWTUtil {

//  43200 min = 1 mois (min*seconde*milliseconde = ..min*60 seconde*1000 milliseconde)
	public static final String SECRET="secret2401";
	public static final String AUTH_HEADER="Authorization";
	public static final long EXPIRE_ACCESS_TOKEN= 2592000000L;// 1mois d'expiration 
	public static final long EXPIRE_REFRESH_TOKEN = 93312000000L; // 3ans d'expiration
	public static final String PREFIX="Bearer ";
	
}
