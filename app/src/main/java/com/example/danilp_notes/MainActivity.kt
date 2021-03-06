package com.example.danilp_notes

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    // Changing lay for spawning note
    var layturn : Boolean = true
    // Sets card id textview
    var num : Int = 1
    // Saves card id for Edit
    val APP_PREFERENCES : String = "mysettings"
    val APP_PREFERENCES_ID : String = "PresentId"
    lateinit var mSettings : SharedPreferences
    // Db
    lateinit var dbHelper: DBHelper
    lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initiate db
        dbHelper = DBHelper(this)
        database = dbHelper.writableDatabase
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        // Show saved notes
        val cursor: Cursor = database.query(DBHelper.TABLE_CONTACTS, null,
            null, null, null, null, null)

        if(cursor.moveToFirst()){
            do {
                num = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_ID))
                createNew(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_HEADER)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_NOTE)))
            }while (cursor.moveToNext())
        }

        // Button creates new note
        button.setOnClickListener{
            // id ++
            num++
            // Create new note in DB
            val contentValues = ContentValues()
            contentValues.put(DBHelper.KEY_HEADER, "")
            contentValues.put(DBHelper.KEY_NOTE, "")
            database.insert(DBHelper.TABLE_CONTACTS, null, contentValues)
            // Save id to get it in Edit
            val editor = mSettings.edit()
            editor.putString(APP_PREFERENCES_ID, num.toString())
            editor.apply()
            val intent = Intent(this, Edit::class.java)
            startActivity(intent)
            createNew("Header","Your note")
        }
        // Action bar
        supportActionBar?.hide()
    }
    // Create new note on main
    private fun createNew(headertext : String, notetext : String){
        // Select lay for note
        var lay: LinearLayout = if (layturn) findViewById(R.id.firstlay)
        else findViewById(R.id.secondlay)
        layturn = !layturn

        // Create note
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
        header.setTypeface(null, Typeface.BOLD)

        var note = TextView(this)
        note.text = notetext
        note.hint = "Your note"
        note.setHintTextColor(Color.parseColor("#CCFCB759"))
        note.setTextColor(Color.parseColor("#CCFCB759"))
        note.textSize = 16F

        var numtext = TextView(this)
        numtext.visibility = View.GONE
        numtext.text = num.toString()

        // Listener for Edit
        card.setOnClickListener{
            val editor = mSettings.edit()
            editor.putString(APP_PREFERENCES_ID, numtext.text.toString())
            editor.apply()

            val intent = Intent(this, Edit::class.java)
            startActivity(intent)
        }

        //Check visibility
        if (headertext == "") {
            header.visibility = View.GONE
        }
        if (notetext == ""){
            note.visibility = View.GONE
        }

        // Add note to lay
        cardlay.addView(header)
        cardlay.addView(note)
        cardlay.addView(numtext)
        card.addView(cardlay)
        
        lay.addView(card)
    }
}
