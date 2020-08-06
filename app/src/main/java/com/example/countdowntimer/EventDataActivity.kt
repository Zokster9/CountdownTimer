package com.example.countdowntimer

import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_event_data.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
class EventDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_data)
        var event: Event

        findViewById<Button>(R.id.btAddEvent).setOnClickListener {
            if (!isValidEventName(eventName.text.toString())) {
                Snackbar.make(it, "Enter the name for the event!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else if (!isValidEventDateFormat(eventDate.text.toString())) {
                Snackbar.make(it, "Enter the date for the event in the correct format (e.g. 01.01.2020.)", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else if (!isValidEventDate(eventDate.text.toString())) {
                Snackbar.make(it, "Enter a valid date!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            } else {
                if (eventTime.text.toString() == "") {
                    event = Event(eventName.text.toString(), getDate(eventDate.text.toString()), LocalTime.MIDNIGHT)
                } else {
                    if (!isValidEventTimeFormat(eventTime.text.toString())) {
                        Snackbar.make(it, "Enter the time for the event in the correct format (e.g. 17:32)", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    } else if (!isValidEventTime(eventTime.text.toString(), eventDate.text.toString())) {
                        Snackbar.make(it, "The time entered must be at least 1h from now", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    } else {
                        event = Event(eventName.text.toString(), getDate(eventDate.text.toString()), getTime(eventTime.text.toString()))
                    }
                }
            }
        }
    }

    private fun getDate(eventDate: String) : LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.")
        return LocalDate.parse(eventDate, formatter)
    }

    private fun getTime(eventTime: String) : LocalTime {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return LocalTime.parse(eventTime, formatter)
    }

    private fun isValidEventName(eventName: String) : Boolean {
        return eventName != ""
    }

    private fun isValidEventDateFormat(eventDate: String) : Boolean {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.")
        return try {
            LocalDate.parse(eventDate, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    private fun isValidEventTimeFormat(eventTime: String) : Boolean {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return try {
            LocalTime.parse(eventTime, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    private fun isValidEventDate(eventDate: String) : Boolean {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.")
        val date = LocalDate.parse(eventDate, formatter)
        val currentDate = LocalDate.now()
        return date >= currentDate
    }

    private fun isValidEventTime(eventTime: String, eventDate: String) : Boolean {
        val time = getTime(eventTime)
        val date = getDate(eventDate)
        val currentDate = LocalDate.now()
        if (date == currentDate) {
            var currentTime = LocalTime.now()
            currentTime = currentTime.plusHours(1)
            return time >= currentTime
        }
        return true
    }
}