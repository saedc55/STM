package saedc.example.com;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import androidx.core.app.NavUtils;

import android.widget.TimePicker;

import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import saedc.example.com.Alarm.AlarmReceiver;
import saedc.example.com.Alarm.LocalData;


public class SettingsActivity extends AppCompatPreferenceActivity implements
        Preference.OnPreferenceClickListener {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (preference, value) -> {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);

        } else if (preference instanceof RingtonePreference) {
            // For ringtone preferences, look up the correct display value
            // using RingtoneManager.
            if (TextUtils.isEmpty(stringValue)) {
                // Empty values correspond to 'silent' (no ringtone).
                preference.setSummary(R.string.pref_ringtone_silent);

            } else {
                Ringtone ringtone = RingtoneManager.getRingtone(
                        preference.getContext(), Uri.parse(stringValue));

                if (ringtone == null) {
                    // Clear the summary if there was a lookup error.
                    preference.setSummary(null);
                } else {
                    // Set the summary to reflect the new ringtone display
                    // name.
                    String name = ringtone.getTitle(preference.getContext());
                    preference.setSummary(name);
                }
            }

        } else {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    //General

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);
            salarynotify();

            final SharedPreferences shrd = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final SharedPreferences.Editor editor = shrd.edit();

            LocalData localData;


            findPreference("dailyremainder").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    timenotify();

                    return false;
                }
            });


            findPreference("clocktime").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(final Preference preference) {


                    Calendar now = Calendar.getInstance();
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            final SharedPreferences hourPreferences = getActivity().getSharedPreferences("h&m", MODE_PRIVATE);
                            final SharedPreferences.Editor editor1 = hourPreferences.edit();
                            editor1.putInt("selectedHour", selectedHour);
                            editor1.putInt("selectedMinute", selectedMinute);

                            preference.setSummary(getFormatedTime(selectedHour, selectedMinute));
                            editor.putString("clocktime", String.valueOf(getFormatedTime(selectedHour, selectedMinute)));
                            editor.commit();
                            timenotify();
                        }
                    }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
                    mTimePicker.setTitle("اختر الوقت");
                    mTimePicker.show();

                    return false;
                }
            });


            findPreference("notifications_salary").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    salarynotify();
                    return false;
                }
            });


            findPreference("datemonthly").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                Calendar now = Calendar.getInstance();

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onPreferenceClick(final Preference preference) {
                    final MaterialNumberPicker numberPicker = new MaterialNumberPicker(getActivity());
                    numberPicker.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(31);
                    numberPicker.setTextSize(100);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("يوم الراتب")
                            .setView(numberPicker)
                            .setNegativeButton(getString(android.R.string.cancel), null)
                            .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    preference.setSummary(String.valueOf(numberPicker.getValue()));
                                    editor.putString("datemonthly", String.valueOf(numberPicker.getValue()));
                                    editor.commit();

                                    salarynotify();
                                }
                            })
                            .show();

                    return false;
                }
            });

            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
            bindPreferenceSummaryToValue(findPreference("datemonthly"));
            bindPreferenceSummaryToValue(findPreference("clocktime"));


        }

        public void timenotify() {
            final SharedPreferences hourPreferences = getActivity().getSharedPreferences("h&m", MODE_PRIVATE);

            final SharedPreferences shrd = PreferenceManager.getDefaultSharedPreferences(getActivity());
            Calendar setcalendar = Calendar.getInstance();
            setcalendar.set(Calendar.HOUR_OF_DAY, hourPreferences.getInt("selectedHour", 8));
            setcalendar.set(Calendar.MINUTE, hourPreferences.getInt("selectedMinute", 1));
            setcalendar.set(Calendar.SECOND, 0);

            if (shrd.getBoolean("dailyremainder", true)) {
                Intent intent1 = new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 11, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                am.setRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            } else {
                Intent intent1 = new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 11, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                am.cancel(pendingIntent);
                pendingIntent.cancel();
            }


        }


        public String getFormatedTime(int h, int m) {
            final String OLD_FORMAT = "HH:mm";
            final String NEW_FORMAT = "hh:mm a";

            String oldDateString = h + ":" + m;
            String newDateString = "";

            try {
                SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
                Date d = sdf.parse(oldDateString);
                sdf.applyPattern(NEW_FORMAT);
                newDateString = sdf.format(d);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return newDateString;
        }

        @TargetApi(Build.VERSION_CODES.N)
        public Locale getCurrentLocale() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return getResources().getConfiguration().getLocales().get(0);
            } else {
                //noinspection deprecation
                return getResources().getConfiguration().locale;
            }
        }


        public void salarynotify() {

            final SharedPreferences shrd = PreferenceManager.getDefaultSharedPreferences(getActivity());
            cancelAlarm();
            if (shrd.getBoolean("notifications_salary", true)) {

                Calendar currenttime = Calendar.getInstance();
                currenttime.set(Calendar.DAY_OF_MONTH, Integer.valueOf(shrd.getString("datemonthly", "27")));
                Intent intent1 = new Intent(getActivity(), AlarmReceiver.class);
                intent1.putExtra("savingName", "المدخرات");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 55, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                am.setRepeating(AlarmManager.RTC_WAKEUP,
                        currenttime.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY * 30, pendingIntent);

            } else {

                cancelAlarm();

            }
        }

        public void cancelAlarm() {
            Intent intent1 = new Intent(getActivity(), AlarmReceiver.class);
            intent1.putExtra("savingName", "المدخرات");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 55, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
            am.cancel(pendingIntent);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


}
