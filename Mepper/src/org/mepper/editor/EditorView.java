/* EditorView.java 1.0 2010-2-2
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

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.undo.UndoManager;

import org.mepper.editor.map.Map;

/**
 * <B>EditorView</B> represent the view of the <I>map editor (not Editor)</I>. 
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-8 created
 * @since org.mepper.editor Ver 1.0
 * 
 */
public interface EditorView extends DoubleBufferable{
	String IMAGE_CHANGED_LISTENER = "image.changed";
	String UNDO_HAPENED="undo.happened";
	
	@Override
	JComponent getComponent();
	Editor getEditor();
	Map getMap();
	void setMap(Map map);
	
	Selection getSelection();
	Rectangle getMapBounds();
	Collection<? extends Action> getActions();
	
	void activate(Editor editor);
	
	Point screenToMap(Point mousePoint);
	Point mapToScreen(Point mapPoint);

	UndoManager getUndoManager();
	
	void setGrid(boolean b);
	boolean isGrid();
	boolean isCoordinate();
	void setCoordinate(boolean b);
	
	
}
