package com.getmebag.bag.userprofile;

/**
 * Created by karthiktangirala on 4/19/15.
 */
public class ProfileItem {
    private final String itemIndicationIcon;
    private final String itemDescription;
    private final String itemDescriptionHeader;
    private final int itemDescriptionHeaderColor;
    private final String itemActionIcon;
    private final int itemActionType;
    private final int itemActionIconSize;
    private final String itemCTAIcon;
    private final String itemCTADialogMessage;

    public ProfileItem(Builder builder) {
        this.itemIndicationIcon = builder.itemIndicationIcon;
        this.itemDescription = builder.itemDescription;
        this.itemDescriptionHeader = builder.itemDescriptionHeader;
        this.itemDescriptionHeaderColor = builder.itemDescriptionHeaderColor;
        this.itemActionIcon = builder.itemActionIcon;
        this.itemActionType = builder.itemActionType;
        this.itemActionIconSize = builder.itemActionIconSize;
        this.itemCTAIcon = builder.itemCTAIcon;
        this.itemCTADialogMessage = builder.itemCTADialogMessage;
    }

    public String getItemIndicationIcon() {
        return itemIndicationIcon;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemDescriptionHeader() {
        return itemDescriptionHeader;
    }

    public int getItemDescriptionHeaderColor() {
        return itemDescriptionHeaderColor;
    }

    public String getItemActionIcon() {
        return itemActionIcon;
    }
    public int getItemActionType() {
        return itemActionType;
    }

    public int getItemActionIconSize() {
        return itemActionIconSize;
    }

    public String getItemCTAIcon() {
        return itemCTAIcon;
    }
    public String getItemCTADialogMessage() {
        return itemCTADialogMessage;
    }

    public static class Builder {

        private String itemIndicationIcon;
        private String itemDescription;
        private String itemDescriptionHeader;
        private int itemDescriptionHeaderColor;
        private String itemActionIcon;
        private int itemActionType;
        private int itemActionIconSize;
        private String itemCTAIcon;
        private String itemCTADialogMessage;

        public Builder setItemIndicationIcon(String itemIndicationIcon) {
            this.itemIndicationIcon = itemIndicationIcon;
            return this;
        }

        public Builder setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
            return this;
        }

        public Builder setItemDescriptionHeader(String itemDescriptionHeader) {
            this.itemDescriptionHeader = itemDescriptionHeader;
            return this;
        }

        public Builder setItemDescriptionHeaderColor(int itemDescriptionHeaderColor) {
            this.itemDescriptionHeaderColor = itemDescriptionHeaderColor;
            return this;
        }

        public Builder setItemActionIcon(String itemActionIcon) {
            this.itemActionIcon = itemActionIcon;
            return this;
        }

        public Builder setItemActionType(int itemActionType) {
            this.itemActionType = itemActionType;
            return this;
        }

        public Builder setItemActionIconSize(int itemActionIconSize) {
            this.itemActionIconSize = itemActionIconSize;
            return this;
        }

        public Builder setItemCTAIcon(String itemCTAIcon) {
            this.itemCTAIcon = itemCTAIcon;
            return this;
        }

        public Builder setItemCTADialogMessage(String itemCTADialogMessage) {
            this.itemCTADialogMessage = itemCTADialogMessage;
            return this;
        }

        public ProfileItem build() {
            return new ProfileItem(this);
        }

    }

}
