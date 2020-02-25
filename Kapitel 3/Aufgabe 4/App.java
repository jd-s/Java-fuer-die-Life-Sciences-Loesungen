package de.bit.pl2.ex06;

import java.sql.*;

/*
 * This class creates a database for hospitals and doctors in Entenhausen.
 * The databases contains one table for hospitals and one for doctors.
 * For the hospital table, the hospitalID (int) is the primary key, and the hospital name, the capacity and directors
 * are columns with the properties text, int, and text, respectively.
 * For the staff table, staffID (int) is the primary key and further columns are last name (test),
 * field of profession (text), hospitalID and the ID for collaborating hospitals. The latter two require a foreign key
 * that refers to a hospitalID in the hospital table.
 * */

/*Relations in our database:
 * Hospital : Director is 1 : 1
 * Hospital : Doctor is n : m
 * Doctor : Field of profession is 1 : 1
 * Orthopedist : Collaborations is 1 : n
 */


public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        App app = new App();

        app.insertHospital("Erpelshospital", 500, "MacMoneysac");
        app.insertHospital("Gansklinik", 800, "Zorngiebel");
        app.insertHospital("Centralhospital", 300, "Duck");
        app.insertStaff("GustavGans", "Surgeon", 1);
        app.insertStaff("DoretteDuck", "Orthopedist", 1, 2);
        app.insertStaff("GundelGaukeley", "Orthopedist", 2, 1, 3);
        app.insertStaff("EmilErpel", "Internist", 2);
        app.insertStaff("Tick", "Surgeon", 3);
        app.insertStaff("Trick", "Orthopedist", 3, 1, 2);

        app.selectAllHospitals();
        app.selectAllStaff();
        app.selectAllCollaborators();
        System.out.println("Now I'm updating staff 4");
        app.updateStaff(4, "EmilErpel", "Surgeon", 2, 1, 3);
        app.selectAllHospitals();
        app.selectAllStaff();
        app.selectAllCollaborators();
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
        String sql = "UPDATE HOSPITAL SET HospitalName = ?, Capacity = ?, Director = ? WHERE HospitalID = ?";
        try (Connection c = this.connect();
             PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, capacity);
            pstmt.setString(3, director);
            pstmt.setInt(4, hospitalID);
            pstmt.executeUpdate();
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
