package com.astontech.console;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.*;

import com.astontech.bo.*;
import com.astontech.bo.interfaces.Home;
import com.astontech.bo.interfaces.ILocation;
import com.astontech.bo.interfaces.Site;
import com.astontech.dao.PersonDAO;
import com.astontech.dao.mysql.PersonDAOImpl;
import common.helpers.DateHelper;
import common.helpers.MathHelper;
import org.apache.log4j.Logger;

public class Main {

    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        //notes:    private static method for the lesson
        LessonRecursionComplex(new File("C:\\Users\\"));
    }

    private static void LessonRecursionComplex(File dir) {
        try {
            File[] files = dir.listFiles();
            for(File file : files) {
                if(file.isDirectory()){
                    //notes:    recursion happens here
                    logger.info("directory: " + file.getCanonicalPath());
                    LessonRecursionComplex(file);
                } else {
                    logger.info("     file: " + file.getCanonicalPath());
                }
            }
        } catch (IOException ioEx) {
            logger.error(ioEx);
        }
    }

    private static void LessonRecursion(int recursionCount) {
        logger.info("Recursive Count = " + recursionCount);
        if(recursionCount > 0)
            LessonRecursion(recursionCount - 1);
    }

    private static void LessonDeserialization() {
        Person person = null;
        try {
            FileInputStream fileIn = new FileInputStream("./ser_person.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            person = (Person) in.readObject();
            in.close();
            fileIn.close();

        } catch (FileNotFoundException fileEx) {
            logger.error(fileEx);
        } catch (IOException ioEx) {
            logger.error(ioEx);
        } catch (ClassNotFoundException clzEx) {
            logger.error(clzEx);
        }

        logger.info("Deserialized object: " + person.ToString());
    }

    private static void LessonSerialization() {
        //notes:    get an object from db
        PersonDAO personDAO = new PersonDAOImpl();
        Person person = personDAO.getPersonById(1);

        //notes:    serialize to a text file
        try {
            FileOutputStream fileOut = new FileOutputStream("./ser_person.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(person);
            out.close();
            fileOut.close();
            logger.info("Object serialized and written to file: ./ser_person.txt");
            logger.info("Serialized object: " + person.ToString());
        } catch (IOException ioEx) {
            logger.error(ioEx);
        }
    }

    private static void LessonBoxUnboxCast() {
        //notes:    BOXING = act of converting a value type to a ref type.
        //          UNBOXING = converting a ref type to a value type.

        //notes:    boxing
        int x = 10;
        Object o = x;
        LessonReflectionAndGenerics(o.getClass());

        //notes:    unboxing (this is casting, particularly 'explicit' casting)
        int y = (int) o;
        logger.info(y);

        //notes:    implicit casting
        int i = 100;
        double d = i;

        double db = 1.92;
        // int in = db;  this will fail

        //notes:    explicit casting
        int in = (int) db;
        logger.info(in);

    }

    private static <T> void LessonReflectionAndGenerics(Class<T> genericClass) {

        logger.info("Full Name: " + genericClass.getName());
        logger.info("Simple Name: " + genericClass.getSimpleName());
        for(Field field : genericClass.getDeclaredFields()) {
            logger.info("Field: " + field.getName() + " - Type: " + field.getType());
        }
        for(Method method : genericClass.getDeclaredMethods()) {
            logger.info("Method: " + method.getName());
        }

    }

    private static void LessonDAODelete() {
        PersonDAO personDAO = new PersonDAOImpl();

        if (personDAO.deletePerson(7))
            logger.info("Person Deleted Successfully.");
        else
            logger.info("Person Delete Failed!");

    }

    private static void LessonDAOUpdate() {
        PersonDAO personDAO = new PersonDAOImpl();

        Person person = personDAO.getPersonById(7);
        person.setMiddleName("UPDATED!!!");

        if (personDAO.updatePerson(person))
            logger.info("Person Updated Successfully.");
        else
            logger.info("Person Update Failed!");
    }

    private static void LessonDAOInsert(){
        Person person = new Person();
        person.setFirstName("Tony");
        person.setMiddleName("IronMan");
        person.setLastName("Stark");
        person.setBirthDate(new Date());
        person.setSSN("yyy-yy-yyyy");

        PersonDAO personDAO = new PersonDAOImpl();
        int id = personDAO.insertPerson(person);

        logger.info("New Person Record Inserted.  ID = " + id);
    }

    private static void LessonDAO() {
        //region CREATE MENU
        PersonDAO personDAO = new PersonDAOImpl();
        List<Person> personList = personDAO.getPersonList();

        System.out.println("================================");

        for(Person person : personList) {
            System.out.println(person.getPersonId() + ") " + person.getLastName() + ", " + person.getFirstName());
        }

        System.out.println("================================");

        //endregion

        //region PROMPT USER
        Scanner reader = new Scanner(System.in);
        System.out.println("Please Select a Person from list: ");
        String personId = reader.nextLine();
        //endregion

        //region GET PERSON DETAILS
        Person personDetail = personDAO.getPersonById(Integer.parseInt(personId));

        System.out.println("----- PERSON DETAILS -----");
        System.out.println("Full Name: " + personDetail.GetFullName());
        System.out.println("DOB: " + personDetail.getBirthDate());
        System.out.println("SSN: " + personDetail.getSSN());
        //endregion
    }

    private static void LessonGetStoredProc() {
        Connection conn = LessonDBConnection();
        try {
            String sp = "{call GetPerson(?,?)}";
            CallableStatement cStmt = conn.prepareCall(sp);

            cStmt.setInt(1, 20);
            cStmt.setInt(2, 1);
            ResultSet rs = cStmt.executeQuery();

            while(rs.next()){
                /*
                    PersonId                index 1  INT
					FirstName               index 2  VARCHAR
					MiddleName              index 3  VARCHAR
					LastName                index 4  VARCHAR
					BirthDate               index 5  DATETIME
					SocialSecurityNumber    index 6  VARCHAR
                 */
                logger.info(rs.getInt(1) + ": " + rs.getString(2) + " " + rs.getString(4));
            }

        } catch (SQLException sqlEx) {
            logger.error(sqlEx);
        }
    }

    private static void LessonExecQuery() {
        Connection conn = LessonDBConnection();
        try {
            Statement statement = conn.createStatement();
            String sql = "select PersonId, FirstName, LastName from person";

            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                int personId = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);

                logger.info(personId + ": " + firstName + " " + lastName);
            }
            conn.close();


        } catch (SQLException sqlEx) {
            logger.error(sqlEx);
        }
    }

    private static Connection LessonDBConnection() {
        String dbHost = "localhost";
        String dbName = "hr";
        String dbUser = "consoleUser";
        String dbPass = "qwe123$!";
        String useSSL = "false";
        String procBod = "true";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            logger.error("MySQL Driver not found! " + ex);
            return null;
        }

        logger.info("MySQL Driver Registered.");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":3306/" + dbName + "?useSSL=" + useSSL + "&noAccessToProcedureBodies=" + procBod, dbUser, dbPass);
        } catch (SQLException ex) {
            logger.error("Connection failed! " + ex);
            return null;
        }

        if(connection != null) {
            logger.info("Successfully connected to MySQL database");
            return connection;
        } else {
            logger.info("Connection failed!");
            return null;
        }

    }

    private static void LessonLogging() {
        //notes:    levels of logging.
        logger.debug("This is a DEBUG log message");
        logger.info("This is an INFO log message");

        //notes:    production levels
        logger.warn("This is a WARN log message");
        logger.error("This is an ERROR log message");
        logger.fatal("This is a FATAL log message");

        //notes:    log an exception
        try {
            int i = 10 / 0;
        } catch (ArithmeticException ex) {
            System.out.println("An Error Occurred! Please contact your system admin.");
            logger.error("An exception occurred: " + ex);
        }

    }

    private static void LessonInterfacesTest() {
        Site MN010 = new Site();
        MN010.setSiteName("MN010");
        MN010.setCoffeeMachines(2);
        MN010.setConferenceRooms(1);
        MN010.setCubicles(8);
        MN010.setOffices(6);
        MN010.setTrainingDesks(36);

        Home BipsHouse = new Home();
        BipsHouse.setAddress("1 Main St.");
        BipsHouse.setOwner(new Employee("Bipin", "Butala"));

        LessonInterfaces(MN010);
        LessonInterfaces(BipsHouse);
    }

    private static void LessonInterfaces(ILocation Ilocation){
        System.out.println("=======================");
        System.out.println("Location Name:" + Ilocation.getLocationName());
        System.out.println("Can Have Meetings: " + Ilocation.canHaveMeetings());
        System.out.println("Number of Workspaces: " + Ilocation.numberOfWorkspaces());
        System.out.println("Has Coffee! : " + Ilocation.hasCoffee() );
    }

    private static void LessonValueVsRef() {
        //notes:    reference type
        Employee firstEmp = new Employee();
        firstEmp.setFirstName("Bipin");

        Employee secondEmp = firstEmp;
        firstEmp.setFirstName("Dean");
        secondEmp.setFirstName("Bob");

        System.out.println(firstEmp.getFirstName());

        //notes:    value types
        int firstInt = 10;
        int secondInt = firstInt;

        firstInt = 20;

        System.out.println(secondInt);
    }

    private static void LessonHash() {
        //notes:     key-value pairs / value list

        //todo:     HashTable
        /*
            1) does NOT allow null for either key or value
            2) synchronized, thread safe, but performance is decreased
         */

        System.out.println("---HASH TABLE---");

        Hashtable<Integer, String> firstHashTable = new Hashtable<>();
        firstHashTable.put(1, "Inheritance");
        firstHashTable.put(2, "Polymorphism");
        firstHashTable.put(3, "Abstraction");
        firstHashTable.put(4, "Encapsulation");
        //firstHashTable.put(5, null);  // throw a NullPointerException (NPE)

        System.out.println("value from give key: " + firstHashTable.get(3));

        for(Integer key : firstHashTable.keySet()){
            System.out.println("key: " + key + " - value: " + firstHashTable.get(key));
        }

        System.out.println("---------------");
        System.out.println("---------------");

        //todo:     HashMap
        /*
            1) DOES allow null for either key or value
            2) un-synchronized, not thread safe, better performance
         */
        System.out.println("---HASH MAP----");

        HashMap<Integer, String> firstHashMap = new HashMap<Integer, String>();
        firstHashMap.put(1, "Inheritance");
        firstHashMap.put(2, "Polymorphism");
        firstHashMap.put(3, "Abstraction");
        firstHashMap.put(4, "Encapsulation");
        firstHashMap.put(6, "Encapsulation");
        firstHashMap.put(5, null);

        System.out.println("value from give key: " + firstHashMap.get(2));

        for(Integer key : firstHashMap.keySet()){
            System.out.println("key: " + key + " - value: " + firstHashMap.get(key));
        }

        System.out.println("---------------");
        System.out.println("---------------");

        //todo:     HashSet
        /*
            1) built in mechanism for duplicates
            2) used for where you want to maintain a unique list
         */
        System.out.println("---HASH SET----");
        HashSet<String> oopPrinc = new HashSet<String>();
        oopPrinc.add("Inheritance");
        oopPrinc.add("Encapsulation");
        oopPrinc.add("Encapsulation");
        oopPrinc.add("Encapsulation");
        oopPrinc.add("Encapsulation");

        if(oopPrinc.contains("Polymorphism"))
            System.out.println("value exists");
        else
            System.out.println("value does not exist");

        for(String s : oopPrinc) {
            System.out.println(s);
        }


    }

    private static void LessonPolymorphism() {
        //notes:    compile time polymorphism  -  overloaded

        //notes:    run-time polymorphism  - override
        BaseBO baseBO = new BaseBO();
        System.out.println(baseBO.test_method());

        EntityType entityType = new EntityType();
        System.out.println(entityType.test_method());
    }

    private static void LessonInstanceVsStatic() {

        System.out.println(MathHelper.E);
        System.out.println(MathHelper.PI);

        System.out.println(MathHelper.square(4));

    }

    private static void LessonComplexProperties() {

        //notes:    when to use inheritance (should answer the question: 'IS A?')
        //          when to use complex (nested) objects (should answer the question: 'HAS A?')

        EntityType emailWorkType = new EntityType("Work");
        emailWorkType.setEntityTypeId(1);

        Email myEmail = new Email("bipin@bip.com");
        myEmail.setEmailType(emailWorkType);

        System.out.println(myEmail.getEmailAddress() + " Type:" + myEmail.getEmailType().getEntityTypeName());

        //notes:    collection/list of complex(nested) objects as a property.
        Employee myEmployee = new Employee();

        myEmployee.getEmails().add(new Email("test@test.com"));
        myEmployee.getEmails().add(new Email("dan@test.com"));
        myEmployee.getEmails().add(new Email("jason@test.com"));

        for(Email email : myEmployee.getEmails()) {
            System.out.println(email.getEmailAddress());
        }




    }

    private static void LessonCollections() {
        //notes:    List<T> - generic type 'T'
        List<Employee> employeeList = new ArrayList<Employee>();

        Employee emp1 = new Employee("Dan", "Simmer");          // index 0
        Employee emp2 = new Employee("James", "McRoberts");     // 1
        Employee emp3 = new Employee("Sean", "Nilsen");         // 2

        employeeList.add(emp1);
        employeeList.add(emp2);
        employeeList.add(emp3);

        employeeList.add(new Employee("Adrian", "Ratanyake"));  // 3
        employeeList.add(new Employee("John", "Doe"));          // 4

        System.out.println(employeeList.get(0).GetFullName());

        for(Employee e : employeeList){
            System.out.println(e.GetFullName());
        }

    }

    private static void LessonMethods() {
        //notes:    method signature / declaration
        /*
            <access modifier> <instance/static> <return data type> <method name> (<data type> <param name>, <data type> <param name>, ...) { body }
              private            static            void             LessonMethods    ( nothing passed in)

              public                                int              getPersonId     ( nothing passed in)

              public                                void             setPersonID      (int personID)
         */

        //notes:    constructors are special methods with same name as class
//        Employee constructorEmployee = new Employee("Bipin", "Butala");
//        System.out.println(constructorEmployee.getFirstName() + " " + constructorEmployee.getLastName());
//
//        Employee const2Employee = new Employee("Simmer");
//        System.out.println(const2Employee.getLastName());

        Employee employeeJames = new Employee();
        System.out.println(employeeJames.GetFullName());


    }

    private static void LessonInheritance() {
        //notes:    4 principles(tenets) Encapsulation, Abstraction, Inheritance, Polymorphism

        Employee employeeBip = new Employee();
        employeeBip.setFirstName("Bipin(e)");
        employeeBip.setLastName("Butala(e)");
        employeeBip.setId(3);

        System.out.println(employeeBip.getId() + ": " + employeeBip.getFirstName() + " " + employeeBip.getLastName());

        Person personBip = new Person();
        personBip.setFirstName("Bipin(p)");
        personBip.setLastName("Butala(p)");
        personBip.setId(2);

        System.out.println(personBip.getId() + ": " + personBip.getFirstName() + " " + personBip.getLastName());

    }

    private static void LessonClassObjects() {
        //notes:    instantiating a new object
        Person myFirstPerson = new Person();
        myFirstPerson.setFirstName("Bipin");
        myFirstPerson.setLastName("Butala");
        myFirstPerson.setTitle("Mr.");

        myFirstPerson = new Person();
        myFirstPerson.setFirstName("Sarah");
        myFirstPerson.setLastName("Butala");
        myFirstPerson.setTitle("Mrs.");

        System.out.print(myFirstPerson.getTitle());
        System.out.print(" ");
        System.out.print(myFirstPerson.getFirstName());
        System.out.print(" ");
        System.out.println(myFirstPerson.getLastName());

//        System.out.print(mySecondPerson.getTitle());
//        System.out.print(" ");
//        System.out.print(mySecondPerson.getFirstName());
//        System.out.print(" ");
//        System.out.println(mySecondPerson.getLastName());

        //notes:    setting value for super (inherited class)
        myFirstPerson.setId(3);
        System.out.print(myFirstPerson.getId());


    }

    private static void LessonExceptions() throws Exception{
        //todo:     simple unhandled exception
//        String firstName = "Bipin";
//        int x = Integer.parseInt(firstName);
//
//        System.out.print("Integer value: ");
//        System.out.println(x);

        //todo:     try - catch block
//        String firstName = "Bipin";
//        try {
//            int x = Integer.parseInt(firstName);
//
//            System.out.print("Integer value: ");
//            System.out.println(x);
//        } catch(NumberFormatException ex) {
//            System.out.println("Exception: Invalid Number");
//        }

        //todo:     try - catch with multiple catch blocks
//        String firstName = null;
//        try {
//            int x = Integer.parseInt(firstName);
//
//            System.out.print("Integer value: ");
//            System.out.println(x);
//        }
//        catch (NumberFormatException ex)
//        {
//            System.out.println("Exception: Number Format Error.");
//        }
//        catch (IllegalArgumentException ex)
//        {
//            System.out.println("Exception: String was null or empty");
//        }
//        catch (Exception ex)
//        {
//            System.out.println("Exception: Generic Exception");
//        }


        //todo:     try - catch with multiple catch and finally block
//        String firstName = "9";
//        try {
//            int x = Integer.parseInt(firstName);
//
//            System.out.print("Integer value: ");
//            System.out.println(x);
//        }
//        catch (IllegalArgumentException ex)
//        {
//            System.out.println("Exception: String was null or empty");
//        }
//        catch (NullPointerException ex)
//        {
//            System.out.println("Exception: Null Pointer");
//        }
//        catch (Exception ex)
//        {
//            System.out.println("Exception: Generic Exception");
//        }
//        finally
//        {
//            System.out.println("Program has been completed regardless of exceptions.");
//        }


        //todo:     exception object
        //notes:    base exception class
//        String firstName = "bipin";
//        try {
//            int x = Integer.parseInt(firstName);
//
//            System.out.print("Integer value: ");
//            System.out.println(x);
//        }
//        catch (IllegalArgumentException ex)
//        {
//            System.out.println("Exception: " +  ex.toString());
//        }
//        catch (NullPointerException ex)
//        {
//            System.out.println("Exception: " + ex.toString());
//        }
//        catch (Exception ex)
//        {
//            System.out.println("Exception: " + ex.toString());
//        }
//        finally
//        {
//            System.out.println("Program has been completed regardless of exceptions.");
//        }

        //todo:     throwing an exception
//        String firstName = null;
//        try {
//            int x = Integer.parseInt(firstName);
//
//            System.out.print("Integer value: ");
//            System.out.println(x);
//        }
//        catch (Exception ex)
//        {
//            throw new Exception("A custom exception from LessonException method.", ex);
//        }

    }

    private static void LessonFlowControl() {
        //notes:    IF-ELSE
        String name = "bipin";

        if(name.equals("dan")) {
            System.out.println("correct first name");
            System.out.println("another line");

        } else {
            System.out.println("incorrect first name");

            if (name.length() > 10) {
                System.out.println("very long first name.");
            } else if(name.length() > 5) {
                System.out.println("long first name.");
            } else {
                System.out.println("short first name.");
            }
        }

        //notes:    CASE-SWITCH
        switch(name)
        {
            case "bipin":
                System.out.println("cool first name");
                break;

            case "dan":
            case "danny":
            case "daniel":
                System.out.println("first name is dan, danny, or daniel");
                break;

            default:
                System.out.println("some other first name");
                break;
        }
    }

    private static void LessonOperators() {
        //notes:    plus / minus
        int val = 10;
        System.out.println(val + 10);
        System.out.println(val - 10);

        //notes:    modulus (%) the remainder
        int modVal = 10 % 3;  //the remainder of 10 / 3 which is 1
        System.out.println(modVal);

        //notes:    increment (++) / decrement (--)
        System.out.println("increment after: ");
        System.out.println(val++);  //10
        System.out.println(val++);  //11
        System.out.println(val++);  //12
        System.out.println(val);    //val = 13;

        val = 10;
        System.out.println("increment before: ");
        System.out.println(++val);  //11
        System.out.println(++val);  //12
        System.out.println(++val);  //13
        System.out.println(val);    //val = 13;

        val = 10;
        System.out.println("decrement after: ");
        System.out.println(val--);  //10
        System.out.println(val--);  //9
        System.out.println(val--);  //8
        System.out.println(val);    //val = 7;

        val = 10;
        System.out.println("decrement before: ");
        System.out.println(--val);  //9
        System.out.println(--val);  //8
        System.out.println(--val);  //7
        System.out.println(val);    //val = 7;

        //notes:    logical equals (==) / logical not equal (!=)
        val = 10;
        int anotherVal = 10;

        if(val != anotherVal)
            System.out.println("Not Equals");
        else
            System.out.println("Equals");

        //notes:    logical AND (&&) / logical OR (||)
        if(val == 10 && anotherVal == 10 || val == 11 || val == 13)
            System.out.println("is true");
        else
            System.out.println("not true");

        //notes:    negation (!)
        boolean boolVar = true;
        if(!boolVar)
            System.out.println("is true");
        else
            System.out.println("not true");

    }

    private static void LessonLists() {
        //notes:    collections / Lists
        List<String> myStringCollection = new ArrayList<String>();

        myStringCollection.add("1st String");
        myStringCollection.add("2nd String");
        myStringCollection.add("3rd String");
        myStringCollection.add("4th String");
        myStringCollection.add("5th String");

        System.out.println(myStringCollection);

        for(String singleString : myStringCollection) {
            System.out.println(singleString);
        }

        List<Integer> myIntCollection = new ArrayList<Integer>();

        myIntCollection.add(10);
        myIntCollection.add(20);
        myIntCollection.add(30);
        myIntCollection.add(40);
        myIntCollection.add(50);

        System.out.println(myIntCollection);

        for(int singleInt : myIntCollection) {
            System.out.println(singleInt);
        }

        //notes:    arrays

        String[] myStringArray = new String[6];

        myStringArray[0] = "1st";
        myStringArray[1] = "2nd";
        myStringArray[2] = "3rd";
        myStringArray[3] = "4th";
        myStringArray[4] = "5th";

        System.out.println(myStringArray);

        for(String singleString : myStringArray) {
            System.out.println(singleString);
        }



    }

    private static void LessonStrings() {

        //notes:    strings have a value or not.
        String firstString = "";
        firstString = "something";
        firstString = null;

        if(firstString == null || firstString.isEmpty()) {
            System.out.println("String is empty");
        } else {
            System.out.println("String has a value");
        }

        //notes:    immutable - unable to be changed....
        firstString = "another value";  //this creates a new string

        for(int x = 0; x <= 100; x++) {
            firstString = "new value: " + Integer.toString(x);
            System.out.println(firstString);  //we are creating a new string for every iteration!! 100 strings!!
            //VERY INEFFICIENT!!
        }

        //notes:    StringBuilder()
        StringBuilder myStringBuilder = new StringBuilder();

        for(int x = 0; x <= 100; x++) {
            myStringBuilder.append("new value with string builder: ")
                           .append(Integer.toString(x))
                           .append("\n");
        }

        System.out.println(myStringBuilder);


        //notes:    searching strings (indexOf, lastIndexOf)
        String myName = "Bipin Butala";
        /*

            string can be visualized as an array of characters.
             B   i   p   i   n       B   u...
            [0] [1] [2] [3] [4] [5] [6] [7]...

            note that the array is zero based (starts at zero)

            character at index 2 is "p"

         */

        int indexOf = myName.indexOf("i");
        System.out.println(indexOf);

        int lastIndexOf = myName.lastIndexOf("B");
        System.out.println(lastIndexOf);

        //notes:    enumerating a string
        String largeString = "This is a longer string than before";
        for(char c : largeString.toCharArray()) {
            System.out.println(c);
        }

        //notes:    substring(beginning index) or substring(beginning index, end index)
        String partOfLargerString = largeString.substring(8, 16);
        System.out.println(partOfLargerString);

    }

    private static void LessonDataTypes() {
        //notes:    primitive data types
        //          int (number)
        //          float (floating point .)
        //          double (larger number)
        //          boolean (true / false)
        //          char (character)

        //notes:    common data types
        boolean myBool = false;
        int myInt = 4;
        String myString = "some words";
        Date myDate = new Date();

        System.out.println(myBool);
        System.out.println(myInt);
        System.out.println(myString);
        System.out.println(myDate);

        //notes:    parsing / converting data types

        //notes:    string -> int
        String numberString = "321";
        int intFromString = Integer.parseInt(numberString);

        System.out.println(intFromString);


        //notes:    int -> string
        int i = 312;
        String stringFromInt = Integer.toString(i);

        System.out.println(stringFromInt);


        //notes:    date -> string
        String stringFromDate = myDate.toString();

        System.out.println(stringFromDate);

    }

    private static void LessonVariables(){
        //notes:    use variable declared outside of this scope


        //notes:    declare multiple variables and set
        String lastName = "Butala", firstName = "Bipin";

        //notes:    output to the console
        System.out.println(firstName + " " + lastName);

        //notes:    create a Scanner object
        Scanner reader = new Scanner(System.in);
        //notes:    prompt the user
        System.out.print("Enter your name: ");
        //notes:    read input from the user and store it a variable
        String input = reader.nextLine();
        //notes:    print the value back to the screen
        System.out.println("Hello " + input);

    }
}
