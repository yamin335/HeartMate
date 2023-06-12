package com.tovalue.me.ui.datenightcatalog

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.adapter.DataNightWeekConst
import com.tovalue.me.adapter.DateNightCatalogWeeksAdapter
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentDateNightCatalogIdeasBinding
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.datenightcatalog.*
import com.tovalue.me.model.datingJourney.Journey
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.ui.datenightcatalog.viewmodel.DateNightCatalogViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils


class DateNightCatalogIdeasFragment : Fragment() {

    private var _binding: FragmentDateNightCatalogIdeasBinding? = null
    private val binding get() = _binding!!
    private var args: Bundle? = null
    private var datingData: Journey? = null
    private var dataNightAdapter: DateNightCatalogWeeksAdapter? = null

    private val dateNightCatalogViewModel: DateNightCatalogViewModel by activityViewModels()


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = arguments

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDateNightCatalogIdeasBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args?.let {
            datingData = Gson().fromJson(it.getString(Constants.DATING_OBJECT), Journey::class.java)
        }

        binding.upgradeToPremiumLL.isVisible = !checkIsPremiumAccount()
        setClickListener()

        getDateNightCatalog()


    }


    /* Check is Premium Account*/
    private fun checkIsPremiumAccount(): Boolean {
        return MomensityBingoApp.preferencesManager?.getStringValue(Constants.SUBSCRIPTION_PLAN_STATUS)
            .equals(Constants.ACTIVE)
    }


    private fun getDateNightCatalog() {


        datingData?.let {
            DialogUtils.showDialog(requireContext(), false)
            dateNightCatalogViewModel.getDateNightCatalog(it.dating_partner_id, it.group_id)
            dateNightObserver()
        }

    }

    private fun dateNightObserver() {

        dateNightCatalogViewModel.getDateNightCatalogLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is DateNightCatalogViewModel.UiEventDateNightCatalogData.Data -> {
                    DialogUtils.hideDialog()
                    setViews(it.data)

                }
                is DateNightCatalogViewModel.UiEventDateNightCatalogData.Error -> {
                    DialogUtils.hideDialog()
                    showToast(it.errorMsg.toString())
                }
            }
        }

    }

    private fun getAllPromotedEvent() {


        datingData?.let {
            dateNightCatalogViewModel.getAllPromotedEvent(it.dating_partner_id, it.group_id)
            allPromotedEventObserver()
        }

    }

    private fun allPromotedEventObserver() {
        dateNightCatalogViewModel.getAllPromotedData.observe(viewLifecycleOwner) {
            when (it) {
                is DateNightCatalogViewModel.UiEventAllPromotedData.Data -> {
                    setPromotedData(it.data)
                }
                is DateNightCatalogViewModel.UiEventAllPromotedData.Error -> {
                    DialogUtils.hideDialog()
                }
            }
        }
    }

    /* set Promoted Data */
    private fun setPromotedData(promotedData: HolderData.PromotedEventResponse) {
        DialogUtils.hideDialog()
        dataNightAdapter?.addPremiumPromoted(promotedData)
    }

    /* set Views */
    @SuppressLint("SuspiciousIndentation")
    private fun setViews(data: HolderData.DateNightCatalogResponse) {
        setWeekRecyclerView(datingData?.group_id ?: 1)
        datingData?.let {
            binding.learStoryBehindTV.text =
                getString(R.string.dataNightCatalogIdeaFragmentHeading, it?.first_name)

            if (data.date_night_catalog.isEmpty()) {

                showToast("No Data Found")
            } else {
                dataNightAdapter?.setWeeksData(data.date_night_catalog)
            }
        }

        binding.toolTitle.text =
            getString(R.string.toolBarTitle, data.date_count.toString())

        if (checkIsPremiumAccount())
            getAllPromotedEvent()


    }


    private fun setWeekRecyclerView(
        groupId: Int
    ) {
        dataNightAdapter = DateNightCatalogWeeksAdapter(
            requireContext()
        ) { click, date_number, topic, pos, weekID ->

            when (click) {

                DataNightWeekConst.WEEK_TAP -> {

                    val bundleData = DateNightCatalogIdeaDetailsDataObj(
                        dateNightId = date_number ?: "",
                        partnerUserId = datingData?.dating_partner_id ?: 0,
                        topics = topic ?: "",
                        week_id = weekID ?: -1,
                        groupId = groupId
                    )
                    val bundle = Bundle()
                    bundle.putString(
                        Constants.DATE_NIGHT_CATALOG_IDEAS_DETAILS_DATA,
                        Gson().toJson(bundleData)
                    )

                    startActivity(
                        NavigationActivity.createIntent(
                            requireActivity(),
                            Constants.DATE_NIGHT_CATALOG_IDEA_DETAILS,
                            bundle
                        )
                    )

                }
                DataNightWeekConst.PROMOTED_TAP -> {}
                DataNightWeekConst.PROMOTED_SEE_MORE_TAP -> {
                    dataNightAdapter?.let {

                        val bundle = Bundle()
                        bundle.putString(
                            Constants.PROMOTED_EVENT_RESPONSE_LIST,
                            Gson().toJson(it.getWholeList()[pos ?: 0])
                        )
//                        startActivity(Intent(requireContext(), HelperActivity::class.java).apply {
//                            putExtra(
//                                Constants.OPEN_FRAGMENT,
//                                PromotedEventSeeAllFragment::class.java.simpleName
//                            )
//                            putExtras(bundle)
//                        })

                        startActivity(
                            NavigationActivity.createIntent(
                                requireActivity(),
                                Constants.PROMOTED_EVENT_SEE_ALL,
                                bundle
                            )
                        )


                    }

                }
            }


        }

        binding.weeksRV.apply {

            adapter = dataNightAdapter


        }

    }


    /* Set Click :Listener*/
    private fun setClickListener() {

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.upgradeToPremiumLL.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(Constants.SUBSCRIPTION_TYPE, 1)
            startActivity(
                NavigationActivity.createIntent(
                    requireActivity(),
                    Constants.SUBSCRIPTION,
                    bundle
                )
            )
        }


    }

}