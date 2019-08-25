package com.guestlogix.traveleruikit.calendarpicker;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarPicker extends LinearLayout {
    private WeekView[] weekViews = new WeekView[6];
    private TextView monthTextView;
    private int currentMonth;
    private int currentYear;
    private Date selectedDate;
    private CalendarPickerListener listener = null;

    public interface CalendarPickerListener {
        void onMonthChange(int month, int year);
        void onDateSelect(Date date);
        boolean isDateAvailable(Date date);
    }

    public CalendarPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOrientation(VERTICAL);

        Button prevButton = new Button(context);
        prevButton.setText("<");
        prevButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 150));
        prevButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int month = currentMonth - 1;
                if (month < 1) {
                    CalendarPicker.this.setMonth(12, currentYear - 1);
                } else {
                    CalendarPicker.this.setMonth(month, currentYear);
                }
            }
        });

        Button nextButton = new Button(context);
        nextButton.setText(">");
        nextButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, 150));
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int month = currentMonth + 1;
                if (month > 12) {
                    CalendarPicker.this.setMonth(1, currentYear + 1);
                } else {
                    CalendarPicker.this.setMonth(month, currentYear);
                }
            }
        });

        monthTextView = new TextView(context);
        monthTextView.setTypeface(null, Typeface.BOLD);
        monthTextView.setTextSize(20);
        monthTextView.setLayoutParams(new LayoutParams(0, 150, 7));
        monthTextView.setGravity(Gravity.CENTER);

        LinearLayout heading = new LinearLayout(context);
        heading.setOrientation(HORIZONTAL);

        heading.addView(prevButton);
        heading.addView(monthTextView);
        heading.addView(nextButton);

        this.addView(heading);

        DateFormatSymbols symbols = new DateFormatSymbols();

        LinearLayout labels = new LinearLayout(context);
        labels.setOrientation(HORIZONTAL);
        labels.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        for (int i = 1; i < 8; i++) {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LayoutParams(0, 100, 1));
            textView.setText(symbols.getShortWeekdays()[i]);
            textView.setGravity(Gravity.CENTER);

            labels.addView(textView);
        }

        this.addView(labels);

        for (int i = 0; i < 6; i++) {
            final int week = i;

            weekViews[i] = new WeekView(context, new WeekView.Listener() {
                @Override
                public void onButtonClick(int day) {
                    Date date = getDate(Pair.create(week, day));
                    CalendarPicker.this.setSelectedDate(date);
                }
            });

            this.addView(weekViews[i]);
        }
    }

    public void setListener(CalendarPickerListener listener) {
        this.listener = listener;
    }

    public void setMonth(int month, int year) {
        currentMonth = month;
        currentYear = year;

        reloadDates();

        if (listener != null)
            listener.onMonthChange(currentMonth, currentYear);
    }

    public int getMonth() {
        return currentMonth;
    }

    public int getYear() {
        return currentYear;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void reloadDates() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth, 1);

        int startingDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                int dayOfMonth = ((i * 7) + j) - startingDayOfWeek + 2;

                ToggleButton button = weekViews[i].buttons[j];

                if (dayOfMonth <= 0 || dayOfMonth > daysInMonth) {
                    button.setText(null);
                    button.setTextOn(null);
                    button.setTextOff(null);
                    button.setEnabled(false);
                    button.setVisibility(INVISIBLE);
                } else {
                    button.setTextOff(String.valueOf(dayOfMonth));
                    button.setTextOn(String.valueOf(dayOfMonth));
                    button.setText(String.valueOf(dayOfMonth));
                    button.setEnabled(true);
                    button.setVisibility(VISIBLE);

                    if (listener != null) {
                        calendar.set(currentYear, currentMonth, dayOfMonth);
                        button.setEnabled(listener.isDateAvailable(calendar.getTime()));
                    }
                }

                if (selectedDate != null) {
                    calendar.setTime(selectedDate);

                    if (currentMonth == calendar.get(Calendar.MONTH) && currentYear == calendar.get(Calendar.YEAR) && dayOfMonth == calendar.get(Calendar.DAY_OF_MONTH)) {
                        button.setChecked(true);
                    } else {
                        button.setChecked(false);
                    }
                }
            }
        }

        calendar.set(currentYear, currentMonth, 1);

        monthTextView.setText(new SimpleDateFormat("MMMM, yyyy", Locale.getDefault()).format(calendar.getTime()));
    }

    public void setSelectedDate(Date date) {
        if (selectedDate != null) {
            Pair<Integer, Integer> currentPosition = getPosition(selectedDate);
            ToggleButton button = weekViews[currentPosition.first].buttons[currentPosition.second];
            button.setChecked(false);
        }

        Pair<Integer, Integer> position = getPosition(date);
        ToggleButton button = weekViews[position.first].buttons[position.second];
        button.setChecked(true);

        selectedDate = date;

        if (listener != null)
            listener.onDateSelect(date);
    }

    static class WeekView extends LinearLayout {
        interface Listener {
            void onButtonClick(int day);
        }

        private ToggleButton[] buttons = new ToggleButton[7];

        WeekView(Context context, Listener listener) {
            super(context);

            this.setOrientation(HORIZONTAL);
            this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            for (int i = 0; i < 7; i++) {
                final int index = i;

                ToggleButton button = new ToggleButton(context);
                button.setLayoutParams(new LayoutParams(0,100,1));

                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onButtonClick(index);
                    }
                });
                this.addView(button);

                buttons[i] = button;
            }
        }
    }

    static private Pair<Integer, Integer> getPosition(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        return Pair.create(week - 1 , day - 1);
    }

    private Date getDate(Pair<Integer, Integer> position) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth, 1);

        int startingDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int day = ((position.first * 7) + position.second + 1) - (startingDayOfWeek - 1);

        calendar.set(currentYear, currentMonth, day);
        return calendar.getTime();
    }
}
