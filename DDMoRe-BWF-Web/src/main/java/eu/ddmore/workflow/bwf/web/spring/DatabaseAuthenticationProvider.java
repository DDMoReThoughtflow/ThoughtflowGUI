package eu.ddmore.workflow.bwf.web.spring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import eu.ddmore.workflow.bwf.client.model.Authority;
import eu.ddmore.workflow.bwf.client.model.User;
import eu.ddmore.workflow.bwf.web.service.impl.UserServiceImpl;

public class DatabaseAuthenticationProvider implements AuthenticationProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseAuthenticationProvider.class);
	
	private static final String ROLE_PREFIX = "ROLE_";
	
	private UserServiceImpl userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String plainPassword = authentication.getCredentials().toString();
		String encodedPassword = new Md5PasswordEncoder().encodePassword(plainPassword, username);
		
		/** Try to do login */
		User user = null;
		try {
			user = (User) getUserService().login(username, encodedPassword);
		} catch (Exception ex) {
			LOG.error("Error do login for '" + username + "'.", ex);
		}
		
		if (user != null) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			if (user.getAuthorities() != null && user.getAuthorities().hasAuthorities()) {
				for (Authority authority : user.getAuthorities().getAuthorities()) {
					authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + authority.getRole().toString()));
				}
			}
			UserInfo userInfo = new UserInfo(user);
			return new UsernamePasswordAuthenticationToken(userInfo, user.getPassword(), authorities);
		}		
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	public UserServiceImpl getUserService() {
		return this.userService;
	}

	public void setUserService(UserServiceImpl userService) {
		this.userService = userService;
	}
}
