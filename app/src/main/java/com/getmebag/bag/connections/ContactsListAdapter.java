package com.getmebag.bag.connections;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.getmebag.bag.R;
import com.getmebag.bag.dialog.ViewPagerDialogFragment;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by karthiktangirala on 4/19/15.
 */
public class ContactsListAdapter extends ArrayAdapter<Object> {

    ArrayList<Object> items;
    private LayoutInflater layoutInflater;

    @Inject
    ViewPagerDialogFragment viewPagerDialogFragment;

    @Inject
    public ContactsListAdapter(Context context, final LayoutInflater layoutInflater) {
        super(context, 0);
        this.layoutInflater = layoutInflater;
    }

    @Override
    public void addAll(Collection<? extends Object> collection) {
        this.items = new ArrayList<>(collection);
        super.addAll(this.items);
    }

    @Override
    public int getCount() {
        return (items != null ? items.size() : 0);
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        return (getItem(position) instanceof ContactListItem ? 0 :
                (getItem(position) instanceof String ? 1 :
                        (getItem(position) instanceof Boolean ? 2 :
                                3)));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);

        switch (viewType) {

            case 0:
                ViewHolder holder;
                if (convertView == null) {
                    convertView = layoutInflater
                            .inflate(R.layout.list_item_invite_contacts, parent, false);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                setUp(holder, position);
                return convertView;

            case 1:
                HeaderViewHolder headerViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater
                            .inflate(R.layout.list_header_invite_contacts, parent, false);
                    headerViewHolder = new HeaderViewHolder(convertView);
                    convertView.setTag(headerViewHolder);
                } else {
                    headerViewHolder = (HeaderViewHolder) convertView.getTag();
                }
                setUp(headerViewHolder, position);
                return convertView;

            case 2:
                convertView = layoutInflater.inflate(R.layout.list_footer_invite_contacts_share,
                        parent, false);
                View footerSepartor = convertView.findViewById(R.id.invite_contacts_footer_separator);

                if (getCount() == 3) {
                    footerSepartor.setVisibility(VISIBLE);
                } else {
                    footerSepartor.setVisibility(GONE);
                }

                return convertView;

            case 3:
                convertView = layoutInflater.inflate(R.layout.header_fragment_invite_contacts,
                        parent, false);
                setUpMainHeaderView(position, convertView);
                return convertView;

            default:
                return convertView;

        }
    }

    private void setUpMainHeaderView(int position, View convertView) {
        TextView whyShouldIInviteLink = (TextView) convertView.
                findViewById(R.id.invite_contacts_big_header_why_invite_link);
        TextView whyShouldIInviteIcon = (TextView) convertView.
                findViewById(R.id.invite_contacts_big_header_why_invite_link_icon);
        whyShouldIInviteLink.
                setOnClickListener(whyShouldIInviteOnClickListener(
                        (FragmentManager) getItem(position)));
        whyShouldIInviteIcon.
                setOnClickListener(whyShouldIInviteOnClickListener(
                        (FragmentManager) getItem(position)));
    }

    private View.OnClickListener whyShouldIInviteOnClickListener(final FragmentManager supportFragmentManager) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(supportFragmentManager);
            }
        };
    }


    private void setUp(HeaderViewHolder headerViewHolder, int position) {
        String item = (String) items.get(position);
        headerViewHolder.headerText.setText(item);
    }

    private void setUp(ViewHolder holder, int position) {
        ContactListItem item = (ContactListItem) items.get(position);

        holder.name.setText(item.name);
        holder.phoneNumber.setText(item.phone);
        holder.image.setVisibility(GONE);
        holder.imageAlt.setVisibility(VISIBLE);

        if (item.photoThumbnailUrl != null) {
            holder.image.setVisibility(VISIBLE);
            holder.image.setImageURI(Uri.parse(item.photoThumbnailUrl));
            holder.imageAlt.setVisibility(GONE);
        } else {
            if (item.name != null && item.name.length() > 1) {
                holder.imageAlt.setVisibility(VISIBLE);
                holder.imageAlt.setText(getContext().getString(R.string.icon_font_user));
                holder.image.setVisibility(GONE);
            }
        }

    }

    static class ViewHolder {

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        @InjectView(R.id.contacts_list_main_icon_alt)
        TextView imageAlt;
        @InjectView(R.id.contacts_list_main_icon)
        CircleImageView image;
        @InjectView(R.id.contacts_list_name)
        TextView name;
        @InjectView(R.id.contacts_list_phone_number)
        TextView phoneNumber;
        @Optional
        @InjectView(R.id.contacts_list_invite_button)
        TextView inviteButton;

    }

    static class HeaderViewHolder {

        public HeaderViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        @InjectView(R.id.contacts_list_header_text)
        TextView headerText;

    }

    private void showEditDialog(FragmentManager supportFragmentManager) {
        viewPagerDialogFragment.show(supportFragmentManager,
                getContext().getString(R.string.dialog_invite_contacts_why_should_i_invite));
    }

}
