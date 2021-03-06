/*last edited 1/22/2019
This code is essentially a schedule planner. It contains a calendar that allows the user 
to keep track of the date and month. Moreover, they are able to 
jot down any notes (e.g, reminders) that they need to remember. 
*/

package calendar;
        
import static calendar.CalendarControl.refreshCalendar;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Calendar{
	static JLabel lblMonth, lblYear;
	static JButton btnPrev, btnNext;
	static JTable CalendarTb;
	static JComboBox cmbYear;
        static JComboBox cmbTheme;
	static JFrame FMain;
	static Container pane;
	static DefaultTableModel mCalendarTb; //Table model
	static JScrollPane sCalendarTb; //The scrollpane
	static JPanel pnlCalendar;
        static JPanel note;
        static TextArea area;
        static JButton but;
        static ImageIcon logoImg;
        static String themeSelect;
	static int rYear, realMonth, realDay, currYear, currMonth;

	public static void main (String args[]) throws IOException{
		//Look and feel
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch (ClassNotFoundException e) {}
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
		catch (UnsupportedLookAndFeelException e) {}
                
		//Prepare frame
		FMain = new JFrame ("Calendar"); //Create frame
		FMain.setSize(655, 375); //Set size, 400x400 pizels
		pane = FMain.getContentPane(); //Get content pane
		pane.setLayout(null); //Apply null layout
		FMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close when X is clicked
                FMain.setLocationRelativeTo(null);
                
		//Create controls
		lblMonth = new JLabel ("January");
		lblYear = new JLabel ("Change year:");
		cmbYear = new JComboBox();
                cmbTheme = new JComboBox();
		btnPrev = new JButton ("<<");
		btnNext = new JButton (">>");
                but = new JButton("Save Note");
		mCalendarTb = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
		CalendarTb = new JTable(mCalendarTb);
		sCalendarTb = new JScrollPane(CalendarTb);
		pnlCalendar = new JPanel(null);
                area = new TextArea(readFile("src/calendar/userStr.txt"));

		//Set border
		pnlCalendar.setBorder(BorderFactory.createTitledBorder("Calendar"));
		
		
		btnPrev.addActionListener(new btnPrev_Action());
		btnNext.addActionListener(new btnNext_Action());
		cmbYear.addActionListener(new cmbYear_Action());
                cmbTheme.addActionListener(new cmbTheme_Action());
                but.addActionListener(new ButtonSave());
		
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
                
		//Set row/column count
		CalendarTb.setRowHeight(38);
		mCalendarTb.setColumnCount(7);
		mCalendarTb.setRowCount(6);
		
		//Populate table
		for (int i=rYear-100; i<=rYear+100; i++){
			cmbYear.addItem(String.valueOf(i));
		}
                
                String[] Themes = {"Blue", "Red", "Green", "Orange"};
                for(int i=0;i<4;i++){
                    cmbTheme.addItem(Themes[i]);
                }
                
                logoImg = new ImageIcon("/src/calendar/logo.png");
                FMain.setIconImage(logoImg.getImage());
		
                //Refresh calendar
		refreshCalendar (realMonth, rYear); //Refresh calendar
	}
	
	

	static class CalendarTbRenderer extends DefaultTableCellRenderer{
		public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
			super.getTableCellRendererComponent(table, value, selected, focused, row, column);
                        switch(themeSelect){
                            case "Blue":
                                if (column == 0 || column == 6){ //Week-end
                                        setBackground(new Color(173,215,246));
                                }
                                else{ //Week
                                        setBackground(new Color(169,255,247));
                                }
                                if (value != null){  
                                        if (Integer.parseInt(value.toString()) == realDay && currMonth == realMonth && currYear == rYear){ //Today
                                                setBackground(new Color(108, 190, 237));  
                                        }
                                }
                                break;
                                
                            case "Red":
                                if (column == 0 || column == 6){ //Week-end
                                        setBackground(new Color(170,99,99));
                                }
                                else{ //Week
                                        setBackground(new Color(229,188,188));
                                }
                                if (value != null){  
                                        if (Integer.parseInt(value.toString()) == realDay && currMonth == realMonth && currYear == rYear){ //Today
                                                setBackground(new Color(225, 77, 77));  
                                        }
                                }
                                break;
                                
                            case "Green":
                                if (column == 0 || column == 6){ //Week-end
                                        setBackground(new Color(62,214,115));
                                }
                                else{ //Week
                                        setBackground(new Color(113,247,159));
                                }
                                if (value != null){  
                                        if (Integer.parseInt(value.toString()) == realDay && currMonth == realMonth && currYear == rYear){ //Today
                                                setBackground(new Color(31,137,68));  
                                        }
                                }
                                break;
                                
                            case "Orange":
                                if (column == 0 || column == 6){ //Week-end
                                        setBackground(new Color(246,213,126));
                                }
                                else{ //Week
                                        setBackground(new Color(252,246,237));
                                }
                                if (value != null){  
                                        if (Integer.parseInt(value.toString()) == realDay && currMonth == realMonth && currYear == rYear){ //Today
                                                setBackground(new Color(255,125,50));  
                                        }
                                }
                                break;
                        }
			setBorder(null);
			setForeground(Color.black);
			return this;  
		}
	}
        public static void copyFile() throws IOException{
            FileWriter fw = new FileWriter("src/calendar/userStr.txt");
            String str;
            str = area.getText();
            fw.write(str);
            fw.close();
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
        static class cmbTheme_Action implements ActionListener{
            public void actionPerformed (ActionEvent e){
                if(cmbTheme.getSelectedItem() != null){
                    themeSelect = cmbTheme.getSelectedItem().toString();
                    refreshCalendar(currMonth, currYear);
                }
            }
        }
        static class ButtonSave implements ActionListener{
            public void actionPerformed(ActionEvent e) {
                try{
                    copyFile();
                } catch (IOException em){
                }
            }
        }
        private static String readFile(String pathname) throws IOException {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());        

        try (Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + System.lineSeparator());
            }
        return fileContents.toString();
        }
    }
}
