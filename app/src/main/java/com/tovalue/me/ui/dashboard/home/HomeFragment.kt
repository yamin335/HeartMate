package com.tovalue.me.ui.dashboard.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.adapter.DatingCardClickAction
import com.tovalue.me.adapter.DatingJourneyCardAdapter
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentHomeBinding
import com.tovalue.me.dialog.ExitDatingJourneyDialog
import com.tovalue.me.model.datingJourney.DatingResponse
import com.tovalue.me.model.datingJourney.Journey
import com.tovalue.me.ui.dashboard.DashboardActivity
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.ui.datenightcatalog.DataNightCatalogCoverDialog
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.livedata.observeOnce
import com.yarolegovich.discretescrollview.DSVOrientation
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.ScaleTransformer

class HomeFragment : Fragment(),
    DiscreteScrollView.OnItemChangedListener<DatingJourneyCardAdapter.DatingJourneyCardViewHolder> {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val dashBoardViewModel: HomeViewModel by activityViewModels()
    private val viewModel: DashboardViewModel by activityViewModels()
    private var infiniteAdapter: InfiniteScrollAdapter<*>? = null

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDatingJourneys()
        setUpClickListener()
    
        // in case of removing group id after first launch group id will be = 0 [@remove]
        if (viewModel.getGroupId() != 0) goToDatingJourneyHomeScreen()
    
        // redirect to DateNight View for fresh registration for once then flush the value being used for
        // incase of account reclaimation no need to redirect
        if (viewModel.getGroupId() == 0 && viewModel.getInvitationRedirection()){
            // remove saved value after visit
            viewModel.removeInvitationRedirection()
            // redirect to invitation tab
            (requireActivity() as DashboardActivity).changeViewpager(R.id.dashboard_fav)
        }
    }
    
    private fun goToDatingJourneyHomeScreen() {
        startActivity(
            NavigationActivity.createIntent(
                requireActivity(),
                Constants.DATING_JOURNEY_HOME
            )
        )
    }

    private fun setUpRecyclerview(datingJourneyList: ArrayList<Journey>) {

        infiniteAdapter = InfiniteScrollAdapter.wrap(
            DatingJourneyCardAdapter(
                requireContext(),
                datingJourneyList
            ) {

                    action, datingData ->
                when (action) {

                    DatingCardClickAction.CANCEL -> {
                        showExitDatingJourneyDialog()
                    }
                    DatingCardClickAction.CATALOG -> {
                        datingData?.let { startDatingNightCatalog(datingData) }

                    }
                    DatingCardClickAction.UPGRADE -> {
                        datingData?.our_status?.let {
                            when(it.level) {
                                "Level 1" -> goToLevel2InvitationPromptFragment(datingData)
                                "Level 2" -> goToUpGradeToLevelFragment(datingData)
                            }
                        }
                    }
                    DatingCardClickAction.DATING_JOURNEY -> {
                        goToDatingJourney(datingData)
                    }

                }

            })
        binding.dataCardSV.apply {

            setOrientation(DSVOrientation.HORIZONTAL)
            addOnItemChangedListener(this@HomeFragment)
            adapter = infiniteAdapter

            setItemTransformer(
                ScaleTransformer.Builder()
                    .setMinScale(0.8f)
                    .build()
            );

        }

    }

    //Needed this method
    private fun goToLevel2InvitationPromptFragment(datingData: Journey?) {
        datingData?.let {
        
            val bundle = Bundle()
            bundle.putString(Constants.SELECTED_NAME, datingData.first_name)
            bundle.putInt(Constants.GROUP_ID, datingData.group_id)
            bundle.putString(Constants.SELECTED_AVATAR, datingData.avatar)
            bundle.putString(Constants.SELECTEDTVMScore, datingData.value_me_score)
            it.our_status?.let {
                bundle.putString(Constants.LEVEL_COUNT, it.level)
            }
            
            startActivity(NavigationActivity.createIntent(requireActivity(), Constants.LEVEL_2_INVITATION_PROMPT, bundle))
        }
    
    }


    private fun setUpClickListener() {
        binding.btnInvite.setOnClickListener {
            requireActivity()
            (requireActivity() as DashboardActivity).changeViewpager(R.id.dashboard_fav)
        }
    }

    /* Go To Dating Journey */
    private fun goToDatingJourney(datingData: Journey?) {


        datingData?.let {
            val bundle = Bundle()
            bundle.putString(Constants.DATING_OBJECT, Gson().toJson(it))


            startActivity(
                NavigationActivity.createIntent(
                    requireActivity(),
                    Constants.DATING_JOURNEY_HOME,
                    bundle
                )
            )

        }

    }

    /* Start Dating Night Catalog */
    private fun startDatingNightCatalog(datingData: Journey) {

        DialogUtils.showDialog(requireContext(), false)


        dashBoardViewModel.getDateNightCatalog(
            partnerUserId = datingData.dating_partner_id,
            groupId = Constants.group_id
        )

        dashBoardViewModel.getDateNightCatalogLiveData().observeOnce(lifecycleOwner =viewLifecycleOwner ) {
            when (it) {
                is HomeViewModel.UiEventDateNightCatalogData.Data -> {
                    DialogUtils.hideDialog()
                    var dialog: DataNightCatalogCoverDialog? = null

                    dialog = DataNightCatalogCoverDialog(
                        datingData,
                        it.data.date_night_catalog_cover.url
                    ) {
                        dialog?.let { it.dismiss() }
                        val bundle = Bundle()
                        bundle.putString(Constants.DATING_OBJECT, Gson().toJson(datingData))
                        startActivity(
                            NavigationActivity.createIntent(
                                requireActivity(),
                                Constants.DATE_NIGHT_CATALOG_IDEAS, bundle = bundle
                            )
                        )
                    }
                    dialog.show(parentFragmentManager, HomeFragment::class.java.simpleName)

                }
                is HomeViewModel.UiEventDateNightCatalogData.Error -> {
                    DialogUtils.hideDialog()
                    showToast(it.errorMsg.toString())
                }
            }
        }


    }

    private fun showExitDatingJourneyDialog() {

        val dialog = ExitDatingJourneyDialog()
        dialog.show(parentFragmentManager, HomeFragment::class.java.simpleName)

    }

    private fun goToUpGradeToLevelFragment(datingData: Journey?) {
        datingData?.let {

            val bundle = Bundle()
            bundle.putString(Constants.SELECTED_NAME, datingData.first_name)
            bundle.putInt(Constants.GROUP_ID, datingData.group_id)
            it.our_status?.let {
                bundle.putString(Constants.LEVEL_COUNT, it.level)
            }


//            startActivity(Intent(requireContext(), HelperActivity::class.java).apply {
//                putExtra(Constants.OPEN_FRAGMENT, UpGradeToLevelFragment::class.java.simpleName)
//                putExtras(bundle)
//            })

            startActivity(
                NavigationActivity.createIntent(
                    requireActivity(),
                    Constants.UPGRADE_TO_LEVEL, bundle
                )
            )


        }

    }


    private fun getDatingJourneys() {
//        DialogUtils.showDialog(requireContext(), false)

        dashBoardViewModel.getDatingJourney()
        datingJourneyObserver()

    }

    private fun datingJourneyObserver() {

        dashBoardViewModel.dataJourney.observe(viewLifecycleOwner) {
            when (it) {
                is HomeViewModel.UiEventDateJourneys.Data -> {
                    setDatingObserverRV(it.data)
                    Log.e("findDataGetData", "datingJourneyObserver: ${it.data}" )
                }
                is HomeViewModel.UiEventDateJourneys.Error -> {
                    showToast(it.errorMsg.toString())
                }
            }
        }


    }


    private fun setDatingObserverRV(response: DatingResponse) {
        if (response.datingJourneyTutorial != null) {
            binding.apply {
                titleText.text = response.datingJourneyTutorial.title
                headText.text = response.datingJourneyTutorial.heade
                response.datingJourneyTutorial.bullets?.let {
                    when (it.size) {

                    }
                    if (it.size > 3) {
                        tvBullet4.text = it[3]
                        tvBullet3.text = it[2]
                        tvBullet2.text = it[1]
                        tvBullet1.text = it[0]
                        bullet1.visibility = View.VISIBLE
                        bullet2.visibility = View.VISIBLE
                        bullet3.visibility = View.VISIBLE
                        bullet4.visibility = View.VISIBLE
                    } else if (it.size > 2) {
                        tvBullet3.text = it[2]
                        tvBullet2.text = it[1]
                        tvBullet1.text = it[0]
                        bullet1.visibility = View.VISIBLE
                        bullet2.visibility = View.VISIBLE
                        bullet3.visibility = View.VISIBLE
                        bullet4.visibility = View.GONE
                    } else if (it.size > 1) {
                        tvBullet2.text = it[1]
                        tvBullet1.text = it[0]
                        bullet1.visibility = View.VISIBLE
                        bullet2.visibility = View.VISIBLE
                        bullet3.visibility = View.GONE
                        bullet4.visibility = View.GONE
                    } else if (it.size > 0) {
                        tvBullet1.text = it[0]
                        bullet1.visibility = View.VISIBLE
                        bullet2.visibility = View.GONE
                        bullet3.visibility = View.GONE
                        bullet4.visibility = View.GONE
                    } else {
                        bullet1.visibility = View.GONE
                        bullet2.visibility = View.GONE
                        bullet3.visibility = View.GONE
                        bullet4.visibility = View.GONE
                    }
                }

                noDatingJourneyLayout.visibility = View.VISIBLE
                dataCardSV.visibility = View.GONE
            }
        }

        val journeys = response.carousel?.journeys

        if (journeys.isNullOrEmpty()) {
            binding.noDatingJourneyLayout.visibility = View.VISIBLE
            binding.dataCardSV.visibility = View.GONE
        } else {
            binding.noDatingJourneyLayout.visibility = View.GONE
            binding.dataCardSV.visibility = View.VISIBLE
            setUpRecyclerview(journeys)
        }
    }


    override fun onCurrentItemChanged(
        viewHolder: DatingJourneyCardAdapter.DatingJourneyCardViewHolder?,
        adapterPosition: Int
    ) {

    }

}