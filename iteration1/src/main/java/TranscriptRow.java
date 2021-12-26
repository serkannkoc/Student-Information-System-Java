import java.util.ArrayList;

public class TranscriptRow {
    private Course course;
    private String letterGrade;

    public TranscriptRow(Course course, String letterGrade) {
        this.course = course;
        this.letterGrade = letterGrade;
    }

    public TranscriptRow(Course course){
        this.course = course;
    }

    public TranscriptRow(ArrayList<Course> courseOffered, String letterGrade){

    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }
}