package com.example.danilp_notes

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.card_edit.*
import java.lang.reflect.Array

class Edit : AppCompatActivity() {
    // DB
    lateinit var database: SQLiteDatabase
    lateinit var mSettings: SharedPreferences
    // Preference that gets id
    val APP_PREFERENCES: String = "mysettings"
    val APP_PREFERENCES_ID: String = "PresentId"
    lateinit var id: String
    // Selection for DB
    val selection = "_id =?"

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_edit)

        // Init DB
        val dbHelper = DBHelper(this)
        database = dbHelper.writableDatabase

        // Get id from preference
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        id = mSettings.getString(APP_PREFERENCES_ID, "").toString()

        // Selection for DB
        val selectionArgs = arrayOf(id)

        // Get saved note
        val cursor: Cursor = database.query(DBHelper.TABLE_CONTACTS, null, selection,
            selectionArgs, null, null, null)
        if (cursor.moveToFirst()){
            Header.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_HEADER)))
            Note.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_NOTE)))
        }

        // Set homebutton to actionbar
        val actionBar = supportActionBar
        actionBar!!.setHomeButtonEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.title = ""
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton)
    }

    // Actionbar menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Select menu item
        when (item.itemId){
            R.id.action_delete -> {
                onDelete()
                return true
            }
            R.id.action_set_time -> {
                setNotification(Header.text.toString(),Note.text.toString())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Set actionbar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Get homebutton click
    override fun onSupportNavigateUp(): Boolean {
        onSave()
        return true
    }

    // Save note
    private fun onSave(){
        val selectionArgs = arrayOf(id)
        val contentValues = ContentValues()
        contentValues.put(DBHelper.KEY_HEADER, Header.text.toString())
        contentValues.put(DBHelper.KEY_NOTE, Note.text.toString())

        database.update(DBHelper.TABLE_CONTACTS, contentValues, DBHelper.KEY_ID + "= ?", arrayOf(id));

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Delete note
    private fun onDelete(){
        val selectionArgs = arrayOf(id)

        database.delete(DBHelper.TABLE_CONTACTS, selection, selectionArgs)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun setNotification(title: String, message: String) {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                "NOTE_CHANNEL",
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "CHANNEL_FOR_SHOWING_NOTES"
            mNotificationManager.createNotificationChannel(channel)
        }
        val mBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.backbutton)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setWhen(System.currentTimeMillis() + 10000)

        val intent = Intent(applicationContext, Edit::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(pi)
        mNotificationManager.notify(0, mBuilder.build())
    }
}