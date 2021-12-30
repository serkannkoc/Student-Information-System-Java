import com.google.gson.JsonArray;

import java.util.ArrayList;

public class Course {
    private String courseName;
    private transient   ArrayList<Student> studentList;
    private int courseCredit;
    private int semester;
    private Lecturer lecturer;
    private int quota;
    private Course prequisite;
    private String prequisiteName;
    private String electiveType;
    private JsonArray courseHour;



    public Course(String courseName, int credit, int semester, Lecturer lecturer, int quota, Course prequisite, JsonArray courseHour) {
        this.courseName = courseName;
        this.courseCredit = credit;
        this.semester = semester;
        this.lecturer = lecturer;
        this.quota = quota;
        this.prequisite = prequisite;
        this.courseHour = courseHour;
    }

    public Course(){
        this.studentList= new ArrayList<Student>();
    }


    public String getPrequisiteName() {
        return prequisiteName;
    }
    public void setPrequisiteName(String prequisiteName) {
        this.prequisiteName = prequisiteName;
    }


    public void setElectiveType(String electiveType) {
        this.electiveType = electiveType;
    }

    public String getElectiveType() {
        return electiveType;
    }

    public String getCourseName() {
        return courseName;
    }

    public void enrollStudent(Student student){
      this.studentList.add(student);
    }

    public  ArrayList<Student> getEnrolledStudents(){
        return this.studentList;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(int credit) {
        this.courseCredit = credit;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public Course getPrequisite() {
        return prequisite;
    }

    public void setPrequisite(Course prequisite) {
        this.prequisite = prequisite;
    }

    public JsonArray getCourseHour() {
        return courseHour;
    }

    public void setCourseHour(JsonArray courseHour) {
        this.courseHour = courseHour;
    }
}
