/* MepperModel.java 1.0 2010-2-2
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
package org.mepper.app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;

import org.mepper.app.action.CopyAction;
import org.mepper.app.action.CutAction;
import org.mepper.app.action.NewLayerAction;
import org.mepper.app.action.PasteAction;
import org.mepper.app.action.TileManagerAction;
import org.zhiwu.app.DefaultModel;
import org.zhiwu.app.action.RedoAction;
import org.zhiwu.app.action.UndoAction;
import org.zhiwu.app.action.ViewSwitchAction;
import org.zhiwu.utils.AppResources;

/**
 * <B>MepperModel</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-8 created
 * @since org.mepper.app Ver 1.0
 * 
 */
public class MapModel extends DefaultModel{

	
	
	@Override
	public List<JMenu> createMenu() {// TODO move to view
		AppResources resource =app.getResource();
		List<JMenu> list=new ArrayList<JMenu>();

		JMenu editMenu = new JMenu();
		resource.configMenu(editMenu, "edit");
		editMenu.add(view.getAction(UndoAction.ID));
		editMenu.add(view.getAction(RedoAction.ID));
		editMenu.addSeparator();
		editMenu.add(getAction(CopyAction.ID));
		editMenu.add(getAction(CutAction.ID));
		editMenu.add(getAction(PasteAction.ID));
		list.add(editMenu);



		JMenu mapMenu = new JMenu();
		resource.configMenu(mapMenu, "map");
		mapMenu.add(getAction(NewLayerAction.ID));
		mapMenu.add(getAction(TileManagerAction.ID));

		list.add(mapMenu);

		return list;
	}
	
	@Override
	protected void initActions() {
		super.initActions();
		for(Iterator<String> i=viewIterator();i.hasNext();){
			String viewName=i.next();
			putAction(viewName,new ViewSwitchAction(app,viewName));
		}
		
	}
}
