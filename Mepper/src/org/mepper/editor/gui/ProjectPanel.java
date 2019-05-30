/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ProjectPanel.java
 *
 * Created on 2011-4-25, 10:54:09
 */

package org.mepper.editor.gui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.mepper.app.action.DeleteResourceAction;
import org.mepper.app.action.ExportAction;
import org.mepper.app.action.ImportAction;
import org.mepper.app.action.LibraryManagerAction;
import org.mepper.app.action.NewMapAction;
import org.mepper.app.action.NewProjectAction;
import org.mepper.app.action.PropertyAction;
import org.mepper.editor.map.Map;
import org.mepper.editor.tile.Tile;
import org.mepper.gui.ImageListModel;
import org.mepper.gui.ResourcesTreeCellRenderer;
import org.mepper.io.Storable;
import org.mepper.resources.DefaultProjectManager;
import org.mepper.resources.LibraryResource;
import org.mepper.resources.ProjectManager;
import org.mepper.resources.ResourcesEvent;
import org.mepper.resources.ResourcesListener;
import org.mepper.resources.ResourcesMessager;
import org.mepper.resources.StorableResource;
import org.zhiwu.app.AppManager;
import org.zhiwu.utils.AppResources;

/**
 *
 * @author root
 */
public class ProjectPanel extends javax.swing.JPanel {
	protected static final String MAP_CHANGED_PROPERTY = "selection.changed";
	public static final String CURRENT_NODE_CHANGED = "current changed";
	
	private final AppResources r=AppManager.getResources();
	private ProjectManager manager;
	private  MapView mapView;
	
	private final ImageListModel listModel;
	
	private final TreeSelectionListener treeSelectionListener =new TreeSelectionListener() {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			StorableResource  newValue = (StorableResource) e.getPath().getLastPathComponent();
			manager.setCurrentNode(newValue);
			updateActions(managerTree.getSelectionPath().getParentPath());
			messageChanged(newValue);
		}
	};
	
    public ProjectPanel() {
    	listModel = new ImageListModel();
    	listModel.setImageDimension(48,24);
    	
    	initManager();
        initComponents();
    }
    
	
	public void init(MapView mapView) {
    	this.mapView =mapView;
		initListeners();
	}
    
    
	private void initListeners() {
		managerTree.addTreeSelectionListener(treeSelectionListener);	
		
		managerTree.getCellEditor().addCellEditorListener(new CellEditorListener() {
			@Override
			public void editingStopped(ChangeEvent e) {
				cellValueChanged((DefaultCellEditor) e.getSource());
			}
			
			@Override
			public void editingCanceled(ChangeEvent e) {
				cellValueChanged((DefaultCellEditor) e.getSource());
			}
		});
		
		managerTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() ==2) {
					StorableResource r=(StorableResource) managerTree.getSelectionPath().getLastPathComponent();
					Storable source;
					
					if( (r instanceof LibraryResource) && (!manager.isSignificant(r))){
						listModel.clear();
						for(int i=0,j=r.getChildCount();i<j;i++){
							source = r.getChild(i).getSource();
							if(source instanceof Tile){
								listModel.add((Tile) source);
							}
						}
						jTabbedPane1.setSelectedIndex(jTabbedPane1.getSelectedIndex()+1);
						return;
					}
					
					source =r.getSource();
					if( ( source instanceof Map) ){
						Map map = (Map) source;
						firePropertyChange(MAP_CHANGED_PROPERTY, null, map);
						return;
					}
					if( source instanceof Tile ){
						mapView.getEditor().setUserObject(source);
						return;
					}
				}
				if (SwingUtilities.isRightMouseButton(e)) {
					handlePopupMenu(e);
				}
			}
		});
		
		jList1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)
						&& e.getClickCount() == 2) {
					ImageListModel model =(ImageListModel) jList1.getModel();
					Tile tile = (Tile) model.getData(jList1.getSelectedIndex());
					mapView.getEditor().setUserObject(tile);
					return;
				}
			}
		});
	}
	
	protected void updateActions(TreePath path) {
		final StorableResource r= manager.getCurrentNode();
		final Storable source = r.getSource();

		
		Action newMapAction =mapView.getAction(NewMapAction.ID);
		newMapAction.setEnabled(path!= null);
		
		Action importAction =mapView.getAction(ImportAction.ID);
		importAction.setEnabled(path!= null);
		
		Action exportaAction = mapView.getAction(ExportAction.ID);
		exportaAction.setEnabled(source instanceof Map);	
		
		Action libraryManagerAction = mapView.getAction(LibraryManagerAction.ID);
		libraryManagerAction.setEnabled(path != null);
		
		Action deleteAction = mapView.getAction(DeleteResourceAction.ID);
		deleteAction.setEnabled(!manager.isSignificant(r) && path != null);
		
	}

	private void handlePopupMenu(MouseEvent e) {
		TreePath  path =managerTree.getSelectionPath();
		if(path == null){
			return;
		}
		updateActions(path.getParentPath());
		Point p=e.getPoint();
		JPopupMenu menu = new JPopupMenu();
		
		menu.add(mapView.getAction(NewMapAction.ID));
		menu.add(mapView.getAction(NewProjectAction.ID));
		menu.add(mapView.getAction(ImportAction.ID));
		menu.add(mapView.getAction(ExportAction.ID));
		menu.add(mapView.getAction(LibraryManagerAction.ID));
		menu.add(mapView.getAction(DeleteResourceAction.ID));
		
		Action propertyAction = new PropertyAction(mapView.getApplication(), manager.getCurrentNode());
		propertyAction.setEnabled(managerTree.getSelectionPath().getParentPath() != null);
		menu.add(propertyAction);

        menu.show(this, p.x, p.y);
	}


	private void cellValueChanged(DefaultCellEditor e){
		TreePath path=managerTree.getSelectionPath();
		if(path == null){
			return;
		}
		StorableResource r=(StorableResource) path.getLastPathComponent();
		r.setName(e.getCellEditorValue().toString());
	}
	
	private void initManager() {
		manager= DefaultProjectManager.getInstance();
    	manager.addResourceListener(new ResourcesListener() {
			@Override
			public void resourceRemoved(ResourcesEvent e) {
				managerChanged(e);
			}

			@Override
			public void resourceChanged(ResourcesEvent e) {
				managerChanged(e);
			}
			
			@Override
			public void resourceAdded(ResourcesEvent e) {
				nodeStructureChanged(getRoot());
				DefaultTreeModel model=(DefaultTreeModel) managerTree.getModel();
				TreeNode[] nodes=model.getPathToRoot(e.getReousrce());
				TreePath path=new TreePath(nodes);
				managerTree.setSelectionPath(path);
				
				messageChanged((StorableResource) path.getLastPathComponent());
			}
		});
	}
	
	private void managerChanged(ResourcesEvent e) {
		managerTree.removeTreeSelectionListener(treeSelectionListener);
		
		StorableResource r=e.getReousrce();
		nodeStructureChanged(r);
		messageChanged(r);
		
		managerTree.addTreeSelectionListener(treeSelectionListener);
	}

	protected void nodeStructureChanged(MutableTreeNode node) {
		DefaultTreeModel treeModel = (DefaultTreeModel) managerTree.getModel();
		treeModel.nodeStructureChanged(node);
		TreePath path = new TreePath(treeModel.getPathToRoot(manager.getCurrentNode()));
		managerTree.setSelectionPath(path);
		managerTree.expandPath(path);
	}

