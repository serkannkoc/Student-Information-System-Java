import java.util.ArrayList;

public class Transcript {
    private Student student;
    private ArrayList<TranscriptRow> transcriptRow;
    private double givenCredit;
    private double completedCredit;
    private double point;
    private double gano;

    public Transcript(Student student, ArrayList<TranscriptRow> transcriptRow, double givenCredit, double completedCredit, double point, double gano) {
        this.student = student;
        this.transcriptRow = transcriptRow;
        this.givenCredit = givenCredit;
        this.completedCredit = completedCredit;
        this.point = point;
        this.gano = gano;
    }

    public Transcript(){
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ArrayList<TranscriptRow> getTranscriptRow() {
        return transcriptRow;
    }

    public void setTranscriptRow(ArrayList<TranscriptRow> transcriptRow) {
        this.transcriptRow = transcriptRow;
    }

    public double getGivenCredit() {
        return givenCredit;
    }

    public void setGivenCredit(double givenCredit) {
        this.givenCredit = givenCredit;
    }

    public double getCompletedCredit() {
        return completedCredit;
    }

    public void setCompletedCredit(double completedCredit) {
        this.completedCredit = completedCredit;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public double getGano() {
        return gano;
    }

    public void setGano(double gano) {
        this.gano = gano;
    }
}
