/* Selection.java 1.0 2010-2-2
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
package org.mepper.editor;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeListener;

/**
 * <B>Selection</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-9 created
 * @since org.mepper.editor Ver 1.0
 * 
 */
public interface Selection {
	void add(Selection s);
	void add(Shape shape);
	
	void subtract(Selection other);
	void subtract(Shape shape);
	
	Dimension getExtension();
	void setExtension(Dimension ext);
	
	Shape getValidSelection();
	Shape getExactSelection();
	
	
	boolean contains(Rectangle bounds);
	boolean contains(int x,int y);
	
	Shape getClip();
	void setClip(Shape shape);
	
	boolean isEmpty();
	void reset();
	
//	void translate(int x, int y);
	AffineTransform getTransform();
	
	void addPropertyChangeListener(PropertyChangeListener l);
	void removePropertyChangeListener(PropertyChangeListener l);
	
	
	
}
