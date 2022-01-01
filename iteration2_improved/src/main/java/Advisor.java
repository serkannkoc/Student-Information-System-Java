import java.util.ArrayList;
import java.util.Arrays;

public class Advisor extends Lecturer {
    private transient ArrayList<Student> studentArrayList;
    private transient ArrayList<String> teErrorArrayList;
    private transient ArrayList<String> projectErrorArrayList;
    private transient ArrayList<String> quotaErrorArrayList;
    private transient ArrayList<String> collisionErrorArrayList;

    public Advisor(String lecturerName, String department, String rank) {
        super(lecturerName, department, rank);
        this.studentArrayList = new ArrayList<Student>();
        this.teErrorArrayList = new ArrayList<String>();
        this.projectErrorArrayList = new ArrayList<String>();
        this.quotaErrorArrayList = new ArrayList<String>();
        this.collisionErrorArrayList = new ArrayList<String>();
    }

    public ArrayList<String> getTeErrorArrayList() {
        return teErrorArrayList;
    }

    public ArrayList<String> getProjectErrorArrayList() {
        return projectErrorArrayList;
    }

    public ArrayList<String> getQuotaErrorArrayList() {
        return quotaErrorArrayList;
    }

    public ArrayList<String> getCollisionErrorArrayList() {
        return collisionErrorArrayList;
    }

    public ArrayList<Student> getStudentArrayList() {
        return studentArrayList;
    }

    public void setStudentArrayList(ArrayList<Student> studentArrayList) {
        this.studentArrayList = studentArrayList;
    }

    public void addApprovalCourses() {

        for(int studentCount=0; studentCount<studentArrayList.size(); studentCount++)
        {
            Student student = studentArrayList.get(studentCount);
            Transcript transcript = new Transcript();
            ArrayList<TranscriptRow> transcriptRowArrayList = new ArrayList<>();
            int extraCredit=0;

            for(int transcriptBeforeCount=0; transcriptBeforeCount<student.getTranscriptBefore().getTranscriptRow().size(); transcriptBeforeCount++) {
                TranscriptRow transcriptRow = student.getTranscriptBefore().getTranscriptRow().get(transcriptBeforeCount);
                transcriptRowArrayList.add(transcriptRow);
            }

            for(int courseOfferedCount=0; courseOfferedCount<student.getCourseOffered().size(); courseOfferedCount++){
                Course course = student.getCourseOffered().get(courseOfferedCount);

                if(course.getElectiveType() != null) {
                    if (course.getElectiveType().equals("TE") && student.getTranscriptBefore().getCompletedCredit() < 155) {
                        student.addError("The advisor didnt approve TE " + course.getCourseName() + " because student completed credits less than 155");
                        teErrorArrayList.add(Integer.toString(student.getStudentNumber()));
                    }
                }

                else if(student.getTranscriptBefore().getCompletedCredit() < 165 && (course.getCourseName().equals("Engineering Project I")
                        || course.getCourseName().equals("Engineering Project II"))) {
                    student.addError("The advisor didnt approve " + course.getCourseName() + " because student completed credits less than 165");
                    projectErrorArrayList.add(Integer.toString(student.getStudentNumber()));
                }

                else {
                    TranscriptRow transcriptRow = new TranscriptRow(course);
                    transcriptRowArrayList.add(transcriptRow);
                    extraCredit += course.getCourseCredit();
                }
            }

            transcript.setTranscriptRow(transcriptRowArrayList);
            transcript.setGivenCredit(student.getTranscriptBefore().getGivenCredit()+extraCredit);
            transcript.setCompletedCredit(student.getTranscriptBefore().getCompletedCredit());
            transcript.setPoint(student.getTranscriptBefore().getPoint());
            transcript.setGano(student.getTranscriptBefore().getGano());
            student.setTranscriptAfter(transcript);
        }
    }

}