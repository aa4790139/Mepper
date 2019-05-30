/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewCustomTileDialog.java
 *
 * Created on 2011-4-27, 18:08:48
 */

package org.mepper.resources.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.mepper.editor.tile.CustomTile;
import org.mepper.resources.DefaultResource;
import org.mepper.resources.ResourcesManager;
import org.zhiwu.app.AppDialog;
import org.zhiwu.app.AppManager;
import org.zhiwu.app.Application;
import org.zhiwu.app.action.AbsAction;
import org.zhiwu.utils.AppLogging;
import org.zhiwu.utils.AppResources;
import org.zhiwu.utils.FileUtils;

/**
 * <B>NewCustomTileDialog</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-27 created
 * @since org.mepper.resource.gui Ver 1.0
 * 
 */
public class NewCustomTileDialog extends AppDialog {
	private File imageFile;
	private final ResourcesManager manager;
	private CustomTile tile;
	private final AppResources r= AppManager.getResources();
	private final Action addPropertyAction =new AbsAction("add") {
		@Override
		public void actionPerformed(ActionEvent e) {
			DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
			model.insertRow(jTable1.getRowCount(), new Object[]{"",""});
		}
	};
	
	private final Action deletePropertyAction = new AbsAction("delete") {
		@Override
		public void actionPerformed(ActionEvent e) {
			DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
			model.removeRow(jTable1.getSelectedRow());
		}
	};
	
	private final Action confirmAction =new AbsAction("confirm") {
		@Override
		public void actionPerformed(ActionEvent e) {
			confirm();
		}
	};
	
	private final Action colorChooserAction = new AbsAction("choose") {
		@Override
		public void actionPerformed(ActionEvent e) {
			Color color= JColorChooser.showDialog(NewCustomTileDialog.this, r.getString("color"), jLabel6.getBackground());
			if(color == null){
				return;
			}
			jLabel6.setBackground(color);
			jTextField4.setText("r=" + color.getRed() + ",g=" + color.getGreen() + ",b=" + color.getBlue() );
		}
	};
	
	private final Action fileChooserAction = new AbsAction("choose") {
		@Override
		public void actionPerformed(ActionEvent e) {
			File file=FileUtils.openFile(NewCustomTileDialog.this, JFileChooser.FILES_ONLY,
					new FileNameExtensionFilter(r.getString("image"), "jpg","png","psd","gif","jpeg","bmp"));
			if(file == null){
				return;
			}
			
			imageFile = file;
			jTextField5.setText(imageFile.getAbsolutePath());
		}
	};
	
	public NewCustomTileDialog(Application app, ResourcesManager manager,
			CustomTile tile) {
		super(app);
		this.manager = manager;
		this.tile = tile;
		initComponents();
		setTitle(r.getString("new.custom.tile.dialog.title"));

		initFiled();

	}

	protected void confirm() {
		messager.setText("");
		
		String name = jTextField2.getText().trim();
		if(name.length()==0){
			messager.setText(r.getString("name") +" "+r.getString("must.be.entered"));
			return;
		}
		
		String value =jTextField3.getText().trim();
		if (value.length() ==0 ) {
			messager.setText(r.getString("value") +" "+r.getString("must.be.entered"));
			return;
		}
		String desc =jTextField1.getText().trim();
		Color color =jLabel6.getBackground();
		
		for(String n:tile.propertyNames()){
			tile.removeProperty(n);
		}
		
		int p = jTable1.convertColumnIndexToView(0);
		int q= jTable1.convertColumnIndexToView(1);
		for(int i=0,j=jTable1.getRowCount();i<j;i++){
			String pn =jTable1.getValueAt(i, p).toString();
			String pv= jTable1.getValueAt(i, q).toString();
			if(pn.startsWith("_")){
				messager.setText(r.getString("error.data.format")+": "+pn);
			}
			if(pn.trim().equals("") || pv.trim().equals("")){
				messager.setText(r.getString("name")+"/"+r.getString("value")+" "+r.getString("must.be.entered"));
				return;
			}
			if(tile.getProperty(pn)!= null){
				messager.setText(pn + " "+ r.getString("already.existed"));
				return;
			}
			tile.putProperty(pn, pv);
		}
		
		tile.setValue(value);
		tile.setColor(color);
		
		if (imageFile != null) {
			try {
				tile.setImage(ImageIO.read(imageFile));
			} catch (IOException e) {
				AppLogging.handleException(e);
			}
		}

		tile.setName(name);
		tile.setDescription(desc);
		
		if(tile.getID() ==0 ){
			DefaultResource tileResources = new DefaultResource(tile);
			manager.createResourceID(tileResources);
			manager.addResourceChild(tileResources);
		}
		
		fireOptionSelected(COMFIRM_OPTION); 		
	}

	private void initFiled() {
		if(tile == null){
			tile = new CustomTile();
		}
		
		jTextField2.setText(tile.getName());
		jTextField3.setText(tile.getValue());
		jTextField1.setText(tile.getDescription());
		
		Color color=tile.getColor();
		jLabel6.setBackground(color);
		jTextField4.setText("r=" + color.getRed() + ",g=" + color.getGreen() + ",b=" + color.getBlue() );
	
		DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
		for(String name:tile.propertyNames()){
			if(name.startsWith("_")){
				continue;
			}
			model.insertRow(0, new String[]{name,tile.getProperty(name)});
			tile.removeProperty(name);
		}
	
	}

	/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        messager = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setText(r.getString("name"));

        jLabel3.setText(r.getString("value"));

        jLabel1.setText(r.getString("description"));

        jLabel4.setText(r.getString("color"));

        jTextField4.setEditable(false);
        jTextField4.setText("0,204,0");

        jButton4.setAction(colorChooserAction);
        jButton4.setText(r.getString("edit"));

        jLabel5.setText(r.getString("image"));

        jTextField5.setEditable(false);

        jButton5.setAction(fileChooserAction);
        jButton5.setText(r.getString("browse"));

        jLabel6.setBackground(new java.awt.Color(255, 0, 0));
        jLabel6.setForeground(new java.awt.Color(0, 204, 0));
        jLabel6.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(56, 56, 56)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton4)
                            .addComponent(jButton5)))
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4)
                            .addComponent(jLabel4)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, r.getString("properties"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), new java.awt.Color(51, 51, 51))); // NOI18N
        jPanel2.setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setOrientation(1);
        jToolBar1.setRollover(true);

        jButton1.setAction(addPropertyAction);
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        jButton2.setAction(deletePropertyAction);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

        jPanel2.add(jToolBar1, java.awt.BorderLayout.LINE_END);

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                r.getString("name"), r.getString("value")
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jButton3.setAction(confirmAction);
        jButton3.setText(r.getString("confirm"));

        messager.setForeground(new java.awt.Color(204, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                    .addComponent(messager)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(messager))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel messager;
    // End of variables declaration//GEN-END:variables

}
