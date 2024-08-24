package r9.quiz.component;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.media.AudioClip;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
 


import r9.quiz.CourseCreatingManager;
import r9.quiz.cards.CardPage;
import r9.quiz.util.Utils;

 

public class AudioControlPane extends JPanel{
	 
	private static final long serialVersionUID = 1L;
	
	private JButton playButton;
	private JButton stopButton;
	private JLabel  timerLabel;
 
	private   AudioClip audioClip;
	private Timer timer;
	private long startTime;

	private JButton jbtnVoice;

	private JButton jbtnVoiceDelete;
	 
	
	public AudioControlPane(){
	 
	    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);
	    add(Box.createRigidArea(new Dimension(0, 5)));
	     add( new JLabel("播放语音文件： "));
	    add(Box.createRigidArea(new Dimension(0, 5)));
		playButton = new JButton("播放");
		 add( playButton );
		playButton.addActionListener(new ActionListener(){
        	@Override
			public void actionPerformed(ActionEvent e) {
				playButton.setEnabled(false);
				CardPage card = CourseCreatingManager.sharedInstance.getCurrentPage();
				URL url = null;
				try {
					String filename = CourseCreatingManager.sharedInstance.getCourseFolder() 
							+ File.separator + card.getVoiceFileName();
					url = new File(filename).toURI().toURL();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				}
				audioClip = new AudioClip(url.toString());
				audioClip.play();
				startTime = System.currentTimeMillis() ;
				timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask(){
                 	@Override
					public void run() {
                 		if ( audioClip.isPlaying() ){
						    long time = System.currentTimeMillis() ;
						    double diff = (time - startTime) / 1000.0; 
					    	timerLabel.setText("时间 ： " + diff);
                 		} else {
                 			 if ( timer != null ){
         						timer.cancel();
         						timer = null;
         					}
         				     timerLabel.setText("时间 ： ");
                 		}
					}}, 1000, 500);
			}});
		add(Box.createRigidArea(new Dimension(0,5)));
		stopButton = new JButton("停止");
		stopButton.addActionListener(new ActionListener(){
        	@Override
			public void actionPerformed(ActionEvent e) {
				 playButton.setEnabled(true);
				 audioClip.stop();
				 if ( timer != null ){
						timer.cancel();
						timer = null;
					}
				 timerLabel.setText("时间 ： ");
				
			}});
		 add( stopButton );
		 add(Box.createRigidArea(new Dimension(0,5)));
		timerLabel = new JLabel("");
		 add( timerLabel );
		
		 add(Box.createRigidArea(new Dimension(0,5)));
		 
		 jbtnVoice = new JButton("");
	      jbtnVoice.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				File whatFile = Utils.getFileFromChooser(null, ".",
					    JFileChooser.OPEN_DIALOG, new String[]{"mp3"}, "选择语音文件");
			    if ( whatFile != null ){
			 		String  filename = CourseCreatingManager.sharedInstance.saveVoiceFile(  whatFile , true);
					if ( filename == null ){
						JOptionPane.showMessageDialog(null,
								"引入文件： " +  whatFile.getName() + " 失败" );
					} else {
						nodeChanged( );
					}
		 	 }
			}});
	 	jbtnVoice.setText("导入");
		jbtnVoice.setToolTipText("插入声音文件");
	    add(jbtnVoice);
	    
		jbtnVoiceDelete = new JButton();
		jbtnVoiceDelete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				 CourseCreatingManager.sharedInstance.deleteVoice();
				 nodeChanged( );
			}});
		jbtnVoiceDelete.setText("删除");
		jbtnVoiceDelete.setToolTipText("删除声音文件");
		add(jbtnVoiceDelete);
		 
		 
		
		playButton.setEnabled(false);
		stopButton.setEnabled(false); 
	}
	
 
	
	public void nodeChanged( ){
		 
		CardPage card = CourseCreatingManager.sharedInstance.getCurrentPage();
		if ( card == null )
			return;
		String filename = card.getVoiceFileName();
		playButton.setEnabled(filename != null);
		stopButton.setEnabled(filename != null);
	 	if ( audioClip != null ){
			audioClip.stop();
			audioClip = null;
		}
		
		if ( timer != null ){
			timer.cancel();
			timer = null;
		}
	}
}
