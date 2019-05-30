/* OrthogonalMap.java 1.0 2010-2-2
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
package org.mepper.editor.map;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import org.mepper.editor.coordinate.RectangleTranslator;

/**
 * <B>OrthogonalMap</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-5-30 created
 * @since org.mepper.editor.map Ver 1.0
 * 
 */
public class OrthogonalMap extends AbstractMap {
	
	public OrthogonalMap() {
		setCoordinateTranslator(new RectangleTranslator());
	}
	
	@Override
	public void setExtension(int rows, int columns) {
		super.setExtension(rows, columns);
		size.width = columns*tileWidth;
		size.height = rows*tileHeight;
	}

	@Override
	public void drawGrid(int x, int y, Graphics2D g) {
		for(int i=0;i<column;i++){
			g.drawLine(0, tileHeight*i, size.width, tileHeight*i);
		}
		
		for(int i=0;i<row;i++){
			g.drawLine(i*tileWidth, 0, i*tileWidth, size.height);
		}
	
	}
	
	@Override
	public void drawCoordinate(int x, int y, Graphics2D g) {

		x += tileWidth/4;
		y += tileHeight/2 ;
		for(int i=0;i<row;i++){
			for (int j = 0; j < column; j++) {
				g.drawString(i+","+j, x , y + tileHeight*j );
			}
			x += this.tileWidth;
		}
	}

	

	@Override
	public Shape getBounds(Point startPoint, Dimension extension, boolean adjust) {
		GeneralPath bounds = new GeneralPath();
		Point p = mapToScreen(startPoint);
		bounds.moveTo(p.x, p.y);
		bounds.lineTo(p.x+extension.width*offset.stepX, p.y);
		bounds.lineTo(p.x+extension.width*offset.stepX, p.y+extension.height*offset.stepY);
		bounds.lineTo(p.x, p.y+extension.height*offset.stepY);
		bounds.moveTo(p.x, p.y);
		return bounds;
	}

}
