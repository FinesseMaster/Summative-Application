package calendar;
        
import static calendar.CalendarControl.refreshCalendar;
import javax.swing.*;

import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Calendar{
	static JLabel lblMonth, lblYear;
	static JButton btnPrev, btnNext;
	static JTable CalendarTb;
	static JComboBox cmbYear;
	static JFrame FMain;
	static Container pane;
	static DefaultTableModel mCalendarTb; //Table model
	static JScrollPane sCalendarTb; //The scrollpane
	static JPanel pnlCalendar;
	static int rYear, realMonth, realDay, currYear, currMonth;

	public static void main (String args[]){
		//Look and feel
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch (ClassNotFoundException e) {}
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
		catch (UnsupportedLookAndFeelException e) {}

		//Prepare frame
		FMain = new JFrame ("Calendar"); //Create frame
		FMain.setSize(330, 375); //Set size, 400x400 pizels
		pane = FMain.getContentPane(); //Get content pane
		pane.setLayout(null); //Apply null layout
		FMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close when X is clicked

		//Create controls
		lblMonth = new JLabel ("January");
		lblYear = new JLabel ("Change year:");
		cmbYear = new JComboBox();
		btnPrev = new JButton ("<<");
		btnNext = new JButton (">>");
		mCalendarTb = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
		CalendarTb = new JTable(mCalendarTb);
		sCalendarTb = new JScrollPane(CalendarTb);
		pnlCalendar = new JPanel(null);

		//Set border
		pnlCalendar.setBorder(BorderFactory.createTitledBorder("Calendar"));
		
		
		btnPrev.addActionListener(new btnPrev_Action());
		btnNext.addActionListener(new btnNext_Action());
		cmbYear.addActionListener(new cmbYear_Action());
		
		CalendarControl.CalendarCont();
		
		//Make frame visible
		FMain.setResizable(false);
		FMain.setVisible(true);
		
		//Get real month/year
		GregorianCalendar cal = new GregorianCalendar(); //Create calendar
		realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
		realMonth = cal.get(GregorianCalendar.MONTH); //Get month
		rYear = cal.get(GregorianCalendar.YEAR); //Get year
		currMonth = realMonth; //Match month and year
		currYear = rYear;
		
		//Add headers
		String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
		for (int i=0; i<7; i++){
			mCalendarTb.addColumn(headers[i]);
		}
		
		CalendarTb.getParent().setBackground(CalendarTb.getBackground()); //Set background

		//No resize/reorder
		CalendarTb.getTableHeader().setResizingAllowed(false);
		CalendarTb.getTableHeader().setReorderingAllowed(false);

		CalendarControl.CellSelection();
		//Set row/column count
		CalendarTb.setRowHeight(38);
		mCalendarTb.setColumnCount(7);
		mCalendarTb.setRowCount(6);
		
		//Populate table
		for (int i=rYear-100; i<=rYear+100; i++){
			cmbYear.addItem(String.valueOf(i));
		}
		
		//Refresh calendar
		refreshCalendar (realMonth, rYear); //Refresh calendar
	}
	
	

	static class CalendarTbRenderer extends DefaultTableCellRenderer{
		public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
			super.getTableCellRendererComponent(table, value, selected, focused, row, column);
			if (column == 0 || column == 6){ //Week-end
				setBackground(new Color(255, 220, 220));
			}
			else{ //Week
				setBackground(new Color(255, 255, 255));
			}
			if (value != null){
				if (Integer.parseInt(value.toString()) == realDay && currMonth == realMonth && currYear == rYear){ //Today
					setBackground(new Color(220, 220, 255));
				}
			}
			setBorder(null);
			setForeground(Color.black);
			return this;  
		}
	}

	static class btnPrev_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currMonth == 0){ //Back one year
				currMonth = 11;
				currYear -= 1;
			}
			else{ //Back one month
				currMonth -= 1;
			}
			refreshCalendar(currMonth, currYear);
		}
	}
	static class btnNext_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currMonth == 11){ //Foward one year
				currMonth = 0;
				currYear += 1;
			}
			else{ //Foward one month
				currMonth += 1;
			}
			refreshCalendar(currMonth, currYear);
		}
	}
	static class cmbYear_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (cmbYear.getSelectedItem() != null){
				String b = cmbYear.getSelectedItem().toString();
				currYear = Integer.parseInt(b);
				refreshCalendar(currMonth, currYear);
			}
		}
	}
}
