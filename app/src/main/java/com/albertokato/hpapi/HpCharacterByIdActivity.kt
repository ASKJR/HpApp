package com.albertokato.hpapi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HpCharacterByIdActivity : AppCompatActivity() {
    private lateinit var etHpCharacterId: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewResult: TextView
    private lateinit var hpApi: HpApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hp_character_by_id)
        etHpCharacterId = findViewById(R.id.etHpCharacterId)
        progressBar = findViewById(R.id.progressBar)
        textViewResult = findViewById(R.id.textViewResult)
        hpApi = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HpApi::class.java)
    }

    fun getHpCharacterById(view: View) {
        val id = etHpCharacterId.text.toString()
        progressBar.visibility = View.VISIBLE
        textViewResult.text = ""

        if (id.isEmpty()) {
            Toast.makeText(this, "Por favor, insira um ID", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
            return
        }

        lifecycleScope.launch {
            try {
                val hpCharacters: List<HpCharacter>? = withContext(Dispatchers.IO) {
                    hpApi.getCharacterById(id)
                }
                val hpCharacter = hpCharacters?.firstOrNull()
                progressBar.visibility = View.GONE
                if (hpCharacter != null) {
                    textViewResult.text = "Nome: ${hpCharacter.name} Casa: ${hpCharacter.house}"
                } else {
                    textViewResult.text = "Personagem não encontrado"
                    etHpCharacterId.text.clear()
                }

            } catch (e: Exception) {
                Log.e("HpCharacterByIdActivity", "Erro ao buscar personagem: ${e.message}")
                progressBar.visibility = View.GONE
                Toast.makeText(this@HpCharacterByIdActivity, "Personagem não encontrado, tente novamente", Toast.LENGTH_SHORT).show()
            }

        }
    }

}