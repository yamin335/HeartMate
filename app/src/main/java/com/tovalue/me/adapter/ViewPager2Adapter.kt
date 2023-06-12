package com.tovalue.me.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tovalue.me.ui.auth.primeriv.InvestmentGuideFragment
import com.tovalue.me.ui.auth.primeriv.PossibilitiesGuideFragment
import com.tovalue.me.ui.dashboard.favorite.LevelOneInvitationFragment
import com.tovalue.me.ui.dashboard.home.HomeFragment
import com.tovalue.me.ui.dashboard.profile.ProfileFragment
import com.tovalue.me.ui.dashboard.pushalert.PushAlertsFragment
import com.tovalue.me.ui.dashboard.setting.SettingFragment
import com.tovalue.me.ui.dashboard.upcomingplans.UpcomingPlansFragment
import com.tovalue.me.ui.datingjourneyjournal.DatingPartnerDatesFragment
import com.tovalue.me.ui.datingjourneyjournal.DatingPartnerObservationsFragment

class ViewPager2Adapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragmentList: MutableList<FragmentName> = mutableListOf()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (fragmentList[position]) {
            FragmentName.PROFILE -> HomeFragment()
            FragmentName.PUSH -> PushAlertsFragment()
            FragmentName.HOME -> ProfileFragment()
            FragmentName.FAVORITE -> LevelOneInvitationFragment()
            FragmentName.SETTING -> SettingFragment()
            FragmentName.INVESTMENT -> InvestmentGuideFragment()
            FragmentName.POSSIBILITIES -> PossibilitiesGuideFragment()
            FragmentName.DATE_JOURNAL -> DatingPartnerDatesFragment()
            FragmentName.OBSERVATION_JOURNAL -> DatingPartnerObservationsFragment()
            FragmentName.UPCOMING_PLANS -> UpcomingPlansFragment()
        }
    }

    override fun getItemId(position: Int): Long {
        return fragmentList[position].ordinal.toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        val fragment = FragmentName.values()[itemId.toInt()]
        return fragmentList.contains(fragment)
    }

    fun add(fragment: FragmentName) {
        fragmentList.add(fragment)
        notifyDataSetChanged()
    }

    fun add(index: Int, fragment: FragmentName) {
        fragmentList.add(index, fragment)
        notifyDataSetChanged()
    }

    fun remove(index: Int) {
        fragmentList.removeAt(index)
        notifyDataSetChanged()
    }

    fun remove(name: FragmentName) {
        fragmentList.remove(name)
        notifyDataSetChanged()
    }

    enum class FragmentName {
        PROFILE,
        PUSH,
        HOME,
        FAVORITE,
        SETTING,
        INVESTMENT,
        POSSIBILITIES,

        /* DATE Fragment Name*/
        DATE_JOURNAL,
        OBSERVATION_JOURNAL,
        UPCOMING_PLANS
    }
}
