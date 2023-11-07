import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVParser {
    public static void main(String[] args) throws IOException {
        ArrayList<BufferedReader> csvScanners = new ArrayList<>();
        for(String s : args){
            csvScanners.add(new BufferedReader(new FileReader(s)));
        }
        //length-29 for Final scores, length-30 for Midterm scores
        ArrayList<String[]> rows = new ArrayList<>();
        double averageExamImprovement = 0;
        double averageLabAttendance = 0;
        int numStudents = 0;
        int sectionNum = 1;
        ArrayList<String> excellentStudents;
        for(BufferedReader sc : csvScanners){
            String line;
            excellentStudents = new ArrayList<>();
            double avgMidterm = 0;
            double avgFinal = 0;
            System.out.println("Section "+ sectionNum + " Data");
            while((line = sc.readLine()) != null){
                String[] row = line.split(",");
                if(row[row.length - 1].length() <= 2) {
                    if(row[row.length - 1].equals("A") || row[row.length - 1].equals("A-")) excellentStudents.add(row[0] + ", " + row[1]);
                    rows.add(row);
                    double avgLabAttendance = 0;
                    for(int i = 44; i > 33; i--){if(Double.parseDouble(row[row.length - i]) > 0) avgLabAttendance++;}
                    avgLabAttendance = avgLabAttendance/12;
                   // System.out.println("Student's Lab Attendance percentange is: " + avgLabAttendance);
                    averageLabAttendance += avgLabAttendance;
                    avgMidterm += Double.parseDouble(row[row.length - 31]);
                    avgFinal += Double.parseDouble(row[row.length - 30]);
                    //System.out.println("Midterm score is: " + row[row.length - 31]);
                    //System.out.println("Final score is: " + row[row.length - 30]);
                    if(Double.parseDouble(row[row.length - 31]) > 0 && Double.parseDouble(row[row.length - 30]) > 0)
                        averageExamImprovement += ( Double.parseDouble(row[row.length - 30]) - Double.parseDouble(row[row.length - 31]));
                    numStudents++;
                    //System.out.println(row[row.length - 1]);
                }
            }
            System.out.println("\nAverage Midterm Exam Score is: " + (avgMidterm/numStudents));
            System.out.println("Average Final Exam Score is: " + (avgFinal/numStudents));
            System.out.println("Average Improvement from Midterm to Final is: " + (averageExamImprovement/numStudents));
            System.out.println("Average Lab Attendance percentage is: " + (averageLabAttendance/numStudents));
            averageExamImprovement = 0;
            averageLabAttendance = 0;
            numStudents = 0;
            //System.out.println("A-students are: ");
            //for(String s : excellentStudents) System.out.println("- " + s);
            System.out.println("\n");
            sectionNum++;
            sc.close();
        }

    }

}
