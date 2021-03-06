package com.example.charibee.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charibee.adapters.MessageAdapter;
import com.example.charibee.data.Data;
import com.example.charibee.functions.CustomItemDivider;
import com.example.service.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    public static final String TAG = "MessagesFragment";

    // ui views
    TextView tvNoMsgs;
    RecyclerView rvMsgs;

    // data
    List<ParseUser> allMsgsWith = new ArrayList<>();
    MessageAdapter adapter;

    public MessagesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        // bind views
        tvNoMsgs = view.findViewById(R.id.messages_no_msgs_tv);
        rvMsgs = view.findViewById(R.id.messages_msgs_rv);
        return view;
    }

    @Override
    public void onResume() {
        // populate data
        allMsgsWith = Data.getCurrUser().getAllMessagesWith();
        updateAdapter(allMsgsWith);
        super.onResume();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // recycler view adapter
        updateAdapter(allMsgsWith);

        // add dividers btwn msgs
        RecyclerView.ItemDecoration dividerItemDecoration = new CustomItemDivider(ContextCompat.getDrawable(getContext(), R.drawable.recycler_view_divider));
        rvMsgs.addItemDecoration(dividerItemDecoration);

        // populate data
        allMsgsWith = Data.getCurrUser().getAllMessagesWith();
        updateAdapter(allMsgsWith);

        if (allMsgsWith.size() > 0) {
            tvNoMsgs.setVisibility(View.GONE);
        }
    }

    // update org adapter given list of organizations
    private void updateAdapter(List<ParseUser> msgsWithVar) {
        adapter = new MessageAdapter(getContext(), msgsWithVar); // (1) create adapter
        rvMsgs.setAdapter(adapter); // (2) set adapter on rv
        rvMsgs.setLayoutManager(new LinearLayoutManager(getContext())); // (3) set layout manager on rv
        adapter.notifyDataSetChanged();
    }

}