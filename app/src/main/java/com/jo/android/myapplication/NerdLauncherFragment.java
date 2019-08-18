package com.jo.android.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public  class NerdLauncherFragment extends Fragment {
    private static final String TAG = "NerdLauncherFragment";
    RecyclerView recyclerView;
    public static NerdLauncherFragment newInstance(){
        return new NerdLauncherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_nerd_launcher,container,false);
        recyclerView=v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return v;

    }

    public void setupAdapter(){
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm=getActivity().getPackageManager();
        List<ResolveInfo> activities=pm.queryIntentActivities(startupIntent,0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo resolveInfo, ResolveInfo t1) {
                PackageManager pm=getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(resolveInfo.loadLabel(pm).toString(),t1.loadLabel(pm).toString());

            }
        });
        recyclerView.setAdapter(new ActivitiesAdapters(activities));
        Log.i(TAG, "Found " + activities.size() + " activities.");


    }

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ResolveInfo resolveInfo;
        TextView textViewActivityLabel;

        public ActivityHolder(@NonNull View itemView) {
            super(itemView);
            textViewActivityLabel=(TextView) itemView;
            textViewActivityLabel.setOnClickListener(this);

        }

        public void bindActivity(ResolveInfo ri){
            resolveInfo=ri;
            PackageManager pm=getActivity().getPackageManager();
            String label=resolveInfo.loadLabel(pm).toString();
            textViewActivityLabel.setText(label);


        }

        @Override
        public void onClick(View view) {
            ActivityInfo activityInfo=resolveInfo.activityInfo;

            Intent i=new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName,activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);

        }
    }
    private class ActivitiesAdapters extends RecyclerView.Adapter<ActivityHolder>{

         private final List<ResolveInfo> mActivities;

        private ActivitiesAdapters(List<ResolveInfo> activities ){
            mActivities = activities;
        }

        @NonNull
        @Override
        public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            View view=layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {

            ResolveInfo resolveInfo=mActivities.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }
}
