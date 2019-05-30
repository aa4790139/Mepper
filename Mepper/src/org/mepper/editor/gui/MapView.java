/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MapView.java
 *
 * Created on 2011-4-25, 10:09:51
 */

package org.mepper.editor.gui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.mepper.app.action.AboutAction;
import org.mepper.app.action.ButtonFactory;
import org.mepper.app.action.DeleteResourceAction;
import org.mepper.app.action.ExportAction;
import org.mepper.app.action.ImportAction;
import org.mepper.app.action.LibraryManagerAction;
import org.mepper.app.action.NewLayerAction;
import org.mepper.app.action.NewMapAction;
import org.mepper.app.action.NewProjectAction;
import org.mepper.app.action.ShowCoordinateAction;
import org.mepper.app.action.ShowGridAction;
import org.mepper.app.action.TerrainAction;
import org.mepper.editor.DefaultEditor;
import org.mepper.editor.Editor;
import org.mepper.editor.EditorAdapter;
import org.mepper.editor.EditorEvent;
import org.mepper.editor.EditorListener;
import org.mepper.editor.EditorView;
import org.mepper.editor.MapEditorView;
import org.mepper.editor.map.Map;
import org.mepper.resources.ProjectManager;
import org.mepper.tool.Tool;
import org.mepper.tool.ToolEvent;
import org.mepper.tool.ToolListener;
import org.zhiwu.app.AbsView;
import org.zhiwu.app.Model;
import org.zhiwu.app.action.ConfigAction;
import org.zhiwu.app.action.ExitAction;
import org.zhiwu.app.action.RedoAction;
import org.zhiwu.app.action.UndoAction;
import org.zhiwu.utils.AppResources;

/**
 * <B>MapView</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-25 created
 * @since org.mepper.editor.gui Ver 1.0
 * 
 */
public class MapView extends AbsView {
	private Editor editor;// not null
	private JToolBar currentToolbar;
	
	protected Action undoAction;
	protected Action redoAction;
	
