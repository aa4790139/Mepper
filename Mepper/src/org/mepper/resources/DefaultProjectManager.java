/* DefaultProjectDirector.java 1.0 2010-2-2
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

import java.io.File;
import java.io.IOException;

import org.mepper.app.MapApplication;
import org.mepper.editor.map.Map;
import org.mepper.io.MapIOHelper;
import org.zhiwu.app.AppManager;
import org.zhiwu.app.config.AppPreference;
import org.zhiwu.utils.AppLogging;


/**
 * <B>DefaultProjectDirector</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-8 created
 * @since org.mepper.editor.project Ver 1.0
 * 
 */
public class DefaultProjectManager  extends AbstractResourcesManager implements ProjectManager {
	private static DefaultProjectManager instance;
	private static final Object RESOURCE_LOCK = new Object();
	
	private static int projectID= 0;
	private static int mapID= 0;
	
	private DefaultProjectManager() {
	}
	
	public static DefaultProjectManager getInstance(){
		if (instance == null) {
			synchronized (RESOURCE_LOCK) {
				if (instance == null) {
					instance = new DefaultProjectManager();
					instance.init();
				}
			}
		}
		return instance;
	}
	
	@Override
	public void init() {
		initID();
		initNodes();
		// read from files
		readLibrarys();
	}
	
	private void initID() {
		AppPreference pref=AppManager.getPreference(MapApplication.class.getName());
		projectID = pref.getInteger("project.id");
		mapID = pref.getInteger("map.id");
	}
	
	private void initNodes() {
		root=new LibraryResource("root");
		currentNode = root;
	}
	
	@Override 
	public boolean existProject(String projectName) {
		for(int i=0,j=root.getChildCount();i<j;i++){
			if (root.getChild(i).getName().equals(projectName)) {
				return true;
			}
		}
		return false;
	}
	
	public void readLibrarys() {
		String dataPath = getDataPath()+"project";
		MapIOHelper helper = new MapIOHelper();
		for (StorableResource r : helper.read(dataPath)) {
			root.addChild(r);
		}
	}
	
	@Override
	public void save() {
		AppPreference pref=AppManager.getPreference(MapApplication.class.getName());
		pref.put("project.id",projectID);
		pref.put("map.id", mapID);
		
		String dataPath = getDataPath()+"project/";
		MapIOHelper helper = new MapIOHelper();

		File f = new File(dataPath);
		if(!f.exists()){
			f.mkdirs();
		}
		
		try {
			for (int i = 0, j = root.getChildCount(); i < j; i++) {
				helper.save(root.getChild(i), dataPath);
			}
		} catch (IOException e) {
			AppLogging.handleException(e);
		}
	}
	
	@Override
	public ProjectResource getCurrentProject() {
		if(currentNode == root){
			return null;
		}
		StorableResource  r= currentNode;
		while(!(r instanceof ProjectResource)){
			r = r.getParentResource();
		}
		return (ProjectResource) r;
	}

	@Override
	public void createResourceID(StorableResource r) {
		synchronized (RESOURCE_LOCK) {
			if (r instanceof ProjectResource) {
				r.setID( ++projectID);
				return;
			}

			if(r.getSource() instanceof Map){
				r.getSource().setID(++mapID);
			}
		}
//		return Storable.ILLEGAL_ID;
	}

	@Override
	public boolean isSignificant(StorableResource r) {
		int id= r.getID();
		if (id == LIBRARY_SET_ID || id==MAP_SET_ID) {
			return true;
		}
		return false;
	}

	@Override
	public void addResourceChild(StorableResource child) {
		if ( child  instanceof ProjectResource){
			addProject((ProjectResource) child);
			return;
		}
		if(child instanceof LibraryResource){
			addLibrary((LibraryResource)child);
			return;
		}
		addMapResource(child);
	}

	

	@Override
	public void addProject(ProjectResource project) {
		root.addChild(project);
		currentNode = project;
		fireResourceAdded(new ResourcesEvent(this, project));
	}
	
	@Override
	public void addLibrary(LibraryResource child) {
		StorableResource r=getCurrentProject().getChildByID(LIBRARY_SET_ID);
		r.addChild(child);
		fireResourceAdded(new ResourcesEvent(this, child));
	}

	@Override
	public void addMap(Map map) {
		DefaultResource mapResource = new DefaultResource(map);
		createResourceID(mapResource);
//		map.addMapListener(l);
		addMapResource(mapResource);
		
	}
	
	private void addMapResource(StorableResource mapResource) {
		StorableResource mapSet=getCurrentProject().getChildByID(MAP_SET_ID);
		mapSet.addChild(mapResource);
		fireResourceAdded(new ResourcesEvent(this, mapResource));		
	}
	
}
