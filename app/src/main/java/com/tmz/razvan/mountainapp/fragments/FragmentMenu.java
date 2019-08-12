package com.tmz.razvan.mountainapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.tmz.razvan.mountainapp.Core.UserCore;
import com.tmz.razvan.mountainapp.R;
import com.tmz.razvan.mountainapp.activities.EmergencyActivity;
import com.tmz.razvan.mountainapp.activities.LoginActivity;
import com.tmz.razvan.mountainapp.activities.ProfileActivity;
import com.tmz.razvan.mountainapp.models.MenuItemModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentMenu extends Fragment {

    public FragmentMenu() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragment_menu, container, false);

        InitMenuList(rootView);
        return rootView;
    }

    private void InitMenuList(View rootView) {
        List<MenuItemModel> menuItemsList = new ArrayList<MenuItemModel>();
        menuItemsList.add(new MenuItemModel("Profile", R.drawable.ic_profile));
        menuItemsList.add(new MenuItemModel("My Hikes", R.drawable.ic_my_hikes));
        menuItemsList.add(new MenuItemModel("Emergency Signal", R.drawable.ic_sos));
        menuItemsList.add(new MenuItemModel("Settings", R.drawable.ic_settings));
        menuItemsList.add(new MenuItemModel("Log Out", R.drawable.ic_logout));

        final ListView listview = (ListView)rootView.findViewById(R.id.lv_fragment_menu_list);
        MenuItemModelAdapter menuListAdapter = new MenuItemModelAdapter(this.getContext(), R.layout.row_menu, menuItemsList);
        listview.setAdapter(menuListAdapter );
        listview.setDivider(null);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i)
                {
                    case 0:
                        intent = new Intent(getContext(), ProfileActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getContext(), EmergencyActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        UserCore.Instance().setLoggedIn(false);
                        intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private class MenuItemModelAdapter extends ArrayAdapter<MenuItemModel>
    {
        private final Context context;
        private final List<MenuItemModel> menuListItems;

        public MenuItemModelAdapter(Context context, int textViewResourceId, List<MenuItemModel> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            menuListItems = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_menu, parent, false);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.iv_row_menu_picture);
            TextView textView = (TextView) rowView.findViewById(R.id.iv_row_menu_content);

            MenuItemModel menuItem = menuListItems.get(position);

            textView.setText(menuItem.getText());
            imageView.setImageDrawable(getResources().getDrawable(menuItem.getResourceId()));
            return rowView;
        }
    }
}


