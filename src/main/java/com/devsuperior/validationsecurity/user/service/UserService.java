package com.devsuperior.validationsecurity.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.validationsecurity.user.entity.User;
import com.devsuperior.validationsecurity.user.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private static final String USERNAME_NOT_FOUND_MESSAGE = "User not found.";

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository
				.findWithRolesByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND_MESSAGE));

		String[] authorities = user
				.getRoles()
				.stream()
				.map(role -> role.getAuthority())
				.toArray(size -> new String[size]);

		return org.springframework.security.core.userdetails.User
				.withUsername(user.getEmail())
				.password(user.getPassword())
				.authorities(authorities)
				.build();
	}

}
