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
 * AdjListOutputFile name is passed through the command line as args[1]
 * Output to AdjListOutputFile with the format:
 * 1. c lines, each starting with a different course ID, then
 * listing all of that course's prerequisites (space separated)
 */
public class AdjList {

  private int V; // Number of vertices
  private CourseNode[] adjList; // Graph as AdjList (stores edges)
  private ArrayList<CourseNode> courses; // Stores all courses

  // Constructor to create AdjList
  public AdjList(
    int V,
    ArrayList<CourseNode> courses,
    ArrayList<String> edge1,
    ArrayList<String> edge2
  ) {
    this.V = V;
    adjList = new CourseNode[V];
    this.courses = courses;

    // Inserts edges into adjacency list
    for (int i = 0; i < adjList.length; i++) {
      for (int j = 0; j < edge1.size(); j++) {
        if (courses.get(i).getCourseID().equals(edge1.get(j))) {
          adjList[i] = insertToFront(adjList[i], edge2.get(j));
        }
      }
    }
  }

  public int getV() {
    return V;
  }

  public void setV(int v) {
    V = v;
  }

  public CourseNode[] getAdjList() {
    return adjList;
  }

  public void setAdjList(CourseNode[] adjList) {
    this.adjList = adjList;
  }

  public ArrayList<CourseNode> getCourses() {
    return courses;
  }

  public void setCourses(ArrayList<CourseNode> courses) {
    this.courses = courses;
  }

  // Prints Graph
  public void printAdjList() {
    for (int i = 0; i < V; i++) {
      StdOut.print(courses.get(i).getCourseID());

      for (CourseNode ptr = adjList[i]; ptr != null; ptr = ptr.getNext()) {
        StdOut.print(" " + ptr.getCourseID());
      }

      StdOut.println();
    }
  }

  // Inserts a Node to the front of a LinkedList
  public CourseNode insertToFront(CourseNode front, String course) {
    CourseNode prereq = new CourseNode(course, null);

    if (front == null) {
      front = prereq;
      return prereq;
    }

    prereq.setNext(front);
    front = prereq;
    return prereq;
  }

  public static void main(String[] args) {
    if (args.length < 2) {
      StdOut.println(
        "Execute: java -cp bin prereqchecker.AdjList <adjacency list INput file> <adjacency list OUTput file>"
      );
      return;
    }

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

    String AdjListOutputFile = args[1];

    StdOut.setFile(AdjListOutputFile);

    csCourses.printAdjList();

    StdOut.close();
  }
}
