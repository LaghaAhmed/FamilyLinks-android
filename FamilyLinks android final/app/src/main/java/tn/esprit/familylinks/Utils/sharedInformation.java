package tn.esprit.familylinks.Utils;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by L on 27-12-15.
 */
public class sharedInformation {
    public static List<user> sharedUsers;
    public static List<user> sharedUsersName;
    public static List<user> sharedFamily;
    public static List<ParseObject> sharedGeneration;
    public static List<ParseObject> sharedBigger;
    public static List<ParseObject> sharedSmaller;
    public static String sharedParseLastName;
    public static String sharedParseCountry;
    public static user sharedcurrentPerson;
    public static user sharedSelectedPerson;

    public static ParseObject sharedParsePerson;
    public static ParseObject sharedParseUnknown;
    public static ParseObject sharedParseFather;
    public static ParseObject sharedParseMother;
    public static ParseObject sharedParseSpouse;

    public static String sharedParseMotherId;
    public static String sharedParseFatherId;
    public static String sharedParsePersonId;

    public static user findUser(String objId)
    {
        for(user u:sharedInformation.sharedUsers){
            if(u.personId.equals(objId))return u;
        }
        user u=new user(objId);
        return u;
    }

}
