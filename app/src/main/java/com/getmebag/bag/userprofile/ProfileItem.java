package com.getmebag.bag.userprofile;

import com.getmebag.bag.dialog.DialogActionsListener;
import com.getmebag.bag.model.CachedUserData;

/**
 * Created by karthiktangirala on 4/19/15.
 */
public class ProfileItem {
    private final String itemIndicationIcon;
    private final String itemDescription;
    private final String itemDescriptionHeader;
    private final int itemDescriptionColor;
    private final int itemDescriptionHeaderColor;
    private final String itemActionIcon;
    private final int itemActionType;
    private final int itemActionIconSize;
    private final String itemCTAIcon;
    private final String itemCTADialogMessage;
    private final String itemCTADialogTitle;
    private final CachedUserData cachedUserData;
    private final DialogActionsListener dialogActionsListener;

    public ProfileItem(Builder builder) {
        this.itemIndicationIcon = builder.itemIndicationIcon;
        this.itemDescription = builder.itemDescription;
        this.itemDescriptionHeader = builder.itemDescriptionHeader;
        this.itemDescriptionColor = builder.itemDescriptionColor;
        this.itemDescriptionHeaderColor = builder.itemDescriptionHeaderColor;
        this.itemActionIcon = builder.itemActionIcon;
        this.itemActionType = builder.itemActionType;
        this.itemActionIconSize = builder.itemActionIconSize;
        this.itemCTAIcon = builder.itemCTAIcon;
        this.itemCTADialogMessage = builder.itemCTADialogMessage;
        this.cachedUserData = builder.cachedUserData;
        this.itemCTADialogTitle = builder.itemCTADialogTitle;
        this.dialogActionsListener = builder.dialogActionsListener;
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

    public int getItemDescriptionColor() {
        return itemDescriptionColor;
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

    public String getItemCTADialogTitle() {
        return itemCTADialogTitle;
    }

    public CachedUserData getCachedUserData() {
        return cachedUserData;
    }

    public DialogActionsListener getDialogActionsListener() {
        return dialogActionsListener;
    }

    public static class Builder {

        private String itemIndicationIcon;
        private String itemDescription;
        private String itemDescriptionHeader;
        private int itemDescriptionColor;
        private int itemDescriptionHeaderColor;
        private String itemActionIcon;
        private int itemActionType;
        private int itemActionIconSize;
        private String itemCTAIcon;
        private String itemCTADialogMessage;
        private String itemCTADialogTitle;
        private CachedUserData cachedUserData;
        private DialogActionsListener dialogActionsListener;

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

        public Builder setItemDescriptionColor(int itemDescriptionColor) {
            this.itemDescriptionColor = itemDescriptionColor;
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

        public Builder setItemCTADialogTitle(String itemCTADialogTitle) {
            this.itemCTADialogTitle = itemCTADialogTitle;
            return this;
        }

        public Builder setCachedUserData(CachedUserData cachedUserData) {
            this.cachedUserData = cachedUserData;
            return this;
        }

        public Builder setDialogActionsListener(DialogActionsListener listener) {
            this.dialogActionsListener = listener;
            return this;
        }

        public ProfileItem build() {
            return new ProfileItem(this);
        }

    }

}
