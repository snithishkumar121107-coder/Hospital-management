import java.sql.*;
import java.util.Scanner;

public class HospitalManagement {
    static final String URL = "jdbc:mysql://localhost:3306/hospital";
    static final String USER = "root";
    static final String PASSWORD = "test@123";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            int choice;
            do {
                System.out.println("\n========== Patient Management System ==========");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. Update Patient Phone");
                System.out.println("4. Delete Patient");
                System.out.println("5. Search Patient by ID");
                System.out.println("6. Search Patient by Joining Date");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
                
                choice = sc.nextInt();
                sc.nextLine(); // Clear buffer after integer input

                switch(choice) {
                    case 1:
                        System.out.print("Enter Patient ID: ");
                        int patient_id = sc.nextInt();
                        sc.nextLine(); // Clear buffer
                        
                        System.out.print("Enter Patient Name: ");
                        String patient_name = sc.nextLine();
                        
                        System.out.print("Enter Patient Age: ");
                        int age = sc.nextInt();
                        sc.nextLine(); // Clear buffer
                        
                        System.out.print("Enter Patient Gender: ");
                        String gender = sc.nextLine();
                        
                        System.out.print("Enter Patient PH NO: ");
                        String phone = sc.nextLine();
                        
                        System.out.print("Enter Admission / Joining Date (YYYY-MM-DD): ");
                        String joining_date = sc.nextLine();
                        
                        System.out.print("Enter Affected With (Illness): ");
                        String affected_with = sc.nextLine();
                        
                        System.out.print("Enter Assigned Doctor: ");
                        String assigned_doctor = sc.nextLine();
                        
                        System.out.print("Enter Assigned Ward: ");
                        String assigned_ward = sc.nextLine();
                        
                        String insert = "INSERT INTO hospital_management VALUES(?,?,?,?,?,?,?,?,?)";
                        try (PreparedStatement ps = con.prepareStatement(insert)) {
                            ps.setInt(1, patient_id);
                            ps.setString(2, patient_name);
                            ps.setInt(3, age);
                            ps.setString(4, gender);
                            ps.setString(5, phone);
                            ps.setString(6, joining_date);
                            ps.setString(7, affected_with);
                            ps.setString(8, assigned_doctor);
                            ps.setString(9, assigned_ward);
                            
                            int row = ps.executeUpdate();
                            if(row > 0) System.out.println("Patient Added Successfully.");
                        }
                        break;

                    case 2:
                        String selectAll = "SELECT * FROM hospital_management";
                        try (Statement st = con.createStatement();
                             ResultSet rs = st.executeQuery(selectAll)) {
                            
                            System.out.println("\n-------------------------------------------------------------------------------------------------------");
                            System.out.println("ID\tNAME\tAGE\tGENDER\tPHONE\t\tJOIN DATE\tAFFECTED WITH\tDOCTOR\tWARD");
                            System.out.println("-------------------------------------------------------------------------------------------------------");
                            while(rs.next()) {
                                System.out.println(rs.getInt("patient_id") + "\t" + 
                                                   rs.getString("patient_name") + "\t" + 
                                                   rs.getInt("age") + "\t" + 
                                                   rs.getString("gender") + "\t" + 
                                                   rs.getString("phone") + "\t" +
                                                   rs.getString("joining_date") + "\t" +
                                                   rs.getString("affected_with") + "\t\t" +
                                                   rs.getString("assigned_doctor") + "\t" +
                                                   rs.getString("assigned_ward"));
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Enter Patient ID: ");
                        int pid = sc.nextInt();
                        sc.nextLine(); // Clear buffer
                        
                        System.out.print("Enter New Phone No: ");
                        String newphone = sc.nextLine();
                        
                        String update = "UPDATE hospital_management SET phone=? WHERE patient_id=?";
                        try (PreparedStatement ps2 = con.prepareStatement(update)) {
                            ps2.setString(1, newphone);
                            ps2.setInt(2, pid);
                            int updateRow = ps2.executeUpdate();
                            if(updateRow > 0) System.out.println("Patient Updated Successfully.");
                            else System.out.println("Patient Not Found.");
                        }
                        break;

                    case 4:
                        System.out.print("Enter Patient ID to Delete: ");
                        int did = sc.nextInt();
                        sc.nextLine(); // Clear buffer
                        
                        String delete = "DELETE FROM hospital_management WHERE patient_id=?";
                        try (PreparedStatement ps3 = con.prepareStatement(delete)) {
                            ps3.setInt(1, did);
                            int deleteRow = ps3.executeUpdate();
                            if(deleteRow > 0) System.out.println("Patient Info Deleted Successfully.");
                            else System.out.println("Patient Info Not Found.");
                        }
                        break;

                    case 5:
                        System.out.print("Enter Patient ID to search: ");
                        int searchId = sc.nextInt();
                        sc.nextLine(); // Clear buffer
                        
                        String search = "SELECT * FROM hospital_management WHERE patient_id=?";
                        try (PreparedStatement ps4 = con.prepareStatement(search)) {
                            ps4.setInt(1, searchId);
                            try (ResultSet rsSearch = ps4.executeQuery()) {
                                if (rsSearch.next()) {
                                    System.out.println("\nPatient Found:");
                                    System.out.println("ID: " + rsSearch.getInt("patient_id"));
                                    System.out.println("Name: " + rsSearch.getString("patient_name"));
                                    System.out.println("Age: " + rsSearch.getInt("age"));
                                    System.out.println("Gender: " + rsSearch.getString("gender"));
                                    System.out.println("Phone: " + rsSearch.getString("phone"));
                                    System.out.println("Joining Date: " + rsSearch.getString("joining_date"));
                                    System.out.println("Affected With: " + rsSearch.getString("affected_with"));
                                    System.out.println("Assigned Doctor: " + rsSearch.getString("assigned_doctor"));
                                    System.out.println("Assigned Ward: " + rsSearch.getString("assigned_ward"));
                                } else {
                                    System.out.println("Patient Record Not Found.");
                                }
                            }
                        }
                        break;

                    case 6:
                        System.out.print("Enter Joining Date to search (YYYY-MM-DD): ");
                        String searchDate = sc.nextLine();
                        
                        String searchByDateSql = "SELECT * FROM hospital_management WHERE joining_date=?";
                        try (PreparedStatement ps5 = con.prepareStatement(searchByDateSql)) {
                            ps5.setString(1, searchDate);
                            try (ResultSet rsDate = ps5.executeQuery()) {
                                boolean found = false;
                                System.out.println("\nPatients admitted on " + searchDate + ":");
                                System.out.println("-------------------------------------------------------------------------------------------------------");
                                while (rsDate.next()) {
                                    found = true;
System.out.println("ID: " + rsDate.getInt("patient_id") +" | Name: " + rsDate.getString("patient_name") +" | Illness: " + rsDate.getString("affected_with") +" | Doctor: " + rsDate.getString("assigned_doctor") +" | Ward: " + rsDate.getString("assigned_ward"));}if (!found) {System.out.println("No records found matching this admission date.");}}}break;case 7:System.out.println("Thank You...");break;default:System.out.println("Invalid Choice. Choose between 1-7.");}} while(choice != 7);} catch(ClassNotFoundException e) {System.out.println("MySQL Driver Not Found.");} catch(SQLException e) {System.out.println("Database Error: " + e.getMessage());} catch(Exception e) {System.out.println("Unexpected Error occurred: " + e.getMessage());} finally {sc.close();}}}