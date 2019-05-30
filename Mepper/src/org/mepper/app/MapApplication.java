/* MepperApplication.java 1.0 2010-2-2
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
package org.mepper.app;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.FileLock;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.mepper.app.action.AboutAction;
import org.mepper.app.action.ButtonFactory;
import org.mepper.app.action.CopyAction;
import org.mepper.app.action.CutAction;
import org.mepper.app.action.HelpAction;
import org.mepper.app.action.NewMapAction;
import org.mepper.app.action.PasteAction;
import org.mepper.app.action.TileManagerAction;
import org.mepper.resources.DefaultProjectManager;
import org.mepper.resources.DefaultResourcesManager;
import org.mepper.utils.PrefixFilenameFilter;
import org.zhiwu.app.AppManager;
import org.zhiwu.app.Application;
import org.zhiwu.app.ApplicationConstant;
import org.zhiwu.app.DefaultApplication;
import org.zhiwu.app.Model;
import org.zhiwu.app.View;
import org.zhiwu.app.action.ExitAction;
import org.zhiwu.app.action.SaveAction;
import org.zhiwu.app.action.VersionAction;
import org.zhiwu.app.config.AppPreference;
import org.zhiwu.utils.AppLogging;
import org.zhiwu.utils.AppResources;
import org.zhiwu.utils.FileUtils;
import org.zhiwu.utils.SystemTrayProxy;

/**
 * <B>MepperApplication</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-8 created
 * @since org.mepper.app Ver 1.0
 * 
 */
public class MapApplication extends DefaultApplication {
	private static String lockPath="resources/.lock";
	private FileLock lock;
	
	public MapApplication() {
		checkOneInstance();
		initWelcome();
	}
	
	@Override
	public void init() {
            super.init();
//            config.addItem(MapConfigItem.class);
//            config.addItem(ResourcesConfigItem.class);

            ///   init resource
            setState(ApplicationConstant.READING_DATA);
            DefaultResourcesManager.getInstance().init();
            DefaultProjectManager.getInstance().init();
    }
	 
