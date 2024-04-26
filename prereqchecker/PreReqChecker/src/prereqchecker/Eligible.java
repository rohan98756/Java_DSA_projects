package prereqchecker;

import java.util.*;

/**
 * 
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
 * EligibleInputFile name is passed through the command line as args[1]
 * Read from EligibleInputFile with the format:
 * 1. c (int): Number of courses
 * 2. c lines, each with 1 course ID
 * 
 * Step 3:
 * EligibleOutputFile name is passed through the command line as args[2]
 * Output to EligibleOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class Eligible {

    private AdjList Graph; 
    private ArrayList<String> CoursesTaken;
    private ArrayList<String> coursesToAdd = new ArrayList<>();

    public Eligible(AdjList Graph, ArrayList<String> CoursesTaken)
    {
        this.Graph = Graph;
        this.CoursesTaken = CoursesTaken;
    }

    // Returns true if the course is already present in the ArrayList and false otherwise
    private boolean checkIfCourseIsPresent(String course)
    {
        for(String c : coursesToAdd)
        {
            if(c.equals(course))
            {
                return true;
            }
        }

        for(String c: CoursesTaken)
        {
            if(c.equals(course))
            {
                return true;
            }
        }

        return false; 
    }

    public ArrayList<String> getCoursesTaken() {
        return CoursesTaken;
    }

    public void setCoursesTaken(ArrayList<String> coursesTaken) {
        CoursesTaken = coursesTaken;
    }

    public int findIndexOfCourse(String course) {
        ArrayList<CourseNode> courses = Graph.getCourses();

        int i = -1;

        for (int j = 0; j < Graph.getV(); j++) {
            if (courses.get(j).getCourseID().equals(course)) {
                i = j;
                break;
            }
        }
        return i;
    }

    public void addALLCoursesTaken()
    {

        for(String course: CoursesTaken)
        {
            addAllNeighborsCoursesTaken(course);
        }

        for(String course: coursesToAdd)
        {
            CoursesTaken.add(course);
        }
    }

    private void addAllNeighborsCoursesTaken(String course)
    {
          for(CourseNode ptr = Graph.getAdjList()[findIndexOfCourse(course)]; ptr!=null; ptr=ptr.getNext())
            {
                if(checkIfCourseIsPresent(ptr.getCourseID()) == false)
                {
                    coursesToAdd.add(ptr.getCourseID());
                    addAllNeighborsCoursesTaken(ptr.getCourseID());
                }
            }     
    }

    private ArrayList<String> findEligibleCourses()
    {
        ArrayList<String> eligibleCourses = new ArrayList<String>();

        ArrayList<CourseNode> courses = Graph.getCourses();

        boolean canTake= true; 
  
        CourseNode [] graph = Graph.getAdjList();
        for(int i=0; i<courses.size(); i++)
        {
            // Traverse through vertices of the graph to see if the courses have been taken
            if(checkIfCourseIsPresent(courses.get(i).getCourseID()) == false)
            {
                CourseNode ptr = graph[i];
                // If course hasn't been taken check if all edges (prereqs) have been taken
                while(ptr!=null)
                {
                    // If one of the prereqs hasn't been taken exit the loop and DO NOT ADD the COURSE
                    if(CoursesTaken.indexOf(ptr.getCourseID()) == -1)
                    {
                        canTake = false; 
                        break;
                    }
                    ptr = ptr.getNext();
                }
                // If all edges have been taken add the course
                if(canTake == true)
                {
                     eligibleCourses.add(courses.get(i).getCourseID());
                }
            }
            canTake = true;
        }

        return eligibleCourses;
    }

    public void printElgCourses()
    {
        ArrayList<String> arr = findEligibleCourses();
        for(String course: arr)
        {
            StdOut.println(course);
        }

    }

    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.Eligible <adjacency list INput file> <eligible INput file> <eligible OUTput file>");
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
    AdjList csCourses = new AdjList(vertices, courses, edge1, edge2);



    // Read from input file
    String elInputFile = args[1];
    StdIn.setFile(elInputFile);
    int c = Integer.parseInt(StdIn.readLine());
    ArrayList<String> coursesTaken = new ArrayList<>();
    for(int i=0; i<c; i++)
    {
        coursesTaken.add(StdIn.readLine());
    }
    // Make Eligible instance variable
    Eligible test = new Eligible(csCourses,coursesTaken);
    test.addALLCoursesTaken();


    // Read to output file
    String elOutputFile = args[2];
    StdOut.setFile(elOutputFile);
    test.printElgCourses();
    StdOut.close();

    


	// WRITE YOUR CODE HERE
    }
}
