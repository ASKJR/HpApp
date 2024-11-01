package com.albertokato.hpapi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }

    fun redirectTo(view: View) {
        //Fecha o aplicativo quando o botão de sair for clicado
        if (view.id == R.id.buttonExit) {
            finishAffinity()
            return
        }
        val intent = when (view.id) {
            R.id.buttonHpCharacterById -> Intent(this, HpCharacterByIdActivity::class.java)
            R.id.buttonHpStaff -> Intent(this, HpStaffActivity::class.java)
            R.id.buttonHpCharactersByHouse -> Intent(this, HpCharactersByHouseActivity::class.java)
            else -> return
        }
        startActivity(intent)
    }
}