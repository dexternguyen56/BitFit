package com.example.bitfit

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val button = findViewById<Button>(R.id.button_save_item)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(findViewById<EditText>(R.id.EditText_AddItem).text)
                && TextUtils.isEmpty(findViewById<EditText>(R.id.EditText_addCal).text) ) {

                setResult(Activity.RESULT_CANCELED, replyIntent)

            } else {
                val healthItem = findViewById<EditText>(R.id.EditText_AddItem).text.toString()
                val calories = findViewById<EditText>(R.id.EditText_addCal).text.toString()

                replyIntent.putExtra(EXTRA_FOOD, healthItem)
                replyIntent.putExtra(EXTRA_CALORIES,calories)
                setResult(Activity.RESULT_OK, replyIntent)


            }


            finish()
        }
    }


    companion object {
        const val EXTRA_FOOD = "FOOD_EXTRA"
        const val EXTRA_CALORIES = "CALORIES_EXTRA"
    }

    //hide keyboard
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }


}