package r9.quiz.cards;

import javax.swing.AbstractListModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import r9.quiz.CourseCreatingManager;
 

public class CardPreviewListModel extends AbstractListModel {
	 
	private static final long serialVersionUID = 1L;
 
	private TreeModelListener treeModelListener;

	public CardPreviewListModel( ) {
	 	treeModelListener = new TreeModelListener() {

			@Override
			public void treeNodesChanged(TreeModelEvent arg0) {
				rebuildList();
			}

			@Override
			public void treeNodesInserted(TreeModelEvent arg0) {
				rebuildList();
			}

			@Override
			public void treeNodesRemoved(TreeModelEvent arg0) {
				rebuildList();
			}

			@Override
			public void treeStructureChanged(TreeModelEvent arg0) {
				rebuildList();
			}
		};
	}
	 
	 

	private void rebuildList() {
		 
		this.fireContentsChanged(this, 0, getSize() );
	}

	public int getSize() {
		Exam cardset = CourseCreatingManager.sharedInstance.getExam();
		if ( cardset == null )
			return 0;
 
		
		boolean hasCover = cardset.getCoverImage() != null;
		return   cardset.getCardList().size() + (hasCover ? 1 : 0);
	}

	public CardPage getElementAt(int index) {
		Exam cardset = CourseCreatingManager.sharedInstance.getExam();
		if ( cardset == null )
			return null;
		boolean hasCover = cardset.getCoverImage() != null;
		if ( hasCover ){
			if ( index == 0 )
				return cardset.getCardAt(0);
			else
				return cardset.getCardAt(index - 1);
		} else {
			return cardset.getCardAt(index);
		}
		
	}
}
