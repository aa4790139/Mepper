/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LibraryPanel.java
 *
 * Created on 2011-4-8, 15:28:44
 */

package org.mepper.resources.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.mepper.editor.map.DefaultLayer;
import org.mepper.editor.map.Map;
import org.mepper.editor.map.MapFactory;
import org.mepper.editor.tile.CustomTile;
import org.mepper.editor.tile.DefaultTile;
import org.mepper.editor.tile.Tile;
import org.mepper.editor.tile.TileFactory;
import org.mepper.gui.ResourcesTreeCellRenderer;
import org.mepper.io.Storable;
import org.mepper.resources.DefaultResourcesManager;
import org.mepper.resources.LibraryResource;
import org.mepper.resources.ResourcesEvent;
import org.mepper.resources.ResourcesListener;
import org.mepper.resources.ResourcesManager;
import org.mepper.resources.ResourcesMessager;
import org.mepper.resources.StorableResource;
import org.zhiwu.app.AppDialog;
import org.zhiwu.app.AppDialogListener;
import org.zhiwu.app.AppManager;
import org.zhiwu.app.Application;
import org.zhiwu.app.DialogEvent;
import org.zhiwu.app.View;
import org.zhiwu.app.action.AbsAction;
import org.zhiwu.utils.AppOptionPanel;
import org.zhiwu.utils.AppResources;


/**
 *
 * @author root
 */
public class LibraryPanel extends javax.swing.JPanel {
	protected static final String SELECTION_CHANGED_PROPERTY = "selection.changed";
	public static final String CURRENT_NODE_CHANGED = "current changed";
	private final AppResources r=AppManager.getResources();
	private ResourcesManager manager;
	private View parent;
	
	private final TreeSelectionListener treeSelectionListener =new TreeSelectionListener() {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			StorableResource  newValue = (StorableResource) e.getPath().getLastPathComponent();
			manager.setCurrentNode(newValue);
			messageChanged(newValue);
		}
	};

    /** Creates new form LibraryPanel */
    public LibraryPanel() {
//    	init();
    }
    
    public void init(View view) {
    	this.parent =view;
    	
		initResourceManager();
		initComponents();
		initListeners();
//		nodeStructureChanged(getRoot());
		selectionNode();
	}

	private void selectionNode() {
		StorableResource r=getRoot().getChild(0);
		if(r != null){
			manager.setCurrentNode(r);
		}
	}

	private void initListeners() {
		managerTree.getSelectionModel().addTreeSelectionListener(treeSelectionListener);
		
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
					Storable sp =r.getSource();
					
					if( sp instanceof DefaultTile){
						Tile t=  TileFactory.createCandidateTile((Tile) sp);
						BufferedImage image = t.getImage();
						
						Map map= MapFactory.getDefaultMap();
						map.setName(r.getName());
						map.setTileStep(t.tileWidth(),t.tileHeight());
						map.setExtension(1, 1);
						map.setSize(image.getWidth(), image.getHeight());
						map.addLayer(new DefaultLayer(), 0);
								
						map.addTile(new int[][]{{0,0}},new Tile[]{t});
						firePropertyChange(SELECTION_CHANGED_PROPERTY, null, map);
						return;
					}
					if(sp instanceof CustomTile){
						final Application app=parent.getApplication();
						app.setEnable(false);
						final AppDialog dialog=new NewCustomTileDialog(app,manager,(CustomTile) sp);
						dialog.showDialog(new AppDialogListener(){
							@Override
							public void optionSelection(DialogEvent evt) {
								dialog.dispose();// other option
								app.setFoucs(); 
								app.setEnable(true);
							}
						}); 
					}
				}
				
				if (SwingUtilities.isRightMouseButton(e)) {
					TreePath path= managerTree.getSelectionPath();
					if(path == null){
						return;
					}
					handlePopupMenu(e);
				}
			}
		});
	}
	private void handlePopupMenu(MouseEvent e) {
		Point p=e.getPoint();
		JPopupMenu menu = new JPopupMenu();
		final StorableResource child = manager.getCurrentNode();
		
		Action deleteAction = new AbsAction("delete") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(AppOptionPanel.showConfirmDeleteDialog(LibraryPanel.this, child)){
					manager.setCurrentNode(child.getParentResource());
					manager.removeResource(child);
				}
			}
		};
		deleteAction.setEnabled(child.getParentResource() != null);
		menu.add(deleteAction);
		
		
		Action propertyAction = new AbsAction("property") {
			@Override
			public void actionPerformed(ActionEvent e) {
				final Application app= parent.getApplication();
				app.setEnable(false);
				final AppDialog dialog = new PropertyDialog(app, child);
				dialog.showDialog(new AppDialogListener() {
					@Override
					public void optionSelection(DialogEvent evt) {
						dialog.dispose();
						app.setEnable(true);
					}
				});
			}
		};
		propertyAction.setEnabled(child != null);
		menu.add(propertyAction);
		
		

        menu.show(this, p.x, p.y);
	}
	

	private void cellValueChanged(DefaultCellEditor e){
		TreePath path=managerTree.getSelectionPath();
		StorableResource r=(StorableResource) path.getLastPathComponent();
		r.setName(e.getCellEditorValue().toString());
	}

	private void initResourceManager() {
		manager = DefaultResourcesManager.getInstance();
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
		managerTree.getSelectionModel().removeTreeSelectionListener(treeSelectionListener);
		
		StorableResource r=e.getReousrce();
		nodeStructureChanged(r);
		messageChanged(r);
		
		managerTree.getSelectionModel().addTreeSelectionListener(treeSelectionListener);
	}

	
	protected void nodeStructureChanged(MutableTreeNode node) {
		DefaultTreeModel treeModel = (DefaultTreeModel) managerTree.getModel();
		treeModel.nodeStructureChanged(node);
		TreePath path = new TreePath(treeModel.getPathToRoot(manager.getCurrentNode()));
		managerTree.expandPath(path);
		managerTree.setSelectionPath(path);
	}
	public ResourcesManager getManager() {
		return manager;
	}
	
	 

	/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        managerTree = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        managerTree.setBorder(javax.swing.BorderFactory.createTitledBorder(r.getString("tile.manager.view.tree.tiles")));
        managerTree.setModel(new DefaultTreeModel(getRoot()));
        managerTree.setCellRenderer(new ResourcesTreeCellRenderer());
        managerTree.setEditable(true);
        managerTree.setRootVisible(false);
        managerTree.setShowsRootHandles(true);
        jScrollPane1.setViewportView(managerTree);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
        add(jPanel1, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree managerTree;
    // End of variables declaration//GEN-END:variables

	public boolean existLibrary(StorableResource parent,String libraryName) {
		for(int i=0,j=parent.getChildCount();i<j;i++){
			StorableResource  node=(StorableResource ) parent.getChildAt(i);
			if(node.getName().equals(libraryName)){
				return true;
			}
		}
		return false;
	}


	public StorableResource  getRoot() {
		return manager.getRoot();
	}

	public void addLibrary(String libraryName) {
		manager.addResourceChild(new LibraryResource(libraryName));
	}
	
	protected void messageChanged(StorableResource r) {
		firePropertyChange("message", null, ResourcesMessager.getMessage(r));
	}
}

