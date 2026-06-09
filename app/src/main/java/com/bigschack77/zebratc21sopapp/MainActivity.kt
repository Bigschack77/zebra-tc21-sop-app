package com.bigschack77.zebratc21sopapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bigschack77.zebratc21sopapp.data.repository.InMemorySopRepository
import com.bigschack77.zebratc21sopapp.ui.SopApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SopApp(repository = InMemorySopRepository())
        }
    }
}
