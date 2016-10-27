package tn.esprit.familylinks.Utils;

import android.graphics.Bitmap;

/**
 * Created by L on 27-12-15.
 */
public class user {
    public String personId="";
    public String username="";
    public String mail="@";
    public String name="";
    public String lastName="";
    public String gender="";
    public String age="1/2/1992";
    public String job="";
    public String country="";
    public String fatherId="";
    public String motherId="";
    public String spouseId="";
    //String age;
    public Bitmap photoIS;

    public user(String personId,String username,String mail,String name,String lastName,String gender,
                String age,String job,String country, Bitmap photoInputStream,String fatherId,String motherId,String spouseId) {
        this.personId = personId;
        this.username = username;
        this.mail = mail;
        this.name = name;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.job = job;
        this.country = country;
        this.photoIS = photoInputStream;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.spouseId = spouseId;
    }
    public user(String name,String lastName) {
        this.name = name;
        this.lastName = lastName;
    }
    public user(String personId,String name,String lastName,String gender,
                String age,String job,String country, Bitmap photoInputStream,String fatherId,String motherId,String spouseId) {
        this.personId = personId;
        this.name = name;
        this.lastName = lastName;
        this.mail = "unk@nown.uk";
        this.gender = gender;
        this.age = age;
        this.job = job;
        this.country = country;
        this.photoIS = photoInputStream;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.spouseId = spouseId;
    }

    public user(String personId,String username,String mail,String name,String lastName,String gender,
                String age,String job, Bitmap photoInputStream) {
        this.personId = personId;
        this.username = username;
        this.mail = mail;
        this.name = name;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.job = job;
        this.photoIS = photoInputStream;
    }

    public user(String name,String lastName,
                String age,String job, Bitmap photoInputStream) {
        this.name = name;
        this.lastName = lastName;
        this.age = "18";
        this.job = job;
        this.photoIS = photoInputStream;
    }
    public user(String name, Bitmap photoInputStream) {
        this.name = name;
        this.job = "battal";
        //this.age = age;
        this.photoIS = photoInputStream;
    }
    public user() {
        this.name = "";
        this.job = "";
        //this.age = age;
    }
    public user(String objId) {
        this.personId = objId;
    }

}
