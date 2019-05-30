/* PropertyAction.java 1.0 2010-2-2
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

import org.mepper.resources.PropertySupported;
import org.mepper.resources.gui.PropertyDialog;
import org.zhiwu.app.AppDialog;
import org.zhiwu.app.AppDialogListener;
import org.zhiwu.app.Application;
import org.zhiwu.app.DialogEvent;

/**
 * <B>PropertyAction</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-9-27 created
 * @since org.mepper.app.action Ver 1.0
 * 
 */
public class PropertyAction extends MapEditorAction{
	public final static String ID="property";
	private final PropertySupported propertySupported;
	
	public PropertyAction(Application app,PropertySupported propertySupported) {
		super(app,ID);
		this.propertySupported = propertySupported;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.setEnable(false);
		final AppDialog dialog = new PropertyDialog(app, propertySupported);
		dialog.showDialog(new AppDialogListener() {
			@Override
			public void optionSelection(DialogEvent evt) {
				dialog.dispose();
				app.setEnable(true);
			}
		});
	}
	
	@Override
	public String getID() {
		return ID;
	}


}
