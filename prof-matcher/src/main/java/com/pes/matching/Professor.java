package com.pes.matching;

public class Professor {
    private final String name;
    private final String designation;
    private final String research;
    private final String teaching;
    private final String about;
    private final String publicationsJournals;
    private final String publicationsConferences;
    private final String education;
    private final String mail;
    private final String phone;

    // Constructor
    public Professor(String name, String designation, String research, String teaching, 
                     String about, String publicationsJournals, String publicationsConferences, 
                     String education, String mail, String phone) {
        this.name = name;
        this.designation = designation;
        this.research = research;
        this.teaching = teaching;
        this.about = about;
        this.publicationsJournals = publicationsJournals;
        this.publicationsConferences = publicationsConferences;
        this.education = education;
        this.mail = mail;
        this.phone = phone;
    }

    // Getters
    public String getName() { return name; }
    public String getDesignation() { return designation; }
    public String getResearch() { return research; }
    public String getTeaching() { return teaching; }
    public String getAbout() { return about; }
    public String getPublicationsJournals() { return publicationsJournals; }
    public String getPublicationsConferences() { return publicationsConferences; }
    public String getEducation() { return education; }
    public String getMail() { return mail; }
    public String getPhone() { return phone; }
}