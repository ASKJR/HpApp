package com.albertokato.hpapi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HpStaffActivity : AppCompatActivity() {
    private lateinit var hpApi: HpApi
    private lateinit var tvHpStaff: TextView
    private lateinit var staff: List<HpCharacter>
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hp_staff)
        tvHpStaff = findViewById(R.id.tvHpStaff)
        progressBar = findViewById(R.id.progressBarStaff)

        hpApi = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HpApi::class.java)

        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                staff = withContext(Dispatchers.IO) {
                    hpApi.getStaff()
                }

                if (staff.isNotEmpty()) {
                    staff = staff.sortedBy { it.name }
                    staff = staff.mapIndexed { index, hpCharacter ->
                        hpCharacter.copy(name = "${index + 1}. ${hpCharacter.name}\n")
                    }
                    val hpStaffNames = staff.joinToString("") { it.name }
                    tvHpStaff.text = hpStaffNames
                } else {
                    tvHpStaff.text = "Nenhum professor encontrado"
                }
                progressBar.visibility = View.GONE
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Log.e("HpStaffActivity", "Erro ao buscar professores: ${e.message}")
                tvHpStaff.text = "Erro ao buscar professores: ${e.message}"
            }
        }
    }
}