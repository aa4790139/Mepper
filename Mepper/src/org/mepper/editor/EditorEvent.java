/* EditorEvent.java 1.0 2010-2-2
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

import java.util.EventObject;

import org.mepper.tool.Tool;

/**
 * <B>EditorEvent</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-5-31 created
 * @since org.mepper.editor Ver 1.0
 * 
 */
public class EditorEvent extends EventObject {
	private EditorView view;
	private Tool newTool;
	private Tool oldTool;

	public EditorEvent(Object source) {
		super(source);
	}
	
	public void setView(EditorView view) {
		this.view = view;
	}

	public EditorView getView() {
		return view;
	}


	public void setNewTool(Tool newTool) {
		this.newTool = newTool;
	}

	public Tool getNewTool() {
		return newTool;
	}

	public Tool getOldTool() {
		return oldTool;
	}

	public void setOldTool(Tool oldTool) {
		this.oldTool = oldTool;
	}


}
