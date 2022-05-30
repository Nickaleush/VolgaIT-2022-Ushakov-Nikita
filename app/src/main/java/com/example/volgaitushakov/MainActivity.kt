package com.example.volgaitushakov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_fragment)
        supportFragmentManager.beginTransaction().add(R.id.main_fragment, StockFragment()).commit()
    }
}