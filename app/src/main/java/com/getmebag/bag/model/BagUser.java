package com.getmebag.bag.model;

/**
 * Created by karthiktangirala on 3/2/15.
 */
public class BagUser {

    private final String provider;
    private final String providerId;
    private final String token;
    private final String accessToken;

    private final String email;
    private final String profilePictureURL;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String birthDate;
    private final String gender;
    private final String profileLink;
//    private final Location location;
    private final String phoneNumber;

    private BagUser(Builder builder) {
        this.provider = builder.provider;
        this.providerId = builder.providerId;
        this.token = builder.token;
        this.accessToken = builder.accessToken;

        this.email = builder.email;
        this.profilePictureURL = builder.profilePictureURL;
        this.userName = builder.userName;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.birthDate = builder.birthDate;
        this.gender = builder.gender;
        this.profileLink = builder.profileLink;
//        this.location = builder.location;
        this.phoneNumber = builder.phoneNumber;
    }

    public String getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getToken() {
        return token;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getProfileLink() {
        return profileLink;
    }

//    public Location getLocation() {
//        return location;
//    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public static class Builder {

        private String provider;
        private String providerId;
        private String token;
        private String accessToken;

        private String email;
        private String profilePictureURL;
        private String userName;
        private String firstName;
        private String lastName;
        private String birthDate;
        private String gender;
        private String profileLink;
        //        private Location location;
        private String phoneNumber;

        public Builder setProvider(String provider) {
            this.provider = provider;
            return this;
        }

        public Builder setProviderId(String providerId) {
            this.providerId = providerId;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setProfilePictureURL(String profilePictureURL) {
            this.profilePictureURL = profilePictureURL;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setBirthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder setProfileLink(String profileLink) {
            this.profileLink = profileLink;
            return this;
        }

//        public Builder setLocation(Location location) {
//            this.location = location;
//            return this;
//        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public BagUser build() {
            return new BagUser(this);
        }

    }
}
