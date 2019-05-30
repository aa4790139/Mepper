/* PrefixFilenameFilter.java 1.0 2010-2-2
 * 
 * Copyright (c) 2010 by Chen Zhiwu
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.mepper.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * <B>PrefixFilenameFilter</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-9-29 created
 * @since org.mepper.utils Ver 1.0
 * 
 */
public class PrefixFilenameFilter implements FilenameFilter{
	private final String prefix;
	public PrefixFilenameFilter(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public boolean accept(File dir, String name) {
		return name.startsWith(prefix);
	}

}
