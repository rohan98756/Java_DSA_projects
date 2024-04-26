package prereqchecker;

import java.util.*;

/**
 * Steps to implement this class main method:
 *
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 *
 * Step 2:
 * NeedToTakeInputFile name is passed through the command line as args[1]
 * Read from NeedToTakeInputFile with the format:
 * 1. One line, containing a course ID
 * 2. c (int): Number of courses
 * 3. c lines, each with one course ID
 *
 * Step 3:
 * NeedToTakeOutputFile name is passed through the command line as args[2]
 * Output to NeedToTakeOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class NeedToTake {

  public static void main(String[] args) {
    if (args.length < 3) {
      StdOut.println(
        "Execute: java NeedToTake <adjacency list INput file> <need to take INput file> <need to take OUTput file>"
      );
      return;
    }

    // Construct graph
    String filename = args[0];
    StdIn.setFile(filename);
    int vertices = Integer.parseInt(StdIn.readLine());
    ArrayList<CourseNode> courses = new ArrayList<>();
    for (int i = 0; i < vertices; i++) {
      String course = StdIn.readLine();
      CourseNode current = new CourseNode(course, null);
      courses.add(current);
    }
    int edges = Integer.parseInt(StdIn.readLine());
    ArrayList<String> edge1 = new ArrayList<>();
    ArrayList<String> edge2 = new ArrayList<>();
    for (int i = 0; i < edges; i++) {
      String course = StdIn.readString();
      String preq = StdIn.readString();
      edge1.add(course);
      edge2.add(preq);
    }
    AdjList Graph = new AdjList(vertices, courses, edge1, edge2);

    String needToTakein = args[1];
    StdIn.setFile(needToTakein);

    String targetCourse = StdIn.readLine();

    int d = Integer.parseInt(StdIn.readLine());

    ArrayList<String> CoursesTaken = new ArrayList<String>();

    for (int i = 0; i < d; i++) {
      CoursesTaken.add(StdIn.readLine());
    }

    Eligible take = new Eligible(Graph, CoursesTaken);
    take.addALLCoursesTaken();
    // Stores all courses currently taken including all prereqs
    ArrayList<String> list = take.getCoursesTaken();

   
    
    CourseNode front = Graph.getAdjList()[take.findIndexOfCourse(targetCourse)];
    CourseNode ptr = front;

    // Stores all the courses that still need to be taken
    ArrayList<CourseNode> ccc = new ArrayList<>();

    ArrayList<String> boo = new ArrayList<>();

    while (ptr != null) {
      for (String c : list) {
        if (ptr.getCourseID().equals(c)) {
          continue;
        } else {
          ccc.add(ptr);
        }
      }
      ptr = ptr.getNext();
    }

    for (CourseNode a : ccc) {
      boo.add(a.getCourseID());
    }

    Eligible take2 = new Eligible(Graph, boo);
    take2.addALLCoursesTaken();
    ArrayList<String> chee = take2.getCoursesTaken();
   // ArrayList<String> b = new ArrayList<String>();


    chee.removeAll(list);

    String NeedToTakeOutputFile = args[2];
    StdOut.setFile(NeedToTakeOutputFile);

    ArrayList<String> newList = new ArrayList<String>(); 
  
        // Traverse through the first list 
        for (String element : chee) { 
  
            // If this element is not present in newList 
            // then add it 
            if (!newList.contains(element)) { 
  
                newList.add(element); 
            } 
        } 
  
        // return the new list 
      
    for(String beee: newList)
    {
      StdOut.println(beee);
    }

    // WRITE YOUR CODE HERE
  }
}