	private void initWelcome() {
		final WelcomeView welcome=new WelcomeView(10);
		welcome.launch();
		addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue().equals(ApplicationConstant.STATE_STARTED)) {
					welcome.dispose();
					MapApplication.this
							.removePropertyChangeListener(this);
				} else {
					String name=evt.getNewValue().toString();
					welcome.setText(getResource().getString(name));
//					for(int i=0;i<50000;i++){
//						String s=i+"";
//						System.out.println(s);
//					}
				}				
			}
		});
	}

	protected boolean checkOneInstance() {
		if(!isLocked()){// no application was run.
			return true;
		}
		
		// there is a application runing.
		// so, attention it.
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					JOptionPane.showMessageDialog(null,
							"application already running...","",JOptionPane.WARNING_MESSAGE);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}finally{
			System.exit(0);
		}
		return false;
	}
	
	private boolean isLocked() {
		try {// if the lock was null, anthor program was locking the file.
			lock=new FileOutputStream(new File(lockPath)).getChannel().tryLock();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		// do not close stream or file will be unlocked
		return lock==null;
	}
	
	@Override
	protected void initModelActions() {
		Model model = getModel();

//		model.putAction(OpenAction.ID, new OpenAction(this));
//		model.putAction(CloseAction.ID, new CloseAction(this));
		model.putAction(ExitAction.ID, new ExitAction(this));

		model.putAction(CopyAction.ID, new CopyAction(this));
		model.putAction(CutAction.ID, new CutAction(this));
		model.putAction(PasteAction.ID, new PasteAction(this));

//		model.putAction(FillAction.ID, new FillAction(this));
//		model.putAction(PenAction.ID, new PenAction(this));

		model.putAction(SaveAction.ID, new SaveAction(this));
//		model.putAction(SaveAsAction.ID, new SaveAsAction(this));

//		model.putAction(AboutAction.ID, new AboutAction(this));
		model.putAction(HelpAction.ID, new HelpAction(this));

		model.putAction(TileManagerAction.ID, new TileManagerAction(this));
		model.putAction(AboutAction.ID, new AboutAction(this));
	} 
	
	@Override
	protected void createToolBar() {
		if(toolbar != null){
			toolbar.removeAll();
			window.getContentPane().remove(toolbar);
		}
		
		View v=getView();
		if(v.getComponenet().getClientProperty("view.toolbar") instanceof JToolBar){
			toolbar=(JToolBar) v.getComponenet().getClientProperty("view.toolbar");
		}else {
			v.getComponenet().putClientProperty("view.toolbar", toolbar);
		}
//		if(v instanceof MapView){
//			MapView view=(MapView) getView();
			// craete toolbar for Tools
//			toolbar=(JToolBar) view.getClientProperty("view.toolbar");
//			toolbar=
//			super.createToolBar();;
//			view.putClientProperty("view.toolbar", toolbar);
//			return;
//		}
		List<JToolBar> list = new ArrayList<JToolBar>();
		list.addAll(ButtonFactory.createToolBar(this));
		list.add(v.getToolbar());
		for(JToolBar b:list){
			toolbar.add(b,0);
		}
		
		
	}
	
	
	@Override
	protected JMenu createFileMenu() {
		AppResources resource =getResource();
		Model model = getModel();
		JMenu menu = new JMenu();

		resource.configMenu(menu, "file");

		menu.add(model.getAction(NewMapAction.ID));
//		menu.add(model.getAction(OpenAction.ID));
		menu.addSeparator();
		menu.add(model.getAction(SaveAction.ID));
//		menu.add(model.getAction(SaveAsAction.ID));
//		menu.add(model.getAction(CloseAction.ID));
		menu.addSeparator();
		menu.add(model.getAction(ExitAction.ID));

		return menu;
	}
	
//	protected JMenu createToolMenu() {
//		AppResources resource = getResource();
//		JMenu menu=new JMenu();
//		resource.configMenu(menu, "tool");
//		
////		for(Iterator<String> i=model.viewIterator();i.hasNext();){
////			menu.add(new ViewSwitchAction(this,i.next()));
////		}
//		menu.add(model.getAction(ConfigAction.ID));
//		return menu;
//	}
	
	@Override
	protected JMenu createHelpMenu() {
		AppResources resource = getResource();
		Model model = getModel();
		JMenu menu = new JMenu();

		resource.configMenu(menu, "help");

		menu.add(getView().getAction(VersionAction.ID));
		menu.add(model.getAction(AboutAction.ID));
		menu.addSeparator();
		menu.add(model.getAction(HelpAction.ID));

		return menu;
	}
	
	
	@Override
	public MapModel getModel() {
		return (MapModel)super.getModel();
	}
	
	@Override
	public void start() {
		super.start();
		startTray();
		
		
		// for test
//		model.getAction(TileManagerView.class.getName()).actionPerformed(null);
	}

	private void startTray() {
		SystemTrayProxy tray = new SystemTrayProxy();
		AppResources r=getResource();
		r.configTray(tray);
		
		PopupMenu popup=new PopupMenu();
		MenuItem item=new MenuItem();
		item.setLabel(r.getString("exit"));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getModel().getAction(ExitAction.ID).actionPerformed(null);
			}
		});
		popup.add(item);
		tray.setPopup(popup);
		
		try {
			tray.start(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1 &&
							e.getClickCount() ==1) {
						Application app = MapApplication.this;
						app.setVisible(!app.isVisible());
					}
				}
			});
		} catch (Exception e1) {
			AppLogging.handleException(e1);
		}		
	}
	
	
	
	@Override
	public void exit() {
		backupDatabase();
		saveData();
		
		// release lock
		try {
			lock.release();
		} catch (IOException e) {
			AppLogging.handleException(e);
		}
		super.exit();
	}


	private void saveData() {
		DefaultResourcesManager.getInstance().save();
    	DefaultProjectManager.getInstance().save();
    	
    	/// remove old data
    	removeOldBackupData();
	}
	
	private void backupDatabase() {
		AppPreference pref= AppManager.getPreference(MapApplication.class.getName());
		String path =pref.get("database.path");
		SimpleDateFormat format=new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		String backup=path +"."+ format.format(Calendar.getInstance().getTime())+File.separator;
		File bkf = new File(backup);
		if( ! bkf.exists()){
			bkf.mkdirs();
		}
		
		// backup all resources
		File dataDirectory=new File(path);
		if(! dataDirectory.exists()){
			dataDirectory.mkdirs();
		}
		
		File[] files=dataDirectory.listFiles();
		for(File f: files){
			String name = f.getName();
			if(f.getName().startsWith(".")){
				continue;
			}
			File newFile = new File(backup+name);
			f.renameTo(newFile);
			FileUtils.delete(f);
		}		
	}
	
	private void removeOldBackupData() {
		AppPreference pref= AppManager.getPreference(MapApplication.class.getName());
		String path =pref.get("database.path");
		File[] files = new File(path).listFiles(new PrefixFilenameFilter("."));
		if(files.length <1){
			return;
		}
		SimpleDateFormat format=new SimpleDateFormat("yy-MM-dd HH-mm-ss");//TODO - :
		
		Calendar todayCalendar=createCalendar(0);
		Calendar yesterdayCalendar = createCalendar(-1);
		
		
		File yesterdayFile=null,todayFile=null;
		
		try {
			File file,tempFile;
			for (int i=0,j=files.length;i<j;i++) {
				file = files[i];
				Date date = format.parse(file.getName().substring(1));
				if (date.before(yesterdayCalendar.getTime())) {
					FileUtils.delete(file);
					continue;
				}
				Calendar fileCalendar = Calendar.getInstance();
				fileCalendar.setTime(date);
				
				tempFile = compare(yesterdayCalendar, yesterdayFile, fileCalendar,file) ;
				yesterdayFile =   tempFile == null?yesterdayFile:tempFile;
				tempFile = compare(todayCalendar, todayFile, fileCalendar, file);
				todayFile =   tempFile == null?todayFile:tempFile;

			}
		} catch (ParseException e1) {
			AppLogging.handleException(e1);
		}		
	}

	private Calendar createCalendar(int value) {
		Calendar yesterdayCalendar =Calendar.getInstance();
		yesterdayCalendar.add(Calendar.DAY_OF_YEAR, value);
		yesterdayCalendar.set(Calendar.HOUR_OF_DAY, 0);
		yesterdayCalendar.set(Calendar.MINUTE, 0);
		yesterdayCalendar.set(Calendar.SECOND, 0);
		return yesterdayCalendar;
	}
	

	private File compare(Calendar calendar, File file,
			Calendar otherCalendar, File otherFile) {
		if(calendar.get(Calendar.DAY_OF_YEAR) == otherCalendar.get(Calendar.DAY_OF_YEAR)){
			if(calendar.before(otherCalendar)){
				cloneCalendar(calendar , otherCalendar);
				if(file!=null){
					FileUtils.delete(file);
				}
				return otherFile;
			}else {
				FileUtils.delete(otherFile);
				return file;
			}
		}
		return null;
	}

	private void cloneCalendar(Calendar target, Calendar source) {
		target.setTimeInMillis(source.getTimeInMillis());
	}


	
	
	
	
}

