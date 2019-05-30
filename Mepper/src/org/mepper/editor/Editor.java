/* Editor.java 1.0 2010-2-2
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

import java.util.List;

import org.mepper.tool.Tool;


/**
 * <B>Editor</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-8 created
 * @since org.mepper.editor Ver 1.0
 * 
 */
public interface Editor {

//	String TOOL_CHANGED_PROPERTY = "tool.changed";
//	String VIEW_CHANGED_PROPERTY = "view.changed";
	
	void add(EditorView v);
	void remove(EditorView v);
	
	EditorView getActivateView();
	void setActivateView(EditorView v);
	
	List<EditorView> getEditorViews();
//	void addEditorListener(EditorListener l);
//	void removeEditorListener(EditorListener l);

	void setTool(Tool tool);
	Tool getTool();
	
	boolean isEnable();
	void setEnable(boolean b);
	
//	void addTools(Collection<Tool> tools);//FIXME remove this method
//	List<Tool> getAllTools();//FIXME remove this method

	void setUserObject(Object object);
	Object getuserobject();
	
//	void setPaintingTile(Tile t);
//	Tile getPaintingTile();
	
	void addEditorListener(EditorListener l);
	void removeEditorhangeListener(EditorListener l);

	boolean canUndo();
	boolean canRedo();
	void undo();
	void redo();
}
