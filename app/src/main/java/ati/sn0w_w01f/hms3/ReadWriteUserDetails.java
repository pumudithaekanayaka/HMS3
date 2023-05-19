package ati.sn0w_w01f.hms3;

public class ReadWriteUserDetails {

    public String firstname,lastname,natid,blood,address,dob,gender,contact;
    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String textfirstname,String textlastname,String textnatid,String textaddress,String textnumber,String textdob,String textgender,String textblood){

        this.firstname = textfirstname;
        this.lastname = textlastname;
        this.natid = textnatid;
        this.blood = textblood;
        this.address = textaddress;
        this.dob = textdob;
        this.gender = textgender;
        this.contact = textnumber;
    }
}
