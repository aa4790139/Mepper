/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ThumbnailPanel.java
 *
 * Created on 2010-12-16, 17:24:33
 */

package org.mepper.editor.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import org.mepper.editor.DoubleBufferable;
import org.mepper.editor.EditorView;

public class SnapshotPanel extends javax.swing.JPanel {
	/** The target to draw snapshot. */
	private JComponent sourceComponent;
	private DoubleBufferable source;
	/** The parent of the source. */
	private JScrollPane parent;
	private BufferedImage snapshot;
	
	private final static Rectangle DefaultBounds=new Rectangle(0, 0, 100, 100);
	/** the snapshot source */
	private Rectangle snapBounds=DefaultBounds;
	
	/** current editing area indicator. */
	private Rectangle indicator;
	
	/** The zoom factor. */
	private double zoomFactor;

	private final PropertyChangeListener l=new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if(evt.getPropertyName().equals(EditorView.IMAGE_CHANGED_LISTENER)){
				drawThumbnail();
			}
		}
	};
	private final MouseListener mouse=new MouseAdapter() {
		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
			if(snapBounds.contains(e.getPoint())){
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				indicateParent(e);
			}else {
				setCursor(Cursor.getDefaultCursor());
			}
		}
	};

	private final MouseMotionListener montion=new MouseMotionAdapter() {
		@Override
		public void mouseDragged(java.awt.event.MouseEvent e) {
			if(snapBounds.contains(e.getPoint())){
				indicateParent(e);
			}else {
				setCursor(Cursor.getDefaultCursor());
			}
		};
		@Override
		public void mouseMoved(java.awt.event.MouseEvent e) {
			if(snapBounds.contains(e.getPoint())){
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}else {
				setCursor(Cursor.getDefaultCursor());
			}
		};
	};
	private final AdjustmentListener hl=
		new AdjustmentListener() { 
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			indicator.x=snapBounds.x+(int)(e.getValue()* zoomFactor);
			repaint();
		}
	};
	
	private final AdjustmentListener vl=
		new AdjustmentListener() {
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			// move the rectange.
			indicator.y=snapBounds.y+(int)(e.getValue()* zoomFactor);
			repaint();
		}
	};
	
	private final ComponentListener resizeListener =new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent e) {
			resizeIndicator();
		}
	};
	
    public SnapshotPanel() {
        initComponents();
        setOpaque(false);
        
        addComponentListener(new ComponentAdapter() {
        	@Override
        	public void componentResized(ComponentEvent e) {
        		resizeSnapshot();
        		resizeIndicator();
        		drawThumbnail();
        	}
		});
        
        addMouseListener(mouse);
        addMouseMotionListener(montion);
    }
    
	public void guard( DoubleBufferable source) {
		if(source == null){
			return;
		}
		// caculate zoom factor.
		setSource(source);
		
		// todo: guarante no exception throws.  //		if(p.getParent().getParent().getClass().equals(JScrollPane.class)){
		handleScrollbar( );		
		
//		source.removePropertyChangeListener(l);// remove the listener,in case the
//		source.addPropertyChangeListener(l);
		drawThumbnail();
	}

	
	private void setSource(DoubleBufferable bufferable) {
		this.source= bufferable;
		this.sourceComponent= source.getComponent();
		sourceComponent.removePropertyChangeListener(l);
		sourceComponent.addPropertyChangeListener(l);
		
		resizeSnapshot();
	}

	/**
	 * caculate the zoom factor and resize snapshot.
	 */
	protected void resizeSnapshot() {
		if(sourceComponent == null){
			return;
		}
		
		double h=getHeight(),w=getWidth();
		double H=sourceComponent.getHeight(),W=sourceComponent.getWidth();
		double r1, r2;// ratio is the really ratio, r1 and r2 are temo .. for caculate.
		r1 = h / H;
		r2 = w / W;
	
		zoomFactor=r1<r2?r1:r2;
		int x,y,width ,height;
		width = (int) (W*zoomFactor);
		width = width <1?1:width;
		height= (int) (H*zoomFactor);
		height = height<1?1:height;
		x=(int) ((w-width)/2);
		y=(int) ((h-height)/2);
		
		snapBounds=new Rectangle(x, y, width, height);
	}

	private void handleScrollbar() {
		JScrollPane scrollPane=(JScrollPane) source.getComponent().getParent().getParent()	;	

		if(parent !=null ){
//			if(parent != scrollPane){
				parent.getHorizontalScrollBar().removeAdjustmentListener(hl);
				parent.getVerticalScrollBar().removeAdjustmentListener(vl);
				parent.removeComponentListener(resizeListener);
//			}
		}
		// create current area editor indicator by zoom factor.
		parent=scrollPane;
		resizeIndicator();
		parent.getHorizontalScrollBar().addAdjustmentListener(hl);
		parent.getVerticalScrollBar().addAdjustmentListener(vl);
		parent.addComponentListener(resizeListener);
	}

	private void resizeIndicator() {
		if(parent == null){
			return;
		}
		Rectangle r=parent.getBounds();
		JScrollBar HBar=parent.getHorizontalScrollBar();
		JScrollBar VBar=parent.getVerticalScrollBar();
		indicator = new Rectangle(
				(int)(HBar.getValue() * zoomFactor)+snapBounds.x, 
				(int)(VBar.getValue() * zoomFactor)+snapBounds.y, 
				(int)(r.width* zoomFactor), 
				(int)(r.height * zoomFactor));
		reviseIndicator(indicator);
		repaint();
	}
	
	private void reviseIndicator(Rectangle r) {
		if((r.x+r.width)>(snapBounds.x+snapBounds.width)){
			r.width=snapBounds.x+snapBounds.width-r.x;
		}
		if((r.y+r.height)>(snapBounds.y+snapBounds.height)){
			r.height=snapBounds.y+snapBounds.height-r.y;
		}
	}

	/**
	 * draw the view. create thumbnail and call repaint();
	 */
	public void drawThumbnail() {
		if (sourceComponent != null) {
			snapshot = new BufferedImage(snapBounds.width, snapBounds.height,
					BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g = snapshot.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.drawImage(source.getImage(), 0, 0, snapBounds.width,
					snapBounds.height, this);
		}
		validate();
		repaint();
	}

	protected void indicateParent(java.awt.event.MouseEvent e) {
		if(parent == null){
			return;
		}
		Dimension d=parent.getSize();
		JScrollBar HBar=parent.getHorizontalScrollBar();
		JScrollBar VBar=parent.getVerticalScrollBar();
		HBar.setValue((int)((e.getX()-snapBounds.x)/zoomFactor) -d.width/2);
		VBar.setValue((int)((e.getY()-snapBounds.y)/zoomFactor) -d.height/2);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(sourceComponent == null){
			return;
		}
		
		if (indicator == null) {
			indicator = new Rectangle(0, 300, 300, 300);
		} 
		
		
		Graphics2D g2 = (Graphics2D) g.create();
		
		g2.setColor(Color.WHITE);
		g2.fill(snapBounds);
		
		g2.setColor(Color.BLACK);
		g2.draw(snapBounds);
		
		g.translate(snapBounds.x, snapBounds.y);
		g.drawImage(snapshot, 0, 0, this);

		g2.setColor(Color.RED);
		g2.draw(indicator);
	}
	
	

	
	

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
	
}
