package com.example.inventoryapp.ui.dashboard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inventoryapp.R
import android.content.Intent
import com.google.android.material.card.MaterialCardView
import com.example.inventoryapp.activity_motorep_reportes

class Reportes : AppCompatActivity() {

    private lateinit var coordinatorLayoutR: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reportes)

        coordinatorLayoutR = findViewById(R.id.CoordinatorLayoutR)
        ViewCompat.setOnApplyWindowInsetsListener(coordinatorLayoutR) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val cvMotorepDashboard = findViewById<MaterialCardView>(R.id.CardViewMotorepDashboard)
        cvMotorepDashboard.setOnClickListener(){
            val intent = Intent(this, activity_motorep_reportes::class.java)
            startActivity(intent)
        }

    }


}