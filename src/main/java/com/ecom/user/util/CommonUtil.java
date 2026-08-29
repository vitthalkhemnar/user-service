package com.ecom.user.util;

import java.util.Collection;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
	
	public static String getCurrentUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}
}
