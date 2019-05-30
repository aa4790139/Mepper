/* ToolEvent.java 1.0 2010-2-2
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
package org.mepper.tool;

import java.awt.Point;
import java.util.EventObject;

/**
 * <B>ToolEvent</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-9 created
 * @since org.mepper.tool Ver 1.0
 * 
 */
public class ToolEvent extends EventObject{
	private static final Point DEFAULT_POINT=new Point();
	private Point screenPoint = DEFAULT_POINT;
	private Point mapPoint = DEFAULT_POINT;

	public ToolEvent(Object source) {
		super(source);
	}

	public ToolEvent(Object source, Point screenPoint, Point mapPoint) {
		this(source);
		this.screenPoint = screenPoint;
		this.mapPoint = mapPoint;
	}

	public Point getScreenPoint() {
		return screenPoint;
	}

	public Point getMapPoint() {
		return mapPoint;
	}

}
