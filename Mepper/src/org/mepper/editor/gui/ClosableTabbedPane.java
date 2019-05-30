/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClosableTabbedPane.java
 *
 * Created on 2010-12-27, 14:51:36
 */

package org.mepper.editor.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.mepper.editor.Editor;
import org.mepper.editor.EditorAdapter;
import org.mepper.editor.EditorListener;
import org.mepper.editor.EditorView;
import org.mepper.resources.gui.TileEditorView;
import org.zhiwu.app.AppManager;
import org.zhiwu.app.action.AbsAction;
import org.zhiwu.utils.AppResources;

public class ClosableTabbedPane extends JTabbedPane {
	private final Icon imageIcon;
	private Editor editor;
	
	private final EditorListener editorListener= new EditorAdapter() {
		@Override
		public void viewAdded(org.mepper.editor.EditorEvent e) {
			EditorView v= e.getView();
			add(v.getMap().getName(), v.getComponent());
		};
	};
	
	private final ChangeListener changeListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			if (editor == null) {
				return;
			}
			EditorView p =getActivateComponent();
			editor.setActivateView(p);
		}
	};  
	
	private final MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (!SwingUtilities.isRightMouseButton(e)) {
				return ;
			}
			JPopupMenu popup = new JPopupMenu();
			
			Action closeAll = new AbsAction("close.all") {
				@Override
				public void actionPerformed(ActionEvent e) {
					for(int i=getTabCount();i>=0;i--){
						removeTabAt(i);
					}
				}
			};
			popup.add(closeAll);
			
			Action closeOther= new AbsAction("close.other") {
				@Override
				public void actionPerformed(ActionEvent e) {
					int p=getSelectedIndex();
					for(int i=getTabCount()-1;i>=0;i--){
						if(i != p){
							removeTabAt(i);
						}
					}
				}
			};
			popup.add(closeOther);
			
			Action closeLeft= new AbsAction("close.left") {
				@Override
				public void actionPerformed(ActionEvent e) {
					int p=getSelectedIndex();
					for (int i = 0; i < p; i++) {
						removeTabAt(0);
					}
				}
			};
			popup.add(closeLeft);
			
			Action closeRight= new AbsAction("close.right") {
				@Override
				public void actionPerformed(ActionEvent e) {
					int p=getSelectedIndex();
					for (int i = getTabCount()-1; i >p; i--) {
						removeTabAt(i);
					}
				}
			};
			popup.add(closeRight);
			
			popup.show(ClosableTabbedPane.this, e.getX(), e.getY());
		};
	};
	
    public ClosableTabbedPane() {
        initComponents();
        
        AppResources r=AppManager.getResources();
//        tileIcon=  r.getImageIcon("closable.tabbed.pane.icon.tile");
        imageIcon=  r.getImageIcon("closable.tabbed.pane.icon.image");
        
        addChangeListener(changeListener);
        addMouseListener(mouseListener);
        
    }
    
	protected EditorView getActivateComponent() {
		EditorView view=null;
		int p=getSelectedIndex();
		if(p==-1){
			return null;
		}
		Component c=getComponentAt(p);
		if( c instanceof JScrollPane){
			JScrollPane sc=(JScrollPane) c;
			view= (EditorView) sc.getViewport().getView();
		}else {
			view =(EditorView) c;
		}
		return view;
	}

	@Override
    public Component add(String title, Component c) {
		if( ! (c instanceof EditorView)){
			throw new IllegalArgumentException("editor view only");
		}
    	if(existComponent(c)){
    		return null;
    	}
    	int p=getSelectedIndex()+1;
    	JScrollPane scrollPane=new JScrollPane(c);
    	scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
		
		insertTab(title, null, scrollPane, title, p);
		setSelectedIndex(p);
		setTabComponentAt(p, new ClosablePanel(title, this, p, getIcon(c)));
    	
		return scrollPane;
    }
    

	private Icon getIcon(Component c) {
		if(c instanceof TileEditorView){
			return imageIcon;
		}
		return null;
	}

	private boolean existComponent(Component c) {
		int p= getIndex(c);
		if(p != -1){
			setSelectedIndex(p);
			return true;
		}
		return false;
	}


	private int getIndex(Component c) {
		Component other;
		for(int i=0,j=getTabCount();i<j;i++){
			other=super.getComponentAt(i) ;
			if(other instanceof JScrollPane){
				JScrollPane pane=(JScrollPane) other;
				other= pane.getViewport().getView();
			}
			
			if(c.equals(other)){
				return i;
			}
		}
		return -1;
	}
	
	public int getEditorCount() {
		return super.getTabCount();
	}

	public void setEditor(Editor e) {
		if(editor != null){
			editor.removeEditorhangeListener(editorListener);
		}
		this.editor=e;
		editor.addEditorListener(editorListener);
	}
	
	

    private void initComponents() {
    	setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }


	
}
