package com.example.recipeproject.UI.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.recipeproject.DataAccess.DataAccess;
import com.example.recipeproject.InterfaceGetData.getUserCallback;
import com.example.recipeproject.Repsentation.ViewPagerAdapter;
import com.example.recipeproject.UI.activities.ChangePasswordActivity;
import com.example.recipeproject.UI.activities.HomeActivity;
import com.example.recipeproject.R;
import com.example.recipeproject.UI.activities.UpdateUserProfileActivity;
import com.example.recipeproject.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ImageButton button = (ImageButton) rootview.findViewById(R.id.btnPopupProfile);

        // pop up menu
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.logout_menu_item:
                                logoutUser();
                                return true;
                            case R.id.update_profile_menu_item:
                                startActivity(new Intent(view.getContext(), UpdateUserProfileActivity.class));
                                return true;
                            case R.id.change_pwd_menu_item:
                                startActivity(new Intent(view.getContext(), ChangePasswordActivity.class));
                                return true;
                        }
                        return false;
                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.profile_page_popup_menu, popup.getMenu());
                popup.show();
            }
        });

        tabLayout = rootview.findViewById(R.id.tabLayout);
        viewPager2 = rootview.findViewById(R.id.viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this.getActivity());
        adapter.addFragment(new FavoriteRecipesFragment(), "Favorites");
        adapter.addFragment(new MyRecipeFragment(), "My recipes");

        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Favorites");
                        break;
                    case 1:
                        tab.setText("My recipes");
                        break;
                }
            }
        }).attach();

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView userName = getView().findViewById(R.id.textViewName);
        CircleImageView userAvatar = getView().findViewById(R.id.userAvatar);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DataAccess.getUserById(new getUserCallback() {
            @Override
            public void onResponse(User user) {
                userName.setText(user.getName());

                Picasso.with(getContext())
                        .load(user.getImage_link())
                        .error(R.drawable.placeholder_avatar_foreground)
                        .resize(200,200)
                        .centerCrop()
                        .into(userAvatar);
            }
        }, firebaseUser.getUid());

    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this.getContext(), HomeActivity.class));
    }

}