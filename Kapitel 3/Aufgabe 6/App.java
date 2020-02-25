package de.bit.pl2.ex06;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
	private static Connection db;

    public static void main(String args[]) {    	
        try {
        	openDatabase();
            Pattern pattern = Pattern.compile("import|group");
            if (args == null || args.length == 0) {
                System.out.println("Please use either --import <file> or --group <name>");
                return;
            }
            Matcher matcher = pattern.matcher(args[0]);
        	if(args.length != 2 && matcher.find()) {
        		System.out.println("Please use either --import <file> or --group <name>");
        		return;
        	}
        	// Allow the user to use as many dashes as he wants
        	String command = args[0].replace("-", "");
        	String param = args[1];

        	if(command.equalsIgnoreCase("import"))
        	{
                Scanner scanner = new Scanner(new File(param));
                // Advance once to ignore the csv header
                scanner.nextLine();

                // Read all members from file
                while(scanner.hasNextLine())
                {
                	String[] values = scanner.nextLine().split(",");
                	String group = values[0].trim();
                	String name = values[1].trim();
                	String matrikel = values[2].trim();
                	String mail = values[3].trim();

                	executeUpdateSql("INSERT INTO members (groupname, name, matrikel, mail) VALUES ('" + group + "', '" + name + "', '" + matrikel + "', '" + mail + "');");
                	System.out.println("Inserted " + name);
                }
                scanner.close();
                System.out.println("Done");
        	}
        	else if(command.equalsIgnoreCase("group"))
        	{
                try(Statement stmt = db.createStatement())
                {
                	String sql = "SELECT name,mail FROM members WHERE groupname = '" + param + "'";
                    ResultSet result = stmt.executeQuery(sql);
                    // Create lists to store names and mails read from the database
                    List<String> names = new ArrayList<>();
                    List<String> mails = new ArrayList<>();
                    // Actually read the data and add to the lists
                    while(result.next())
                    {
                    	String name = result.getString("name");
                    	String mail = result.getString("mail");
                    	names.add(name);
                    	mails.add(mail);
                    }
                    // Early out if there are no values for the given group
                    if(names.isEmpty())
                    {
                    	System.out.println("No members found for group " + param);
                    	System.exit(0);
                    }
                    // Print all the mail addresses separated by semicolon
                    for(int i = 0; i < mails.size(); i++)
                    {
                    	String mail = mails.get(i);
                    	System.out.print(mail);
                    	if(i < mails.size() - 1)
                    		System.out.print(";");
                    }
                    System.out.println("");
                    // Print all the names separated by comma
                    System.out.print("Dear ");
                    for(int i = 0; i < names.size(); i++)
                    {
                    	String name = names.get(i);
                    	System.out.print(name);
                    	if(i < names.size() - 2)
                    		System.out.print(", ");
                    	else if(i == names.size() - 2)
                    		System.out.print(" and ");
                    }
                }    
        	}
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            // Exit with non-zero error code to indicate something went wrong
            System.exit(1);
        }
    }
    
    private static void openDatabase() throws Exception
    {
        Class.forName("org.sqlite.JDBC");

        // Opens existing database or creates a new one
        db = DriverManager.getConnection("jdbc:sqlite:groups.db");
        //System.out.println("Opened database successfully");
        
        String sql = "CREATE TABLE IF NOT EXISTS members " +
                "(id		INTEGER	PRIMARY KEY AUTOINCREMENT	NOT NULL," +
                "groupname	TEXT								NOT NULL, " +
                "name		TEXT								NOT NULL," +
                "matrikel	TEXT								NOT NULL," +
                "mail		TEXT								NOT NULL)";
        executeUpdateSql(sql);
    }

    private static void executeUpdateSql(String sql) throws Exception
    {
        // Create the statement in a try block to avoid having to manually close it
        // and prevent problems with stuff not being closed when an exception happens.
        try(Statement stmt = db.createStatement())
        {
            stmt.executeUpdate(sql);
        }    	
    }
}
