package com.tovalue.me.ui.datingjourneyjournal

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.adapter.YearViewRVAdapter
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentDatingJourneyHomeBinding
import com.tovalue.me.model.datingJourney.HomeJourney
import com.tovalue.me.model.datingJourney.Journey
import com.tovalue.me.model.datingJourney.JourneyHomeResponse
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.ui.datenightcatalog.datenightcatalogscratchoffer.SelectAndCreateDateNightOfferDialog
import com.tovalue.me.ui.datingjourneyjournal.viewmodel.DatingJourneyViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.ScaleTransformer


class DatingJourneyHomeFragment : Fragment(),
    DiscreteScrollView.ScrollStateChangeListener<YearViewRVAdapter.MonthHolder>,
    DiscreteScrollView.OnItemChangedListener<YearViewRVAdapter.MonthHolder> {

    private var _binding: FragmentDatingJourneyHomeBinding? = null
    private val binding get() = _binding!!

    private val datingJourneyViewModel: DatingJourneyViewModel by activityViewModels()
    private val dashboardViewModel: DashboardViewModel by activityViewModels()


    private var args: Bundle? = null
    private var datingData: Journey? = null
    private var datingHomeData: JourneyHomeResponse? = null
    private var infiniteAdapter: InfiniteScrollAdapter<*>? = null

    private var subscriptionStatus: String? = null
    private var subscriptionType: String? = null


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = arguments

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDatingJourneyHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscriptionStatus = Constants.INACTIVE
        subscriptionType = Constants.LEVEL_1


//        datingJourneyViewModel.getDateJourney()

        setView()
        setRv()
        //setClickListener(it.response)
        setUpObserver()


        if (datingData!!.group_id != 0) {
            DialogUtils.showDialog(requireActivity(), false)
            datingJourneyViewModel.getDatingHomeJourney(datingData!!.group_id)
        }
    }

    private fun setUpObserver() {
        datingJourneyViewModel.journeyState.observe(viewLifecycleOwner) {
            when (it) {
                is DatingJourneyViewModel.DatingJourneyEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is DatingJourneyViewModel.DatingJourneyEvent.DataResponse -> {
                    DialogUtils.hideDialog()

                    Log.i("TAG->", "setUpObserver: ${Gson().toJson(it.response)}")

                    setData(it.response)
                    setClickListener(it.response)

                }
            }
        }
    }

    private fun setData(response: JourneyHomeResponse) {


        Glide.with(requireActivity()).load(response.homeJourney[0].my_avatar)
            .error(R.drawable.default_avatar).into(binding.userImg)
        Glide.with(requireActivity()).load(response.homeJourney[0].partner_avatar)
            .error(R.drawable.default_avatar).into(binding.partnerImg)
        binding.descriptionTv.text = response.homeJourney[0].goal_description

        if (response.homeJourney[0].level == "Level 1") {
            binding.ivPlanOur.visibility = View.INVISIBLE
        } else {
            binding.ivPlanOur.visibility = View.VISIBLE
        }
    }

    /* Set Recycler View */
    private fun setRv() {


        val yearMonthList = resources.getStringArray(R.array.year_list)

        infiniteAdapter = InfiniteScrollAdapter.wrap(
            YearViewRVAdapter(requireContext(), yearMonthList)
        )

        binding.monthRV.addScrollStateChangeListener(this)

        binding.monthRV.apply {
            setOrientation(DSVOrientation.HORIZONTAL)
            addOnItemChangedListener(this@DatingJourneyHomeFragment)

            adapter = infiniteAdapter
            hasFixedSize()

            setItemTransformer(
                ScaleTransformer.Builder()
                    .setMinScale(0.8f)
                    .build()
            );

        }
    }

    private fun setView() {

        binding.backBtn.setOnClickListener { requireActivity().onBackPressed() }

        args?.let {

            datingData = Gson().fromJson(
                it.getString(Constants.DATING_OBJECT),
                Journey::class.java
            )
        }

        datingData?.let {
            Glide.with(requireActivity()).load(it.avatar)
                .error(R.drawable.default_avatar)
                .into(binding.partnerImg)

            if (it.our_status!!.level == "Level 1") {
                binding.ivPlanOur.visibility = View.INVISIBLE
            } else {
                binding.ivPlanOur.visibility = View.VISIBLE
            }

        }


    }

    private fun setClickListener(response: JourneyHomeResponse) {
        binding.updateOurDatingJournalBtn.setOnClickListener {
            if (response.homeJourney.first().level.equals("1", true)) {
                goToLevel2InvitationPromptFragment()
            } else {
                startActivity(
                    NavigationActivity.createIntent(
                        requireActivity(),
                        Constants.DATING_PARTNER_DATING_JOURNAL_FRAGMENT,
                        args
                    )
                )
            }
        }

        binding.createDateNightOfferBtn.setOnClickListener {
            openSelectCreateDateNightDialog(response.homeJourney[0])
            //openAddPersonalEventDialog()
        }

        binding.ivOpenCalender.setOnClickListener {


            replaceFragmentSafely(
                DatingPartnerOurCalenderFragment.newInstance(Gson().toJson(response)),
                containerViewId = R.id.navigation_root_container,
                addToStack = true
            )
        }

        binding.llMeetGreet.setOnClickListener {
            replaceFragmentSafely(
                DatingPartnerMeetGreetFragment.newInstance(Gson().toJson(response), "0", "0", 0),
                containerViewId = R.id.navigation_root_container,
                addToStack = true
            )

        }

        binding.ivPlanOur.setOnClickListener {

            response.homeJourney.first().level.let {
                if (it.equals("1", true) or it.equals("2", true)) {
                    goToUpGradeToLevelFragment()
                } else {
                    //TODO else case need to be handled
                }
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCurrentItemChanged(
        viewHolder: YearViewRVAdapter.MonthHolder?,
        adapterPosition: Int
    ) {

        viewHolder?.let {
            it.changeFocusImage()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onScrollStart(
        currentItemHolder: YearViewRVAdapter.MonthHolder,
        adapterPosition: Int
    ) {
        currentItemHolder.changeImageUnFocus()

    }

    override fun onScrollEnd(
        currentItemHolder: YearViewRVAdapter.MonthHolder,
        adapterPosition: Int
    ) {

    }

    override fun onScroll(
        scrollPosition: Float,
        currentPosition: Int,
        newPosition: Int,
        currentHolder: YearViewRVAdapter.MonthHolder?,
        newCurrent: YearViewRVAdapter.MonthHolder?
    ) {
    }

    private fun openSelectCreateDateNightDialog(homeJourney: HomeJourney) {
        val dialogFragment = SelectAndCreateDateNightOfferDialog(homeJourney)
        dialogFragment.isCancelable = true
        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        val prev: Fragment? =
            childFragmentManager.findFragmentByTag(Constants.SELECT_CREATEDATE_NIGHT_OFFER_DIALOG_FRAGMENT)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        dialogFragment.show(ft, Constants.SELECT_CREATEDATE_NIGHT_OFFER_DIALOG_FRAGMENT)
    }

    //Needed this method
    private fun goToLevel2InvitationPromptFragment() {
        datingData?.let {

            val bundle = Bundle()
            bundle.putString(Constants.SELECTED_NAME, it.first_name)
            bundle.putInt(Constants.GROUP_ID, it.group_id)
            bundle.putString(Constants.SELECTED_AVATAR, it.avatar)
            bundle.putString(Constants.SELECTEDTVMScore, it.value_me_score)
            it.our_status?.let {
                bundle.putString(Constants.LEVEL_COUNT, it.level)
            }

            startActivity(
                NavigationActivity.createIntent(
                    requireActivity(),
                    Constants.LEVEL_2_INVITATION_PROMPT,
                    bundle
                )
            )
        }

    }


    private fun goToUpGradeToLevelFragment() {
        datingData?.let {

            val bundle = Bundle()
            bundle.putString(Constants.SELECTED_NAME, it.first_name)
            bundle.putInt(Constants.GROUP_ID, it.group_id)
            it.our_status?.let {
                bundle.putString(Constants.LEVEL_COUNT, it.level)
            }

            startActivity(
                NavigationActivity.createIntent(
                    requireActivity(),
                    Constants.UPGRADE_TO_LEVEL, bundle
                )
            )


        }

    }
}