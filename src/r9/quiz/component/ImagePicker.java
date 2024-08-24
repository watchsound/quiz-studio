package r9.quiz.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import r9.quiz.CourseCreatingManager;
import r9.quiz.util.Utils;

public class ImagePicker {
     public static final int BatchSize = 10;
	public static interface Callback{
		void select(File file);
		void delete();
	}
	
	public static void showPopupMenu(Component target, final Callback callback){
		List<File> files = CourseCreatingManager.sharedInstance.getAllImageFiles( );
		JPopupMenu menu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("删除当前图片");
		deleteItem.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				 callback.delete();
			}});
		menu.add(deleteItem);
		JMenuItem importItem = new JMenuItem("导入图片");
		importItem.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				File whatFile = Utils.browseForImage(".", JFileChooser.OPEN_DIALOG,
						new String[] {"jpg", "png"}, "选择图像");
				if (whatFile != null) {
					File f = CourseCreatingManager.sharedInstance.importImageFile(whatFile, false);
					if( f != null ){
					    callback.select(f);
					}
				}
			}});
		menu.add(importItem);
		
		
		menu.addSeparator(); 
		addMenuItem(menu, files, 0, callback);
		menu.show(target, 0, 0);
	}
	private static void addMenuItem(Component container, List<File> files, int startIndex, final Callback callback){
		 if( startIndex >= files.size()) return;
		 for(int i = startIndex; i < startIndex + BatchSize && i <files.size(); i++){
			 final File file  = files.get(i);
			 JMenuItem aItem = new JMenuItem(file.getName());
			 aItem.addActionListener(new ActionListener(){ 
					public void actionPerformed(ActionEvent e) {
						 callback.select(file);
					}});
			 if( container instanceof JPopupMenu)
			    ((JPopupMenu)container).add(aItem);
			 else if ( container instanceof JMenu )
				 ((JMenu)container).add(aItem);
		 }
		 if( startIndex + BatchSize <= files.size() ){
			 JMenu menu = new JMenu("...");
			 if( container instanceof JPopupMenu)
				    ((JPopupMenu)container).add(menu);
			 else if ( container instanceof JMenu )
					 ((JMenu)container).add(menu);
			 addMenuItem(menu, files, startIndex + BatchSize, callback);
		 }
	}
}
