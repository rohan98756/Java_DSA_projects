package prereqchecker;

// import java.util.*;

public class CourseNode {
        
        private String courseID;
        private CourseNode next;

        public CourseNode(String d, CourseNode n) {
            courseID = d;
            next = n;
        }
        
        public CourseNode() {
            this(null, null);
        }

        public String getCourseID() {
            return courseID;
        }
    
        public void setCourseID(String courseID) {
            this.courseID = courseID;
        }
    
        public CourseNode getNext() {
            return next;
        }
    
        public void setNext(CourseNode next) {
            this.next = next;
        }
        
}
