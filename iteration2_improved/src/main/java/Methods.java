import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import com.google.gson.*;

public class Methods {

    // This method returns the createNewStudents parameter in the input.json file.
    public static boolean getCreateNewStudent() throws FileNotFoundException {
        File input = new File(System.getProperty("user.dir")+"/iteration2_improved/src/main/input.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();

        Boolean createNewStudents = fileObject.get("createNewStudents").getAsBoolean();

        return createNewStudents;
    }

    // This method returns a numerical semester value by comparing the year parameter
    // it receives with the semester parameter in input.json.
    public static int getSemester(int year) throws FileNotFoundException {
        File input = new File(System.getProperty("user.dir")+"/iteration2_improved/src/main/input.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();

        JsonArray jsonArrayOfCourses = fileObject.get("courses").getAsJsonArray();
        String semester = fileObject.get("semester").getAsString();

        int semester_int = 1;

        if(semester.equals("FALL"))
            semester_int = year*2-1;
        else
            semester_int = year*2;

        return semester_int;
    }

    // This method pulls the advisors from the advisor list in the input.json file.
    public static ArrayList<Advisor> getAdvisors() throws FileNotFoundException {
        ArrayList<Advisor> advisorArrayList = new ArrayList<Advisor>();

        File input = new File(System.getProperty("user.dir") + "/iteration2_improved/src/main/input.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();

        JsonArray jsonArrayOfAdvisor = fileObject.get("advisors").getAsJsonArray();

        for (JsonElement advisorElement : jsonArrayOfAdvisor) {
            JsonObject advisorJsonObject = advisorElement.getAsJsonObject();

            String name = advisorJsonObject.get("advisorName").getAsString();
            String department = advisorJsonObject.get("department").getAsString();
            String rank = advisorJsonObject.get("rank").getAsString();

            Advisor advisor = new Advisor(name,department,rank);
            advisorArrayList.add(advisor);
        }

        return advisorArrayList;
    }


    // This method pulls related courses according to the entered semester parameter.
    public static ArrayList<Course> getSemesterCourses( ArrayList<Course> courses, int semester) {
        ArrayList<Course> courseArrayList = new ArrayList<Course>();

        for(Course courseElement: courses){
            if(courseElement.getSemester()==semester) courseArrayList.add(courseElement);
        }

        return courseArrayList;
    }

    // This method pulls related courses according to the entered elective course type parameter.
    public static ArrayList<Course> getElectiveCourses( ArrayList<Course> courses, String typeOfElective) {
        ArrayList<Course> courseArrayList = new ArrayList<Course>();

        for(Course courseElement: courses){
            if(courseElement.getElectiveType()==typeOfElective) courseArrayList.add(courseElement);
        }

        return courseArrayList;
    }

    // This method creates the lessons in the input.json file with our course classes.
    public static ArrayList<Course> createCourses() throws FileNotFoundException {

        ArrayList<Course> courseArrayList = new ArrayList<Course>();

        File input = new File(System.getProperty("user.dir")+"/iteration2_improved/src/main/input.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();

        JsonArray jsonArrayOfCourses = fileObject.get("courses").getAsJsonArray();
        int semester=9;
        int i = 1;
        for (JsonElement courseElement : jsonArrayOfCourses) {
            JsonObject courseJsonObject = courseElement.getAsJsonObject();

            if(true){
                for ( ;i < semester; i++) {
                    for (JsonElement semesterElement : courseJsonObject.get(Integer.toString(i)).getAsJsonArray()) {
                        JsonObject semesterCourseJsonObject = semesterElement.getAsJsonObject();

                        Course course = new Course();
                        course.setCourseName(semesterCourseJsonObject.get("courseName").getAsString());
                        course.setCourseCredit(semesterCourseJsonObject.get("courseCredit").getAsInt());
                        course.setCourseHour(semesterCourseJsonObject.get("courseHourCode").getAsJsonArray());
                        course.setSemester(i);
                        if(semesterCourseJsonObject.has("prequisite"))
                            course.setPrequisiteName(semesterCourseJsonObject.get("prequisite").getAsString());
                        courseArrayList.add(course);
                    }
                }
            }

            if(true){
                for(JsonElement semesterElement: courseJsonObject.get("NTE").getAsJsonArray()){
                    JsonObject semesterCourseJsonObject = semesterElement.getAsJsonObject();
                    Course course = new Course();
                    course.setCourseName(semesterCourseJsonObject.get("courseName").getAsString());
                    course.setCourseCredit(semesterCourseJsonObject.get("courseCredit").getAsInt());
                    course.setCourseHour(semesterCourseJsonObject.get("courseHourCode").getAsJsonArray());

                    if(semesterCourseJsonObject.has("prequisite"))
                        course.setPrequisiteName(semesterCourseJsonObject.get("prequisite").getAsString());
                    if(semesterCourseJsonObject.get("quota").getAsInt()>0)
                        course.setQuota(semesterCourseJsonObject.get("quota").getAsInt());

                    course.setElectiveType("NTE");
                    courseArrayList.add(course);
                }
            }
            if(true){
                for(JsonElement semesterElement: courseJsonObject.get("ENG-UE").getAsJsonArray()){
                    JsonObject semesterCourseJsonObject = semesterElement.getAsJsonObject();
                    Course course = new Course();
                    course.setCourseName(semesterCourseJsonObject.get("courseName").getAsString());
                    course.setCourseCredit(semesterCourseJsonObject.get("courseCredit").getAsInt());
                    course.setCourseHour(semesterCourseJsonObject.get("courseHourCode").getAsJsonArray());

                    if(semesterCourseJsonObject.has("prequisite"))
                        course.setPrequisiteName(semesterCourseJsonObject.get("prequisite").getAsString());
                    if(semesterCourseJsonObject.get("quota").getAsInt()>0)
                        course.setQuota(semesterCourseJsonObject.get("quota").getAsInt());

                    course.setElectiveType("ENG-UE");
                    courseArrayList.add(course);
                }
            }

            if(true){
                for(JsonElement semesterElement: courseJsonObject.get("TE").getAsJsonArray()){
                    JsonObject semesterCourseJsonObject = semesterElement.getAsJsonObject();
                    Course course = new Course();
                    course.setCourseName(semesterCourseJsonObject.get("courseName").getAsString());
                    course.setCourseCredit(semesterCourseJsonObject.get("courseCredit").getAsInt());
                    course.setCourseHour(semesterCourseJsonObject.get("courseHourCode").getAsJsonArray());

                    if(semesterCourseJsonObject.has("prequisite"))
                        course.setPrequisiteName(semesterCourseJsonObject.get("prequisite").getAsString());
                    if(semesterCourseJsonObject.get("quota").getAsInt()>0)
                        course.setQuota(semesterCourseJsonObject.get("quota").getAsInt());

                    course.setElectiveType("TE");
                    courseArrayList.add(course);
                }
            }

            if(true){
                for(JsonElement semesterElement: courseJsonObject.get("ENG-FTE").getAsJsonArray()){
                    JsonObject semesterCourseJsonObject = semesterElement.getAsJsonObject();
                    Course course = new Course();
                    course.setCourseName(semesterCourseJsonObject.get("courseName").getAsString());
                    course.setCourseCredit(semesterCourseJsonObject.get("courseCredit").getAsInt());
                    course.setCourseHour(semesterCourseJsonObject.get("courseHourCode").getAsJsonArray());

                    if(semesterCourseJsonObject.has("prequisite"))
                        course.setPrequisiteName(semesterCourseJsonObject.get("prequisite").getAsString());
                    if(semesterCourseJsonObject.get("quota").getAsInt()>0)
                        course.setQuota(semesterCourseJsonObject.get("quota").getAsInt());

                    course.setElectiveType("ENG-FTE");
                    courseArrayList.add(course);
                }
            }

        }


        for(Course course3: courseArrayList){
            if ( course3.getPrequisiteName()!=null && !course3.getPrequisiteName().equals("")){
                if(true){
                    for(Course course4: courseArrayList) {
                        if(course4.getCourseName().equals(course3.getPrequisiteName()) )
                        {
                        course3.setPrequisite(course4);
                        }
                    }
                }

            }
        }

        return courseArrayList;

    }

    public static boolean checkForQuota(Course course){
        if( course.getEnrolledStudents()!=null && (course.getQuota() > course.getEnrolledStudents().size()))
            return true;
        if(course.getEnrolledStudents()==null)
            return true;

        return  false;
    }

    // This method creates new students based on the createNewStudents parameter in input.json
    // or create courseOffered information from existing students.
    public static ArrayList<Student> initializeStudents() throws FileNotFoundException {
        ArrayList<Student> studentArrayList = new ArrayList<Student>();
        ArrayList<Course> courseArrayList = createCourses();

        if(getCreateNewStudent() == true) {
            for (int year = 1; year <= 4; year++) {

                Random r = new Random();
                int random = r.nextInt(getAdvisors().size()-0) + 0;

                for (int studentCount = 0; studentCount < 70; studentCount++) {
                    Student student = new Student();

                    student.setStudentNumber(Student.generateStudentNumber(year, studentCount));
                    student.setYear(year);
                    student.setTranscriptBefore(courseArrayList);

                    student.setAdvisor(getAdvisors().get(random));

                    student.setCourseOffered(courseArrayList);

                    studentArrayList.add(student);
                    Student.studentArrayList.add(student);

                    //student.createStudentJSON();
                }
            }

            System.out.println("• " + studentArrayList.size() + " students were recreated.");
        }

        else{
            Gson gson = new Gson();
            File folder = new File(System.getProperty("user.dir")+"\\iteration2_improved\\src\\main\\students");
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                        try (Reader reader = new FileReader(System.getProperty("user.dir")+"\\iteration2_improved\\src\\main\\students\\"+listOfFiles[i].getName())) {
                        Student student = gson.fromJson(reader, Student.class);
                        student.setCourseOffered(courseArrayList);
                        student.setTranscriptBefore(courseArrayList);
                        studentArrayList.add(student);
                        student.createStudentJSON();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return studentArrayList;
    }

}
