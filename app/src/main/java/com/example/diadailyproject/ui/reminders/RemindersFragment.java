package com.example.diadailyproject.ui.reminders;

import static com.example.diadailyproject.Notification2.channelID;
import static com.example.diadailyproject.Notification2.messageExtra;
import static com.example.diadailyproject.Notification2.notificationID;
import static com.example.diadailyproject.Notification2.reminderExtra;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.diadailyproject.*;
import com.example.diadailyproject.databinding.FragmentRemindersBinding;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class RemindersFragment extends Fragment {

    private FragmentRemindersBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRemindersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createNotificationChannel();
        binding.notifyButton.setOnClickListener(v -> scheduleNotification());
    }

    private void scheduleNotification() {
        Intent intent = new Intent(requireContext(), Notification2.class);
        String reminder = binding.reminderET.getText().toString();
        String message = binding.messageET.getText().toString();
        intent.putExtra(reminderExtra, reminder);
        intent.putExtra(messageExtra, message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        long time = getTime();
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );
        showAlert(time, reminder, message);
    }

    private void showAlert(long time, String reminder, String message) {
        Date date = new Date(time);
        DateFormat dateFormat = android.text.format.DateFormat.getLongDateFormat(requireContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(requireContext());

        new AlertDialog.Builder(requireContext())
                .setTitle("Notification Scheduled")
                .setMessage(
                        "Reminder: " + reminder +
                                "\nMessage: " + message +
                                "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
                .setPositiveButton("Okay", (dialog, which) -> {})
                .show();
    }

    private long getTime() {
        int minute = binding.timePicker.getMinute();
        int hour = binding.timePicker.getHour();
        int day = binding.datePicker.getDayOfMonth();
        int month = binding.datePicker.getMonth();
        int year = binding.datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTimeInMillis();
    }

    private void createNotificationChannel() {
        String name = "Notification Channel";
        String desc = "A Channel Dedicated to Notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelID, name, importance);
        channel.setDescription(desc);
        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
}
