package com.idiotBox.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordHasher {
	
	public String getHashedString(String simpleString) {
		String hashedString = BCrypt.hashpw(simpleString, BCrypt.gensalt());
		return hashedString;
	}
	
	public boolean matchHashedString(String simpleString, String encodedString) {
		return BCrypt.checkpw(simpleString, encodedString);
	}
}
