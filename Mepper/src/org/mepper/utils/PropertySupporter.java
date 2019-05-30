/* PropertySupporter.java 1.0 2010-2-2
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mepper.resources.PropertySupported;

/**
 * <B>PropertySupporter</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-28 created
 * @since org.mepper.utils Ver 1.0
 * 
 */
public class PropertySupporter  implements PropertySupported{
	protected final HashMap<String, String> properties;
	
	public PropertySupporter() {
		properties = new HashMap<String, String>(8);
	}
	
	@Override
	public List<String>  propertyNames() {
		Set<String> set =properties.keySet();
		List<String> names =new ArrayList<String>(set.size());
		for(Iterator<String> it=set.iterator();it.hasNext();){
			String name = it.next();
			if(name != null){
				names.add(name);
			}
		}
		return names;
	}
	
	@Override
	public void putProperty(String key, String value) {
		if(key ==null){
			return;
//			throw new NullPointerException();
		}
		properties.put(key, value);
	}

	@Override
	public void removeProperty(String key) {
		properties.remove(key);
	}
	@Override
	public String getProperty(String key) {
		return properties.get(key);
	}
	
	
	
	
	
	
	
}
