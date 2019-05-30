/* NewCustomTileAction.java 1.0 2010-2-2
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
package org.mepper.resources.action;

import java.awt.event.ActionEvent;

import org.mepper.resources.ResourcesManager;
import org.mepper.resources.gui.NewCustomTileDialog;
import org.zhiwu.app.AppDialog;
import org.zhiwu.app.Application;
import org.zhiwu.app.DialogEvent;
import org.zhiwu.app.AppDialogListener;
import org.zhiwu.app.action.AppAction;

/**
 * <B>NewCustomTileAction</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-27 created
 * @since org.mepper.resource.action Ver 1.0
 * 
 */
public class NewCustomTileAction extends AppAction{

	public static final String ID = "new.value.tile";
	private final ResourcesManager manager;

	public NewCustomTileAction(Application app, ResourcesManager manager) {
		super(app,ID);
		this.manager =manager;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.setEnable(false);
		final AppDialog dialog=new NewCustomTileDialog(app,manager,null);
		dialog.showDialog(new AppDialogListener(){
			@Override
			public void optionSelection(DialogEvent evt) {
				dialog.dispose();// other option
				app.setFoucs(); 
				app.setEnable(true);
			}
		}); 
	}

}
