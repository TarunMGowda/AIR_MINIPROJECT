package com.pes.matching;

public class Professor {
    private String name, designation, research, teaching, about;
    private String pubJournals, pubConfs, education, mail, phone;
    private String responsibilities, image, department, campus;

    public Professor(String name, String designation, String research, String teaching, 
                     String about, String pubJournals, String pubConfs, String education, 
                     String mail, String phone, String responsibilities, String image, 
                     String department, String campus) {
        this.name = name; this.designation = designation; this.research = research;
        this.teaching = teaching; this.about = about; this.pubJournals = pubJournals;
        this.pubConfs = pubConfs; this.education = education; this.mail = mail;
        this.phone = phone; this.responsibilities = responsibilities; this.image = image;
        this.department = department; this.campus = campus;
    }

    // Getters
    public String getName() { return name; }
    public String getDesignation() { return designation; }
    public String getResearch() { return research; }
    public String getTeaching() { return teaching; }
    public String getAbout() { return about; }
    public String getPubJournals() { return pubJournals; }
    public String getPubConfs() { return pubConfs; }
    public String getEducation() { return education; }
    public String getMail() { return mail; }
    public String getPhone() { return phone; }
    public String getResponsibilities() { return responsibilities; }
    public String getImage() { return image; }
    public String getDepartment() { return department; }
    public String getCampus() { return campus; }
}