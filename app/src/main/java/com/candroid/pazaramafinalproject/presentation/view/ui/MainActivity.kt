package com.candroid.pazaramafinalproject.presentation.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.candroid.pazaramafinalproject.databinding.ActivityMainBinding
import com.candroid.pazaramafinalproject.util.UtilsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UtilsActivity.setCurrentActivity(this@MainActivity)
    }

    override fun onDestroy() {
        UtilsActivity.setCurrentActivity(null)
        super.onDestroy()
    }
}