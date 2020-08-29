package com.xan.nrcdemo

import android.content.Context
import android.os.Bundle
import android.text.format.Time
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.xan.nrcdemo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActvityViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(MainActvityViewModel::class.java)
        viewModel.context = this
        val pagerAdapter = FragmentAdapter(this, 2)
        ViewPager.adapter = pagerAdapter
        TabLayoutMediator(tablayout, ViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "NRC"
                1 -> tab.text = "Passport"
            }
        }.attach()

        val sharedPreferences = this.getSharedPreferences("nrcdemo", Context.MODE_PRIVATE)
        var today = Time(Time.getCurrentTimezone())
        today!!.setToNow()
        if (!sharedPreferences.getString("day", "0").equals(today.monthDay.toString())) {
            viewModel.setTokenRequest()
            viewModel.takeToken()
        }
        viewModel.showLoading.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
            }
        })
    }
}