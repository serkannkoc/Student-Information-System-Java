import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.gson.*;

public class Process {

    private static Process process;

    static {
        try {
            process = new Process();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean createNewStudent;
    private String semester;
    private ArrayList<Advisor> advisorArrayList = new ArrayList<>();
    private ArrayList<Student> studentArrayList = new ArrayList<>();
    private ArrayList<Course> courseArrayList = new ArrayList<>();

    private Process() throws FileNotFoundException {
        createNewStudent = getCreateNewStudentFromJSON();
        semester = getSemesterFromJSON();
        advisorArrayList = getAdvisorsFromJSON();
        courseArrayList = createCourses();
    }

    public static Process getProcess(){
        return process;
    }

    // This method returns the createNewStudents parameter in the input.json file.
    private boolean getCreateNewStudentFromJSON() throws FileNotFoundException {
        File input = new File(System.getProperty("user.dir")+"/iteration2_improved/src/main/input.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();

        Boolean createNewStudents = fileObject.get("createNewStudents").getAsBoolean();

        return createNewStudents;
    }

    private String getSemesterFromJSON() throws FileNotFoundException {
        File input = new File(System.getProperty("user.dir")+"/iteration2_improved/src/main/input.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();

        String semester = fileObject.get("semester").getAsString();

        return semester;
    }

    // This method pulls the advisors from the advisor list in the input.json file.
    private ArrayList<Advisor> getAdvisorsFromJSON() throws FileNotFoundException {
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

    // This method creates the lessons in the input.json file with our course classes.
    private ArrayList<Course> createCourses() throws FileNotFoundException {
        ArrayList<Course> myCourseArrayList = new ArrayList<>();

        File input = new File(System.getProperty("user.dir")+"/iteration2_improved/src/main/input.json");
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();

        JsonArray jsonArrayOfCourses = fileObject.get("courses").getAsJsonArray();

        for (JsonElement courseElement : jsonArrayOfCourses) {
            JsonObject courseJsonObject = courseElement.getAsJsonObject();

            for (int i=1; i <= 8; i++) {
                for (JsonElement semesterElement : courseJsonObject.get(Integer.toString(i)).getAsJsonArray()) {
                    JsonObject semesterCourseJsonObject = semesterElement.getAsJsonObject();

                    Course course = new Course();
                    course.setCourseName(semesterCourseJsonObject.get("courseName").getAsString());
                    course.setCourseCredit(semesterCourseJsonObject.get("courseCredit").getAsInt());
                    course.setCourseHour(semesterCourseJsonObject.get("courseHourCode").getAsJsonArray());
                    course.setSemester(i);
                    if(semesterCourseJsonObject.has("prequisite"))
                        course.setPrequisiteName(semesterCourseJsonObject.get("prequisite").getAsString());
                    myCourseArrayList.add(course);
                }
            }

            createElectiveCourses(myCourseArrayList, courseJsonObject, "NTE");
            createElectiveCourses(myCourseArrayList, courseJsonObject, "ENG-UE");
            createElectiveCourses(myCourseArrayList, courseJsonObject, "TE");
            createElectiveCourses(myCourseArrayList, courseJsonObject, "ENG-FTE");
        }

        return myCourseArrayList;
    }

    public ArrayList<Course> createElectiveCourses(ArrayList<Course> myCourseArrayList, JsonObject courseJsonObject, String typeOfElective){
        for(JsonElement semesterElement: courseJsonObject.get(typeOfElective).getAsJsonArray()){
            JsonObject semesterCourseJsonObject = semesterElement.getAsJsonObject();
            Course course = new Course();
            course.setCourseName(semesterCourseJsonObject.get("courseName").getAsString());
            course.setCourseCredit(semesterCourseJsonObject.get("courseCredit").getAsInt());
            course.setCourseHour(semesterCourseJsonObject.get("courseHourCode").getAsJsonArray());

            if(semesterCourseJsonObject.has("prequisite"))
                course.setPrequisiteName(semesterCourseJsonObject.get("prequisite").getAsString());
            if(semesterCourseJsonObject.get("quota").getAsInt()>0)
                course.setQuota(semesterCourseJsonObject.get("quota").getAsInt());

            course.setElectiveType(typeOfElective);
            myCourseArrayList.add(course);
        }

        return myCourseArrayList;
    }

    // This method pulls related courses according to the entered semester parameter.
    private ArrayList<Course> getSemesterCourses(int semester) {
        ArrayList<Course> myCourseArrayList = new ArrayList<Course>();

        for(Course courseElement: courseArrayList){
            if(courseElement.getSemester()==semester) myCourseArrayList.add(courseElement);
        }

        return myCourseArrayList;
    }

    // This method returns related courses according to the entered elective course type parameter.
    private ArrayList<Course> getElectiveCourses(String typeOfElective) {
        ArrayList<Course> myCourseArrayList = new ArrayList<Course>();

        for(Course courseElement: courseArrayList){
            if(courseElement.getElectiveType()==typeOfElective) myCourseArrayList.add(courseElement);
        }

        return myCourseArrayList;
    }

    private boolean checkForQuota(Course course){
        if( course.getEnrolledStudents()!=null && (course.getQuota() > course.getEnrolledStudents().size()))
            return true;
        if(course.getEnrolledStudents()==null)
            return true;

        return  false;
    }

    // This method creates new students based on the createNewStudents parameter in input.json
    // or create courseOffered information from existing students.
    public void initializeStudents() throws FileNotFoundException {

        if(createNewStudent == true) {
            for (int year = 1; year <= 4; year++) {

                Random r = new Random();
                int random = r.nextInt(advisorArrayList.size()-0) + 0;
                ArrayList<Student> studentArrayListForAdvisor = new ArrayList<>();
                Advisor advisor = advisorArrayList.get(random);

                for (int studentCount = 0; studentCount < 70; studentCount++) {
                    Student student = new Student();

                    student.setStudentName(generateRandomName());
                    student.setYear(year);
                    student.setStudentNumber(generateStudentNumber(year, studentCount));
                    student.setTranscriptBefore(createTranscript(year));
                    student.setTranscriptAfter(createTranscript(year));

                    student.setAdvisor(advisor);
                    studentArrayListForAdvisor.add(student);

                    student.setCourseOffered(createCourseOffered(year,student));

                    studentArrayList.add(student);
                }
                advisor.setStudentArrayList(studentArrayListForAdvisor);
            }

            System.out.println("• " + studentArrayList.size() + " students were recreated.\n");
        }

        else{
            Gson gson = new Gson();
            File folder = new File(System.getProperty("user.dir")+"\\iteration2_improved\\src\\main\\students");
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                        try (Reader reader = new FileReader(System.getProperty("user.dir")+"\\iteration2_improved\\src\\main\\students\\"+listOfFiles[i].getName())) {
                        Student student = gson.fromJson(reader, Student.class);
                        //student.setCourseOffered(courseArrayList);
                        //student.setTranscriptBefore(courseArrayList);
                        studentArrayList.add(student);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("• Action was taken on " + studentArrayList.size() + " existing students.");
        }
    }

    private Transcript createTranscript(int year) throws FileNotFoundException {
        int totalGivenCredit = 0;
        int totalCompletedCredit = 0;
        float point = 0;
        int semester = semesterToInt(year);

        Transcript transcript = new Transcript();
        ArrayList<TranscriptRow> transcriptRowArrayList = new ArrayList<TranscriptRow>();
        ArrayList<Course> courseArrayList = new ArrayList<>();

        for (int semesterCount = 1; semesterCount < semester; semesterCount++) {
            TranscriptRow transcriptRow;
            for (Course semesterCourse : getSemesterCourses(semesterCount)) {
                transcriptRow = new TranscriptRow(semesterCourse, generateRandomLetterGrade());

                if (transcriptRow.getLetterGrade().equals("FF") || transcriptRow.getLetterGrade().equals("FD")) {
                    totalGivenCredit += semesterCourse.getCourseCredit();
                } else {
                    totalGivenCredit += semesterCourse.getCourseCredit();
                    totalCompletedCredit += semesterCourse.getCourseCredit();
                }

                point += semesterCourse.getCourseCredit() * getNumericGradeFromLetterGrade(transcriptRow.getLetterGrade());

                courseArrayList.add(semesterCourse);
                transcriptRowArrayList.add(transcriptRow);
            }

            if(semesterCount==2){
                Random r = new Random();
                int random = r.nextInt(getElectiveCourses("NTE").size() - 0) + 0;
                Course nteCourse = getElectiveCourses("NTE").get(random);
                if (!courseArrayList.contains(nteCourse)) {
                    transcriptRow = new TranscriptRow(nteCourse, generateRandomLetterGrade());
                    transcriptRowArrayList.add(transcriptRow);
                }
            }

            else if(semesterCount==7){
                Random r = new Random();

                while(true) {
                    int random = r.nextInt(getElectiveCourses("TE").size() - 0) + 0;
                    Course teCourse = getElectiveCourses("TE").get(random);

                    if (!courseArrayList.contains(teCourse)) {
                        transcriptRow = new TranscriptRow(teCourse, generateRandomLetterGrade());
                        transcriptRowArrayList.add(transcriptRow);
                        break;
                    }
                }

                while(true) {
                    int random = r.nextInt(getElectiveCourses("ENG-UE").size() - 0) + 0;
                    Course engUeCourse = getElectiveCourses("ENG-UE").get(random);

                    if (!courseArrayList.contains(engUeCourse)) {
                        transcriptRow = new TranscriptRow(engUeCourse, generateRandomLetterGrade());
                        transcriptRowArrayList.add(transcriptRow);
                        break;
                    }
                }
            }

           /*else if(semesterCount == 8)
            {
                Random r = new Random();

                for(int count=0; count<3; count++)
                {
                    while(true) {
                        int random = r.nextInt(getElectiveCourses("TE").size() - 0) + 0;
                        Course teCourse = getElectiveCourses("TE").get(random);

                        if (!courseArrayList.contains(teCourse)) {
                            transcriptRow = new TranscriptRow(teCourse, generateRandomLetterGrade());
                            transcriptRowArrayList.add(transcriptRow);
                            break;
                        }
                    }
                }

                while(true) {
                    int random = r.nextInt(getElectiveCourses("ENG-FTE").size() - 0) + 0;
                    Course engFteCourse = getElectiveCourses("ENG-FTE").get(random);

                    if (!courseArrayList.contains(engFteCourse)) {
                        transcriptRow = new TranscriptRow(engFteCourse, generateRandomLetterGrade());
                        transcriptRowArrayList.add(transcriptRow);
                        break;
                    }
                }

                while(true) {
                    int random = r.nextInt(getElectiveCourses("NTE").size() - 0) + 0;
                    Course nteCourse = getElectiveCourses("NTE").get(random);

                    if (!courseArrayList.contains(nteCourse)) {
                        transcriptRow = new TranscriptRow(nteCourse, generateRandomLetterGrade());
                        transcriptRowArrayList.add(transcriptRow);
                        break;
                    }
                }
            }*/
        }

        transcript.setTranscriptRow(transcriptRowArrayList);
        transcript.setGivenCredit(totalGivenCredit);
        transcript.setCompletedCredit(totalCompletedCredit);
        transcript.setPoint(point);
        transcript.setGano(Math.round((point / totalGivenCredit) * 100.0) / 100.0);

        return transcript;
    }

    // This method selects the courses that the student can take according to the current semester.
    private ArrayList<Course> createCourseOffered(int year, Student student) throws FileNotFoundException {
        ArrayList<String> errorArrayList = new ArrayList<String>();
        ArrayList<Course> myCourseArrayList = new ArrayList<Course>();
        int semester_int = semesterToInt(year);

        ArrayList<Course> semesterCourses = getSemesterCourses(semester_int);

        for (Course courseElement : semesterCourses) {
            myCourseArrayList.add(courseElement);
        }

        ArrayList<Course> nteCourses = getElectiveCourses("NTE");
        ArrayList<Course> teCourses = getElectiveCourses("TE");
        ArrayList<Course> ueCourses = getElectiveCourses("ENG-UE");
        ArrayList<Course> fteCourses = getElectiveCourses("ENG-FTE");

        Random r = new Random();

        if (semester_int == 2) {
            while(true) {
                int result = r.nextInt(nteCourses.size() - 0) + 0;
                Course resCourse = nteCourses.get(result);
                if (!myCourseArrayList.contains(resCourse)) {
                    if (checkForQuota(resCourse)) {
                        resCourse.enrollStudent(student);
                        myCourseArrayList.add(resCourse);
                    } else {
                        String error = "The system did not allow " + resCourse.getCourseName() + " because quota is full!";
                        errorArrayList.add(error);
                        student.getAdvisor().getQuotaErrorArrayList().add(Integer.toString(student.getStudentNumber()));
                    }

                    break;
                }
            }

        } else if (semester_int == 7) {
            while (true) {
                int result = r.nextInt(teCourses.size() - 0) + 0;
                Course resCourse = teCourses.get(result);
                if (!myCourseArrayList.contains(resCourse)) {
                    if (checkForQuota(resCourse)) {
                        resCourse.enrollStudent(student);
                        myCourseArrayList.add(resCourse);
                    } else {
                        String error = "The system did not allow TE - " + resCourse.getCourseName() + " because quota is full!";
                        errorArrayList.add(error);
                        student.getAdvisor().getQuotaErrorArrayList().add(Integer.toString(student.getStudentNumber()));
                    }

                    break;
                }
            }

            while (true) {
                int result = r.nextInt(ueCourses.size() - 0) + 0;
                Course resCourse = ueCourses.get(result);

                if (!myCourseArrayList.contains(resCourse)) {
                    if (checkForQuota(resCourse)) {
                        resCourse.enrollStudent(student);
                        myCourseArrayList.add(resCourse);
                    } else {
                        String error = "The system did not allow ENG-UE - " + resCourse.getCourseName() + " because quota is full!";
                        errorArrayList.add(error);
                        student.getAdvisor().getQuotaErrorArrayList().add(Integer.toString(student.getStudentNumber()));
                    }

                    break;
                }
            }

        } else if (semester_int == 8) {
            for (int i = 0; i < 3; i++) {
                while(true) {
                    int result = r.nextInt(teCourses.size() - 0) + 0;
                    Course resCourse = teCourses.get(result);

                    if (!myCourseArrayList.contains(resCourse)) {
                        if (checkForQuota(resCourse)) {
                            resCourse.enrollStudent(student);
                            myCourseArrayList.add(resCourse);
                        } else {
                            String error = "The system did not allow TE - " + resCourse.getCourseName() + " because quota is full!";
                            errorArrayList.add(error);
                            student.getAdvisor().getQuotaErrorArrayList().add(Integer.toString(student.getStudentNumber()));
                        }
                    }

                    break;
                }

            }

            while (true) {
                int result = r.nextInt(fteCourses.size() - 0) + 0;
                Course resCourse = fteCourses.get(result);

                if (!myCourseArrayList.contains(resCourse)) {
                    if (checkForQuota(resCourse)) {
                        resCourse.enrollStudent(student);
                        myCourseArrayList.add(resCourse);
                    } else {
                        String error = "The system did not allow FTE - " + resCourse.getCourseName() + " because quota is full!";
                        errorArrayList.add(error);
                        student.getAdvisor().getQuotaErrorArrayList().add(Integer.toString(student.getStudentNumber()));
                    }

                    break;
                }
            }

            while (true) {
                int result = r.nextInt(nteCourses.size() - 0) + 0;
                Course resCourse = nteCourses.get(result);

                if (!myCourseArrayList.contains(resCourse)) {
                    if (checkForQuota(resCourse)) {
                        resCourse.enrollStudent(student);
                        myCourseArrayList.add(resCourse);
                    } else {
                        String error = "The system did not allow NTE - " + resCourse.getCourseName() + " because quota is full!";
                        errorArrayList.add(error);
                        student.getAdvisor().getQuotaErrorArrayList().add(Integer.toString(student.getStudentNumber()));
                    }

                    break;
                }
            }

        }


        if (student.getTranscriptBefore() != null && student.getTranscriptBefore().getTranscriptRow() != null) {
            for (int firstCourseCount = 0; firstCourseCount < student.getTranscriptBefore().getTranscriptRow().size(); firstCourseCount++) {
                String courseName = student.getTranscriptBefore().getTranscriptRow().get(firstCourseCount).getCourse().getCourseName();
                String letterGrade = student.getTranscriptBefore().getTranscriptRow().get(firstCourseCount).getLetterGrade();

                for (int secondCourseCount = 0; secondCourseCount < myCourseArrayList.size(); secondCourseCount++) {
                    if (myCourseArrayList.get(secondCourseCount).getPrequisiteName() != null && !myCourseArrayList.get(secondCourseCount).getPrequisiteName().equals("")) {
                        String prerequisiteCourseName = myCourseArrayList.get(secondCourseCount).getPrequisiteName();

                        if (courseName.equals(prerequisiteCourseName) && (letterGrade.equals("FD") || letterGrade.equals("FF"))) {
                            String error = "The system did not allow " + myCourseArrayList.get(secondCourseCount).getCourseName() + " because student failed prerequisite " + courseName;
                            errorArrayList.add(error);
                            myCourseArrayList.remove(secondCourseCount);
                            student.getAdvisor().getPrerequisiteErrorArrayList().add(Integer.toString(student.getStudentNumber()));
                        }
                    }
                }
            }
        }

        if (errorArrayList.size() >= 0)
            student.setError(errorArrayList);

        for (int firstCourseCount = 0; firstCourseCount < myCourseArrayList.size() - 1; firstCourseCount++) {
            for (int secondCourseCount = firstCourseCount + 1; secondCourseCount < myCourseArrayList.size(); secondCourseCount++) {
                for (int firstCourseHours = 0; firstCourseHours < myCourseArrayList.get(firstCourseCount).getCourseHour().size(); firstCourseHours++) {
                    for (int secondCourseHours = 0; secondCourseHours < myCourseArrayList.get(secondCourseCount).getCourseHour().size(); secondCourseHours++) {
                        boolean equal = myCourseArrayList.get(firstCourseCount).getCourseHour().get(firstCourseHours).equals(myCourseArrayList.get(secondCourseCount).getCourseHour().get(secondCourseHours));
                        if (equal) {
                            String error ="There is a collision "+ myCourseArrayList.get(firstCourseCount).getCourseName() +" "+(firstCourseHours+1) + ".hour between " + myCourseArrayList.get(secondCourseCount).getCourseName()+" "+(secondCourseHours+1)+ ".hour";
                            errorArrayList.add(error);
                            student.getAdvisor().getCollisionErrorArrayList().add(Integer.toString(student.getStudentNumber()));
                        }
                    }
                }
            }
        }

        return myCourseArrayList;
    }

    private int semesterToInt(int year) throws FileNotFoundException {
        int semester_int = 1;

        if(semester.equals("FALL"))
            semester_int = year*2-1;
        else
            semester_int = year*2;

        return semester_int;
    }

    // This method returns the numeric grade equivalent of the letter grade parameter it receives.
    private double getNumericGradeFromLetterGrade(String letterGrade){
        if(letterGrade.equals("AA"))
            return 4.0;
        else if(letterGrade.equals("BA"))
            return 3.5;
        else if(letterGrade.equals("BB"))
            return 3;
        else if(letterGrade.equals("CB"))
            return 2.5;
        else if(letterGrade.equals("CC"))
            return 2;
        else if(letterGrade.equals("DC"))
            return 1.5;
        else if(letterGrade.equals("DD"))
            return 1;
        else if(letterGrade.equals("FD"))
            return 0.5;
        else
            return 0;
    }

    public void createJSONForAllStudents(){
        for(int studentCount=0; studentCount<studentArrayList.size(); studentCount++)
            studentArrayList.get(studentCount).createStudentJSON();
    }

    public void addApprovalCoursesForAllStudents(){
        for(int advisorCount=0; advisorCount<advisorArrayList.size(); advisorCount++) {
            if(advisorArrayList.get(advisorCount).getStudentArrayList().size() > 0) {
                advisorArrayList.get(advisorCount).addApprovalCourses();
            }
        }
    }

    public void printErrorLog(){
        for(int advisorCount=0; advisorCount<advisorArrayList.size(); advisorCount++) {
            Advisor advisor = advisorArrayList.get(advisorCount);
            if(advisor.getTeErrorArrayList().size() > 0){
                List list = advisor.getTeErrorArrayList().stream().distinct().collect(Collectors.toList());
                System.out.println("> " + advisor.getLecturerName() +"\'s list of students who cannot enroll in TE courses because they have less " +
                        "than 155 credits - ("+ list.size()+" student)");
                System.out.println(list + "\n\n");
            }
            if(advisor.getProjectErrorArrayList().size() > 0){
                List list = advisor.getProjectErrorArrayList().stream().distinct().collect(Collectors.toList());
                System.out.println("> " + advisor.getLecturerName() +"\'s list of students who cannot enroll in graduation project course because they have less " +
                        "than 165 credits - ("+ list.size()+" student)");
                System.out.println(list + "\n\n");
            }
            if(advisor.getQuotaErrorArrayList().size() > 0){
                List list = advisor.getQuotaErrorArrayList().stream().distinct().collect(Collectors.toList());
                System.out.println("> " + advisor.getLecturerName() +"\'s list of students who cannot register for classes due to " +
                        "lack of quota - ("+ list.size()+" student)");
                System.out.println(list + "\n\n");
            }
            if(advisor.getCollisionErrorArrayList().size() > 0){
                List list = advisor.getCollisionErrorArrayList().stream().distinct().collect(Collectors.toList());
                System.out.println("> " + advisor.getLecturerName() +"\'s list of students with conflicting courses - " +
                        "("+ list.size()+" student)");
                System.out.println(list + "\n\n");
            }
            if(advisor.getPrerequisiteErrorArrayList().size() > 0){
                List list = advisor.getPrerequisiteErrorArrayList().stream().distinct().collect(Collectors.toList());
                System.out.println("> " + advisor.getLecturerName() +"\'s list of students who could not register for the course because " +
                        "they could not complete the prerequisite course - ("+ list.size()+" student)");
                System.out.println(list + "\n\n");
            }
        }
    }

    // This method returns a random letter grade to generate the transcript.
    private String generateRandomLetterGrade(){
        Random random = new Random();

        String[] grades = {"AA", "BA", "BB", "BB", "CB", "CC", "DC", "DD", "FD", "FF"};

        String letterGrade = grades[random.nextInt(grades.length)];

        return letterGrade;
    }

    // This method returns a random name by combining the first and last name pool.
    private String generateRandomName(){
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

    // This method generates the school number based on the student's year.
    private int generateStudentNumber(int year, int count){
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
