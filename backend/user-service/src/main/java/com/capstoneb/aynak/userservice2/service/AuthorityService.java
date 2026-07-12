package com.capstoneb.aynak.userservice2.service;

import com.capstoneb.aynak.userservice2.model.Authority;

public interface AuthorityService {

	Authority findOrCreateAuthority(String authorityName);
}
