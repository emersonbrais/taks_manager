package com.example.task_app.utils
import kotlin.random.Random

class KeyUtil {

    companion object {
        fun generateRandomKey(length: Int): String {
            val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            return (1..length)
                .map { characters[Random.nextInt(characters.length)] }
                .joinToString("")
        }
    }

}