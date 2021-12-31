import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.*;

public class Student {
    private int studentNumber;
    private String studentName;
    private int year;
    private ArrayList<Course> courseTaken;
    private Advisor advisor;
    private Transcript transcriptBefore;
    private ArrayList<Course> courseOffered;
    private Transcript transcriptAfter;
    private ArrayList<String> error;

    public Student() {
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<Course> getCourseTaken() {
        return courseTaken;
    }

    public void setCourseTaken(ArrayList<Course> courseTaken) {
        this.courseTaken = courseTaken;
    }

    public Advisor getAdvisor() {
        return advisor;
    }

    public void setAdvisor(Advisor advisor) {
        this.advisor = advisor;
    }

    public Transcript getTranscriptBefore() {
        return transcriptBefore;
    }

    public void setTranscriptBefore(Transcript transcriptBefore) {
        this.transcriptBefore = transcriptBefore;
        this.transcriptAfter = transcriptBefore;
    }

    public ArrayList<Course> getCourseOffered() {
        return courseOffered;
    }

    public void setCourseOffered(ArrayList<Course> courseOffered) {
        this.courseOffered = courseOffered;
    }

    public Transcript getTranscriptAfter() {
        return transcriptAfter;
    }

    public void setTranscriptAfter(Transcript transcriptAfter) {
        this.transcriptAfter = transcriptAfter;
    }

    public ArrayList<String> getError() {
        return error;
    }

    public void setError(ArrayList<String> error) {
        this.error = error;
    }

    // This method takes a student parameter and creates a JSON file with the student's attributes.
    public void createStudentJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(System.getProperty("user.dir") + "\\iteration2_improved\\src\\main\\students\\" + this.getStudentNumber() + ".json")) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
