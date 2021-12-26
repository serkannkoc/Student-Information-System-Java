import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Methods.initializeStudents();
        Advisor.addApprovalCourses(Student.studentArrayList);
        for(int i = 0;i < Student.studentArrayList.size();i++){
            Student.studentArrayList.get(i).createStudentJSON();
        }
    }

}