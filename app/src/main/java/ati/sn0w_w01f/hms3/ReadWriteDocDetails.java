package ati.sn0w_w01f.hms3;

public class ReadWriteDocDetails {

    public String docname,docid,docmail,docmobile;
    public ReadWriteDocDetails(){};

    public ReadWriteDocDetails(String textdocname,String textdocid,String textdocmail,String texdocmobile){

        this.docname = textdocname;
        this.docid =  textdocid;
        this.docmail = textdocmail;
        this.docmobile = texdocmobile;
    }
}
