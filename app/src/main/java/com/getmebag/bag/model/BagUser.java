package com.getmebag.bag.model;

/**
 * Created by karthiktangirala on 3/2/15.
 */
public class BagUser {

    private final String provider;
    private final String providerId;
    private final String token;
    private final CachedUserData cachedUserData;
    private final UserLocation location;
    private final String phoneNumber;
    private final String bagUserName;

    private BagUser(Builder builder) {
        this.provider = builder.provider;
        this.providerId = builder.providerId;
        this.token = builder.token;
        this.cachedUserData = builder.cachedUserData;
        this.location = builder.location;
        this.phoneNumber = builder.phoneNumber;
        this.bagUserName = builder.bagUserName;
    }

    public String getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getUniqueId() {
        return provider + ":" + providerId;
    }

    public String getToken() {
        return token;
    }

    public UserLocation getLocation() {
        return location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBagUserName() {
        return bagUserName;
    }

    public CachedUserData getCachedUserData() {
        return cachedUserData;
    }

    public static class Builder {

        private String provider;
        private String providerId;
        private String token;
        private CachedUserData cachedUserData;
        private UserLocation location;
        private String phoneNumber;
        private String bagUserName;

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

        public Builder setCachedUserData(CachedUserData cachedUserData) {
            this.cachedUserData = cachedUserData;
            return this;
        }

        public Builder setLocation(UserLocation location) {
            this.location = location;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setBagUserName(String bagUserName) {
            this.bagUserName = bagUserName;
            return this;
        }

        public Builder(BagUser bagUser) {
            this.provider = bagUser.provider;
            this.providerId = bagUser.providerId;
            this.token = bagUser.token;
            this.cachedUserData = bagUser.cachedUserData;
            this.location = bagUser.location;
            this.phoneNumber = bagUser.phoneNumber;
            this.bagUserName = bagUser.bagUserName;
        }

        public Builder() {
        }

        public BagUser build() {
            return new BagUser(this);
        }

    }
}
