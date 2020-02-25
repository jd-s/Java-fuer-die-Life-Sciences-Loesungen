package de.bit.pl2.ex04;

public class Student extends Person {
    private int matriculationNumber;
    private Program studyProgram;

    public Student(String lastName, String firstName, Program studyProgram, int matriculationNumber) {
        super(lastName, firstName);
        this.matriculationNumber = matriculationNumber;
        this.studyProgram = studyProgram;
    }

    public int getMatriculationNumber() {
        return matriculationNumber;
    }

    public void setMatriculationNumber(int matriculationNumber) {
        this.matriculationNumber = matriculationNumber;
    }

    public Program getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(Program studyProgram) {

        this.studyProgram = studyProgram;
    }
}
