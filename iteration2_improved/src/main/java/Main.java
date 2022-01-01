import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Process process = Process.getProcess();
        process.initializeStudents();
        process.addApprovalCoursesForAllStudents();
        process.printErrorLog();
        process.createJSONForAllStudents();
    }
}