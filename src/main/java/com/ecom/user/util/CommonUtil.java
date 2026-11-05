package com.ecom.user.util;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ecom.user.constants.AppConstants;

@Component
public class CommonUtil {
	
	public static String getCurrentUsername() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (auth != null) ? auth.getName() : null;
	}

	public static boolean isAdmin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getAuthorities() == null) {
			return false;
		}
		return auth.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
						|| a.getAuthority().equals(AppConstants.ROLE_ADMIN)
						|| a.getAuthority().equals("ADMIN"));
	}

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}
}
