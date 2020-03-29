package com.victorlsn.salto.ui.activities

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.victorlsn.salto.R
import com.victorlsn.salto.ui.adapters.PageFragmentAdapter
import com.victorlsn.salto.ui.fragments.AccessFragment
import com.victorlsn.salto.ui.fragments.BaseFragment
import com.victorlsn.salto.ui.fragments.DoorsFragment
import com.victorlsn.salto.ui.fragments.EmployeesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
    BaseActivity() {
//    private val fragmentManager = supportFragmentManager
    private var adapter: PageFragmentAdapter? = null

    private var doorsFragment = DoorsFragment()
    private var employeesFragment = EmployeesFragment()
    private var accessFragment = AccessFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        adapter = PageFragmentAdapter(supportFragmentManager)
        adapter!!.addFragment(doorsFragment, "Doors")
        adapter!!.addFragment(employeesFragment, "Employees")
        adapter!!.addFragment(accessFragment, "Access")
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 3

        viewpager.addOnPageChangeListener( object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

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
    }


}