/* ResourcesTreeCellRenderer.java 1.0 2010-2-2
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
package org.mepper.gui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.mepper.editor.map.Map;
import org.mepper.editor.tile.CustomTile;
import org.mepper.editor.tile.Tile;
import org.mepper.io.Storable;
import org.mepper.resources.LibraryResource;
import org.mepper.resources.ProjectManager;
import org.mepper.resources.ProjectResource;
import org.mepper.resources.StorableResource;
import org.zhiwu.app.AppManager;
import org.zhiwu.utils.AppResources;

/**
 * <B>ResourcesTreeCellRenderer</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-27 created
 * @since org.mepper.gui Ver 1.0
 * 
 */
public class ResourcesTreeCellRenderer extends DefaultTreeCellRenderer{
	private final Icon projectIcon;
	private final Icon tileIcon;
	private final Icon libraryIcon;
	private final Icon librarySetIcon;
	private final Icon mapIcon;
	private final Icon mapSetIcon;
	private final Icon customIcon;
	
	public ResourcesTreeCellRenderer() {
		AppResources r = AppManager.getResources();
		projectIcon = r.getImageIcon("project.view.project");
		tileIcon = r.getImageIcon("tile.manager.view.tree.tile");
		libraryIcon = r.getImageIcon("library.view");
		librarySetIcon = r.getImageIcon("library.view.set");
		mapIcon = r.getImageIcon("map");
		mapSetIcon = r.getImageIcon("map.set");
		customIcon = r.getImageIcon("tile.manager.view.tree.custom");

	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		
		if(!(value instanceof StorableResource)){
			return this;
		}
		
		StorableResource r= (StorableResource) value;
		if (r instanceof ProjectResource) {
			setIcon(projectIcon);
			return this;
		} 
		
		if (r instanceof LibraryResource) {
			LibraryResource lib = (LibraryResource) value;
			int id = lib.getID();
			if (id == ProjectManager.MAP_SET_ID) {
				setIcon(mapSetIcon);
			} else if (id == ProjectManager.LIBRARY_SET_ID) {
				setIcon(librarySetIcon);
			} else {
				setIcon(libraryIcon);
			}
			setToolTipText(r.getName()+", "+ r.getDescription()+","+r.getID());
			return this;
		} 

		Storable source = r.getSource();
		if (source instanceof CustomTile) {
			setIcon(customIcon);
		} else if (source instanceof Tile) {
			setIcon(tileIcon);
		} else if (source instanceof Map) {
			setIcon(mapIcon);
		}
			
		return this;
	}
	
}
