package Common;
public class Student {
	public long studentid;

	public String studentName;

	public long getStudentid() {
		return studentid;
	}

	public void setStudentid(long studentid) {
		this.studentid = studentid;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Long getStudentId() {
		return studentid;
	}

	public void setStudId(int i) {
		this.studentid = i;
	}

	public String list() {
		return studentid + " " + studentName;
	}

}