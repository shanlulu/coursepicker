/* CoursePickerPanel.java
 * CS 230 final project
 * Modified by: ili
 * Modified date: 05/06/17
 * ili, jwang17, slu5
 */

//have to do oNE BIG CLASS 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class CoursePickerPanel extends JPanel{
  private Search searchObj;
  private FinalClasses fClasses;
  private JButton searchButton;
  private JTextField courseName;
  //private JLabel results;
  
  private JLabel searchResults; //text that displays course search results of user
  private JButton[][] buttons; 
  private JLabel[] timeLabel; 
  private JLabel[] weekdays; 
  private String[] days; 
  private String[] times; 
  private LinkedList<JButton> courses;  
  private SearchTest tester;
  private ImageIcon option;
  private ImageIcon official;
  private FinalClasses finalClasses; 
  private int counter;
  
  
  public CoursePickerPanel(Search sObj, FinalClasses f) {
    searchObj = sObj;
    fClasses = f;
    option = new ImageIcon("images/blue.jpg");
    official = new ImageIcon("images/pink.png");
    counter = 0; //used with setActionCommand to give each course button a unique ID
    
    JTabbedPane tp = new JTabbedPane();
    tp.addTab("Search", searchTab());
    
    add(tp);
    tp.addTab("Schedule Generator", scheduleTab()); 
  }
  
  public JPanel searchTab() {
    JPanel one = new JPanel(new BorderLayout());
    one.setBackground(Color.pink);
    one.add(new JLabel("Please enter the title of the course you wish to take and click search. Example: CS 111"), BorderLayout.NORTH);
    one.add(new JLabel("Course Title: "), BorderLayout.WEST);  
    
//      results = new JLabel ("Information about the course you have searched for will appear here.");
//      results.setMinimumSize(new Dimension(700, 300));
//      results.setPreferredSize(new Dimension(700, 300));
//      results.setMaximumSize(new Dimension(700, 300));
//      add(results, BorderLayout.SOUTH);
    
    searchButton = new JButton("Search");
    searchButton.addActionListener(new ButtonListener(searchObj));
    one.add(searchButton, BorderLayout.EAST);
    
    courseName = new JTextField();
    one.add(courseName, BorderLayout.CENTER); 
    
    return one;
  }
  
  
  public JPanel scheduleTab() {
    JPanel two = new JPanel();
    
    //beginning the arrangement of the panel
    two.setLayout(new FlowLayout()); //use FlowLayout to clearly separate the parts of the tabbed panel
    
    JPanel top = new JPanel(); //put what course you're searching for here
    top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
    
    searchResults = new JLabel("Search results will appear here.",
                               SwingConstants.CENTER);
    top.add(searchResults);
    
    two.add(top);
    
    courses = new LinkedList<JButton>(); //stores all of the buttons representing the classes from the Search object
    
    buttons = new JButton[31][5]; //represents the calendar 
    
    //timeLabel array populated by all of the times ranging from 8:30 to 23:30
    timeLabel = new JLabel[31];
    times = new String[] {"08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30",
      "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00",
      "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30"}; //all of the times for the calendar
    for (int a = 0; a < timeLabel.length; a++) {
      JLabel hourLabel = new JLabel(times[a]); //initializes a time label
      timeLabel[a] = hourLabel;
    }
    
    //weekDays array populated by all of the days of a weekday
    weekdays = new JLabel[5];
    days = new String[]{"M", "T", "W", "TH", "F"};
    for (int b = 0; b < days.length; b++) {
      JLabel dayLabel = new JLabel(days[b]); //initializes a day label
      weekdays[b] = dayLabel;
    }
    
    for (int i = 0; i < buttons.length; i++) {
      c.gridy = i + 1; //sets the row position of the time label
      c.gridx = 0; //gridx set to 0 so that all the time labels are in the first column
      c.insets = new Insets(3, 3, 3, 3); //inserts padding in between each time label
      middle.add(timeLabel[i], c); //adds the time labels to the side of the calendar
      for (int j = 0; j < buttons[i].length; j++) {
        c.gridx = j + 1; //sets the column position of the button to k
        buttons[i][j] = new JButton("");
        buttons[i][j].setPreferredSize(new Dimension(50, 40)); //sets the size of the buttons
        c.insets = new Insets(-3, -3, -3, -3); //insets with negative values removes spaces in between the buttons
        middle.add(buttons[i][j], c);
        buttons[i][j].setEnabled(false); //prevents user from clicking any buttons on the calendar
      }
    }
    for (int k = 0; k < weekdays.length; k++) { //adds the weekday labels on the top of the calendar
      c.gridy = 0; //set to 0 so that all day labels are in the first row
      c.gridx = k + 1; //sets column position of the day label
      c.insets = new Insets(3, 3, 3, 3); //inserts padding between day label
      middle.add(weekdays[k], c);
      c.gridy = k + 1; //sets the row position of the button to j
    }
    
    two.add(middle);
    
    return two;
    
  }
  
  public void setSearchResults() {
    for (int c = 0; c < searchObj.getSize(); c++) {
      System.out.println(searchObj.getSize());
      String[] dates = searchObj.getCourse(c).getDate(); //gets the String array of dates that the class occurs
      String d = ""; 
      //stores all of the days within dates array into a string in order to display on GUI
      for (int f = 0; f < dates.length; f++) {
        if (dates[f] != null) {
          d += dates[f];
        }
      }
      
      String s = searchObj.getCourse(c).getStartTime(); 
      String e = searchObj.getCourse(c).getEndTime();
      
      //creates new button to be displayed next to calendar; user would click this to add course to their calendar
      JButton course = new JButton();
      course.setText("<html>Course: " + searchObj.getCourse(c).getTitle() + "<br>Time: " + s + "-" + e + 
                     "<br>Meeting times: " + d + "<br>Professor: " + searchObj.getCourse(c).getProf());
      course.addActionListener(new ButtonListener(searchObj));
      course.setActionCommand(Integer.toString(counter)); //ActionCommand represents a unique idenfication for the button
      courses.add(course); //adds button to the LinkedList of course buttons
      counter++;
      
      top.add(course);
    }
    
    add(top); //adds top panel to overall ScheduleTabPanel
  }
  

