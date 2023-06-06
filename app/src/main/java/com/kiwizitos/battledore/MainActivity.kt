package com.kiwizitos.battledore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.kiwizitos.battledore.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusTxt = binding.txtStatus
        val playerHealthTxt = binding.txtPlayerHealth
        val botHealthTxt = binding.txtBotHealth
        val attackButton = binding.btnAttack

        var status = true
        var playerHealth = 100
        var botHealth = 100

        fun setData(newPlayerHealth: Int?, newBotHealth: Int?, newStatus: Boolean) {
            statusTxt.text = if (newStatus) {
                "Sua vez"
            } else {
                "Vez do bot"
            }
            playerHealthTxt.setText(getString(R.string.vida_do_jogador, newPlayerHealth))
            botHealthTxt.setText(getString(R.string.vida_do_bot, newBotHealth))
        }

        setData(newPlayerHealth = playerHealth, newBotHealth = botHealth, newStatus = status)

        fun attack(damage: Int) {
            botHealth -= damage
            status = false
            setData(newPlayerHealth = playerHealth, newBotHealth = botHealth, newStatus = false)
        }

        fun takeDamage(damage: Int) {
            playerHealth -= damage
            status = true
            setData(newPlayerHealth = playerHealth, newBotHealth = botHealth, newStatus = true)
        }

        fun resetData() {
            status = true
            playerHealth = 100
            botHealth = 100
            setData(newPlayerHealth = playerHealth, newBotHealth = botHealth, newStatus = status)
            attackButton.isVisible = true
        }

        fun setDialog(text: String) {
            val alert = AlertDialog.Builder(this)
            alert.create()
            alert.setTitle(text)
            alert.setPositiveButton("Recomeçar") { dialog, which ->
                resetData()
                dialog.dismiss()
            }
            alert.setNegativeButton("Sair") { dialog, which ->
                finish()
            }
            alert.show()
        }

        fun currentLife() {
            if (playerHealth <= 0) {
                playerHealthTxt.setText(getString(R.string.vida_do_jogador, 0))
                attackButton.isVisible = false
                setDialog("Você perdeu")
            }

            if (botHealth <= 0) {
                botHealthTxt.setText(getString(R.string.vida_do_bot, 0))
                attackButton.isVisible = false
                setDialog("Você venceu")
            }
        }

        attackButton.setOnClickListener {
            attack(Random.nextInt(1, 15))
            attackButton.isVisible = false
            Handler().postDelayed({
                takeDamage(Random.nextInt(0, 15))
                attackButton.isVisible = true
            }, 1000)
            currentLife()
        }
    }
}

