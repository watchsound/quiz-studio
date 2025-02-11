/*
GNU Lesser General Public License

ImageFileChooserPreview
Copyright (C) 2000  Frits Jalvingh & Howard Kistler

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
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/** Class provides a preview window for the selected image file
  */
class ImageFileChooserPreview extends JComponent implements PropertyChangeListener
{
	private static final int previewWidth  = 100;
	private static final int previewHeight = 100;

	private ImageIcon imageThumb = null;
	private File imageFile = null;

	/** This class requires a file chooser to register with so this class will
	  * be notified when a new file is selected in the browser.
	  * @param JFileChooser that this preview window is used in.
	  */
	public ImageFileChooserPreview(JFileChooser parent)
	{
		setPreferredSize(new Dimension(previewWidth , previewHeight));
		parent.addPropertyChangeListener(this);
	}

	/** Loads a new image into the preview window, and scales it if necessary.
	  */
	public void loadImage()
	{
		if(imageFile == null)
		{
			imageThumb = null;
			return;
		}
		imageThumb = new ImageIcon(imageFile.getPath());

		// Check if thumb requires scaling
		if(imageThumb.getIconHeight() < previewHeight && imageThumb.getIconWidth() < previewWidth)
		{
			return;
		}
		int	w = previewWidth;
		int	h = previewHeight;
		if(imageThumb.getIconHeight() > imageThumb.getIconWidth())
		{
			w = -1;
		}
		else
		{
			h = -1;
		}
		imageThumb = new ImageIcon(imageThumb.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT));
	}

	/** Callback (event handler) to indicate that a property of the
	  * JFileChooser has changed. If the selected file has changed cause a new
	  * thumbnail to load.
	  */
	public void propertyChange(PropertyChangeEvent e)
	{
		if(e.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY))
		{
			imageFile = (File)e.getNewValue();
			if(isShowing())
			{
				loadImage();
				repaint();
			}
		}
	}

	/** Paints the icon of the current image, if one's present..
	  * @param Graphics object to use when painting the component.
	  */
	public void paintComponent(Graphics g)
	{
		if(imageThumb == null)
		{
			loadImage();
		}
		if(imageThumb == null)
		{
			return;
		}
		int	x = (getWidth() - imageThumb.getIconWidth()) / 2;
		int	y = (getHeight() - imageThumb.getIconHeight()) / 2;
		if(y < 0)
		{
			y = 0;
		}
		if(x < 5)
		{
			x = 5;
		}
		imageThumb.paintIcon(this, g, x, y);
	}
}

