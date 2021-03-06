package com.example.charibee.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.charibee.activities.EditProfileActivity;
import com.example.charibee.activities.UpdateInterestsActivity;
import com.example.charibee.adapters.InterestsIconAdapter;
import com.example.charibee.data.RoleTheme;
import com.example.charibee.models.User;
import com.example.service.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    User user;

    // ui views
    private TextView tvInterests;
    public ImageView ivAvatar;
    private ImageView ivAdmin;
    private TextView tvFullName;
    private TextView tvUsername;
    private TextView tvBio;
    private RecyclerView rvInterests;

    // adapter for interests
    private InterestsIconAdapter adapter;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // find views
        tvInterests = view.findViewById(R.id.profile_interests_tv);
        ivAvatar = view.findViewById(R.id.profile_avatar_iv);
        ivAdmin = view.findViewById(R.id.profile_admin_iv);
        tvFullName = view.findViewById(R.id.profile_fullname);
        tvUsername = view.findViewById(R.id.profile_username);
        tvBio = view.findViewById(R.id.profile_bio);
        rvInterests = view.findViewById(R.id.profile_interests_rv);

        // change toolbar layout
        setHasOptionsMenu(true);

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set values
        setValues();

        // set admin/volunteer views
        setAdminViews();
    }

    // options menu for editing profile & updating interests
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.profile_frag_top_menu, menu);

        // if admin, hide interests setting
        if (RoleTheme.isAdmin()) {
            menu.findItem(R.id.pf_update_interests).setVisible(false);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pf_edit_pfp:
                goEditProfileActivity();
                return true;
            case R.id.pf_update_interests:
                goUpdateInterestsActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // set values of all the ui fields
    private void setValues() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        user = new User(currentUser);

        String firstName = (String) currentUser.get("firstName");
        String lastName = (String) currentUser.get("lastName");
        String username = currentUser.getUsername();
        String bio = user.getBio();

        // set profile pic
        ParseFile profilePic = (ParseFile) currentUser.get("profilePic");
        if (profilePic != null) {
            try {
                if (profilePic.getUrl() != null) {
                    downloadAndShowImage(profilePic);
                } else if (profilePic.getData() != null) {
                    byte[] photoBytes = profilePic.getData();
                    ivAvatar.setImageBitmap(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length));
                } else {
                    Log.e(TAG, "couldnt load profile pic");
                }
            } catch (ParseException e) {
                downloadAndShowImage(profilePic);
            }
        } else {
            Log.e(TAG, "couldn't load profile pic");
        }

        // set text
        tvFullName.setText(firstName + " " + lastName);
        tvUsername.setText("@" + username);
        tvBio.setText(bio);

        // set interests
        updateAdapter(user.getStringInterests());
    }

    // if current user is an admin, change some views
    private void setAdminViews() {
        if (ParseUser.getCurrentUser().get("role").equals("Organizer")) {
            ivAdmin.setImageResource(R.drawable.ic_admin);
            tvInterests.setVisibility(View.GONE);
            rvInterests.setVisibility(View.GONE);
        } else {
            ivAdmin.setVisibility(View.GONE);
        }
    }

    // update the adapter for interests
    private void updateAdapter(List<String> interestsList) {
        adapter = new InterestsIconAdapter(getContext(), interestsList); // (1) create adapter
        rvInterests.setAdapter(adapter); // (2) set adapter on rv
        rvInterests.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)); // (3) set layout manager on rv
        adapter.notifyDataSetChanged();
    }

    // updating values when profile edited
    @Override
    public void onResume() {
        super.onResume();
        setValues();
    }

    // go to edit profile activity
    private void goEditProfileActivity() {
        Intent i = new Intent(getContext(), EditProfileActivity.class);
        startActivity(i);
    }

    // go to update interests activity
    private void goUpdateInterestsActivity() {
        Intent i = new Intent(getContext(), UpdateInterestsActivity.class);
        startActivity(i);
    }

    // shows an image
    private void downloadAndShowImage(ParseFile profilePic) {
        Glide.with(getContext())
                .load(httpToHttps(profilePic.getUrl()))
                .circleCrop()
                .into(ivAvatar);
    }

    // converts http link to https
    private String httpToHttps(String url) {
        if (url == null) {
            return "";
        }

        if (url.contains("https")) {
            return url;
        }

        String httpsUrl = "https" + url.substring(4);
        return httpsUrl;
    }

}