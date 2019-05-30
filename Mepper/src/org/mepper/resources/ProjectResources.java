/* MapProject.java 1.0 2010-2-2
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
package org.mepper.resources;

import org.mepper.io.StorablePropertySupporter;


/**
 * <B>MapProject</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-8 created
 * @since org.mepper.editor.project Ver 1.0
 * 
 */
//FIXME remove this class, use LibraryResource instead(try use the "_type" property.
public class ProjectResources extends DefaultResources{ 
	public ProjectResources() {
		super(new StorablePropertySupporter());
	}
	
	public ProjectResources(String name) {
		this();
		setName(name);
	}


	public void initLibrary() {
		LibraryResources mapSet=new LibraryResources("地图");
		mapSet.setID(ProjectManager.MAP_SET_ID);
		addChild(mapSet);
		 
		
		LibraryResources libSet=new LibraryResources("库");
		libSet.setID(ProjectManager.LIBRARY_SET_ID);
		addChild(libSet);
	}

	public boolean existMap(String name) {
		StorableResource r=getChildByID(ProjectManager.MAP_SET_ID);
		for(int i=0,j=getChildCount();i<j;i++){
			r= getChild(i);
			if(r.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
} 
