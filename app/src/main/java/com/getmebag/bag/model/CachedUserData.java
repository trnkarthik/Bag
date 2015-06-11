package com.getmebag.bag.model;

/**
 * Created by karthiktangirala on 3/2/15.
 */
public class CachedUserData {

    private final String accessToken;

    private final String email;
    private final String profilePictureURL;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String birthDate;
    private final String gender;
    private final String profileLink;

    private CachedUserData(Builder builder) {
        this.accessToken = builder.accessToken;
        this.email = builder.email;
        this.profilePictureURL = builder.profilePictureURL;
        this.userName = builder.userName;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.birthDate = builder.birthDate;
        this.gender = builder.gender;
        this.profileLink = builder.profileLink;
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

    public static class Builder {

        private String accessToken;
        private String email;
        private String profilePictureURL;
        private String userName;
        private String firstName;
        private String lastName;
        private String birthDate;
        private String gender;
        private String profileLink;

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

        public Builder(CachedUserData cachedUserData) {
            this.accessToken = cachedUserData.accessToken;
            this.email = cachedUserData.email;
            this.profilePictureURL = cachedUserData.profilePictureURL;
            this.userName = cachedUserData.userName;
            this.firstName = cachedUserData.firstName;
            this.lastName = cachedUserData.lastName;
            this.birthDate = cachedUserData.birthDate;
            this.gender = cachedUserData.gender;
            this.profileLink = cachedUserData.profileLink;
        }

        public Builder() {
        }

        public CachedUserData build() {
            return new CachedUserData(this);
        }

    }
}
