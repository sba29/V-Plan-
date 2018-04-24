package vplan;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;
import static vplan.UI.load_all_data;
@SuppressWarnings("unused")
public class UI implements Comparable<Task> {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JButton btnNewButton_1;
	private JButton btnDifficulty;
	private JButton btnDate;
	private JButton btnSubject;
	private JLabel lblSortBy;
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private ArrayList<String> dispList = new ArrayList<String>();
	/**
	 * Launch the application.
	 */
        private final static String REMINDERS_SAVE_FILENAME = "reminders.txt";
      
	public static void main(String[] args) {
                load_all_data();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JList<Task> list = new JList<Task>();
                list.setCellRenderer( new ColorRender() );
                list.setListData((taskList.toArray(new Task[taskList.size()]))); //tells list to use custom rendered which as set below
		list.setBounds(148, 25, 145, 199);
		frame.getContentPane().add(list);
		
		textField = new JTextField("Title");
		textField.setBounds(10, 25, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField("Date (MM/dd/yy)");
		textField_1.setBounds(10, 55, 86, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField("Subject");
		textField_2.setBounds(10, 85, 86, 20);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField("Difficulty (1-10)");
		textField_3.setBounds(10, 115, 86, 20);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(10, 157, 89, 23);
		frame.getContentPane().add(btnCreate);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                                String difficulty = textField_3.getText();
                                String date = textField_1.getText();
                                Boolean dateValid = validateDate(date); //vchecks for valid date and difficulty inputs
                                Boolean difficultyValid = validateDifficulty(difficulty);
                                if(dateValid && difficultyValid){ //if inputs are valid, create and add new task to list. Reload display
				Task newTask = new Task(textField.getText(), textField_2.getText(), textField_1.getText(), Integer.parseInt(textField_3.getText()));
				taskList.add(newTask);
                                save_all_data(); //saves new list
				Task[] tempList = taskList.toArray(new Task[taskList.size()]);
				list.setListData(tempList);
				frame.repaint();
                                }
			}
		});
                
