package com.example.dynamicforms

import DynamicFormScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dynamicforms.ui.theme.DynamicFormsTheme
import java.io.BufferedReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Read JSON from assets
        val jsonString = assets.open("form_data.json")
            .bufferedReader()
            .use(BufferedReader::readText)

        setContent {
            DynamicFormsTheme {
                DynamicFormScreen(jsonString = jsonString)
            }
        }
    }
}