package com.multazamgsd.glimovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.multazamgsd.glimovie.databinding.ActivityMainBinding
import com.multazamgsd.glimovie.presentation.MovieGridAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieGridAdapter: MovieGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}