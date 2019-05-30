/* TerrainAction.java 1.0 2010-2-2
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
import org.mepper.editor.map.Layer;
import org.mepper.editor.map.Map;
import org.mepper.editor.tile.DavidTransitor;
import org.mepper.editor.tile.TerrainTransitor;
import org.mepper.resources.LibraryResource;
import org.mepper.resources.ProjectManager;
import org.mepper.resources.ProjectResource;
import org.zhiwu.app.Application;
import org.zhiwu.app.action.AppAction;

/**
 * <B>TerrainAction</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-9-26 created
 * @since org.mepper.app.action Ver 1.0
 * 
 */
public class TerrainAction extends AppAction{
	public static final String ID="terrain.handle";
	private final Editor editor ;
	private final ProjectManager projectManager;

	public TerrainAction(Application app,Editor editor,ProjectManager projectManager) {
		super(app,ID);
		this.editor =editor;
		this.projectManager = projectManager;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EditorView v=  editor.getActivateView();
		if(v == null){
			return;
		}
		
		ProjectResource pro= projectManager.getCurrentProject();
		if(pro == null){
			return;
		}
		
		Map map =v.getMap();
		int index = map.getSelectedIndex();
		if(index == -1){
			return;
		}
		Layer l = map.getLayer(index);
		if(l == null){
			return;
		}
		
		TerrainTransitor t=new DavidTransitor();
		t.setLayer(l);
		t.addLibrary((LibraryResource) pro.getChildByID(ProjectManager.LIBRARY_SET_ID));
		int p = map.getLayer(DavidTransitor.LAYER_NAME);
		if(p!= -1){
			map.removeLayer(p);
			p--;
		}
		map.addLayer(t.transition(), index+1);
	}
	
	@Override
	public String getID() {
		return ID;
	}

}
