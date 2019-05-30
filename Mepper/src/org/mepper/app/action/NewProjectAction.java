/* NewProjectAction.java 1.0 2010-2-2
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

import org.mepper.editor.gui.NewProjectDialog;
import org.mepper.resources.ProjectManager;
import org.zhiwu.app.AppDialog;
import org.zhiwu.app.Application;
import org.zhiwu.app.DialogEvent;
import org.zhiwu.app.AppDialogListener;
import org.zhiwu.app.action.AppAction;

/**
 * <B>NewProjectAction</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-8 created
 * @since org.mepper.app.action Ver 1.0
 * 
 */
public class NewProjectAction extends AppAction{
	public static final String ID="new.project";
	private final ProjectManager director;

	public NewProjectAction(Application app,ProjectManager director) {
		super(app,ID);
		this.director = director;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.setEnable(false);
		final AppDialog dialog=new NewProjectDialog(app,director);
		dialog.showDialog(new AppDialogListener(){
			@Override
			public void optionSelection(DialogEvent evt) {
				dialog.dispose();// other option
				app.setFoucs();
				app.setEnable(true);
			}
		}); 
		setEnabled(true);
	}
	@Override
	public String getID() {
		return ID;
	}
}
