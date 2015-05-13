package com.getmebag.bag.connections;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.getmebag.bag.MainActivity;
import com.getmebag.bag.R;
import com.getmebag.bag.androidspecific.prefs.BooleanPreference;
import com.getmebag.bag.annotations.IsThisLoggedInFirstTimeUse;
import com.getmebag.bag.base.BagAuthBaseFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by karthiktangirala on 4/29/15.
 */
public class InviteContactsFragment extends BagAuthBaseFragment {

    ListView listView;

    @Inject
    @IsThisLoggedInFirstTimeUse
    BooleanPreference isThisLoggedInFTX;

    @InjectView(R.id.invite_contacts_finish)
    Button finishButton;

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

        ArrayAdapter adapter = new ArrayAdapter<Contact>(getActivity(),
                android.R.layout.simple_list_item_2, android.R.id.text1, new ContactsProvider(getActivity()).getFrequentContacts()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(getItem(position).name);
                text2.setText(getItem(position).phone);
                return view;
            }
        };

        listView.setAdapter(adapter);
        return rootView;
    }

//    public ArrayList<PhoneContactInfo> getFrequentContacts() {
//        ArrayList<PhoneContactInfo> arrContacts = new ArrayList<PhoneContactInfo>();
//        ContentResolver cr = getActivity().getContentResolver();
//        String [] projection = {ContactsContract.Contacts._ID,
//                ContactsContract.Contacts.DISPLAY_NAME,
//                ContactsContract.Contacts.HAS_PHONE_NUMBER};
//        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
//                projection, null, null, null);
//        if (cur.getCount() > 0) {
//            while (cur.moveToNext()) {
//                String id = cur.getString(
//                        cur.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cur.getString(
//                        cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
////                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
//                    //Query phone here.  Covered next
//
//                Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
//                while (phoneCursor.moveToNext()) {
//                    String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    arrContacts.add(new PhoneContactInfo(name, phoneNumber));
//                }
//                phoneCursor.close();
////                }
//            }
//        }
//        return arrContacts;
//    }
//
//    public ArrayList<PhoneContactInfo> getContacts2() {
//        ArrayList<PhoneContactInfo> arrContacts = new ArrayList<PhoneContactInfo>();
//        PhoneContactInfo phoneContactInfo = null;
//        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//        Cursor cursor = getActivity().getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone._ID}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
////            int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
//
//
//            phoneContactInfo = new PhoneContactInfo(contactName, contactNumber);
////            phoneContactInfo.setPhoneContactID(phoneContactID);
//            phoneContactInfo.setName(contactName);
//            phoneContactInfo.setPhoneNumber(contactNumber);
//            if (isPhoneNumberValid(contactNumber)) {
//                arrContacts.add(phoneContactInfo);
//            }
//            phoneContactInfo = null;
//            cursor.moveToNext();
//        }
//        cursor.close();
//        cursor = null;
//        return arrContacts;
//    }

    private void showFinishButtonIfRequired() {
        if (!isThisLoggedInFTX.get()) {
            finishButton.setVisibility(GONE);
        } else {
            finishButton.setVisibility(VISIBLE);
        }
    }

    private boolean isPhoneNumberValid(String contactNumber) {
        return !isEmpty(contactNumber)
                && Patterns.PHONE.matcher(contactNumber).matches()
                && (contactNumber.length() >= 7);
    }

    @OnClick(R.id.invite_contacts_finish)
    public void inviteContactsFinishButton(View view) {
        startActivity(MainActivity.intent(getActivity()));
    }

}
