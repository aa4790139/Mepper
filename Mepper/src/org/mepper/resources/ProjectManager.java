/* ProjectDirector.java 1.0 2010-2-2
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

import org.mepper.editor.map.Map;


/**
 * <B>ProjectDirector</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-8 created
 * @since org.mepper.editor.project Ver 1.0
 * 
 */
public interface ProjectManager extends ResourcesManager{
	public static final int MAP_SET_ID=-2;
	public static final int LIBRARY_SET_ID=-3;
	
	
	boolean existProject(String projectName);
	void addProject(ProjectResource project);
	ProjectResource getCurrentProject();
	boolean isSignificant(StorableResource r);
//	boolean existMap(String name);
	void addMap(Map map);
	void addLibrary(LibraryResource child);
}
