package com.example.danilp_notes

import android.annotation.SuppressLint
import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var layturn : Boolean = true
    var num : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener{
            createNew()
        }
    }
    @SuppressLint("ResourceAsColor")
    fun createNew(){
        lateinit var lay: LinearLayout
        if (layturn) lay = findViewById(R.id.firstlay)
        else lay = findViewById(R.id.secondlay)
        var card = CardView(this)
        val cardParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        cardParams.setMargins(10, 10,10, 10)
        var shape = GradientDrawable()
        shape.cornerRadius = 30F
        shape.setBackgroundColor(Color.parseColor("#CD660C"))
        card.background = shape
        card.layoutParams = cardParams
        card.setContentPadding(10, 10,10,10)
        card.cardElevation = 8F
        card.maxCardElevation = 12F
        var cardlay = LinearLayout(this)
        cardlay.orientation = LinearLayout.VERTICAL

        var zagolovok = TextView(this)
        card.addView(cardlay)
        
        lay.addView(card)
    }
}
