package org.testytu.mezunapp;

public class Student {


    private String id = "";
    private String ad = "";
    private String soyad = "";
    private String girisYil = "";
    private String mezunYil = "";

    private String profilePhotoURL = "";

    public Student(String id, String ad, String soyad, String girisYil, String mezunYil, String profilePhotoURL) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.girisYil = girisYil;
        this.mezunYil = mezunYil;
        this.profilePhotoURL = profilePhotoURL;
    }

    public String getId() {
        return id;
    }
    public String getAd() {
        return ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public String getGirisYil() {
        return girisYil;
    }

    public String getMezunYil() {
        return mezunYil;
    }

    public String getEgitimAralik(){
        return girisYil+"-"+mezunYil;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ad='" + ad + '\'' +
                ", soyad='" + soyad + '\'' +
                ", girisYil='" + girisYil + '\'' +
                ", mezunYil='" + mezunYil + '\'' +
                ", profilePhotoURL='" + profilePhotoURL + '\'' +
                '}';
    }

    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }
}
