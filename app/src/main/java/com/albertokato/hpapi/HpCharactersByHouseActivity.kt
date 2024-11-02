package com.albertokato.hpapi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HpCharactersByHouseActivity : AppCompatActivity() {
    private lateinit var hpApi: HpApi
    private lateinit var tvHpStudentsByHouse: TextView
    private lateinit var progressBarStudentsByHouse : ProgressBar
    private lateinit var studentsByHouse: List<HpCharacter>
    private lateinit var rbHufflepuff: RadioButton
    private lateinit var rbGryffindor: RadioButton
    private lateinit var rbRavenclaw: RadioButton
    private lateinit var rbSlytherin: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hp_characters_by_house)
        tvHpStudentsByHouse = findViewById(R.id.tvHpStudentsByHouse)
        rbHufflepuff = findViewById(R.id.rbHufflepuff)
        rbGryffindor = findViewById(R.id.rbGryffindor)
        rbRavenclaw = findViewById(R.id.rbRavenclaw)
        rbSlytherin = findViewById(R.id.rbSlytherin)
        progressBarStudentsByHouse = findViewById(R.id.progressBarStudentsByHouse)
        hpApi = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HpApi::class.java)
    }

    fun getHpCharactersByHouse(view: View) {
        cleanUp()
        val selectedHouse = getSelectedHouseByUser()
        lifecycleScope.launch {
            try {
                studentsByHouse = withContext(Dispatchers.IO) {
                    hpApi.getCharactersByHouse(selectedHouse)
                }

                if (studentsByHouse.isNotEmpty()) {
                    // Filtrar apenas os estudantes
                    studentsByHouse = studentsByHouse.filter { it.hogwartsStudent }
                    studentsByHouse = studentsByHouse.sortedBy { it.name }
                    studentsByHouse = studentsByHouse.mapIndexed { index, hpCharacter ->
                        hpCharacter.copy(name = "${index + 1}. ${hpCharacter.name}")
                    }
                    val hpStudentNames = studentsByHouse.joinToString("") { "${it.name}\n" }
                    tvHpStudentsByHouse.text = hpStudentNames
                } else {
                    tvHpStudentsByHouse.text = "Nenhum estudante encontrado"
                }
                progressBarStudentsByHouse.visibility = View.GONE
            } catch (e: Exception) {
                Log.e("HpCharactersByHouseActivity", "Erro ao buscar estudantes: ${e.message}")
                progressBarStudentsByHouse.visibility = View.GONE
                tvHpStudentsByHouse.text = "Erro ao buscar estudantes: ${e.message}"
            }
        }
    }

    private fun cleanUp() {
        progressBarStudentsByHouse.visibility = View.VISIBLE
        tvHpStudentsByHouse.text = ""
        studentsByHouse = emptyList()
    }

    private fun getSelectedHouseByUser(): String {
        return when {
            rbHufflepuff.isChecked -> "Hufflepuff"
            rbGryffindor.isChecked -> "Gryffindor"
            rbRavenclaw.isChecked -> "Ravenclaw"
            rbSlytherin.isChecked -> "Slytherin"
            else -> ""
        }
    }
}