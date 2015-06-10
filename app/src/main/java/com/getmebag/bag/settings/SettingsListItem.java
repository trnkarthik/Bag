package com.getmebag.bag.settings;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

/**
 * Created by karthiktangirala on 5/11/15.
 */

@AutoValue
public abstract class SettingsListItem {

    @Nullable
    public abstract String getMainIcon();

    @Nullable
    public abstract String getTitle();

    @Nullable
    public abstract SettingsItemType getType();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_SettingsListItem.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {

        abstract Builder setMainIcon(final String mainIcon);

        abstract Builder setTitle(final String title);

        abstract Builder setType(final SettingsItemType type);

        abstract SettingsListItem build();

    }

}
