/* IsometricTranslator.java 1.0 2010-2-2
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
 * <B>IsometricTranslator</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-11 created
 * @since org.mepper.editor.coordinate Ver 1.0
 * 
 */
public class DiamondTranslator implements CoordinateTranslator{

	@Override
	public Point screenToMap(Point screen, MapOffset offset) {
		Point p=new Point();
		if(offset.stepX!=0 && offset.stepY!=0){
			double x= offset.stepX/2;
			double y = offset.stepY/2;
			double tmpX = (screen.x - offset.offsetX) / (x);
			double tmpY = (screen.y - offset.offsetY) / (y);

			p.x = (int) ((tmpY - tmpX) / 2);
			p.y = (int) ((tmpY + tmpX) / 2);
		}
		
		return p;
	}

	@Override
	public Point mapToScreen(Point map, MapOffset offset) {
		return new Point((map.y - map.x) * offset.stepX/2,(map.y + map.x) * offset.stepY/2);
	}


}
