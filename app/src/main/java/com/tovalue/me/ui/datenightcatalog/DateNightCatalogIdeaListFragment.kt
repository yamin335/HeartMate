package com.tovalue.me.ui.datenightcatalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentDateNightIdeaListBinding
import com.tovalue.me.model.datenightcatalog.HolderData
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.ui.datenightcatalog.viewmodel.DateNightCatalogViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.extensions.animateVisibility
import com.tovalue.me.util.livedata.EventObserver

class DateNightCatalogIdeaListFragment : Fragment() {
    private var _binding: FragmentDateNightIdeaListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DateNightCatalogViewModel by activityViewModels()
    private lateinit var dateNightIdeListAdapter: DateNightIdeaWeekListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDateNightIdeaListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        if (viewModel.activeSubscription() and
            (viewModel.subscriptionType().equals(Constants.LEVEL_2, true) or
                    viewModel.subscriptionType().equals(Constants.LEVEL_3, true))
        ) {
            binding.moreLayout.animateVisibility(View.GONE)
            binding.rvDateNightIdeaList.animateVisibility(View.VISIBLE)
            getDateNightIdeas()
        }

        /*val list = arrayListOf(
            DateNightWeek(
                "Week 1", arrayListOf(
                    DateNight("Road Trip", "0", 1,
                        arrayListOf(
                            "1","2"
                        ), "", "Learn my musical tastes",
                        "", "val possibilitiies: String?", "http://team.legrandbeaumarche.com/wp-content/uploads/2022/09/pexels-taryn-elliott-4840229-1-scaled.jpg",
                        25),
                    DateNight("Road Trip", "0", 1,
                        arrayListOf(
                            "1","2"
                        ), "", "Learn my musical tastes",
                        "", "val possibilitiies: String?", "http://team.legrandbeaumarche.com/wp-content/uploads/2022/09/pexels-taryn-elliott-4840229-1-scaled.jpg",
                        25),
                    DateNight("Road Trip", "0", 1,
                        arrayListOf(
                            "1","2"
                        ), "", "Learn my musical tastes",
                        "", "val possibilitiies: String?", "http://team.legrandbeaumarche.com/wp-content/uploads/2022/09/pexels-taryn-elliott-4840229-1-scaled.jpg",
                        25)
                )
            ),
            DateNightWeek(
                "Week 2", arrayListOf(
                    DateNight("Road Trip", "0", 1,
                        arrayListOf(
                            "1","2"
                        ), "", "Learn my musical tastes",
                        "", "val possibilitiies: String?", "http://team.legrandbeaumarche.com/wp-content/uploads/2022/09/pexels-taryn-elliott-4840229-1-scaled.jpg",
                        25),
                    DateNight("Road Trip", "0", 1,
                        arrayListOf(
                            "1","2"
                        ), "", "Learn my musical tastes",
                        "", "val possibilitiies: String?", "http://team.legrandbeaumarche.com/wp-content/uploads/2022/09/pexels-taryn-elliott-4840229-1-scaled.jpg",
                        25),
                    DateNight("Road Trip", "0", 1,
                        arrayListOf(
                            "1","2"
                        ), "", "Learn my musical tastes",
                        "", "val possibilitiies: String?", "http://team.legrandbeaumarche.com/wp-content/uploads/2022/09/pexels-taryn-elliott-4840229-1-scaled.jpg",
                        25)
                )
            )
        )
        setWeekRecyclerView(list as List<DateNightWeek>)*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userProfileInfo = viewModel.getUserObj()
        userProfileInfo?.toValueMe?.value?.let { binding.titleHeader.text = "$it Date Ideas" }


        dateNightIdeListAdapter = DateNightIdeaWeekListAdapter {
            DateNightCatalogIdeaListDetailsFragment.dateNightTitle = it.title ?: "Date Night Idea"
            DateNightCatalogIdeaListDetailsFragment.dateNightId = it.date_night_id ?: 0
            startActivity(
                NavigationActivity.createIntent(
                    requireActivity(),
                    Constants.DATE_NIGHT_CATALOG_IDEA_LIST_DETAILS
                )
            )
        }

        binding.rvDateNightIdeaList.apply {
            adapter = dateNightIdeListAdapter
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.moreLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.PASSED_PLAN_TYPE, Constants.LEVEL_2)
            bundle.putBoolean(Constants.SUBSCRIPTION_VIEW_FROM_SETTINGS, false)
            bundle.putBoolean(
                Constants.PASSED_PLAN_STATUS,
                viewModel.activeSubscription()
            )
            startActivity(
                NavigationActivity.createIntent(
                    requireContext(),
                    Constants.SUBSCRIPTION,
                    bundle
                )
            )


        }

        viewModel.dateNightIdeaListResponse.observe(viewLifecycleOwner,
            EventObserver {
                DialogUtils.hideDialog()
                when (it) {
                    is DateNightCatalogViewModel.UIEventDateNightIdea.Data -> {
                        it.data?.date_count?.let { ideas ->
                            binding.titleHeader.text = "$ideas Date Ideas"
                        }
                        val list = it.data?.date_night_catalog
                        setWeekRecyclerView(list)
                    }
                    is DateNightCatalogViewModel.UIEventDateNightIdea.Error -> {
                        showToast(it.errorMsg ?: "Undefined Error!")
                    }
                }
            })

        //.data?.date_night_catalog?.date_night_catalog?.weeks ?: ArrayList()
    }

    /* set Views */
    private fun setWeekRecyclerView(data: List<HolderData.DateNightCatalog>?) {
        data?.let { dateNightIdeListAdapter.submitData(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDateNightIdeas() {
        DialogUtils.showDialog(requireContext(), true)
        viewModel.getDateNightIdeas(page = 1, groupId = 51, userId = viewModel.getUserId())
    }


}