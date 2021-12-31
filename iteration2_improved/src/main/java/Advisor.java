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
            for(int transcriptBeforeCount=0; transcriptBeforeCount<student.getTranscriptBefore().getTranscriptRow().size(); transcriptBeforeCount++)
            {
                TranscriptRow transcriptRow = student.getTranscriptBefore().getTranscriptRow().get(transcriptBeforeCount);
                transcriptRowArrayList.add(transcriptRow);
            }
            for(int courseOfferedCount=0; courseOfferedCount<student.getCourseOffered().size(); courseOfferedCount++){
                Course course = student.getCourseOffered().get(courseOfferedCount);
                TranscriptRow transcriptRow = new TranscriptRow(course);
                transcriptRowArrayList.add(transcriptRow);
            }
            transcript.setTranscriptRow(transcriptRowArrayList);
            student.setTranscriptAfter(transcript);
        }
    }
}