private class ButtonListener implements ActionListener{
  //CourseInformationTest object, containing hash tables, is an input
  private Search searchObj;
  
  public ButtonListener(Search s){
    //passes Search object into ButtonListener and calls the super() constructor
    super();
    searchObj = s;
  }
  
  public void actionPerformed(ActionEvent event) {
    //creates new search whenever search button is pressed, adds new info to searchResults
    if (event.getSource() == searchButton) {
      try {
        String name = courseName.getText().toUpperCase();
        searchObj.searchCourse(name);
        setSearchResults();
        //results.setText(searchObj.getSearchResults().toString());  
      } catch (IllegalArgumentException e){
        System.out.println("blah");
        //results.setText("Invalid course name. Please search again.");
      }
    }
    else {
      int action = Integer.parseInt(event.getActionCommand()); //gets the int representation of the ActionCommand
      for (int n = 0; n < courses.size(); n++) { 
        //searches through the LinkedList of buttons to find a matching actionCommand
        if (Integer.parseInt(courses.get(n).getActionCommand()) == action) {
          courses.get(n).setEnabled(false);
          finalClasses.addClass(searchObj.getCourse(n));
          addToCalendar(n, searchObj.getCourse(n));
        }
      }
      
    }
  }
  private void addToCalendar(int index, Course c) {
    Time start = new Time(c.getStartTime());
    Time end = new Time(c.getEndTime());
    
    //rounds both start and end time to nearest half hour
    start.roundToNearestHalfHour(); 
    end.roundToNearestHalfHour();
    
    //gets corresponding index position for the buttons
    int indexStart = Arrays.asList(times).indexOf(start.toString());
    int indexEnd = Arrays.asList(times).indexOf(end.toString());
    
    //shades the places where the class would occur on the calendar
    for (int m = indexStart; m <= indexEnd; m++) {
      buttons[m][0].setBorder(BorderFactory.createEmptyBorder());
      buttons[m][0].setIcon(official);
      buttons[m][0].addMouseListener(new MouseHover(index));
    } 
  }
}
private class MouseHover implements MouseListener {
  private int i;
  public MouseHover(int index) {
    i = index;
  }
  public void mouseEntered(MouseEvent e) {
    JOptionPane.showMessageDialog(null, courses.get(i).getText());
  }
  public void mouseExited(MouseEvent e) {
  }
  public void mouseClicked(MouseEvent e) {
  }
  public void mousePressed(MouseEvent e) {
  }
  public void mouseReleased(MouseEvent e) {
  }
}
}


