package Common;
import java.util.Comparator;


public class StudentSorting implements Comparator<Student> {
	public int compare(Student o1, Student o2) {    
		return Long.valueOf(o1.getStudentId()).compareTo(Long.valueOf((o2.getStudentId())));      
	}  
}
