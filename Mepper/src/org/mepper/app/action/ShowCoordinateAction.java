/* ShowCoordinateAction.java 1.0 2010-2-2
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
package org.mepper.app.action;

import java.awt.event.ActionEvent;

import org.mepper.editor.Editor;
import org.mepper.editor.EditorView;
import org.zhiwu.app.Application;

/**
 * <B>ShowCoordinateAction</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-9-26 created
 * @since org.mepper.app.action Ver 1.0
 * 
 */
public class ShowCoordinateAction extends MapEditorAction{
	public static final String ID = "coordinate";
	public ShowCoordinateAction(Application app) {
		super(app,ID);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Editor editor =getView().getEditor();
		EditorView v = editor.getActivateView();
		if (v == null) {
			return;
		}
		v.setCoordinate(!v.isCoordinate());
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	
}
