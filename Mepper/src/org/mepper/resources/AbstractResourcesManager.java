/* AbstractResourceManager.java 1.0 2010-2-2
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.mepper.app.MapApplication;
import org.zhiwu.app.AppManager;
import org.zhiwu.app.config.AppPreference;
import org.zhiwu.utils.AppLogging;

/**
 * <B>AbstractResourceManager</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-24 created
 * @since org.mepper.editor.project Ver 1.0
 * 
 */
public abstract class AbstractResourcesManager implements ResourcesManager{
	public EventListenerList listenerList;

	protected StorableResource currentNode;
	protected LibraryResource root;
	 
	protected AbstractResourcesManager() {
		listenerList= new EventListenerList();
	}

	@Override
	public void init() {
	}

	@Override
	public void close() {
	}

	@Override
	public StorableResource getRoot() {
		return root;
	}

	@Override
	public void addResourceChild(StorableResource child) {//FIXME check the ID
		if(currentNode.isLibrary()){
			currentNode.addChild(child);
			currentNode = child;
		}else {
			currentNode = currentNode.getParentResource();
			addResourceChild(child);
			return ;
		}
		fireResourceAdded(new ResourcesEvent(this, child));
	}

	@Override
	public void addResourceToParent(StorableResource parent, StorableResource child) {
		StorableResource pointer= currentNode;
		while(pointer != null){
			if(pointer .equals( parent)){
				pointer.addChild(child);
				return;
			}
			pointer= pointer.getParentResource();
		}
	}

	@Override
	public void addResourceToSlibling(StorableResource slibling) {
		StorableResource pointer= currentNode.getParentResource();
		pointer.addChild(slibling);
	}

//	@Override
//	public TileResources getEdge(Tile t, int edge) {
//		return null;
//	}

	@Override
	public <T extends StorableResource> T getResource(Class<T> clazz, int id) {
		return getResource(clazz, root, id);
	}
	
	@SuppressWarnings("unchecked")
	public  <T extends StorableResource> T  getResource(Class<T> clazz,StorableResource resource,int id){
		if( resource.getID() == id && isInstanceof(resource.getClass(),clazz)){
			return (T) resource; 
		}
		for(int i=0,j=resource.getChildCount();i<j;i++){
			StorableResource r=getResource(clazz,resource.getChild(i), id);
			if(r !=null){
				return (T) r;
			}
		}
		return null;
	}
	
	private boolean isInstanceof(Class<? extends StorableResource> child,Class<? extends StorableResource> parent) {
		for(Class<?> c:child.getInterfaces()){
			if (c.equals(parent)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	@Override
	public StorableResource getResourceChild(int index) {
		return currentNode.getChild(index);
	}

	@Override
	public int resourceChildCount() {
		return currentNode.getChildCount();
	}

	@Override
	public boolean existResource(StorableResource resource) {
		for(int i=0,j=currentNode.getChildCount();i<j;i++){
			if (currentNode.getChild(i).equals(resource)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void setCurrentNode(StorableResource r) {
		fireResourceChanged(new ResourcesEvent(this, this.currentNode =r));
	}

	@Override
	public StorableResource getCurrentNode() {
		return currentNode;
	}

	@Override
	public void addResourceListener(ResourcesListener l) {
		listenerList.add(ResourcesListener.class, l);
	}

	@Override
	public void removeResourceListener(ResourcesListener l) {
		listenerList.remove(ResourcesListener.class, l);
	}

	@Override
	public List<LibraryResource> getLibraries() {
		List<LibraryResource> libraies=new ArrayList<LibraryResource>();
		for(int i=0,j=currentNode.getChildCount();i<j;i++){
			StorableResource r= currentNode.getChild(i);
			if (r.isLibrary()) {
				libraies.add((LibraryResource) r);
			}
		}
		return libraies;
	}

	@Override
	public boolean existChild(StorableResource parent, StorableResource child) {
		for(int i=0,j=parent.getChildCount();i<j;i++){
			if(parent.getChild(i).equals(child)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void removeResource(StorableResource r) {
		currentNode.remove(r);
		fireResourceRemoved(new ResourcesEvent(this, currentNode));
	}

	@Override
	public void removeResource(StorableResource parent, StorableResource child) {
		parent.addChild(child);
	}

	protected void fireResourceAdded(ResourcesEvent e) {
		Object[] listeners=listenerList.getListenerList();
		for(int i=0,j=listeners.length-1;i<j;i+=2){
			if (listeners[i]== ResourcesListener.class) {
				((ResourcesListener)listeners[i+1]).resourceAdded(e);
			}
		}
	}
	
	protected void fireResourceChanged(ResourcesEvent e) {
		Object[] listeners=listenerList.getListenerList();
		for(int i=0,j=listeners.length-1;i<j;i+=2){
			if (listeners[i]== ResourcesListener.class) {
				((ResourcesListener)listeners[i+1]).resourceChanged(e);
			}
		}
	}
	

	protected void fireResourceRemoved(ResourcesEvent e) {
		Object[] listeners=listenerList.getListenerList();
		for(int i=0,j=listeners.length-1;i<j;i+=2){
			if (listeners[i]== ResourcesListener.class) {
				((ResourcesListener)listeners[i+1]).resourceRemoved(e);
			}
		}
	}
	
	
	@Override
	public StorableResource create(StorableResource r) {
		try {
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(bos);
			oos.writeObject(r);
			
			ByteArrayInputStream bis=new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream ois=new ObjectInputStream(bis);
			return (StorableResource) ois.readObject();
		} catch (Exception e) {
			AppLogging.handleException(e);
		} 
		
		return null;
	}
	
	protected String getDataPath() {
		AppPreference pref = AppManager.getPreference(MapApplication.class
				.getName());
		String dataPath = pref.get("database.path");
		dataPath += dataPath.endsWith(File.separator)?"":File.separator;
		return dataPath;
	}

}
