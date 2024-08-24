/*
GNU Lesser General Public License

ImageFileDialog
Copyright (C) 2010 Howard Kistler

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package r9.quiz.component;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
 




import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import r9.quiz.Constants;
import r9.quiz.util.Translatrix;
 

/** Class for providing a dialog that lets the user load a local image and specify its attributes
  */
public class ImageFileDialog extends JDialog implements ActionListener
{
	private String imageDir    = new String();
	private String[] imageExts = new String[0];
	private String imageDesc   = new String();
	private File   imageFile   = (File)null;
	private String imageSrc    = new String();
	private boolean isBackground    = true;
	private String imageWidth  = new String();
	private String imageHeight = new String();

	private JOptionPane jOptionPane;

	private final JLabel     jlblSrc    = new JLabel("----");
	private final JCheckBox jtxfAlt    = new JCheckBox("图片是卡片背景（占据整个卡片）", true);
	private final JCheckBox jtxfFullSize    = new JCheckBox("保持图片全屏大小", false);
	private final JTextField jtxfWidth  = new JTextField(3);
	private final JTextField jtxfHeight = new JTextField(3);
	private final JButton jbtnBrowse = new JButton("Browse");
	
	
	public boolean keepOriginalSize = false;

	public ImageFileDialog(Frame parent, String imgDir, String[] imgExts, String imgDesc, String imgSrc, String title, boolean bModal)
	{
		super(parent, title, bModal);

		this.imageDir  = imgDir;
		this.imageExts = imgExts;
		this.imageDesc = imgDesc;
		this.imageSrc  = imgSrc;

		jbtnBrowse.getModel().setActionCommand("browse"); jbtnBrowse.addActionListener(this);
		final Object[] buttonLabels = { Translatrix.getTranslationString("DialogAccept"), Translatrix.getTranslationString("DialogCancel") };
		Object[] panelContents = {
			"图像来源",    jlblSrc,    jbtnBrowse,
			"图片用途",    jtxfAlt, jtxfFullSize,
			"图像宽度（可选）, 最大不超过" + Constants.CARD_WIDTH,  jtxfWidth,
			"图像高度（可选）, 最大不超过" + Constants.CARD_HEIGHT, jtxfHeight
		};
		jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);

		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we)
			{
				jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		jOptionPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				String prop = e.getPropertyName();
				if(isVisible() 
					&& (e.getSource() == jOptionPane)
					&& (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
				{
					Object value = jOptionPane.getValue();
					if(value == JOptionPane.UNINITIALIZED_VALUE)
					{
						return;
					}
					if(value.equals(buttonLabels[0]))
					{
						imageSrc    = jlblSrc.getText();
						isBackground    = jtxfAlt.isSelected();
						keepOriginalSize = jtxfFullSize.isSelected();
						imageWidth  = jtxfWidth.getText();
						imageHeight = jtxfHeight.getText();
						setVisible(false);
					}
					else if(value.equals(buttonLabels[1]))
					{
						imageSrc    = "";
						isBackground    =  true;
						keepOriginalSize = false;
						imageWidth  = "";
						imageHeight = "";
						setVisible(false);
					}
					else
					{
						jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					}
				}
			}
		});
		 pack();
		
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getActionCommand().equals("browse"))
		{
			imageFile = browseForImage();
			if(imageFile != null)
			{
				jlblSrc.setText(imageFile.getName());
			}
		}
	}

	public File   getImageFile()   { return imageFile; }
	public String getImageSrc()    { return imageSrc; }
	public boolean getImageType()    { return isBackground; }
	public boolean keepFullSize()    { return this.keepOriginalSize; }
	public String getImageWidth()  { return imageWidth; }
	public String getImageHeight() { return imageHeight; }

	public File browseForImage()
	{
		ImageFileChooser jImageDialog = new ImageFileChooser(imageDir);
		jImageDialog.setDialogType(JFileChooser.CUSTOM_DIALOG);
		jImageDialog.setFileFilter(new MutableFilter(imageExts, imageDesc));
		jImageDialog.setDialogTitle(r9.quiz.util.Translatrix.getTranslationString("ImageDialogTitle"));
		int optionSelected = JFileChooser.CANCEL_OPTION;
		optionSelected = jImageDialog.showDialog(this, Translatrix.getTranslationString("Insert"));
		if(optionSelected == JFileChooser.APPROVE_OPTION)
		{
			return jImageDialog.getSelectedFile();
		}
		return (File)null;
	}

	public String getDecisionValue()
	{
		return jOptionPane.getValue().toString();
	}
	
	public static File browseForImageOnly()
	{
		ImageFileChooser jImageDialog = new ImageFileChooser( );
		jImageDialog.setDialogType(JFileChooser.CUSTOM_DIALOG);
		jImageDialog.setFileFilter(new MutableFilter(new String[]{"jpg"}, "我们只接受JPG图片"));
		jImageDialog.setDialogTitle(Translatrix.getTranslationString("ImageDialogTitle"));
		int optionSelected = JFileChooser.CANCEL_OPTION;
		optionSelected = jImageDialog.showDialog(null, Translatrix.getTranslationString("Insert"));
		if(optionSelected == JFileChooser.APPROVE_OPTION)
		{
			return jImageDialog.getSelectedFile();
		}
		return (File)null;
	}
}
