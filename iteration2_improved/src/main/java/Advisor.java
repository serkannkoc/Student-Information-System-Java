import java.util.ArrayList;

public class Advisor extends Lecturer {
    public Advisor(String lecturerName, String department, String rank) {
        super(lecturerName, department, rank);
    }

    public static void addApprovalCourses(ArrayList<Student> studentarray) {

        int NoOfStuWithoutEngProject = 0;

        for (int i = 0; i < Student.studentArrayList.size(); ++i) {
            for (int j = 0; j < Student.studentArrayList.get(i).getCourseOffered().size(); j++) {
                ArrayList<TranscriptRow> transcriptRowArrayList = new ArrayList<TranscriptRow>();
                TranscriptRow transcriptRow = new TranscriptRow(Student.studentArrayList.get(i).getCourseOffered().get(j));
                if (Student.studentArrayList.get(i).getTranscriptBefore().getTranscriptRow() != null) {

                    if (!(Student.studentArrayList.get(i).getCourseOffered().get(j).getCourseName().equals("Engineering Project I") ||
                            Student.studentArrayList.get(i).getCourseOffered().get(j).getCourseName().equals("Engineering Project II"))) {

                        Student.studentArrayList.get(i).getTranscriptAfter().getTranscriptRow().add(transcriptRow);

                    } else if (Student.studentArrayList.get(i).getTranscriptBefore().getCompletedCredit() >= 165) {

                        Student.studentArrayList.get(i).getTranscriptAfter().getTranscriptRow().add(transcriptRow);

                    } else NoOfStuWithoutEngProject++;

                } else {
                    transcriptRowArrayList.add(transcriptRow);
                    Student.studentArrayList.get(i).getTranscriptAfter().setTranscriptRow(transcriptRowArrayList);
                }
            }
        }
    }
}