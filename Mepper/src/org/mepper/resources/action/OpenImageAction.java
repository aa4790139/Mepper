/* OpenAction.java 1.0 2010-2-2
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
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.mepper.resources.gui.ResourcesView;
import org.zhiwu.app.AppManager;
import org.zhiwu.app.Application;
import org.zhiwu.app.action.AppAction;
import org.zhiwu.utils.AppResources;
import org.zhiwu.utils.FileUtils;

/**
 * <B>OpenAction</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2010-12-24 created
 * @since org.map.editor.tile.manager.action Ver 1.0
 * 
 */
public class OpenImageAction extends AppAction{
	public static final String ID="open";

	/**
	 * @param app
	 */
	public OpenImageAction(Application app) {
		super(app,ID);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AppResources r=AppManager.getResources();
		FileNameExtensionFilter image=new FileNameExtensionFilter(
				r.getString("open.extension.image"),//
				"jpg","png","psd","gif","jpeg","bmp");
		File[] files=FileUtils.openFiles(getView(), JFileChooser.FILES_ONLY , image);
		if(files!=null){
			getView().open(files);
		} 
	}

	@Override
	public String getID() {
		return ID;
	}
	
	public ResourcesView getView(){
		return (ResourcesView) app.getView();
	}
	

}
