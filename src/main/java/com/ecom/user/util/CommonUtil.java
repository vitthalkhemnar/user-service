package com.ecom.user.util;

import java.util.Collection;

public class CommonUtil {

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}
}
