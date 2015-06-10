package com.getmebag.bag.connections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.getmebag.bag.MainActivity;
import com.getmebag.bag.R;
import com.getmebag.bag.annotations.AllContactsList;
import com.getmebag.bag.annotations.FrequentContactsList;
import com.getmebag.bag.base.BagAuthBaseFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.Boolean.TRUE;

/**
 * Created by karthiktangirala on 4/29/15.
 */
public class InviteContactsFragment extends BagAuthBaseFragment {

    ListView listView;

    @InjectView(R.id.invite_contacts_finish)
    Button finishButton;

    @Inject
    ContactsListAdapter contactsListAdapter;

    List<Object> contactListItemList;

    @Inject
    @AllContactsList
    List<ContactListItem> allContactsList;

    @Inject
    @FrequentContactsList
    List<ContactListItem> frequentContactsList;

    @Inject
    public InviteContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invite_contacts, container, false);
        ButterKnife.inject(this, rootView);
        showFinishButtonIfRequired();
        listView = (ListView) rootView.findViewById(R.id.contacts_listview);

        contactListItemList = new ArrayList<>();

        contactsListAdapter.addAll(fetchContactsData());

        listView.setAdapter(contactsListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getAdapter().getItem(position) instanceof ContactListItem) {
                    //TODO
                    Toast.makeText(getActivity(),
                            ((ContactListItem) parent.getAdapter().getItem(position)).phone,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    private List<Object> fetchContactsData() {

        //Fragment Manager for Fragment Header
        contactListItemList.add(getActivity().getSupportFragmentManager());

        //String for section Header
        if (frequentContactsList.size() > 0) {
            contactListItemList.add(getString(R.string.contacts_frequent_contacts));
        }
        contactListItemList.addAll(frequentContactsList);

        //String for section Header
        if (allContactsList.size() > 0) {
            contactListItemList.add(getString(R.string.contacts_all_contacts_header));
        }
        contactListItemList.addAll(allContactsList);

        if (allContactsList.size() == 0 && frequentContactsList.size() == 0) {
            contactListItemList.add(getString(R.string.contacts_no_contacts_share_app));
        }
        //Boolean for share list item
        contactListItemList.add(TRUE);

        return contactListItemList;
    }

    private void showFinishButtonIfRequired() {
        if (!isThisLoggedInFTX.get()) {
            finishButton.setVisibility(GONE);
        } else {
            finishButton.setVisibility(VISIBLE);
        }
    }

    @OnClick(R.id.invite_contacts_finish)
    public void inviteContactsFinishButton(View view) {
        startActivity(MainActivity.intent(getActivity()));
    }

}
