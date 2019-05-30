/* CoordinateTranslator.java 1.0 2010-2-2
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
package org.mepper.editor.coordinate;

import java.awt.Point;

import org.mepper.editor.map.MapOffset;

/**
 * <B>CoordinateTranslator</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-9 created
 * @since org.mepper.editor.coordinate Ver 1.0
 * 
 */
public interface CoordinateTranslator {
//	Point screenToMap(Point screen);
//	Point mapToScreen(Point map);
	Point screenToMap(Point screen,MapOffset offset);
	Point mapToScreen(Point map,MapOffset offset);
}
