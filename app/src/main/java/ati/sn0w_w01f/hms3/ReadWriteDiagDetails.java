package ati.sn0w_w01f.hms3;

public class ReadWriteDiagDetails {

    public String patname,patid,diagid,symptoms,diag,med,docmail,notes;
    public ReadWriteDiagDetails(){};

    public ReadWriteDiagDetails(String textpatname,String textpatid,String textdiagid,String textsymptoms,String textdiag,String textmed,String textdocmail,String textnotes){

        this.patname = textpatname;
        this.patid =  textpatid;
        this.diagid = textdiagid;
        this.symptoms = textsymptoms;
        this.diag = textdiag;
        this.med = textmed;
        this.docmail = textdocmail;
        this.notes = textnotes;
    }
}
