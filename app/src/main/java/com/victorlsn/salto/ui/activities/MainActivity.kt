package com.victorlsn.salto.ui.activities

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.victorlsn.salto.R
import com.victorlsn.salto.ui.adapters.PageFragmentAdapter
import com.victorlsn.salto.ui.fragments.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
    BaseActivity() {
    private var adapter: PageFragmentAdapter? = null

    private var doorsFragment = DoorsFragment()
    private var usersFragment = UsersFragment()
    private var accessFragment = AccessFragment()
    private var openDoorFragment = OpenDoorFragment()
    private var eventsFragment = EventsFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        adapter = PageFragmentAdapter(supportFragmentManager)
        adapter!!.addFragment(doorsFragment, "Doors")
        adapter!!.addFragment(usersFragment, "Users")
        adapter!!.addFragment(accessFragment, "Access")
        adapter!!.addFragment(openDoorFragment, "Open Door")
        adapter!!.addFragment(eventsFragment, "Event Log")
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 4

        viewpager.addOnPageChangeListener( object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                val fragment = adapter!!.getItem(position) as BaseFragment
                fragment.resumeFragment()
            }
        })
    }

    private fun setupTabLayout() {
        tabLayout.setupWithViewPager(viewpager)
        tabLayout.getTabAt(0)?.text = adapter?.getTitle(0)
        tabLayout.getTabAt(1)?.text = adapter?.getTitle(1)
        tabLayout.getTabAt(2)?.text = adapter?.getTitle(2)
        tabLayout.getTabAt(3)?.text = adapter?.getTitle(3)
        tabLayout.getTabAt(4)?.text = adapter?.getTitle(4)
    }


}