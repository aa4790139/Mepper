/* ResourceManager.java 1.0 2010-2-2
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

import java.util.List;

/**
 * <B>ResourceManager</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-8 created
 * @since org.mepper.resource Ver 1.0
 * 
 */
public interface ResourcesManager extends ResourcesBuilder {
	void init();
	void save();
	void close();
	
//	TileResources getEdge(Tile t,int edge);
	
	
	int resourceChildCount();
	boolean existResource(StorableResource resource);
	boolean existChild(StorableResource parent, StorableResource child);
	
	void addResourceListener(ResourcesListener l);
	void removeResourceListener(ResourcesListener l);
	
	List<LibraryResource> getLibraries();
	
	void createResourceID(StorableResource r);
	StorableResource getRoot();
	StorableResource create(StorableResource root);
}
