package r9.quiz.problemui.surveyjs;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.JTextComponent;

import r9.quiz.surveyjs.MatrixQuestion;
import r9.quiz.surveyjs.MatrixQuestion.Cell;
import r9.quiz.surveyjs.MatrixQuestion.MatrixField;
import r9.quiz.surveyjs.Question;
import r9.quiz.surveyjs.Question.ChoicesOrderType;
import r9.quiz.surveyjs.Quiz;
import r9.quiz.util.PropertyUIHelper;
import r9.quiz.util.R9Exception;

public class MatrixQuestionPanel extends QuestionCommonPanel{
 
	private static final long serialVersionUID = 1L;
	private JComboBox<ChoicesOrderType> choicesOrderCombo;
	private JTextField colCountField; 
	private ItemCollectionPanel<MatrixField> colsPanel;
	private ItemCollectionPanel<MatrixField> rowsPanel;
	private JCheckBox showLabelBox;
	private JCheckBox isAllRowRequiredBox;
	private AbstractTableModel tableModel;
	private MatrixQuestion mquestion;
	private JTable table;
	private JScrollPane scrollPane;
	private AbstractTableModel tableRowModel;
	   
     
     
	public MatrixQuestionPanel(){
		 
		choicesOrderCombo = new JComboBox<ChoicesOrderType>(ChoicesOrderType.values());
		colCountField = new JTextField();
		
		choicesOrderCombo.setPreferredSize(new Dimension( 100,32));
		choicesOrderCombo.setMaximumSize(new Dimension( 100,32));
		colCountField.setPreferredSize(new Dimension( 100,32));
		colCountField.setMaximumSize(new Dimension( 100,32));
	 
		
		
		showLabelBox = new JCheckBox(rb.getString("ShowNote"));
		isAllRowRequiredBox = new JCheckBox(rb.getString("AllRowNeeded"));
		
		colsPanel = new ItemCollectionPanel<MatrixField>(rb.getString("Text"), rb.getString("Value"), false){ 
			private static final long serialVersionUID = 1L;
			@Override
			public MatrixField updateData(MatrixField t, String f1, String f2, boolean c1) {
				t.value = f2;
				t.text = f1; 
				createTable(mquestion);
				return t;
			}

			@Override
			public void selectData(MatrixField d, JTextComponent f1, JTextComponent f2, JCheckBox c1) {
				 f1.setText(d.text);
				 f2.setText(d.value);
				 createTable(mquestion);
			}

			@Override
			public MatrixField create() {
				 return new MatrixField("","");
			}};
	    colsPanel.setBorder(BorderFactory.createTitledBorder(rb.getString("ColumnSetting")));
		rowsPanel = new ItemCollectionPanel<MatrixField>( rb.getString("Text"), rb.getString("Value"), false){ 
			private static final long serialVersionUID = 1L;
			@Override
			public MatrixField updateData(MatrixField t, String f1, String f2, boolean c1) {
				t.value = f2;
				t.text = f1; 
				createTable(mquestion);
				return t;
			}

			@Override
			public void selectData(MatrixField d, JTextComponent f1, JTextComponent f2, JCheckBox c1) {
				 f1.setText(d.text);
				 f2.setText(d.value);
				 createTable(mquestion);
			}

			@Override
			public MatrixField create() {
				 return new MatrixField("","");
			}};
		rowsPanel.setBorder(BorderFactory.createTitledBorder(rb.getString("RowSetting"))); 
		 createCommonPanel(  );
		 
		 
		 contentPanel.add(PropertyUIHelper.createRow(true, null, new JLabel(rb.getString("OptionsInOneLine")), colCountField,  
				new JLabel( rb.getString("OptionsOrder")), choicesOrderCombo,  showLabelBox,  isAllRowRequiredBox ));
		 contentPanel.add(colsPanel);
		 contentPanel.add(rowsPanel);
		 
		 scrollPane = new JScrollPane( );
		 scrollPane.setMinimumSize(new Dimension(600,100));   
		 scrollPane.setPreferredSize(new Dimension(600,100));   
		 contentPanel.add(scrollPane);
		 createCorrectAnswerRow9();
		 createCommonNavigation( );
	}
	
