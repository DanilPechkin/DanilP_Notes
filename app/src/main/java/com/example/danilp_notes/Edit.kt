package com.example.danilp_notes

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.card_edit.*
import java.lang.reflect.Array

class Edit : AppCompatActivity() {
    lateinit var database: SQLiteDatabase
    lateinit var mSettings: SharedPreferences
    val APP_PREFERENCES: String = "mysettings"
    val APP_PREFERENCES_ID: String = "PresentId"
    lateinit var id: String
    val selection = "_id =?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_edit)

        val dbHelper = DBHelper(this)
        database = dbHelper.writableDatabase
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        id = mSettings.getString(APP_PREFERENCES_ID, "").toString()

        val selectionArgs = arrayOf(id)

        val cursor: Cursor = database.query(DBHelper.TABLE_CONTACTS, null, selection,
            selectionArgs, null, null, null)

        if (cursor.moveToFirst()){
            Header.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_HEADER)))
            Note.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_NOTE)))
        }
        buttonBack.setOnClickListener{
            onSave()
        }
        buttonDelete.setOnClickListener{
            onDelete()
        }
    }
    private fun onSave(){
        val selectionArgs = arrayOf(id)
        val contentValues = ContentValues()
        contentValues.put(DBHelper.KEY_HEADER, Header.text.toString())
        contentValues.put(DBHelper.KEY_NOTE, Note.text.toString())

        database.update(DBHelper.TABLE_CONTACTS, contentValues, DBHelper.KEY_ID + "= ?", arrayOf(id));

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun onDelete(){

    }
}