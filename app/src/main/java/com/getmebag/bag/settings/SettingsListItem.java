package com.getmebag.bag.settings;

/**
 * Created by karthiktangirala on 5/11/15.
 */
public class SettingsListItem {

    private final String mainIcon;
    private final String title;
    private final SettingsItemType type;

    private SettingsListItem(Builder builder) {
        this.mainIcon = builder.mainIcon;
        this.title = builder.title;
        this.type = builder.type;
    }

    public String getMainIcon() {
        return mainIcon;
    }

    public String getTitle() {
        return title;
    }

    public SettingsItemType getType() {
        return type;
    }

    public static class Builder {

        private String mainIcon;
        private String title;
        private SettingsItemType type;

        public Builder setMainIcon(String mainIcon) {
            this.mainIcon = mainIcon;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setType(SettingsItemType type) {
            this.type = type;
            return this;
        }

        public SettingsListItem build() {
            return new SettingsListItem(this);
        }

    }

}
