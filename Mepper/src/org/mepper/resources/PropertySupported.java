/* PropertySupported.java 1.0 2010-2-2
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
package org.mepper.resources;

import java.io.Serializable;
import java.util.List;

/**
 * <B>PropertySupported</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-28 created
 * @since org.mepper.resources Ver 1.0
 * 
 */
public interface PropertySupported extends Serializable{
	String getProperty(String key);
	void putProperty(String key,String value);
	void removeProperty(String key);
	List<String>  propertyNames();
}
