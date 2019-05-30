/* ImageObject.java 1.0 2010-2-2
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

import java.awt.image.BufferedImage;

/**
 * <B>ImageObject</B>
 * difine a object witch can invoke <code>getImage()</code> and <code>getName</code> .
 * This interface is user for UI.
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2010-12-26 created
 * @since org.map.editor.gui Ver 1.0
 * 
 */
public interface ImageObject {
	String getName();
	void setName(String name);
	
	BufferedImage getImage(); 
	void setImage(BufferedImage image);
}
