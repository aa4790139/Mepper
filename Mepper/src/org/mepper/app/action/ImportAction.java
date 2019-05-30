/* ImportAction.java 1.0 2010-2-2
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
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.mepper.editor.map.Map;
import org.mepper.io.MapIOHelper;
import org.mepper.io.MapReader;
import org.mepper.resources.ProjectManager;
import org.zhiwu.app.Application;
import org.zhiwu.utils.FileUtils;

/**
 * <B>ImportAction</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-9-26 created
 * @since org.mepper.app.action Ver 1.0
 * 
 */
public class ImportAction extends MapEditorAction{
	public final static String ID="import";
	private final ProjectManager manager;

	public ImportAction(Application app, ProjectManager manager) {
		super(app,ID);
		this.manager = manager;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		File  f=FileUtils.saveAs(getView().getComponenet(),JFileChooser.FILES_ONLY ,new FileNameExtensionFilter[0]);
		if(f == null){
			return;
		}
		MapReader reader = MapIOHelper.getReader();
		try{
			Map map =reader.readMap(new FileInputStream(f));
			manager.addMap(map);
		}catch (Exception ee){
			JOptionPane.showMessageDialog(getView().getComponenet(), "error data format");
		}
	}
	
	@Override
	public String getID() {
		return ID;
	}

}
