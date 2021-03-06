/*
 * Copyright (C) 2010-2014 The MPDroid Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.namelessdev.mpdroid.fragments;

import com.namelessdev.mpdroid.MPDApplication;
import com.namelessdev.mpdroid.MainMenuActivity;

import org.a0z.mpd.MPD;
import org.a0z.mpd.MPDOutput;
import org.a0z.mpd.exception.MPDServerException;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class OutputsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private final MPDApplication app = MPDApplication.getInstance();

    private ArrayList<MPDOutput> outputs;

    private static final String TAG = "OutputsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        outputs = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ArrayAdapter<MPDOutput> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice, outputs);
        setListAdapter(arrayAdapter);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getListView().setOnItemClickListener(this);

        // Not needed since MainMenuActivity will take care of telling us to refresh
        if (!(getActivity() instanceof MainMenuActivity)) {
            refreshOutputs();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        app.oMPDAsyncHelper.execAsync(new Runnable() {
            @Override
            public void run() {
                final MPD mpd = app.oMPDAsyncHelper.oMPD;
                final MPDOutput output = outputs.get(position);
                try {
                    if (getListView().isItemChecked(position)) {
                        mpd.enableOutput(output.getId());
                    } else {
                        mpd.disableOutput(output.getId());
                    }
                } catch (final MPDServerException e) {
                    Log.e(TAG, "Failed to modify output.", e);
                }
                final Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshOutputs();
                        }
                    });
                }
            }
        });
    }

    public void refreshOutputs() {
        app.oMPDAsyncHelper.execAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<MPDOutput> mpdOutputs = app.oMPDAsyncHelper.oMPD.getOutputs();
                    outputs.clear();
                    outputs.addAll(mpdOutputs);
                } catch (MPDServerException e) {
                    Log.e(TAG, "Failed to list outputs.", e);
                }
                final Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        @SuppressWarnings("unchecked")
                        public void run() {
                            ((ArrayAdapter<MPDOutput>) getListAdapter()).notifyDataSetChanged();
                            final ListView list = getListView();
                            for (int i = 0; i < outputs.size(); i++) {
                                list.setItemChecked(i, outputs.get(i).isEnabled());
                            }
                        }
                    });
                }
            }
        });
    }
}
