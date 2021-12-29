import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Schedule {
    private ArrayList<Course> courseOffered;
    private String[] courseDate;
    private String courseDay;
    private String startOfHour;

    String HOUR_FORMAT = "HH:mm";
    SimpleDateFormat sdfHour = new SimpleDateFormat(HOUR_FORMAT);

    public Schedule(String day, String startOfHour){
        this.setCourseDay(getCourseDay());
        this.setStartOfHour(startOfHour);
    }

    public String getCourseDay() {
        return courseDay;
    }

    public void setCourseDay(String courseDay) {
        this.courseDay = courseDay;
    }

    public String[] getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(String[] courseDate) {
        this.courseDate = courseDate;
    }

    public String getStartOfHour() {
        return startOfHour;
    }

    public void setStartOfHour(String startOfHour) {
        this.startOfHour = startOfHour;
    }
}