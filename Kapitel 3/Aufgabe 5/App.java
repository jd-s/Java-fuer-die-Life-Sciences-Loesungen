package de.bit.pl2.ex06;

import java.sql.*;
import java.util.Scanner;

/*
 * This class creates a database for hospitals and doctors in Entenhausen.
 * The databases contains one table for hospitals and one for doctors.
 * For the hospital table, the hospitalID (int) is the primary key, and the hospital name, the capactiy and directors
 * are columns with the properties text, int, and text, respectively.
 * For the staff table, staffID (int) is the primary key and further columns are last name (test),
 * field of profession (text), hospitalID and the ID for colaborating hospitals. The latter two require a foreign key
 * that refers to a hospitalID in the hospital table.
 * */


public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        App app = new App();
        String flag = "Y";
        do {
            System.out.println("Choose a subject...");
            System.out.println("1. Hospital");
            System.out.println("2. Doctor");
            Scanner reader1 = new Scanner(System.in);
            System.out.println("Enter a choice: ");
            int i = reader1.nextInt();

            System.out.println("Select a task...");
            System.out.println("1. Insert");
            System.out.println("2. Update");
            System.out.println("3. Delete");
            System.out.println("4. Select/Print");
            System.out.println("5. Exit");
            Scanner reader2 = new Scanner(System.in);
            System.out.println("Enter a choice: ");
            int n = reader2.nextInt();
            Connection c = null;
            Statement stmt = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:entenhausen.db");
                c.setAutoCommit(false);
                stmt = c.createStatement();
                String name = "", sql = "";
                if (i == 1) {
                    int id;
                    String hospitalName;
                    int capacity;
                    String director;
                    Scanner scanName;
                    switch (n) {

                        case 1:
                            scanName = new Scanner(System.in);
                            System.out.println("Enter Hospital Name:");
                            scanName = new Scanner(System.in);
                            hospitalName = scanName.nextLine();
                            System.out.println("Enter Capacity:");
                            capacity = scanName.nextInt();
                            System.out.println("Enter Director:");
                            scanName = new Scanner(System.in);
                            director = scanName.nextLine();
                            app.insertHospital(hospitalName, capacity, director);
                            break;

                        case 2:
                            System.out.println("Enter Hospital ID:");
                            scanName = new Scanner(System.in);
                            id = scanName.nextInt();
                            System.out.println("Enter Hospital Name:");
                            scanName = new Scanner(System.in);
                            name = scanName.nextLine();
                            System.out.println("Enter Capacity:");
                            capacity = scanName.nextInt();
                            System.out.println("Enter Director:");
                            director = scanName.nextLine();
                            app.updateHospital(id, name, capacity, director);
                            break;

                        case 3:
                            System.out.println("Enter Hospital ID:");
                            scanName = new Scanner(System.in);
                            id = scanName.nextInt();
                            app.deleteHospital(id);
                            break;

                        case 4:
                            app.selectAllHospitals();
                            break;

                        case 5:
                            System.exit(0);
                            break;

                        default:
                            System.out.println("Oops!!! Wrong Choice...");
                            break;
                    }
                } else if (i == 2) {
                    int id;
                    String lastName;
                    String field;
                    int hospitalID;
                    int collaborateID;
                    Scanner scanName;
                    switch (n) {

                        case 1:
                            scanName = new Scanner(System.in);
                            System.out.println("Enter Family Name:");
                            lastName = scanName.nextLine();
                            System.out.println("Enter Field of Profession:");
                            scanName = new Scanner(System.in);
                            field = scanName.nextLine();
                            System.out.println("Enter Hospital ID:");
                            hospitalID = scanName.nextInt();
                            System.out.println("Enter ID of collaborating Hospital:");
                            collaborateID = scanName.nextInt();
                            app.insertStaff(lastName, field, hospitalID, collaborateID);
                            break;

                        case 2:
                            scanName = new Scanner(System.in);
                            System.out.println("Enter Staff ID:");
                            id = scanName.nextInt();
                            System.out.println("Enter Family Name:");
                            scanName = new Scanner(System.in);
                            lastName = scanName.nextLine();
                            System.out.println("Enter Field of Profession:");
                            scanName = new Scanner(System.in);
                            field = scanName.nextLine();
                            System.out.println("Enter Hospital ID:");
                            hospitalID = scanName.nextInt();
                            System.out.println("Enter ID of colaborating Hospital:");
                            collaborateID = scanName.nextInt();
                            app.updateStaff(id, lastName, field, hospitalID, collaborateID);
                            break;

                        case 3:
                            System.out.println("Enter Staff ID:");
                            scanName = new Scanner(System.in);
                            id = scanName.nextInt();
                            app.deleteStaff(id);
                            break;

                        case 4:
                            app.selectAllStaff();

                            System.out.println("The database contains the following hospitals:");
                            //ResultSet r = stmt.executeQuery("SELECT HospitalID, COUNT(StaffID) AS doctorcount, COUNT(ColaborateID) AS collcount FROM STAFF WHERE ColaborateID > 0 GROUP BY HospitalID");
                            ResultSet r = stmt.executeQuery("SELECT Employer, COUNT(StaffID) AS doctorcount FROM STAFF GROUP BY Employer");
                            while (r.next()) {
                                System.out.println("HospitalID " + r.getInt("Employer") + " has " +
                                        r.getInt("doctorcount") + " doctors.");
                            }

                            ResultSet r2 = stmt.executeQuery("SELECT Hospital, COUNT(Collaborator) AS collcount FROM COLLAB GROUP BY Hospital");
                            while (r2.next()) {
                                System.out.println("HospitalID " + r.getInt("Hospital") + " has " +
                                        r.getInt("collcount") + " collaborations.");
                            }
                            break;

                        case 5:
                            System.exit(0);
                            break;

                        default:
                            System.out.println("Oops!!! Wrong Choice...");
                            break;
                    }
                }
                stmt.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            System.out.println("Continue Y OR N?");
            Scanner reader = new Scanner(System.in);
            flag = reader.nextLine();

        } while (flag.equalsIgnoreCase("Y"));
        System.exit(0);
    }

    /**
     * Connect to the entenhausen.db database or creates a new one
     *
     * @return the Connection object
     */
    private Connection connect() {
        Connection c = null;
        Statement stmt = null;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:entenhausen.db");
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys=ON");
            String sql = "CREATE TABLE IF NOT EXISTS HOSPITALS " +
                    "(HospitalID    INTEGER PRIMARY KEY," +
                    "HospitalName   TEXT              NOT NULL, " +
                    "Capacity       INT               NOT NULL, " +
                    "Director       TEXT              NOT NULL )";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS STAFF" +
                    "(StaffID       INTEGER PRIMARY KEY," +
                    "LastName       TEXT              NOT NULL, " +
                    "Field          TEXT              NOT NULL ," +
                    "Employer       INTEGER REFERENCES HOSPITALS(HospitalID) ON DELETE CASCADE ON UPDATE CASCADE )";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS COLLAB " +
                    "(CollabID    INTEGER PRIMARY KEY," +
                    "Collaborator   INTEGER REFERENCES STAFF(StaffID) ON DELETE CASCADE ON UPDATE CASCADE ," +
                    "Hospital   INTEGER REFERENCES HOSPITALS(HospitalID) ON DELETE CASCADE ON UPDATE CASCADE)";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return c;
    }

    /**
     * Insert a new row into the hospital table
     *
     * @param name     Name of the hospital
     * @param capacity Number of available beds
     * @param director Name of the hospital's director
     */
    public void insertHospital(String name, int capacity, String director) {
        String sql = "INSERT OR IGNORE INTO HOSPITALS(HospitalID, HospitalName, Capacity, Director) SELECT NULL,?, ?, ? WHERE NOT EXISTS(SELECT 1 FROM HOSPITALS WHERE HospitalName = ?)";
        try (Connection c = this.connect();
             PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, capacity);
            pstmt.setString(3, director);
            pstmt.setString(4, name);
            pstmt.executeUpdate();
            System.out.println("Inserted Successfully!!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert a new row into the staff table
     *
     * @param lastName   Name of the staff member
     * @param field      Field of profession
     * @param hospitalID ID of the hospital
     */
    public void insertStaff(String lastName, String field, int hospitalID) {
        String sql = "INSERT OR IGNORE INTO STAFF(StaffID, LastName, Field, Employer) SELECT NULL, ?, ?, ? WHERE NOT EXISTS(SELECT 1 FROM STAFF WHERE LastName = ?)";
        try (Connection c = this.connect();
             PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, lastName);
            pstmt.setString(2, field);
            pstmt.setInt(3, hospitalID);
            pstmt.setString(4, lastName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert a new row into the staff table and the collaborations table,
     * if the doctor collaborates with other hospitals
     *
     * @param lastName      Name of the staff member
     * @param field         Field of profession
     * @param hospitalID    ID of the hospital
     * @param collaborateID list of IDs of the collaborating hospitals
     */
    public void insertStaff(String lastName, String field, int hospitalID, int... collaborateID) {
        String sql = "INSERT OR IGNORE INTO STAFF(StaffID, LastName, Field, Employer) SELECT NULL, ?, ?, ? WHERE NOT EXISTS(SELECT 1 FROM STAFF WHERE LastName = ?)";
        try (Connection c = this.connect();
             PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, lastName);
            pstmt.setString(2, field);
            pstmt.setInt(3, hospitalID);
            pstmt.setString(4, lastName);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            int generatedKey = 0;
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
            insertCollab(generatedKey, collaborateID);
            System.out.println("Inserted Successfully!!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertCollab(int staffID, int... collaborateID) {
        String sql2 = "INSERT INTO COLLAB(CollabID, Collaborator, Hospital) SELECT NULL,?,? WHERE NOT EXISTS(SELECT 1 FROM COLLAB WHERE Collaborator = ? AND Hospital = ?)";
        try (Connection c = this.connect();
             PreparedStatement pstmt2 = c.prepareStatement(sql2)) {
            for (int i : collaborateID) {
                pstmt2.setInt(1, staffID);
                pstmt2.setInt(2, i);
                pstmt2.setInt(3, staffID);
                pstmt2.setInt(4, i);
                pstmt2.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete a staff member specified by the id
     *
     * @param id ID of the staff member
     */
    public void deleteStaff(int id) {
        String sql = "DELETE FROM STAFF WHERE StaffID = ?";

        try (Connection c = this.connect();
             PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Deleted Successfully!!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete a staff member specified by the id
     *
     * @param id ID of the collaboration
     */
    public void deleteCollab(int id) {
        String sql = "DELETE FROM COLLAB WHERE CollabID = ?";

        try (Connection c = this.connect();
             PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete a hospital specified by the id
     *
     * @param id ID of the hospital
     */
    public void deleteHospital(int id) {
        String sql = "DELETE FROM HOSPITALS WHERE HospitalID = ?";
        try (Connection c = this.connect();
             PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Deleted Successfully!!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Update the hospital table
     *
     * @param name     Name of the hospital
     * @param capacity Number of available beds
     * @param director Name of the hospital's director
     */
    public void updateHospital(int hospitalID, String name, int capacity, String director) {
        String sql = "UPDATE HOSPITALS SET HospitalName = ?, Capacity = ?, Director = ? WHERE HospitalID = ?";
        try (Connection c = this.connect();
             PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, capacity);
            pstmt.setString(3, director);
            pstmt.setInt(4, hospitalID);
            pstmt.executeUpdate();
            System.out.println("Updated Successfully!!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Update data of a staff member specified by the id
     *
     * @param staffID    ID of the staff member
     * @param lastName   Name of the staff member
     * @param field      Field of profession
     * @param hospitalID ID of the hospital
     */
    public void updateStaff(int staffID, String lastName, String field, int hospitalID) {
        String sql = "UPDATE STAFF SET LastName = ? , Field = ?, Employer = ? WHERE StaffID = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lastName);
            pstmt.setString(2, field);
            pstmt.setInt(3, hospitalID);
            pstmt.setInt(4, staffID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Update data of a staff member specified by the id
     *
     * @param staffID       ID of the staff member
     * @param lastName      Name of the staff member
     * @param field         Field of profession
     * @param hospitalID    ID of the hospital
     * @param collaborateID ID of the collaborating hospital
     */
    public void updateStaff(int staffID, String lastName, String field, int hospitalID, int... collaborateID) {
        String sql = "UPDATE STAFF SET LastName = ? , Field = ?, Employer = ? WHERE StaffID = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lastName);
            pstmt.setString(2, field);
            pstmt.setInt(3, hospitalID);
            pstmt.setInt(4, staffID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        for (int i : collaborateID) {
            insertCollab(staffID, i);
        }
        System.out.println("Updated Successfully!!!");
    }

    /**
     * Update collaboration data specified by the id
     *
     * @param collabID   ID of the collaboration
     * @param staffID    ID of the staff member
     * @param hospitalID ID of the hospital
     */
    public void updateCollab(int collabID, int staffID, int hospitalID) {
        String sql = "UPDATE COLLAB SET Collaborator = ?, Hospital = ? WHERE CollabID = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, staffID);
            pstmt.setInt(2, hospitalID);
            pstmt.setInt(3, collabID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * select all rows in the hospitals table
     */
    public void selectAllHospitals() {
        String sql = "SELECT HospitalID, HospitalName, Capacity, Director FROM HOSPITALS";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nHospitalID \t Name \t Capacity \t Director");
            while (rs.next()) {
                System.out.println(rs.getInt("HospitalID") + "\t" +
                        rs.getString("HospitalName") + "\t" +
                        rs.getInt("Capacity") + "\t" +
                        rs.getString("Director"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * select all rows in the staff table
     */
    public void selectAllStaff() {
        String sql = "SELECT StaffID, LastName, Field, Employer FROM STAFF";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nStaffID \t Name \t Field \t Employer ");
            while (rs.next()) {
                System.out.println(rs.getInt("StaffID") + "\t" +
                        rs.getString("LastName") + "\t" +
                        rs.getString("Field") + "\t" +
                        rs.getInt("Employer") + "\t");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * select all rows in the collaborators table
     */
    public void selectAllCollaborators() {
        String sql = "SELECT CollabID, Collaborator, Hospital FROM COLLAB";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nCollabID \t Collaborator \t  Hospital ");
            while (rs.next()) {
                System.out.println(rs.getInt("CollabID") + "\t" +
                        rs.getInt("Collaborator") + "\t" +
                        rs.getInt("Hospital") + "\t");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
