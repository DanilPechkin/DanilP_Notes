package com.example.danilp_notes

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var layturn : Boolean = true

    var num : Int = 1

    val APP_PREFERENCES : String = "mysettings"
    val APP_PREFERENCES_ID : String = "PresentId"
    lateinit var mSettings : SharedPreferences
    lateinit var dbHelper: DBHelper
    lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)
        database = dbHelper.writableDatabase
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        val cursor: Cursor = database.query(DBHelper.TABLE_CONTACTS, null,
            null, null, null, null, null)

        if(cursor.moveToFirst()){
            do {
                num = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_ID))
                createNew(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_HEADER)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_NOTE)))
            }while (cursor.moveToNext())
        }


        button.setOnClickListener{
            num++
            val contentValues = ContentValues()
            contentValues.put(DBHelper.KEY_HEADER, "")
            contentValues.put(DBHelper.KEY_NOTE, "")
            database.insert(DBHelper.TABLE_CONTACTS, null, contentValues)
            val editor = mSettings.edit()
            editor.putString(APP_PREFERENCES_ID, num.toString())
            editor.apply()
            val intent = Intent(this, Edit::class.java)
            startActivity(intent)
            createNew("Header","Your note")
        }
    }

    private fun createNew(headertext : String, notetext : String){
        var lay: LinearLayout = if (layturn) findViewById(R.id.firstlay)
        else findViewById(R.id.secondlay)
        layturn = !layturn

        var card = CardView(this)
        val cardParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        cardParams.setMargins(10, 10,10, 10)
        var shape = GradientDrawable()
        shape.cornerRadius = 30F
        shape.setColor(Color.parseColor("#CD660C"))
        card.background = shape
        card.layoutParams = cardParams
        card.setContentPadding(20, 10,20,20)
        card.cardElevation = 8F
        card.maxCardElevation = 12F
        var cardlay = LinearLayout(this)
        cardlay.orientation = LinearLayout.VERTICAL


        var header = TextView(this)
        header.text = headertext
        header.hint = "Header"
        header.setTextColor(Color.parseColor("#CCFCB759"))
        header.setHintTextColor(Color.parseColor("#CCFCB759"))
        header.textSize = 20F

        var note = TextView(this)
        note.text = notetext
        note.hint = "Your note"
        note.setHintTextColor(Color.parseColor("#CCFCB759"))
        note.setTextColor(Color.parseColor("#CCFCB759"))
        note.textSize = 16F

        var numtext = TextView(this)
        numtext.visibility = View.GONE
        numtext.text = num.toString()

        card.setOnClickListener{
            val editor = mSettings.edit()
            editor.putString(APP_PREFERENCES_ID, numtext.text.toString())
            editor.apply()

            val intent = Intent(this, Edit::class.java)
            startActivity(intent)
        }
        
        cardlay.addView(header)
        cardlay.addView(note)
        cardlay.addView(numtext)
        card.addView(cardlay)
        
        lay.addView(card)
    }
}
