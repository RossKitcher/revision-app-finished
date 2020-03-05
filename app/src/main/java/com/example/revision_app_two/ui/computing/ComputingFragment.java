package com.example.revision_app_two.ui.computing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.revision_app_two.MainActivity;
import com.example.revision_app_two.R;
import com.example.revision_app_two.StableArrayAdapter;
import com.example.revision_app_two.ViewTopics;

import java.util.List;

public class ComputingFragment extends Fragment {

    private ComputingViewModel dashboardViewModel;
    SharedPreferences pref;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(ComputingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_computing, container, false);


        pref = getActivity().getSharedPreferences("Pref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("currentSub", "computing");
        editor.commit();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getContext();
        activity.getSupportActionBar().setTitle("Computing Modules");

        List<String> moduleList = MainActivity.getModules(getContext());

        ListView listView = (ListView) getActivity().findViewById(R.id.comp_listview);

        StableArrayAdapter adapter = new StableArrayAdapter(getContext(), android.R.layout.simple_list_item_1, moduleList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("currentMod", item);
                editor.commit();

                Intent intent = new Intent(getContext(), ViewTopics.class);
                startActivity(intent);
            }
        });
    }
}
