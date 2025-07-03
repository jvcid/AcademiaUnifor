package com.example.academiaunifor
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AlteracaoCargaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alteracao_carga)

        val tvExercicioSelecionado = findViewById<TextView>(R.id.tvExercicioSelecionado)
        val sbCarga = findViewById<SeekBar>(R.id.sbCarga)
        val tvCarga = findViewById<TextView>(R.id.tvCarga)

        val exercicio = intent.getStringExtra("exercicio")
        tvExercicioSelecionado.text = "Exercício: $exercicio"

        sbCarga.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvCarga.text = "Carga: $progress kg"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
