package com.example.worldtest.myinfo;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;

import com.example.worldtest.R;

public class SettingsByPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_by_preference);
    }
    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settingsfragment);
            findPreference("prefAndroid").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast toast = Toast.makeText(getActivity(), "最新版本：2.0.1\n您已是最新版本！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    return true;
                }
            });
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        }

//        @Override
//        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.xml.settingsfragment);
//        }
    }
}