	@Override
	public void setupUI(Question question, Quiz quiz){
		 if(!(question instanceof MatrixQuestion))
			 return;
		 mquestion = (MatrixQuestion)question;
		 super.setupUI(mquestion, quiz);
		 
		 this.colCountField.setText(mquestion.getColCount() +"");
		 this.choicesOrderCombo.setSelectedItem(mquestion.getChoicesOrder());
		 showLabelBox.setSelected(mquestion.isShowLabel());
		 isAllRowRequiredBox.setSelected(mquestion.isAllRowRequired());
		 colsPanel.setData(mquestion.getColumns());
		 rowsPanel.setData(mquestion.getRows());
		 createTable(  mquestion );
	}
	@Override
	public void fromUI(Question question, Quiz quiz) throws R9Exception{
		 if(!(question instanceof MatrixQuestion))
			 return;
		 MatrixQuestion q = (MatrixQuestion)question;
		 super.fromUI(q, quiz);
		 try{
			 q.setColCount(Integer.parseInt( this.colCountField.getText()));
		 }catch(Exception e){}
		 
		 q.setShowLabel(showLabelBox.isSelected());
		 q.setAllRowRequired(this.isAllRowRequiredBox.isSelected());
		 q.setChoicesOrder( (ChoicesOrderType) choicesOrderCombo.getSelectedItem());
		 
		 q.cleanup();
	}
	
	protected void createTable(final MatrixQuestion question) { 
		 
		    
		  tableModel = new AbstractTableModel() {
	            public String getColumnName(int col) {
	                return col==0 ? "" : colsPanel.data.get(col-1).text;
	            }
	            public int getRowCount() { return rowsPanel.data.size(); }
	            public int getColumnCount() { return colsPanel.data.size()+1; }
	            public Object getValueAt(int row, int col) {
	            	MatrixField r = rowsPanel.data.get(row);
	            	if( col == 0 )
	            		return r.text;
	            	MatrixField c = colsPanel.data.get(col-1);
	            	Cell cc = question.get(r, c);
	                return cc != null; 
	            }
	            public boolean isCellEditable(int row, int col)
	                { return col != 0; }
	            public void setValueAt(Object value, int row, int col) { 
	            	Boolean s = (Boolean)value;
	            	MatrixField r = rowsPanel.data.get(row);
	            	MatrixField c = colsPanel.data.get(col-1);
	            	Cell cc = question.get(r, c);
	            	if( s) {
	            		if( cc == null) {
	            			cc = new Cell(r,c );
	            			question.getCorrects().add(cc);
	            		} 
	            	} else {
	            		question.getCorrects().remove(cc);
	            	}
	            }
	            public Class getColumnClass(int columnIndex) {
	                return columnIndex == 0 ? String.class : Boolean.class;
	            }
	        };
//	        tableRowModel = new AbstractTableModel() {
//	            public String getColumnName(int col) {
//	                return "row";
//	            }
//	            public int getRowCount() { return rowsPanel.data.size(); }
//	            public int getColumnCount() { return 1; }
//	            public Object getValueAt(int row, int col) {
//	            	 return rowsPanel.data.get(row).value;
//	            }
//	            public boolean isCellEditable(int row, int col)
//	                { return false; }
//	            public void setValueAt(Object value, int row, int col) {  
//	            }
//	            public Class getColumnClass(int columnIndex) {
//	                return String.class;
//	            }
//	        };
		    table=new JTable(tableModel );
//		    JTable headerColumn = new JTable(tableRowModel); 
//		    headerColumn.getColumn("row").setWidth(100);
		  //  headerColumn.setPreferredSize(new Dimension(100,500));
		  
		    scrollPane.setViewportView( table);
//		    scrollPane.setRowHeaderView(headerColumn);
		       
		    table.setFillsViewportHeight(true);
		    table.setDefaultEditor(Boolean.class, new DefaultCellEditor(new JCheckBox()));
		   
	}
	
	
}
