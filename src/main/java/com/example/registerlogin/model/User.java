package com.example.registerlogin.model;

public class User {

    private final String firstName;
    private final String lastName;
    private final String id;
    private final String phoneNumber;
    private final String password;
    private final Boolean active;
    private final String token;
    private final String expiredTime;
    private final String dateCreated;
    private final String email;
    private final String occupation;
    private final String address;
    private final int age;
    private final Gender gender;
    private final String pic;
    private final String role;



    public User(String firstName, String lastName, String id, String phoneNumber, String password, Boolean active, String token, String expiredTime, String dateCreated, String email, String occupation, String address, int age, Gender gender, String pic, String role) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.active = active;
        this.token = token;
        this.expiredTime = expiredTime;
        this.dateCreated = dateCreated;
        this.email = email;
        this.occupation = occupation;
        this.address = address;
        this.age = age;
        this.gender = gender;
        this.pic = pic;
        this.role = role;
    }

    private User(UserBuilder builder) {

        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.id = builder.id;
        this.phoneNumber = builder.phoneNumber;
        this.password = builder.password;
        this.active = builder.active;
        this.token = builder.token;
        this.expiredTime = builder.expiredTime;
        this.dateCreated = builder.dateCreated;
        this.email = builder.email;
        this.occupation = builder.occupation;
        this.address = builder.address;
        this.age = builder.age;
        this.gender = builder.gender;
        this.pic = builder.pic;
        this.role = builder.role;
    }

    public String getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getActive() {
        return active;
    }

    public String getToken() {
        return token;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getEmail() {
        return email;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getPic() {
        return pic;
    }

    public String getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public static class UserBuilder {

        private String firstName;
        private String lastName;
        private String id;
        private String phoneNumber;
        private String password;
        private Boolean active;
        private String token;
        private String expiredTime;
        private String dateCreated;
        private String email;
        private String occupation;
        private String address;
        private int age;
        private Gender gender;
        private String pic;
        private String role;

        public UserBuilder(){

        }

        public UserBuilder role(String role) {
            this.role = role;
            return this;
        }

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public UserBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }
        public UserBuilder active(Boolean active) {
            this.active = active;
            return this;
        }
        public UserBuilder token(String token) {
            this.token = token;
            return this;
        }
        public UserBuilder expiredTime(String expiredTime) {
            this.expiredTime = expiredTime;
            return this;
        }
        public UserBuilder dateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder occupation(String occupation) {
            this.occupation = occupation;
            return this;
        }

        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }

        public UserBuilder age(int age) {
            this.age = age;
            return this;
        }

        public UserBuilder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public UserBuilder pic(String pic) {
            this.pic = pic;
            return this;
        }

        public User build(){
            User user = new User(this);
            return user;
        }
    }
}