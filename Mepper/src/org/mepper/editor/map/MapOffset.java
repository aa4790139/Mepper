/* MapOffset.java 1.0 2010-2-2
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

/**
 * A <B>MapOffset</B>
 * representing the width and height{@code (stepX,stepY)} of a tile ,
 * and the width and height {@code (offsetX,offsetY)} of the map .
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2010-12-21 created
 * @since org.map.editor Ver 1.0
 * 
 */
public class MapOffset {
	/** 地图中每个Tile的宽.
	 *  the width of the a Tile.
	 */
	public int stepX;
	/** 地图中每个Tile的高.
	 *  the height of the Tile.
	 */
	public int stepY;
	
	/** 地图坐标(0,0)对应的实际坐标.
	 *  The offset of coordinates correspond to the logical map coordinates (0,0).
	 */
	public int offsetX;
	public int offsetY;
	
	public MapOffset(int stepX, int stepY, int offsetX, int offsetY) {
		this.stepX=stepX;
		this.stepY=stepY;
		this.offsetX=offsetX;
		this.offsetY=offsetY;
	}

	public MapOffset() {
		this(0,0,0,0);
	}

	public int stepX() {
		return stepX;
	}

	public int stepY() {
		return stepY;
	}

	public int offsetX() {
		return offsetX;
	}

	public int offsetY() {
		return offsetY;
	}

	
	public void setStep(Dimension d){
		stepX=d.width;
		stepY=d.height;
	}
	
	@Override
	public String toString() {
		return "["+stepX+","+stepY+","+offsetX+","+offsetY+"]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj ==null){
			return false;
		}
		if(! (obj instanceof MapOffset)){
			return false;
		}
		MapOffset other=(MapOffset)obj;
		return other.offsetX == offsetX &&
			other.offsetY == offsetY &&
			other.stepX == stepX &&
			other.stepY == stepY;
	}
	
	 /**
     * Translates the offsetX and offsetY of this MapOffset, at location {@code (x,y)}, 
     * by {@code dx} along the {@code x} axis and {@code dy} 
     * along the {@code y} axis so that it now represents the point 
     * {@code (x+dx,y+dy)}.
     *
     * @param       dx   the distance to move this point 
     *                            along the X axis
     * @param       dy    the distance to move this point 
     *                            along the Y axis
     */
	public void translate(int dx, int dy) {
		offsetX += dx;
		offsetY += dy;
	}

	public MapOffset create() {
		return new MapOffset(stepX,stepY,offsetX, offsetY);
	}
}
