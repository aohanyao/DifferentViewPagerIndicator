package com.yida.cloud.merchants

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.yida.cloud.merchants.ui.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)


        initPager()
    }

    private fun initPager() {
        val mFragments = ArrayList<Fragment>()
        mFragments.add(BlankFragment.newInstance())
        mFragments.add(BlankFragment.newInstance())
        mFragments.add(BlankFragment.newInstance())


        vpHome?.apply {
            val mTitle = arrayOf("待办", "任务", "消息")
            adapter = BaseFragmentPagerAdapter(supportFragmentManager, mFragments, mTitle)
            vi1?.titles = arrayListOf("待办", "任务", "消息")
            vi1?.setViewPager(this, 0)
            offscreenPageLimit = mFragments.size * 2
            pageMargin = 40
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
}
