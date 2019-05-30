/* Tool.java 1.0 2010-2-2
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

import java.awt.Cursor;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import org.mepper.editor.Editor;

/**
 * <B>Tool</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-8 created
 * @since org.mepper.tool Ver 1.0
 * 
 */
public interface Tool extends MouseListener,MouseMotionListener,KeyListener {

	void deactivate(Editor editor);
	void activate(Editor editor);
	
	void addToolListener(ToolListener l);
	void removeToolListener(ToolListener l);
	
	Cursor getCursor();
	JComponent getOptionComponent();
//	String getMessage();
	
	void setEditor(Editor editor);
	String getID();
	char getAccelerator();
	

}
