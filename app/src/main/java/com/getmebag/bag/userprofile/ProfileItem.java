package com.getmebag.bag.userprofile;

import com.getmebag.bag.dialog.DialogActionsListener;
import com.getmebag.bag.model.CachedUserData;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

/**
 * Created by karthiktangirala on 4/19/15.
 */

@AutoValue
public abstract class ProfileItem {

    public static Builder builder() {
        return new AutoValue_ProfileItem.Builder()
                .setItemDescriptionColor(0)
                .setItemDescriptionHeaderColor(0)
                .setItemActionType(0)
                .setItemActionIconSize(0);
    }

    @Nullable
    public abstract String getItemIndicationIcon();

    @Nullable
    public abstract String getItemDescription();

    @Nullable
    public abstract String getItemDescriptionHeader();

    @Nullable
    public abstract int getItemDescriptionColor();

    @Nullable
    public abstract int getItemDescriptionHeaderColor();

    @Nullable
    public abstract String getItemActionIcon();

    @Nullable
    public abstract int getItemActionType();

    @Nullable
    public abstract int getItemActionIconSize();

    @Nullable
    public abstract String getItemCTAIcon();

    @Nullable
    public abstract String getItemCTADialogMessage();

    @Nullable
    public abstract String getItemCTADialogTitle();

    @Nullable
    public abstract CachedUserData getCachedUserData();

    @Nullable
    public abstract DialogActionsListener getDialogActionsListener();

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setItemIndicationIcon(String itemIndicationIcon);

        public abstract Builder setItemDescription(String itemDescription);

        public abstract Builder setItemDescriptionHeader(String itemDescriptionHeader);

        public abstract Builder setItemDescriptionColor(int itemDescriptionColor);

        public abstract Builder setItemDescriptionHeaderColor(int itemDescriptionHeaderColor);

        public abstract Builder setItemActionIcon(String itemActionIcon);

        public abstract Builder setItemActionType(int itemActionType);

        public abstract Builder setItemActionIconSize(int itemActionIconSize);

        public abstract Builder setItemCTAIcon(String itemCTAIcon);

        public abstract Builder setItemCTADialogMessage(String itemCTADialogMessage);

        public abstract Builder setItemCTADialogTitle(String itemCTADialogTitle);

        public abstract Builder setCachedUserData(CachedUserData cachedUserData);

        public abstract Builder setDialogActionsListener(DialogActionsListener listener);

        public abstract ProfileItem build();

    }

}
