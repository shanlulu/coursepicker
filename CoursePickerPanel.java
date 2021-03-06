/* CoursePickerPanel.java
 * Jennifer Wang, Isabelle Li, Shan Lu
 * CS 230 Final Project
 * Creates the search and schedule tabs and methods associated with them.
 * Modified by: ili
 * Modified date: 05/06/17
 * Modified by: jwang17
 * Modified date: 05/07/17
 * Modified by: slu5
 * Modified date: 05/08/17
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class CoursePickerPanel extends JPanel{
  private JTabbedPane tp;
  private Search searchObj;
  private FinalClasses finalClasses;
  
  //private instance variables to create first tab
  private JPanel searchTab; //contains all of the elements to search a course
  private JButton searchButton;
  private JTextField courseName;
  private JLabel searchResults;
  
  private JPanel buttonResults; //shows the grid of buttons representing courses
  private LinkedList<JButton> courses;  
  private JButton[][] buttons; //all of the buttons for the calendar
  private JLabel[] timeLabel; 
  private int[] times; 
  private JLabel[] weekdays; 
  private String[] days; 
  private Color[] colors;
  private int counter;
  private int chooseColor;
  private boolean exist;
  
  
  public CoursePickerPanel(Search sObj, FinalClasses f) {
    tp = new JTabbedPane();
    searchObj = sObj;
    finalClasses = f;
    
    //creates the searching part of first tab
    searchTab = new JPanel();
    searchTab.setLayout(new BoxLayout(searchTab, BoxLayout.PAGE_AXIS));
    JPanel searchFunction = new JPanel(new BorderLayout());
    searchFunction.setMaximumSize(new Dimension(500, 200));
    searchFunction.setBackground(Color.pink);
    searchFunction.add(new JLabel("<html>Please enter the title of the course you wish to take and click search.<br>Example: CS 111</html>"), BorderLayout.NORTH);
    searchFunction.add(new JLabel("Course Title: "), BorderLayout.WEST);  
    
    searchButton = new JButton("Search");
    searchButton.addActionListener(new ButtonListener(searchObj));
    searchFunction.add(searchButton, BorderLayout.EAST);
    
    courseName = new JTextField();
    searchFunction.add(courseName, BorderLayout.CENTER); 
    
    searchResults = new JLabel("Buttons will appear where you can add your course.",
                               SwingConstants.CENTER);
    searchFunction.add(searchResults, BorderLayout.SOUTH);
    
    searchTab.add(searchFunction);
    tp.add("Search", searchTab);
    
    //sets up the grid of buttons of classes to add 
    buttonResults = new JPanel();
    buttonResults.setLayout(new WrapLayout()); //WrapLayout taken from a blog; wraps the buttons 
    
    
    //creating second tab
    counter = 0; //used with setActionCommand to give each course button a unique ID
    chooseColor = 0;
    
    JPanel schedule = new JPanel(new FlowLayout()); //represents panel of the second tab
    
    
    courses = new LinkedList<JButton>(); //stores all of the buttons representing the classes from the Search object
    
    buttons = new JButton[31][5]; //represents the calendar 
    
    colors = new Color[] {Color.cyan, Color.red, Color.yellow, Color.blue, Color.gray, Color.green, Color.magenta, 
      Color.orange, Color.pink};
    
    //timeLabel array populated by all of the times ranging from 8:30 to 23:30
    timeLabel = new JLabel[31];
    //all of the times for the calendar as integers to make it easier to compare
    times = new int[] {830, 900, 930, 1000, 1030, 1100, 1130, 1200, 1230, 1300, 1330, 1400, 1430, 1500, 1530, 1600, 
      1630, 1700, 1730, 1800, 1830, 1900, 1930, 2000, 2030, 2100, 2130, 2200, 2230, 2300, 2330}; 

    for (int a = 0; a < timeLabel.length; a++) {
      int length = String.valueOf(times[a]).length(); //gets number of digits in an int
      String timeStr = Integer.toString(times[a]);
      if (length == 3) { //if length is 3 (aka military time is before 10:00 AM)
        timeStr = timeStr.substring(0, 1) + ":" + timeStr.substring(1, 3); //add semi-colon in between first character
        //and the rest of the characters
      }
      else {
        timeStr = timeStr.substring(0, 2) + ":" + timeStr.substring(2, 4); //add semi-colon in between second character
        //and the rest of the characters
      }
      JLabel hourLabel = new JLabel(timeStr); //initializes a time label
      timeLabel[a] = hourLabel;
    }
    
    //weekDays array populated by all of the days of a weekday
    weekdays = new JLabel[5];
    days = new String[]{"M", "T", "W", "TH", "F"};
    for (int b = 0; b < days.length; b++) {
      JLabel dayLabel = new JLabel(days[b]); //initializes a day label
      weekdays[b] = dayLabel;
    }
    
    JPanel calendar = new JPanel(new GridBagLayout()); //stores all of the buttons that make up the calendar
    GridBagConstraints c = new GridBagConstraints();
    
    for (int i = 0; i < buttons.length; i++) {
      c.gridy = i + 1; //sets the row position of the time label
      c.gridx = 0; //gridx set to 0 so that all the time labels are in the first column
      c.insets = new Insets(2, 2, 2, 2); //inserts padding in between each time label
      calendar.add(timeLabel[i], c); //adds the time labels to the side of the calendar
      for (int j = 0; j < buttons[i].length; j++) {
        c.gridx = j + 1; //sets the column position of the button to k
        buttons[i][j] = new JButton("");
        buttons[i][j].setPreferredSize(new Dimension(150, 20)); //sets the size of the buttons
        c.insets = new Insets(-2, -2, -2, -2); //insets with negative values removes spaces in between the buttons
        calendar.add(buttons[i][j], c);
        buttons[i][j].setEnabled(false); //prevents user from clicking any buttons on the calendar
      }
    }
    for (int k = 0; k < weekdays.length; k++) { //adds the weekday labels on the results of the calendar
      c.gridy = 0; //set to 0 so that all day labels are in the first row
      c.gridx = k + 1; //sets column position of the day label
      c.insets = new Insets(3, 3, 3, 3); //inserts padding between day label
      calendar.add(weekdays[k], c);
      c.gridy = k + 1; //sets the row position of the button to j
    }
    
    schedule.add(calendar); 
    tp.add("Schedule", schedule);
    add(tp);
    
  }
  private void setSearchResults() {
    buttonResults.removeAll(); //clears the JPanel of the first tab that contains the grid of courses
    courses.clear(); //removes all of the previous buttons for the courses
    
    for (int g = 0; g < searchObj.getSize(); g++) {
      String[] dates = searchObj.getCourse(g).getDate(); //gets the String array of dates that the class occurs
      String d = ""; 
      //stores all of the days within dates array into a string in order to display on GUI
      for (int f = 0; f < dates.length; f++) {
        if (dates[f] != null) {
          d += dates[f];
        }
      }
      
      String s = searchObj.getCourse(g).getStartTime(); 
      String e = searchObj.getCourse(g).getEndTime();
      
      //creates new button to be displayed next to calendar; user would click this to add course to their calendar
      JButton course = new JButton();
      course.setText("<html> CRN: " + searchObj.getCourse(g).getCRN() + "<br>Course: " + 
                     searchObj.getCourse(g).getTitle() + "<br>Meeting times: " + d + "<br>Time: " + s + "-" + e + 
                     "<br>Professor: " + searchObj.getCourse(g).getProf() + "<br>Section: " + 
                     searchObj.getCourse(g).getSection());
      course.setFont(new Font("Calibri", Font.PLAIN, 11));
      course.addActionListener(new ButtonListener(searchObj));
      course.setActionCommand(Integer.toString(counter)); //ActionCommand represents a unique idenfication for the button
      courses.add(course); //adds button to the LinkedList of course buttons
      counter++;
      course.setPreferredSize(new Dimension(230, 105));
      buttonResults.add(course);
    }
    
    searchTab.add(buttonResults);
    
  }
  
  
  private void addToCalendar(int index, Course c) {
    //determines the position of an added course on the calendar and changes the display of corresponding buttons
    String[] dates = c.getDate();
    
    Time t1 = new Time(c.getStartTime());
    int start = t1.getTime();
    
    Time t2 = new Time(c.getEndTime());
    int end = t2.getTime();

    
    int indexStart = 0;
    int indexEnd = 0;
    
    //if the time of each button is within start time and end time of the course, find start/end indices
    for (int p = 0; p <  times.length; p++) {
      if (times[p] >= start) {
        indexStart = p;
        break;
      }
    }
    
    for (int q = times.length - 1; q > indexStart; q--) {
      if (times[q] <= end) {
        indexEnd = q;
        break;
      }
    }
    
    //notes the places where the class would occur on the calendar & changes display of buttons
    for (int m = 0; m < dates.length; m++) {
      if (dates[m] != null) {
        int dayPos = Arrays.asList(days).indexOf(dates[m]); //gets the index corresponding to the day of the week
        for (int n = indexStart; n <= indexEnd; n++) {
          if (!buttons[n][dayPos].getText().equals("")) { 
            exist = true;
          }
        }
      }
    }
    if (!exist) {
      for (int m = 0; m < dates.length; m++) {
        if (dates[m] != null) {
          int dayPos = Arrays.asList(days).indexOf(dates[m]); //gets the index corresponding to the day of the week
          for (int n = indexStart; n <= indexEnd; n++) {
            //labels each button with CRN and title of course, sets a background color, and adds a mouseClicker
            JButton button = buttons[n][dayPos];
            button.setOpaque(true);
            button.setText(c.getCRN()+ " - " + c.getTitle()); 
            button.setBackground(colors[chooseColor % 9]);
            button.addMouseListener(new MouseClicker(index));
          }
        }
      }
    }
    chooseColor++;
  }
  
  private void removeFromCalendar(Course c) {
    //the reverse process of addToCalendar. changes button display to empty string, removes MouseListener
    String[] dates = c.getDate();
    
    Time t1 = new Time(c.getStartTime());
    int start = t1.getTime();
    
    Time t2 = new Time(c.getEndTime());
    int end = t2.getTime();

    
    int indexStart = 0;
    int indexEnd = 0;
    
    for (int p = 0; p <  times.length; p++) {
      if (times[p] >= start) {
        indexStart = p;
        break;
      }
    }
    
    for (int q = times.length - 1; q > indexStart; q--) {
      if (times[q] <= end) {
        indexEnd = q;
        break;
      }
    }

    //unshades the places where the class would occur on the calendar
    for (int m = 0; m < dates.length; m++) {
      if (dates[m] != null) {
        int dayPos = Arrays.asList(days).indexOf(dates[m]); //gets the index corresponding to the day of the week
        for (int n = indexStart; n <= indexEnd; n++) {
          buttons[n][dayPos].setText("");
          buttons[n][dayPos].setBackground(null);
          //removes the mouselistener from the buttons corresponding to the class being removed
          MouseListener[] mouseListeners = buttons[n][dayPos].getMouseListeners();
          for (MouseListener mouseListener : mouseListeners) {
            buttons[n][dayPos].removeMouseListener(mouseListener);
          }
        }
      }
    }
  }
  
  /* Helper method that searches through finalClasses
   * and removes the class with the same crn as the text inputted
   */
  private int getCourseIndex(String crn) {
    for (int i = 0; i < finalClasses.getSize(); i++) {
      if (crn.equals(finalClasses.getClass(i).getCRN())) {
        return i;
      }
    }
    return -1;
  }
  
  
  private class ButtonListener implements ActionListener{
    //Search object, containing searchResults linked list, is an input
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
          searchResults.setText("Below are all the available sections.");
        } catch (IllegalArgumentException e){
          searchResults.setText("Invalid course name. Please search again.");
        }
      }
      else {
        int action = Integer.parseInt(event.getActionCommand()); //gets the int representation of the ActionCommand
        for (int n = 0; n < courses.size(); n++) { 
          //searches through the LinkedList of buttons to find a matching actionCommand
          if (Integer.parseInt(courses.get(n).getActionCommand()) == action) {
            addToCalendar(n, searchObj.getCourse(n));
            
            //to deal with time conflicts
            if (!exist) {
            finalClasses.addClass(searchObj.getCourse(n));
            tp.setSelectedIndex(1);
            }
            else {
              searchResults.setText("Time conflict. Please choose another section.");
              exist = false;
            }
          }
        }
        
      }
    }
  }
  private class MouseClicker implements MouseListener {
    private int i;
    public MouseClicker(int index) {
      i = index;
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    public void mouseClicked(MouseEvent e) {
      Object[] options = { "OK", "REMOVE" };
      String crn = ((JButton)e.getSource()).getText().substring(0,5); //crn of the course corresponding to said JButton
      int index = getCourseIndex(crn); //index of the course in finalClasses
      int response = JOptionPane.showOptionDialog(null, finalClasses.getClass(index).toString(), "Course Selection", 
                                                  JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,null, options, options[0]);
      if (response == 1) { 
        //once user clicks the Remove button for this course, remove course from calendar display and finalClasses
        removeFromCalendar(finalClasses.getClass(index));
        finalClasses.removeClass(index);
      }
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
  }
}

