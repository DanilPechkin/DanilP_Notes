package com.example.danilp_notes

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var layturn : Boolean = true
    var num : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        button.setOnClickListener{
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

        card.setOnClickListener{
            val intent = Intent(this, Edit::class.java)
            intent.putExtra("key", 1)
            startActivity(intent)
        }

        var header = TextView(this)
        header.text = headertext
        header.setTextColor(Color.parseColor("#CCFCB759"))
        header.textSize = 20F

        var note = TextView(this)
        note.text = notetext
        note.setTextColor(Color.parseColor("#CCFCB759"))
        note.textSize = 16F
        
        cardlay.addView(header)
        cardlay.addView(note)
        card.addView(cardlay)
        
        lay.addView(card)
    }
}
