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
 * ValidPreReqInputFile name is passed through the command line as args[1]
 * Read from ValidPreReqInputFile with the format:
 * 1. 1 line containing the proposed advanced course
 * 2. 1 line containing the proposed prereq to the advanced course
 *
 * Step 3:
 * ValidPreReqOutputFile name is passed through the command line as args[2]
 * Output to ValidPreReqOutputFile with the format:
 * 1. 1 line, containing either the word "YES" or "NO"
 */
public class ValidPrereq {

    private String advancedCourse;
    private String preReq;
    private AdjList graph;
    private boolean[] marked;
    ArrayList<String> courses; 

 
    public ValidPrereq(AdjList graph, String courseOne, String courseTwo) {
        advancedCourse = courseOne;
        preReq = courseTwo;
        this.graph = graph;
        marked = new boolean[graph.getV()];
    }
   
    public void validPreReqCourse(String course1, String prereq) {
        int i = findIndexOfCourse(course1);
        if(marked[i] == false)  marked[i] = true;

        for (CourseNode ptr = graph.getAdjList()[i]; ptr != null; ptr = ptr.getNext()) {
            if (!marked[findIndexOfCourse(ptr.getCourseID())])
            {
                validPreReqCourse(ptr.getCourseID(), prereq);
            }
        }
    }

    private boolean isCyclic()
    {
        // Mark all the vertices as not visited and
        // not part of recursion stack
        boolean[] visited = new boolean[graph.getV()];
        boolean[] recStack = new boolean[graph.getV()];
 
        // Call the recursive helper function to
        // detect cycle in different DFS trees
        for (int i = 0; i < graph.getV(); i++)
            if (isCyclicUtil(i, visited, recStack))
                return true;
 
        return false;
    }

     private boolean isCyclicUtil(int i, boolean[] visited,
                                 boolean[] recStack)
    {
        // Mark the current node as visited and
        // part of recursion stack
        if (recStack[i])
            return true;
 
        if (visited[i])
            return false;
 
        visited[i] = true;
 
        recStack[i] = true;
        CourseNode front = graph.getAdjList()[i];

        for(CourseNode ptr = front; ptr!=null; ptr=ptr.getNext())
        {
            if(isCyclicUtil(findIndexOfCourse(ptr.getCourseID()),visited,recStack))
                return true; 
        }
        recStack[i] = false;
 
        return false;
    }

    public boolean visited(String course) {
        return marked[findIndexOfCourse(course)];
    }

    public int findIndexOfCourse(String course) {
        ArrayList<CourseNode> courses = graph.getCourses();
        int i = -1;

        for (int j = 0; j < graph.getV(); j++) {
            if (courses.get(j).getCourseID().equals(course)) {
                i = j;
                break;
            }
        }
        return i;
    }

    public boolean edgeExists(String course1, String prereq)
    {
        CourseNode[] allEdges = graph.getAdjList();

        int index = findIndexOfCourse(course1);
        CourseNode front = allEdges[index];
        CourseNode ptr = front;
        
        while(ptr!=null)
        {
            if(ptr.getCourseID().equals(prereq))
            {
                return true;
            }
            ptr = ptr.getNext();
        }

        return false;
    }

    public boolean addEdge(String course1, String prereq) {
        ArrayList<CourseNode> courses = graph.getCourses();
        CourseNode[] allEdges = graph.getAdjList();

        int i = -1;

        for (int j = 0; j < graph.getV(); j++) {
            if (courses.get(j).getCourseID().equals(course1)) {
                i = j;
                break;
            }
        }
        if (i == -1)
            return false;

        CourseNode ptr = allEdges[i];
        if(ptr == null)
        {
           allEdges[i] = new CourseNode(prereq,null);
           return true; 

        }
        while (ptr.getNext() != null) {
            ptr = ptr.getNext();
        }
        ptr.setNext(new CourseNode(prereq, null));
        return true;
    }

    public String getAdvancedCourse() {
        return advancedCourse;
    }

    public void setAdvancedCourse(String advancedCourse) {
        this.advancedCourse = advancedCourse;
    }

    public String getPreReq() {
        return preReq;
    }

    public void setPreReq(String preReq) {
        this.preReq = preReq;
    }

    public AdjList getGraph() {
        return graph;
    }

    public void setGraph(AdjList graph) {
        this.graph = graph;
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            StdOut.println(
                    "Execute: java -cp bin prereqchecker.ValidPrereq <adjacency list INput file> <valid prereq INput file> <valid prereq OUTput file>");
            return;
        }
        // WRITE YOUR CODE HERE

        // Reading first file
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

        // Reading second file
        String validPreqInputFile = args[1];
        StdIn.setFile(validPreqInputFile);
        String courseIDOne = StdIn.readLine();
        String courseIDTwo = StdIn.readLine();
        ValidPrereq such = new ValidPrereq(csCourses, courseIDOne, courseIDTwo);

        if(such.edgeExists(courseIDOne, courseIDTwo) == false)
        {
            such.addEdge(courseIDOne, courseIDTwo);
        }
      
        // Reading third (output) file
        String ValidPreReqOutputFile = args[2];
        StdOut.setFile(ValidPreReqOutputFile);

       if(such.isCyclic())
       {
         StdOut.print("NO");
       }else{
         StdOut.print("YES");
       }

        StdOut.close();
    }
}
