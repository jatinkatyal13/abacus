/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author jatin
 */
public class lib {   
    
    public static void main(String args[]){
        lib l = new lib();
        l.exec();
    }
    
    void exec(){
//        System.out.println("insert starts");
//        try {
//            connect();
//            Add_Student(new Student(
//                    3,
//                    "jatin",
//                    "monica",
//                    "sameer",
//                    (new SimpleDateFormat("yyyy-mm-dd")).parse("1997-10-10"),
//                    new Date(),
//                    12,
//                    "Maharaja Agrasen Model School",
//                    "9818590425",
//                    "9811062380",
//                    "Sec-3 Rohini"
//            ));
//        } catch (SQLException ex) {
//            Logger.getLogger(lib.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ParseException ex) {
//            Logger.getLogger(lib.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println("insert stops");

        connect();
        List<Student> list = new ArrayList<>();
        list = Get_Student("name", "jatin", "Monica Katyal");
        for (int i=0; i<list.size(); i++){
            System.out.println(list.get(i).student_name);
        }

    }
    
    static Connection conn = null;
    
    public boolean connect(){
        try {
            String url = "jdbc:sqlite:/Users/jatin/Desktop/test.db";
            conn = DriverManager.getConnection(url);
            return true;
        } catch(SQLException e){
//            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public static boolean disconnect(){
        try {
            if (conn != null) conn.close();
            return true;
        } catch(SQLException e){
            return false;
        }
    }
    
    public static Vector<String> Get_CI(){
        try {
            //List<String> result = new ArrayList<>();
            
            Vector<String> result = new Vector<String>();
            
            
            String sql = "select * from ci";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                result.add(rs.getString("name"));
            }
            
            return result;
        } catch(SQLException e){
            System.out.println(e.getMessage());
            return new Vector<String>();
        }
    }
    
    public boolean Add_Payment(int student_id, Payment payment){
        try{
            String sql = "insert into payment(student_id, date, amount, remarks) values ("
                    + "" + student_id + ","
                    + "'" + new SimpleDateFormat("yyyy-mm-dd").format(payment.date) + "',"
                    + "" + payment.amount + ","
                    + "'" + payment.remarks + "'"
                    + ");";
            Statement stmt = conn.createStatement();
            System.out.println(sql);
            stmt.execute(sql);
            return true;
        } catch(SQLException e){
            return false;
        }
    }
    
    public List<Payment> Get_Payment(int id){
        List<Payment> result = new ArrayList<>();
        
        try {
            String sql = "select * from payment where student_id = " + id + ";";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                result.add(new Payment(
                    new SimpleDateFormat("yyyy-mm-dd").parse(rs.getString("date")),
                    rs.getInt("amount"),
                    rs.getString("remarks")
                ));
            }
        } catch(SQLException e){
            System.out.println(e.toString());
        } catch (ParseException ex) {
            Logger.getLogger(lib.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    public List<Student> Get_Student(String criteria, String data, String ci){
        try {
            List<Student> resultList = new ArrayList<>();
            String condition = "1";
            switch(criteria){
                case "id" :
                    condition = criteria + "=" + data;
                    break;
                case "name" :
                    condition = "student_name" + " like '" + data + "%'";
                    break;
            }
            
            String sql = "";
            if (criteria == "all"){
                sql = "select * from student where ci = '" + ci + "';";
            } else {
                sql = "select * from student where " + condition + " and ci = '" + ci + "';";
            }
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()){
                resultList.add(new Student(
                        rs.getInt("id"),
                        rs.getString("student_name"),
                        rs.getString("ci"),
                        rs.getString("mother_name"),
                        rs.getString("father_name"),
                        new SimpleDateFormat("yyyy-mm-dd").parse(rs.getString("dob")),
                        new SimpleDateFormat("yyyy-mm-dd").parse(rs.getString("join")),
                        rs.getInt("clas"),
                        rs.getString("school"),
                        rs.getString("phone"),
                        rs.getString("mob"),
                        rs.getString("add")
                ));
            }
            
            return resultList;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return new ArrayList<>();
        } catch (ParseException ex) {
            System.out.println(ex.toString());
            return new ArrayList<>();
        }
    }
    
    public boolean Add_Student(Student s) throws SQLException{
        
        // date format for sql
        DateFormat sqlDate = new SimpleDateFormat("yyyy-mm-dd");
        
        String sql = "insert into student values("
                + s.id + ","
                + "'" + s.student_name + "',"
                + "'" + s.mother_name + "',"
                + "'" + s.father_name + "',"
                + "'" + sqlDate.format(s.dob) + "',"
                + "'" + sqlDate.format(s.join) + "',"
                + s.clas + ","
                + "'" + s.school + "',"
                + "'" + s.phone + "',"
                + "'" + s.mob + "',"
                + "'" + s.add + "'"
                + "'" + s.ci + "'"
                + ");";
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        
        return true;
    }
    
    public class Acheivement {
        Date date;
        int position;
        String remarks;
    }
    
    public class Payment {
        Date date;
        int amount;
        String remarks;
        
        Payment(){}
        
        Payment(
            Date date,
            int amount,
            String remarks
        ){
            this.date = date;
            this.amount = amount;
            this.remarks = remarks;
        }
    }
    
    public class Student {
        
        int get_id(){
            return id;
        }
        
        private int id;
        String student_name;
        String mother_name;
        String father_name;
        Date dob;
        Date join;
        int clas;
        String school;
        String phone;
        String mob;
        String add;
        String ci;
        
        Student(){}
        
        Student(
            int id,
            String student_name,
            String ci,
            String mother_name,
            String father_name,
            Date dob,
            Date join,
            int clas,
            String school,
            String phone,
            String mob,
            String add
        ){
            this.id = id;
            this.student_name = student_name;
            this.ci = ci;
            this.mother_name = mother_name;
            this.father_name = father_name;
            this.dob = dob;
            this.join = join;
            this.clas = clas;
            this.school = school;
            this.phone = phone;
            this.mob = mob;
            this.add = add;
        }
        
    }
    
}