	private final PropertyChangeListener messageListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("message")) {
				messager.setText(evt.getNewValue().toString());
			}
		}
	};
	
	private final PropertyChangeListener selectionListener=new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(ProjectPanel.MAP_CHANGED_PROPERTY)) {
				Map map=(Map) evt.getNewValue();
				try {
					EditorView view=new MapEditorView(map); 
					editor.add(view);
//					closableTabbedPane1.add(map.getName(), view.getComponent());
					openToolbar();
				} catch (OutOfMemoryError e) {
					JOptionPane.showMessageDialog(MapView.this, "cannot open the map: too large map");
				}
				
			}
		}
	};
	
	private final ToolListener toolListener=new ToolListener() {
		@Override
		public void toolDone(ToolEvent e) {
			showToolMessage(e);
		}

		@Override
		public void toolChanged(ToolEvent e) {
			showToolMessage(e);
		}
	};
	
	private final EditorListener editorListener = new EditorAdapter() {
		@Override
		public void viewChanged(EditorEvent evt) {
			updateLayerPane(evt.getView());
			updateSnapshot(evt.getView());
			updateUndoAction();			
		}
		
		@Override
		public void toolChanged(EditorEvent evt) {
			Tool tool;
			tool = evt.getOldTool();
			if(tool !=null){
				tool.removeToolListener(toolListener);
			}
			
			tool= evt.getNewTool();
			tool.addToolListener(toolListener);			
		}

		@Override
		public void undoHappened(EditorEvent e) {
				updateUndoAction();
		}
	};
	

    public MapView() {
    	 initComponents();
         initListener();
         initEditor();
    }
    
	private void initEditor() {
		editor = new DefaultEditor();
		editor.addEditorListener(editorListener);
		ButtonFactory.addToolbarToMapEditor(editor, this);
		closableTabbedPane1.setEditor(editor);
	}

	private void initListener() {
		projectPanel1.addPropertyChangeListener(messageListener);
		projectPanel1.addPropertyChangeListener(selectionListener);
	}

	protected void openToolbar() {
		if(currentToolbar != null){
			remove(currentToolbar);
		}
		
		Object obj=getClientProperty(editor.getClass().getName());
		if(obj instanceof JToolBar){
			JToolBar toolbar = (JToolBar)obj;
			add(currentToolbar=toolbar,BorderLayout.WEST);
			validate();
		}
	}
	
	
	@Override
	protected void initActions() {
		ProjectManager manager = projectPanel1.getManager();
		putAction(NewProjectAction.ID, new NewProjectAction(app,manager));
		putAction(NewMapAction.ID, new NewMapAction(app,manager)); 
		putAction(NewLayerAction.ID, new NewLayerAction(app));
		
		putAction(TerrainAction.ID, new TerrainAction(app, editor, manager));
		putAction(ShowCoordinateAction.ID, new ShowCoordinateAction(app));
		putAction(ShowGridAction.ID, new ShowGridAction(app, editor));
		
		putAction(ImportAction.ID, new ImportAction(app,manager));
		putAction(ExportAction.ID, new ExportAction(app,manager));
		putAction(LibraryManagerAction.ID, new LibraryManagerAction(app, manager));
		putAction(DeleteResourceAction.ID, new DeleteResourceAction(app, manager));
		
		undoAction=new UndoAction(app);
		redoAction=new RedoAction(app);
		putAction(UndoAction.ID, undoAction);
		putAction(RedoAction.ID, redoAction);
		undoAction.setEnabled(false);
	}
	
	@Override
	public JMenuBar createMenus() {
		JMenuBar menuBar=new JMenuBar();
		AppResources r = app.getResource();
		Model model=app.getModel();

		// file
		JMenu fileMenu=new JMenu();
		r.configMenu(fileMenu, "file");
		fileMenu.add(getAction(NewProjectAction.ID));
		fileMenu.add(getAction(NewMapAction.ID));
//		fileMenu.add(model.getAction(OpenAction.ID));
		fileMenu.addSeparator();
//		fileMenu.add(model.getAction(SaveAction.ID));
//		fileMenu.add(model.getAction(CloseAction.ID));
//		fileMenu.addSeparator();
		fileMenu.add(model.getAction(ExitAction.ID));
//		fileMenu.add(getAction(SaveAction.ID));
//		fileMenu.add(model.getAction(ExitAction.ID));
		menuBar.add(fileMenu);
		
		//edit
		JMenu editMenu=new JMenu();
		r.configMenu(editMenu, "edit");
		editMenu.add(undoAction);
		editMenu.add(redoAction);
		editMenu.addSeparator();
//		editMenu.add(getAction(DeleteAction.ID));
		menuBar.add(editMenu);
		
		// view
		JMenu viewMenu=new JMenu();
		r.configMenu(viewMenu, "view");
		for(Iterator<String> i=model.viewIterator();i.hasNext();){
			viewMenu.add(model.getAction(i.next()));
		}
		viewMenu.addSeparator();
		viewMenu.add(model.getAction(ConfigAction.ID));
		menuBar.add(viewMenu);
		
		// help
		JMenu helpMenu=new JMenu();
		r.configMenu(helpMenu, "help");
//		helpMenu.add(getAction(VersionAction.ID));
		helpMenu.add(model.getAction(AboutAction.ID));
//		helpMenu.addSeparator();
//		helpMenu.add(model.getAction(HelpAction.ID));
		menuBar.add(helpMenu);

		return menuBar;
	}
	
	@Override
	public JToolBar getToolbar() {
		JToolBar mapViewBar = new JToolBar();

		mapViewBar.add(getAction(NewProjectAction.ID));
		mapViewBar.add(getAction(NewMapAction.ID));
		mapViewBar.addSeparator();
		mapViewBar.add(getAction(UndoAction.ID));
		mapViewBar.add(getAction(RedoAction.ID));
		mapViewBar.addSeparator();
		mapViewBar.add(getAction(TerrainAction.ID));
		
		mapViewBar.add(getAction(ShowGridAction.ID));
		mapViewBar.add(getAction(ShowCoordinateAction.ID));
		mapViewBar.add(getAction(LibraryManagerAction.ID));
		mapViewBar.add(getAction(DeleteResourceAction.ID));
		mapViewBar.addSeparator();
		mapViewBar.add(getAction(ImportAction.ID));
		mapViewBar.add(getAction(ExportAction.ID));
		mapViewBar.addSeparator();
		
		return mapViewBar;
	}
    
	public Editor getEditor() {
		return editor;
	}
	
	@Override
	public boolean canUndo() {
		return editor.canUndo();
	}
	
	@Override
	public boolean canRedo() {
		return editor.canRedo();
	}
	
	@Override
	public void undo() {
		editor.undo();
		updateUndoAction();
	}
	
	@Override
	public void redo() {
		editor.redo();
		updateUndoAction();
	}
	
	private void updateUndoAction() {
		undoAction.setEnabled(editor.canUndo());
		redoAction.setEnabled(editor.canRedo());
	}

    protected void updateSnapshot(EditorView  v) {
		snapshotPanel1.guard(v);
	}
	protected void updateLayerPane(EditorView  v) {
		layerPanel1.setMap( v == null? null: v.getMap() );
	}
	
    private void showToolMessage(ToolEvent e) {
		StringBuilder sb= new StringBuilder();
		Point p;
		
		sb.append("[");
		p=e.getMapPoint();
		sb.append(p.x+","+p.y);
		
		sb.append("],\t[");
		p=e.getScreenPoint();
		sb.append(p.x+","+p.y+"]");
		
		
		messager.setText(sb.toString());
	}
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        snapshotPanel1 = new org.mepper.editor.gui.SnapshotPanel();
        layerPanel1 = new org.mepper.editor.gui.LayerPanel();
        closableTabbedPane1 = new org.mepper.editor.gui.ClosableTabbedPane();
        projectPanel1 = new org.mepper.editor.gui.ProjectPanel();
        jPanel4 = new javax.swing.JPanel();
        informationBar = new javax.swing.JToolBar();
        messager = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerSize(8);
        jSplitPane1.setResizeWeight(0.05);
        jSplitPane1.setOneTouchExpandable(true);

        jSplitPane3.setDividerSize(8);
        jSplitPane3.setResizeWeight(0.85);
        jSplitPane3.setOneTouchExpandable(true);

        jSplitPane2.setDividerSize(8);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setResizeWeight(0.7);
        jSplitPane2.setOneTouchExpandable(true);

        jScrollPane2.setViewportView(snapshotPanel1);

        jSplitPane2.setTopComponent(jScrollPane2);

        layerPanel1.init(this);
        jSplitPane2.setRightComponent(layerPanel1);

        jSplitPane3.setRightComponent(jSplitPane2);
        jSplitPane3.setLeftComponent(closableTabbedPane1);

        jSplitPane1.setRightComponent(jSplitPane3);

        projectPanel1.init(this);
        jSplitPane1.setLeftComponent(projectPanel1);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        informationBar.setRollover(true);
        informationBar.setBorderPainted(false);

        messager.setPreferredSize(new java.awt.Dimension(200, 15));
        informationBar.add(messager);
        informationBar.add(jSeparator1);

        jLabel2.setMaximumSize(new java.awt.Dimension(50, 15));
        informationBar.add(jLabel2);

        jPanel4.add(informationBar, java.awt.BorderLayout.WEST);

        jToolBar1.setRollover(true);
        jPanel4.add(jToolBar1, java.awt.BorderLayout.EAST);

        add(jPanel4, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.mepper.editor.gui.ClosableTabbedPane closableTabbedPane1;
    private javax.swing.JToolBar informationBar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JToolBar jToolBar1;
    private org.mepper.editor.gui.LayerPanel layerPanel1;
    private javax.swing.JLabel messager;
    private org.mepper.editor.gui.ProjectPanel projectPanel1;
    private org.mepper.editor.gui.SnapshotPanel snapshotPanel1;
    // End of variables declaration//GEN-END:variables

}
