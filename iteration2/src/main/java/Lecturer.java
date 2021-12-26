public class Lecturer {
    private String lecturerName;
    private String department;
    private String rank;

    public Lecturer(String lecturerName, String department, String rank) {
        this.lecturerName = lecturerName;
        this.department = department;
        this.rank = rank;
    }

    public Lecturer(){

    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
