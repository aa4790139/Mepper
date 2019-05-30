/* LibraryManagerAction.java 1.0 2010-2-2
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

import org.mepper.editor.gui.LibraryDialog;
import org.mepper.resources.ProjectManager;
import org.mepper.resources.ProjectResource;
import org.mepper.resources.StorableResource;
import org.zhiwu.app.AppDialog;
import org.zhiwu.app.AppDialogListener;
import org.zhiwu.app.Application;
import org.zhiwu.app.DialogEvent;
import org.zhiwu.app.action.AppAction;

/**
 * <B>LibraryManagerAction</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-9-27 created
 * @since org.mepper.app.action Ver 1.0
 * 
 */
public class LibraryManagerAction extends AppAction{
	public final static String ID="manager.library";
	private final ProjectManager manager;

	public LibraryManagerAction(Application app,ProjectManager manager) {
		super(app,ID);
		this.manager = manager;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.setEnable(false);
		
		final StorableResource currentNode= manager.getCurrentNode();
		ProjectResource project= manager.getCurrentProject();
		manager.setCurrentNode(project.getChildByID(ProjectManager.LIBRARY_SET_ID));

		final AppDialog dialog = new LibraryDialog(manager, app);
		dialog.showDialog(new AppDialogListener() {
			@Override
			public void optionSelection(DialogEvent evt) {
				manager.setCurrentNode(currentNode);
				app.setEnable(true);
				dialog.dispose();
			}
		});
	}
	@Override
	public String getID() {
		return ID;
	}
}
