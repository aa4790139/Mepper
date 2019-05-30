/*
 * AbstractEditor.java 1.0 2010-2-2
 * 
 * Copyright (c) 2010 by Chen Zhiwu All rights reserved.
 * 
 * The copyright of this software is own by the authors. You may not use, copy
 * or modify this software, except in accordance with the license agreement you
 * entered into with the copyright holders. For details see accompanying license
 * terms.
 */
package org.mepper.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;

import org.mepper.tool.Tool;

/**
 * <B>AbstractEditor</B>
 * 
 * @author Zhi-Wu Chen. Email: <a
 *         href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-8 created
 * @since org.mepper.editor Ver 1.0
 * 
 */
public class DefaultEditor implements Editor {
	private Tool currentTool;
	protected final List<EditorView> views;
	protected EditorView activateView;
	private boolean enable;
	private Object userObject;
	private final EventListenerList listenerList= new EventListenerList();
	
	private final PropertyChangeListener undoListener=new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if(! evt.getPropertyName().equals(EditorView.UNDO_HAPENED)){
				return;
			}
			fireUndoHappen();
		}
	};

	public DefaultEditor() {
		views = new ArrayList<EditorView>();
	}



	@Override
	public void add(EditorView v) {
		views.add(v);
		activateView=v;
		if(currentTool !=null){
			activateView(currentTool,v.getComponent());
		}
		fireViewAdded(v);
//		firePropertyChange("view.add", null, v);
	}

	@Override
	public void setTool(Tool tool) {
		JComponent c;
		for(EditorView v:views){
			c=v.getComponent();
			deactivateView(c);
			activateView(tool, c);
		}
		fireToolChanged(currentTool,currentTool = tool);
	}

	private void deactivateView(JComponent c) {
		c.removeMouseListener(currentTool);
		c.removeMouseMotionListener(currentTool);
//		c.removeMouseWheelListener(currentTool);
		c.removeKeyListener(currentTool);
	}

	private void activateView(Tool tool, JComponent c) {
		c.addMouseListener(tool);
		c.addMouseMotionListener(tool);
//		c.addMouseWheelListener(tool);
		c.addKeyListener(tool);
		
		c.removePropertyChangeListener(undoListener);
		c.addPropertyChangeListener(undoListener);
		c.setCursor(tool.getCursor());
	}

	@Override
	public Tool getTool() {
		return currentTool;
	}

	@Override
	public void remove(EditorView v) {
		views.remove(v);
	}

	@Override
	public EditorView getActivateView() {
		return activateView;
	}

	@Override
	public void setActivateView(EditorView v) {
		if (v != null) {
			v.activate(this);
		}
		fireActivateViewChanged( activateView =v);
	}

	@Override
	public List<EditorView> getEditorViews() {
		return Collections.unmodifiableList(views);
	}

	@Override
	public boolean isEnable() {
		return enable;
	}

	@Override
	public void setEnable(boolean b) {
		this.enable = b;
	}

	@Override
	public void setUserObject(Object object) {
		this.userObject=object;
	}

	@Override
	public Object getuserobject() {
		return userObject;
	}

	@Override
	public boolean canUndo() {
		if(activateView == null){
			return false;
		}
		return getUndoManager().canUndo();
	}

	@Override
	public boolean canRedo() {
		if(activateView == null){
			return false;
		}
		return getUndoManager().canRedo();
	}

	@Override
	public void undo() {
		getUndoManager().undo();
	}

	@Override
	public void redo() {
		getUndoManager().redo();
	}
	
	private UndoManager getUndoManager(){
		if(activateView == null){
			return null;
		}
		return activateView.getUndoManager();
	}
	
	
	private void fireViewAdded(EditorView v) {
		EditorEvent e = new EditorEvent(this);
		e.setView(v);
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0, j = listeners.length - 1; i < j; i += 2) {
			if (listeners[i] == EditorListener.class) {
				((EditorListener) listeners[i + 1]).viewAdded(e);
			}
		}
	}

	protected void fireUndoHappen() {
		EditorEvent e = new EditorEvent(this);
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0, j = listeners.length - 1; i < j; i += 2) {
			if (listeners[i] == EditorListener.class) {
				((EditorListener) listeners[i + 1]).undoHappened(e);
			}
		}
	}
	
	
	private void fireToolChanged(Tool oldTool,Tool newTool) {
		EditorEvent e = new EditorEvent(this);
		e.setOldTool(oldTool);
		e.setNewTool(newTool);
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0, j = listeners.length - 1; i < j; i += 2) {
			if (listeners[i] == EditorListener.class) {
				((EditorListener) listeners[i + 1]).toolChanged(e);
			}
		}
	}

	protected void fireActivateViewChanged(EditorView v) {
		EditorEvent e = new EditorEvent(this);
		e.setView(v);
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0, j = listeners.length - 1; i < j; i += 2) {
			if (listeners[i] == EditorListener.class) {
				((EditorListener) listeners[i + 1]).viewChanged(e);
			}
		}
	}

	@Override
	public void addEditorListener(EditorListener l) {
		listenerList.add(EditorListener.class, l);
	}

	@Override
	public void removeEditorhangeListener(EditorListener l) {
		listenerList.remove(EditorListener.class, l);
	}
	
	

}
