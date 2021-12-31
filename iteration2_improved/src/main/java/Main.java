import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Process process = Process.getProcess();

        Advisor advisor = process.getAdvisorArrayList().get(0);
        process.initializeStudents();

        advisor.addApprovalCourses(process.getStudentArrayList());

        process.createJSONForAllStudents();


    }

}