		JButton btnNewButton = new JButton("Remove");
		btnNewButton.setBounds(10, 201, 89, 23);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() { //removes selected object from list
			public void actionPerformed(ActionEvent arg0) {
				Task taskName = list.getSelectedValue(); //gets value selected from list
				for(int i=0; i<taskList.size(); i++){ //checks for which element in list is the same as selected
					if(taskList.get(i) == taskName){
						taskList.remove(i);
					}
                                        save_all_data(); //saves the new list
					Task[] tempList = taskList.toArray(new Task[taskList.size()]); //displays the list
					list.setListData(tempList);
					frame.repaint();
				}
			}
			});
		btnNewButton_1 = new JButton("Title");
		btnNewButton_1.addActionListener(new ActionListener() { //sort by title button
			public void actionPerformed(ActionEvent arg0) {
				sortByTitle();
				dispList.clear();
				Task[] tempList = taskList.toArray(new Task[taskList.size()]);
				list.setListData(tempList);
				frame.repaint();
				frame.repaint();
				
			}
		});
		btnNewButton_1.setBounds(320, 54, 89, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		btnDifficulty = new JButton("Difficulty");
		btnDifficulty.setBounds(320, 88, 89, 23);
		btnDifficulty.addActionListener(new ActionListener() { //sort by difficulty button
			public void actionPerformed(ActionEvent arg0) {
				sortByDifficult();
				Task[] tempList = taskList.toArray(new Task[taskList.size()]);
				list.setListData(tempList);
				frame.repaint();
				frame.repaint();
				
			}
		});
		frame.getContentPane().add(btnDifficulty);
		
		btnDate = new JButton("Date");
		btnDate.setBounds(320, 122, 89, 23);
		btnDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { //sort by date
				sortByDate();
				Task[] tempList = taskList.toArray(new Task[taskList.size()]);
				list.setListData(tempList);
				frame.repaint();
			}
		});
		frame.getContentPane().add(btnDate);
		
		btnSubject = new JButton("Subject");
		btnSubject.setBounds(320, 157, 89, 23);
		btnSubject.addActionListener(new ActionListener() { //adds listener to button to sort by sub
			public void actionPerformed(ActionEvent arg0) {
				sortBySubject();
				Task[] tempList = taskList.toArray(new Task[taskList.size()]);
				list.setListData(tempList);
				frame.repaint();
				
			}
		});
		frame.getContentPane().add(btnSubject);
		
		lblSortBy = new JLabel("Sort by:");
		lblSortBy.setBounds(339, 28, 46, 14);
		frame.getContentPane().add(lblSortBy);
		

	}
	public void sortByTitle() { //sorts task list alphabetically by title
		Collections.sort(taskList, new Comparator<Task>() {
		    @Override
		    public int compare(Task o1, Task o2) {
		        return o1.getTitle().compareTo(o2.getTitle());
		    }
		});
	}
	public void sortBySubject() { //sorts alphabetically by subject
		Collections.sort(taskList, new Comparator<Task>() {
		    @Override
		    public int compare(Task o1, Task o2) {
		        return o1.getSubject().compareTo(o2.getSubject());
		    }
		});
	}
	public void sortByDifficult() { //sorts by ascending difficulty
		Collections.sort(taskList, new Comparator<Task>() {
		    @Override
		    public int compare(Task o1, Task o2) {
		    	
		        return Integer.compare(o1.getDifficulty(),o2.getDifficulty());
		    }
		}
              );
	}
	
	public void sortByDate() { //sorts by ascending due date
		Collections.sort(taskList, new Comparator<Task>() {
		    @Override
		    public int compare(Task o1, Task o2) {
		        return o1.getDate().compareTo(o2.getDate());
		    }
		});
	}
		
	@Override
	public int compareTo(Task arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
          public static void save_all_data() { //serializes objects and writes to file
    try {
      ArrayList<Task> reminders_to_serialize = new ArrayList<Task>();
      for (int i = 0; i < taskList.size(); i++) { //creates serialized list
          reminders_to_serialize.add(taskList.get(i));
      }
      
      FileOutputStream file_output_stream = new FileOutputStream(REMINDERS_SAVE_FILENAME); //initializes output stream
      ObjectOutputStream output_stream = new ObjectOutputStream(file_output_stream);
      output_stream.writeObject(reminders_to_serialize); //writes to file
      output_stream.flush();
      output_stream.close();
      file_output_stream.close();
    } catch (Exception e) {e.printStackTrace();}
  }
  
  public static boolean validateDate(String s){ //checks for valid date input
        String[] split =  s.split("/");
        if(split.length == 3){
     try {
        Integer number1 = Integer.parseInt(split[0]);
        Integer number2 = Integer.parseInt(split[0]);
        Integer number3 = Integer.parseInt(split[0]);
        if(number1 < 0 || number1 > 12){
            JOptionPane.showMessageDialog(null, "Please enter a valid date on the format mm/dd/yy.");
            return false;
        } else if(number2 < 0 || number2 > 31){
            JOptionPane.showMessageDialog(null, "Please enter a valid date on the format mm/dd/yy.");
            return false;
        }
    } catch (NumberFormatException nfe) {
        // Command is illegal. Display error message.
        JOptionPane.showMessageDialog(null, "Please enter a valid date on the format mm/dd/yy.");
    }}
      return true;
  }
  
  public static boolean validateDifficulty(String s){ //checks for valid difficulty input
      try {
          Integer number = Integer.parseInt(s);
          if (number <= 0 || number > 10){
            JOptionPane.showMessageDialog(null, "Please enter a valid difficulty between 1 and 10");
            return false;
          }
      }
      catch (NumberFormatException nfe) {
        // Command is illegal. Display error message.
        JOptionPane.showMessageDialog(null, "Please enter a valid difficulty between 1 and 10.");
    }
      return true;
  }
  @SuppressWarnings("unchecked") // This is just to allow me to cast the object to an ArrayList<Task>
  public static boolean load_all_data() {
    File file = new File(REMINDERS_SAVE_FILENAME);
    if (!file.exists() || file.isDirectory()) return false; //catches invalid file
    try {
      FileInputStream file_input_stream = new FileInputStream(REMINDERS_SAVE_FILENAME); //writes to specified file
      ObjectInputStream object_input_stream = new ObjectInputStream(file_input_stream);
      taskList = (ArrayList<Task>)object_input_stream.readObject();
      object_input_stream.close();
      file_input_stream.close();
    } catch (Exception e) {}
    return true;
  }
        
        private static class ColorRender extends DefaultListCellRenderer { //this function overrides the fault render for a list
        public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
            Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
	LocalDate localDate = LocalDate.now();
	String d1 = dtf.format(localDate);
        String d2 = ((Task) value).getDate();
            switch (compareDates(d2, d1)) { //sets color of list item based on return
                case 0:
                    c.setBackground( Color.red );
                    break;
                case 1:
                    c.setBackground (Color.yellow);
                    break;
                default:
                    c.setBackground( Color.white );
                    break;
            }
            return c;
        }

        private int compareDates(String d1, String d2) { //checks if the current date is before, after or around the due date
            String[] s1 = d1.split("/");
            String[] s2 = d2.split("/");
            int month1 = Integer.parseInt(s1[0]);
            int day1 = Integer.parseInt(s1[1]);
            int year1 = Integer.parseInt(s1[2]);
            int month2 = Integer.parseInt(s2[0]);
            int day2 = Integer.parseInt(s2[1]);
            int year2 = Integer.parseInt(s2[2]);
            if (year1 < year2){
         
                return 0;}
            if (year1 > year2) {
                return 2;
            }
            else{
                if (month1 > month2){
            
                    return 2;
                }
                else if (month1 < month2) {
                    return 0;
                }
                else {
                    if (day1 <= day2){
                  
                        return 0;
                    }
                    else if (day1-day2 <= 2){
                     
                        return 1;
                    }
                    else return 2;
                }
        }}
       
    }
        

}
	
    
	