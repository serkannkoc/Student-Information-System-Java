import java.util.ArrayList;

public class Advisor extends Lecturer {

    public Advisor(String lecturerName, String department, String rank) {
        super(lecturerName, department, rank);
    }

    public void addApprovalCourses(ArrayList<Student> studentArrayList) {

        for(int studentCount=0; studentCount<studentArrayList.size(); studentCount++)
        {
            Student student = studentArrayList.get(studentCount);
            Transcript transcript = new Transcript();
            ArrayList<TranscriptRow> transcriptRowArrayList = new ArrayList<>();
            int credit=0;

            for(int transcriptBeforeCount=0; transcriptBeforeCount<student.getTranscriptBefore().getTranscriptRow().size(); transcriptBeforeCount++) {
                TranscriptRow transcriptRow = student.getTranscriptBefore().getTranscriptRow().get(transcriptBeforeCount);
                transcriptRowArrayList.add(transcriptRow);
            }

            for(int courseOfferedCount=0; courseOfferedCount<student.getCourseOffered().size(); courseOfferedCount++){
                Course course = student.getCourseOffered().get(courseOfferedCount);

                if(course.getElectiveType() != null) {
                    if (course.getElectiveType().equals("TE") && student.getTranscriptBefore().getCompletedCredit() < 155)
                        student.addError("The advisor didnt approve TE " + course.getCourseName() + " because student completed credits less than 155");
                }

                else if(student.getTranscriptBefore().getCompletedCredit() < 165 && (course.getCourseName().equals("Engineering Project I")
                        || course.getCourseName().equals("Engineering Project II")))
                    student.addError("The advisor didnt approve " + course.getCourseName() + " because student completed credits less than 165");

                else {
                    TranscriptRow transcriptRow = new TranscriptRow(course);
                    transcriptRowArrayList.add(transcriptRow);
                    credit += course.getCourseCredit();
                }
            }

            transcript.setTranscriptRow(transcriptRowArrayList);
            transcript.setGivenCredit(student.getTranscriptBefore().getGivenCredit()+credit);
            transcript.setCompletedCredit(student.getTranscriptBefore().getCompletedCredit());
            transcript.setPoint(student.getTranscriptBefore().getPoint());
            transcript.setGano(student.getTranscriptBefore().getGano());
            student.setTranscriptAfter(transcript);
        }
    }
}