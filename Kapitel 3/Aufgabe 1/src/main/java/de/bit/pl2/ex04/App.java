package de.bit.pl2.ex04;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class App 
{
    public static void main(String[] args) throws FileNotFoundException
    {
    	if(args.length < 1)
    	{
    		System.out.println("Please call with one parameter: <input path>");
    		return;
    	}
    	String inputPath = args[0];
        Scanner scanner = new Scanner(new File(inputPath));
        // Advance once to ignore the cvs header
        scanner.nextLine();
        
        // Read all students from file
        List<Student> list = new ArrayList<>();
        while(scanner.hasNextLine())
        {
        	String[] values = scanner.nextLine().split(",");
        	String firstName = values[0].trim();
        	String lastName = values[1].trim();
        	int studentId = Integer.valueOf(values[2]);
        	Program program = Program.valueOf(values[3]);
        	
        	Student student = new Student(lastName, firstName, program, studentId);
        	list.add(student);
        }
        scanner.close();

        // Sort the list
        Student[] studentArray = list.toArray(new Student[0]);
        sortByLastName(studentArray);
        List<Student> sortedList = Arrays.asList(studentArray);
        
        // Output into new csv file
        String inputPathShort = inputPath.substring(0, inputPath.lastIndexOf('.'));
        String outputPath = inputPathShort + ".sorted.csv";
        PrintWriter printWriter = new PrintWriter(new File(outputPath));
        // Print header
        printWriter.println("lastname,firstname,stundentid,course");
        for(Student student : sortedList)
        {
        	printWriter.println(student.getLastName() + "," + student.getFirstName() + "," + student.getMatriculationNumber() + "," + student.getStudyProgram());
        }
        // Force the PrintWriter to actually write to the file now before we quit the app
        printWriter.flush();
        
        System.out.println("We are done!");
    }

    public static void sortByLastName(Student[] students)
    {                                
        for(int i = 0; i < students.length - 1; i++)
        {
            for(int j = 0; j < students.length - 1; j++)
            {
                if(students[j].getLastName().compareTo(students[j+1].getLastName()) > 0)
                {
                    Student temp = students[j];
                    students[j] = students[j+1];
                    students[j+1] = temp;
                }
            }
        }
    }
}