//	public ProjectManager getProjectDirector() {
//		return manager;
//	}
	

	public StorableResource  getRoot() {
		return manager.getRoot();
	}
	
	protected void messageChanged(StorableResource r) {
		firePropertyChange("message", null, ResourcesMessager.getMessage(r));
	}
	 
	public ProjectManager getManager() {
		return manager;
	}
	
	
	
	
	private ListModel getImageListModel() {
		return listModel;
	}

	 
 
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        managerTree = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setLayout(new java.awt.BorderLayout());

        managerTree.setBorder(javax.swing.BorderFactory.createTitledBorder(r.getString("project")));
        managerTree.setModel(new DefaultTreeModel(getRoot()));
        managerTree.setCellRenderer(new ResourcesTreeCellRenderer());
        managerTree.setEditable(true);
        managerTree.setRootVisible(false);
        managerTree.setShowsRootHandles(true);
        jScrollPane1.setViewportView(managerTree);

        jTabbedPane1.addTab(r.getString("project"), jScrollPane1);

        jList1.setModel(getImageListModel());
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setCellRenderer(listModel.getCellRenderer());
        jList1.setDragEnabled(true);
        jList1.setFixedCellHeight(26);
        jList1.setLayoutOrientation(javax.swing.JList.VERTICAL_WRAP);
        jList1.setVisibleRowCount(0);
        jScrollPane2.setViewportView(jList1);

        jTabbedPane1.addTab(r.getString("library"), jScrollPane2);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        jTabbedPane1.getAccessibleContext().setAccessibleName(r.getString("project"));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTree managerTree;
    // End of variables declaration//GEN-END:variables

}

