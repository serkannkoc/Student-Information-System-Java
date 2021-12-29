import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private Schedule schedule;
    private ArrayList<String> error;

    static ArrayList<Student> studentArrayList = new ArrayList<Student>();

    public Student() {
        this.studentName = generateRandomName();
    }

    ;

    public Student(int studentNumber, int year, ArrayList<Course> courseTaken,
                   Advisor advisor, Schedule schedule, ArrayList<String> error, ArrayList<Course> courses)
            throws FileNotFoundException {
        this.studentNumber = studentNumber;
        this.studentName = generateRandomName();
        this.year = year;
        this.courseTaken = courseTaken;
        this.advisor = advisor;
        this.schedule = schedule;
        this.error = error;

        setTranscriptBefore(courses);

        setCourseOffered(courses);
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

    public void setTranscriptBefore(ArrayList<Course> courses) throws FileNotFoundException {
        int totalGivenCredit = 0;
        int totalCompletedCredit = 0;
        float point = 0;
        int semester = Methods.getSemester(year);

        Transcript transcript = new Transcript();
        ArrayList<TranscriptRow> transcriptRowArrayList = new ArrayList<TranscriptRow>();

        for (int i = 1; i < semester; i++) {
            for (Course semesterCourse : Methods.getSemesterCourses(courses, i)) {
                TranscriptRow transcriptRow = new TranscriptRow(semesterCourse, generateRandomLetterGrade());

                if (transcriptRow.getLetterGrade().equals("FF") || transcriptRow.getLetterGrade().equals("FD")) {
                    totalGivenCredit += semesterCourse.getCourseCredit();
                } else {
                    totalGivenCredit += semesterCourse.getCourseCredit();
                    totalCompletedCredit += semesterCourse.getCourseCredit();
                }

                point += semesterCourse.getCourseCredit() * Methods.getNumericGradeFromLetterGrade(transcriptRow.getLetterGrade());

                transcriptRowArrayList.add(transcriptRow);
            }

        }

        transcript.setTranscriptRow(transcriptRowArrayList);
        transcript.setGivenCredit(totalGivenCredit);
        transcript.setCompletedCredit(totalCompletedCredit);
        transcript.setPoint(point);
        transcript.setGano(Math.round((point / totalGivenCredit) * 100.0) / 100.0);

        this.transcriptBefore = transcript;
        this.transcriptAfter = transcript;
    }

    // This method creates the transcript before the student chooses a course.
    public Transcript getTranscriptBefore() {
        return transcriptBefore;
    }

    public Transcript getTranscriptAfter() {
        return transcriptAfter;
    }

    public List<Course> takeCourse(Course course) {
        courseTaken.add(course);
        return courseTaken;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Course> getCourseTaken() {
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

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public ArrayList<Course> getCourseOffered() {
        return courseOffered;
    }

    // This method selects the courses that the student can take according to the current semester.
    public void setCourseOffered(ArrayList<Course> courses) throws FileNotFoundException {

        ArrayList<Course> courseArrayList = new ArrayList<Course>();
        int semester_int = Methods.getSemester(year);
        ArrayList<String> errorArrayList = new ArrayList<String>();

        ArrayList<Course> semesterCourses = Methods.getSemesterCourses(courses, semester_int);

        for (Course courseElement : semesterCourses) {
            courseArrayList.add(courseElement);
        }

        ArrayList<Course> nteCourses = Methods.getElectiveCourses(courses, "NTE");
        ArrayList<Course> teCourses = Methods.getElectiveCourses(courses, "TE");
        ArrayList<Course> ueCourses = Methods.getElectiveCourses(courses, "ENG-UE");
        ArrayList<Course> fteCourses = Methods.getElectiveCourses(courses, "ENG-FTE");

        Random r = new Random();

        if (semester_int == 2) {
            int result = r.nextInt(nteCourses.size() - 0) + 0;
            Course resCourse = nteCourses.get(result);
            if (!courseArrayList.contains(resCourse)) {

                if (Methods.checkForQuota(resCourse)) {
                    resCourse.enrollStudent(this);
                    courseArrayList.add(resCourse);
                } else {
                    String error = "The system did not allow " + resCourse.getCourseName() + " because quota is full!";
                    errorArrayList.add(error);
                }
            }

        } else if (semester_int == 7) {
            if (true) {
                int result = r.nextInt(teCourses.size() - 0) + 0;
                Course resCourse = teCourses.get(result);
                if (!courseArrayList.contains(resCourse)) {

                    if (Methods.checkForQuota(resCourse)) {
                        resCourse.enrollStudent(this);
                        courseArrayList.add(resCourse);
                    } else {
                        String error = "The system did not allow " + resCourse.getCourseName() + " because quota is full!";
                        errorArrayList.add(error);
                    }
                }
            }

            if (true) {
                int result = r.nextInt(ueCourses.size() - 0) + 0;
                Course resCourse = ueCourses.get(result);

                if (!courseArrayList.contains(resCourse)) {

                    if (Methods.checkForQuota(resCourse)) {
                        resCourse.enrollStudent(this);
                        courseArrayList.add(resCourse);
                    } else {
                        String error = "The system did not allow " + resCourse.getCourseName() + " because quota is full!";
                        errorArrayList.add(error);
                    }
                }
            }
        } else if (semester_int == 8) {
            for (int i = 0; i < 3; i++) {
                int result = r.nextInt(teCourses.size() - 0) + 0;
                Course resCourse = teCourses.get(result);

                if (!courseArrayList.contains(resCourse)) {

                    if (Methods.checkForQuota(resCourse)) {
                        resCourse.enrollStudent(this);
                        courseArrayList.add(resCourse);
                    } else {
                        String error = "The system did not allow " + resCourse.getCourseName() + " because quota is full!";
                        errorArrayList.add(error);
                    }
                }

            }

            if (true) {
                int result = r.nextInt(fteCourses.size() - 0) + 0;
                Course resCourse = fteCourses.get(result);

                if (!courseArrayList.contains(resCourse)) {

                    if (Methods.checkForQuota(resCourse)) {
                        resCourse.enrollStudent(this);
                        courseArrayList.add(resCourse);
                    } else {
                        String error = "The system did not allow " + resCourse.getCourseName() + " because quota is full!";
                        errorArrayList.add(error);
                    }
                }
            }
            if (true) {
                int result = r.nextInt(nteCourses.size() - 0) + 0;
                Course resCourse = nteCourses.get(result);

                if (!courseArrayList.contains(resCourse)) {

                    if (Methods.checkForQuota(resCourse)) {
                        resCourse.enrollStudent(this);
                        courseArrayList.add(resCourse);
                    } else {
                        String error = "The system did not allow " + resCourse.getCourseName() + " because quota is full!";
                        errorArrayList.add(error);
                    }
                }
            }

        }


        if (this.getTranscriptBefore() != null) {
            if (getTranscriptBefore().getTranscriptRow() != null) {
                for (int i = 0; i < this.getTranscriptBefore().getTranscriptRow().size(); i++) {
                    String courseName = this.getTranscriptBefore().getTranscriptRow().get(i).getCourse().getCourseName();
                    String letterGrade = this.getTranscriptBefore().getTranscriptRow().get(i).getLetterGrade();
                    Student a = this;
                    for (int j = 0; j < courseArrayList.size(); j++) {
                        if (courseArrayList.get(j).getPrequisiteName() != null && !courseArrayList.get(j).getPrequisiteName().equals("")) {
                            String prequisiteCourseName = courseArrayList.get(j).getPrequisiteName();

                            if (courseName.equals(prequisiteCourseName) && (letterGrade.equals("FD") || letterGrade.equals("FF"))) {
                                String error = "The system did not allow " + courseArrayList.get(j).getCourseName() + " because student failed prerequisite " + courseName;
                                errorArrayList.add(error);
                                courseArrayList.remove(j);
                            }
                        }
                    }
                }
            }

            if (errorArrayList.size() > 0)
                this.error = errorArrayList;

        }
        this.courseOffered = courseArrayList;



    }

    public ArrayList<String> getError() {
        return error;
    }

    public void setError(ArrayList<String> error) {
        this.error = error;
    }

    // This method returns a random name by combining the first and last name pool.
    public static String generateRandomName(){
        Random random = new Random();

        String[] firstNames = {"Ahmet", "Mehmet", "Mustafa", "Zeynep", "Elif", "Defne", "Kerem", "Azra", "Miran",
                "Asya", "Hamza", "Öykü", "Ömer Asaf", "Ebrar", "Ömer", "Eylül", "Eymen", "Murat", "Hesna", "Ali", "Nuri",
                "Muhammed", "Gökay", "Koray", "Esra", "Bihter", "Ceyda", "Özge", "Özlem", "Önder", "Ramazan", "Recep",
                "Rabia", "Hilal", "Buse", "Başak", "Serkan", "Ulaş", "Deniz", "Kardelen", "Mervan", "Kübra", "Atilla",
                "İlhan", "Fatih", "Asena", "Ahsen", "Ferit", "Kemal"};
        String[] lastNames = {"Yılmaz", "Deniz", "Karakurt", "Şahin", "Türkay", "Akyıldız", "Yıldız", "Güçlü",
                "Temur", "Duraner", "Yılmazer", "Ekşi", "Ballıca", "Erdoğmuş", "Erdoğan", "Korkmaz", "Çubukluöz",
                "Hüyüktepe", "Zengin", "Büyük", "Küçük", "Türk", "Turgut", "Boz", "Açıkgöz", "Öztürk", "Beler",
                "Çetin", "Bilgin", "Yalçınkaya", "Adıgüzel", "Kartal", "Dinç", "Genç", "Kuruçay", "Parlak", "Karakaya",
                "Kaya"};

        String fullName = firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)];

        return fullName;
    }

    // This method returns a random letter grade to generate the transcript.
    public static String generateRandomLetterGrade(){
        Random random = new Random();

        String[] grades = {"AA", "BA", "BB", "CB", "CC", "DC", "DD", "FD", "FF"};

        String letterGrade = grades[random.nextInt(grades.length)];

        return letterGrade;
    }

    // This method generates the school number based on the student's year.
    public static int generateStudentNumber(int year, int count){
        String numberStart = Integer.toString(150122-year);
        numberStart+="0";

        String studentNumber = "";

        if(count>=9) {
            studentNumber = numberStart + Integer.toString(count + 1);
        }
        else{
            studentNumber = numberStart + Integer.toString(count / 10) + (count + 1);
        }

        return Integer.parseInt(studentNumber);
    }
}
