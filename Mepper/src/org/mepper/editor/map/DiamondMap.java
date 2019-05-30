/* IsometricMap.java 1.0 2010-2-2
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

import org.mepper.editor.coordinate.DiamondTranslator;


/**
 * <B>IsometricMap</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-10 created
 * @since org.mepper.editor.map Ver 1.0
 * 
 */
public class DiamondMap extends AbstractMap{
 
	
	public DiamondMap() {
		setCoordinateTranslator(new DiamondTranslator());
	}
	
	@Override
	public void setExtension(int rows, int columns) {
		super.setExtension(rows, columns);
		size.width = (columns+rows) * tileWidth/2;
		size.height = (columns+rows) * tileHeight/2;
		
		//update offset
		offset.offsetX =  rows * tileWidth/ 2;
	}
	
	@Override
	public void drawGrid(int x, int y, Graphics2D g) {
		int tileWidth = this.tileWidth/2;
		int tileHeight = this.tileHeight/2;

		int x1,y1,x2,y2;
		x1=row*tileWidth + x;
		y1=0 + y;
		x2=0 + x;
		y2=row*tileHeight + y;
		
		for(int i=0;i<column+1;i++){
			g.drawLine(x1+ i*tileWidth,
					y1+i*tileHeight,
					x2+ i*tileWidth,
					y2+i*tileHeight);
		}
		
		x2=(row+column)*tileWidth + x;
		y2=column*tileHeight + y;
		for(int i=0;i<row+1;i++){
			g.drawLine(x1- i*tileWidth,
					y1+i*tileHeight,
					x2- i*tileWidth,
					y2+i*tileHeight);
		}
	} 
	
	@Override
	public void drawCoordinate(int x, int y, Graphics2D g) {
		int tileWidth = this.tileWidth/2;
		int tileHeight = this.tileHeight/2;

		int x1,y1;
		x1=row*tileWidth + x -tileWidth/2;
		y1=0 + y+ tileHeight;
		
		
//		Font font =g.getFont().deriveFont(0);//XXX get font from preference file.
		
		for (int i = 0; i < row ; i++) {
			for (int j = 0; j < column ; j++) {
				g.drawString(i+","+j, x1 + tileWidth * j, y1 + tileHeight * j);
			}
			x1 -= tileWidth;
			y1 += tileHeight;
		}
	}
	
	
	

	/**
	 * 绘制菱形. 
	 * 按标准算法，每个菱形会右移 TileWidth/2 这里已作修改? it seems that the guy forgot to modify
	 * it.
	 * 
	 * ---------------------a example of startPoint:-(remove the blank lines to
	 * see the graphics)--------------------- in the fellow example, we call A
	 * as start point.
	 * 
	 * (x,y) .A startPoint
	 * 
	 *       /\
	 *      /  \ .B
	 *   D. \  /
	 *       \/ C
	 * 
	 * ---------------------a example of extension:---------------------- --> j
	 * | | i V
	 * 
	 * A' startPoint
	 * 
	 * /\
	 * 
	 * / \
	 * 
	 * /\ /\
	 * 
	 * / \/ \
	 * 
	 * D' \ /\ /\
	 * 
	 * \/ \/ \
	 * 
	 * \ /\ /B'
	 * 
	 * \/ \/
	 * 
	 * \ /
	 * 
	 * \/C'
	 * 
	 * B'=( x+bw, y+bh ) C'=( x+(b-a)w,y+(a+b)*w ) D'=( x-aw, y-a*h )
	 * 
	 * 
	 * 
	 * in this case, the extension of the start point is (2,3).
	 * ---------------------end of example----------------------
	 * 
	 * startPoint(x,y) , extension(a,b)
	 * 
	 * for(i=0;i<a;i++){ for(j=0;j<b;j++){
	 * 
	 * } }
	 * 
	 * @param startPoint
	 *            the start point
	 */
	@Override
	public Shape getBounds(Point startPoint, Dimension extension,boolean adjust) {
		GeneralPath bounds=new GeneralPath();
		int stepX= offset.stepX/2;
		int stepY=offset.stepY/2;
		
		Point p = mapToScreen(startPoint);
		int x, y ; 
		x = p.x;
		y = p.y;
		
		if (adjust) {
			// 定制描绘起点坐标
			if (extension.width < 0 && extension.height < 0) {
				x = p.x;
				y = p.y + stepY * 2;
			} else if (extension.width >= 0 && extension.height < 0) {
				x = p.x + stepX;
				y = p.y + stepY;
			} else if (extension.width < 0 && extension.height >= 0) {
				x = p.x - stepX;
				y = p.y + stepY;
			}//				else { // >=0
		}
			
		bounds.moveTo(x, y); // Point A
		bounds.lineTo(x + extension.height * stepX, 
						y + extension.height	* stepY);// point B
		bounds.lineTo(x + (extension.height - extension.width) * stepX, 
						y + (extension.width + extension.height) * stepY);// point C
		bounds.lineTo(x - extension.width * stepX,
						y + extension.width * stepY);// point D
		bounds.lineTo(x, y);
		
		return bounds;
	}

	

//	@Override
//	public Dimension getExtension(Point startPoint, Point finishPoint) {
//		int x = finishPoint.x - startPoint.x;
//		int y = finishPoint.y - startPoint.y;
//		// sets extension
//		x += x>=0?1:-1;// the coordianate system start from zero
//		y += y>=0? 1:-1;// the coordianate system start from zero
//		return new Dimension(x, y);
//	}


	

}
