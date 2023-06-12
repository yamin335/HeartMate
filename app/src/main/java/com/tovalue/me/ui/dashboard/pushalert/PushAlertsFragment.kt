package com.tovalue.me.ui.dashboard.pushalert

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentPushAlertsBinding
import com.tovalue.me.model.notification.NotificationModel
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.ui.datingjourneyjournal.DatingPartnerMeetGreetFragment
import com.tovalue.me.ui.datingjourneyjournal.viewmodel.DatingJourneyMeetGreetViewModel
import com.tovalue.me.ui.datingjourneyjournal.viewmodel.DatingJourneyViewModel
import com.tovalue.me.ui.levelTwoInvitation.LevelTwoInvitationActivity
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.livedata.EventObserver


class PushAlertsFragment : Fragment() {
    private var _binding: FragmentPushAlertsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PushAlertViewModel by viewModels()
    private var adapter: NotificationAdapter? = null
    private var isNew: String? = "false"
    private var notificationType: Boolean? = false
    private var component: String? = null
    private var searchTerm: String? = null
    private var isPageAvailable: Boolean? = null
    private var next_page: Int? = null
    private val datingJourneyViewModel: DatingJourneyViewModel by activityViewModels()
    private val mViewModel: DatingJourneyMeetGreetViewModel by activityViewModels()
    private var notificationModels: NotificationModel? = null

    private var args: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPushAlertsBinding.inflate(inflater, container, false)
        binding.toolbar.inflateMenu(R.menu.notification_menu)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = arguments
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationType = true
        fetchNotifications(1)
        initBinding()
        initMenu()
        initDrawer()
        setUpObserver()
        setUpDataObserver()
        setUpHomeObserver()
    }

    private fun initBinding() {
        adapter = NotificationAdapter { notificationModel ->
            notificationModels = notificationModel
            navigateToView(notificationModel)
        }
        binding.clearTv.setOnClickListener {
            it.isVisible = false
            searchTerm = null
            component = null
            notificationType = true
            fetchNotifications(1)
        }
        binding.notificationTg.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                R.id.read_button -> {
                    if (isChecked) {
                        isNew = "false"
                        notificationType = true
                        binding.notificationRv.adapter = null
                        fetchNotifications(1)
                    }
                }
                R.id.unread_button -> {
                    if (isChecked) {
                        isNew = "true"
                        notificationType = true
                        binding.notificationRv.adapter = null
                        fetchNotifications(1)
                    }
                }
                R.id.all_button -> {
                    if (isChecked) {
                        isNew = "both"
                        notificationType = true
                        binding.notificationRv.adapter = null
                        fetchNotifications(1)
                    }
                }
            }
        }

        val manager = LinearLayoutManager(requireContext().applicationContext)
        binding.notificationRv.setLayoutManager(manager)
        // adding on scroll change listener method for our nested scroll view.
        // adding on scroll change listener method for our nested scroll view.
        binding.idNestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                // on below line we are making our progress bar visible.

                if (next_page!! != 0) {
                    binding.idPBLoading.visibility = View.VISIBLE
                    // on below line we are again calling
                    // a method to load data in our array list.
                    notificationType = false
                    fetchNotifications(next_page!!)
                } else {
                    Toast.makeText(
                        activity?.applicationContext,
                        "No more notifications available.",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.idPBLoading.visibility = View.GONE
                }
            }
        })
    }

    private fun navigateToView(notificationModel: NotificationModel) {

        if (notificationModel.component_name.equals(
                Constants.NOTIFICATION_LEVEL_2_COMMITMENT,
                true
            )
        ) {
            startActivity(
                LevelTwoInvitationActivity.getIntent(
                    requireContext(),
                    notificationModel.item_id
                )
            )
        } else if (notificationModel.component_name.equals(
                Constants.NOTIFICATION_DATE_NIGHT_OFFER,
                true
            ) or notificationModel.component_name.equals(
                Constants.NOTIFICATION_FYI_OFFER_RESCHEDULE_REQUEST,
                true
            ) or notificationModel.component_name.equals(
                Constants.NOTIFICATION_FYI_EVENT_CONFIRMED,
                true
            )
        ) {
            val bundle = Bundle()
            bundle.putString(Constants.PASSED_OBJECT, Gson().toJson(notificationModel))

            startActivity(
                NavigationActivity.createIntent(
                    requireActivity(),
                    Constants.UPCOMING_PLAN_DETAIL, bundle = bundle
                )
            )


        } else if (notificationModel.component_name.equals(
                Constants.NOTIFICATION_FYI_EVENT_RESCHEDULE_REQUEST,
                true
            )
        ) {
            val bundle = Bundle()
            bundle.putString(Constants.PASSED_OBJECT, Gson().toJson(notificationModel))
            startActivity(
                NavigationActivity.createIntent(
                    requireActivity(),
                    Constants.RESCHEDULE_EVENT_OR_OFFER, bundle = bundle
                )
            )

        } else if (notificationModel.component_name.equals(
                Constants.NOTIFICATION_UPCOMING_PLANS,
                true
            )
        ) {
            viewModel.getReviewUpcomingPlan(notificationModel.item_id)
        } else if (notificationModel.component_name.equals(
                Constants.NOTIFICATION_DATING_JOURNAL,
                true
            )
        ) {

            startActivity(
                NavigationActivity.createIntent(
                    requireActivity(),
                    Constants.DATING_JOURNEY_HOME
                )
            )

        } else if (notificationModel.component_name.equals(
                Constants.NOTIFICATION_MOOD_RINGS,
                true
            )
        ) {
            startActivity(
                NavigationActivity.createIntent(
                    requireActivity(),
                    Constants.MOOD_RING_HISTORY
                )
            )
        } else if (notificationModel.component_name.equals(Constants.NOTIFICATION_Review, true)) {

            DialogUtils.showDialog(requireActivity(), false)
            makeApiViewCall(notificationModel.item_id)


            //


        }
    }

    private fun makeApiViewCall(testimonialID: Int) {
        DialogUtils.showDialog(requireActivity(), false)
        mViewModel.getReviewCouplePlan(testimonialID!!)
    }

    private fun setUpDataObserver() {
        mViewModel.reviewCouplePlanState.observe(viewLifecycleOwner) {
            when (it) {
                is DatingJourneyMeetGreetViewModel.GetReviewCouplePlanEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is DatingJourneyMeetGreetViewModel.GetReviewCouplePlanEvent.DataResponse -> {
                    //  DialogUtils.hideDialog()

                    Log.i("TAG->", "setUpObserver------------>: ${Gson().toJson(it.response)}")

                    datingJourneyViewModel.getDatingHomeJourney(it.response.results!!.groupId!!)

/*                    reviewCouplePlanData = it.response

                    binding.subTitleTv.setText(Html.fromHtml("<b>ON</b> " + reviewCouplePlanData!!.results!!.date + " - " + reviewCouplePlanData!!.results!!.title))

                    binding.question0ET.setText(reviewCouplePlanData!!.results!!.field1.toString())
                    binding.question1ET.setText(reviewCouplePlanData!!.results!!.field2.toString())
                    binding.question2ET.setText(reviewCouplePlanData!!.results!!.field3.toString())*/
                    //  setData()
                    // setClickListener()

                }
            }
        }
    }


    private fun setUpHomeObserver() {
        datingJourneyViewModel.journeyState.observe(viewLifecycleOwner) {
            when (it) {
                is DatingJourneyViewModel.DatingJourneyEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is DatingJourneyViewModel.DatingJourneyEvent.DataResponse -> {
                    DialogUtils.hideDialog()

                    Log.i("TAG->", "setUpObserver: ${Gson().toJson(it.response)}")

                    var args:Bundle?=Bundle()
                    args!!.putString("ARG_PARAM1",Gson().toJson(it.response))
                    args!!.putString("For_VIEW","1")
                    args!!.putString("FROM_NOTIFICATION","1")
                    args!!.putInt("TESTIMONIAL_ID", notificationModels!!.item_id)

                    startActivity(
                        NavigationActivity.createIntent(
                            requireActivity(),
                            Constants.DATING_PARTNER_MEET_GREET_FRAGMENT,
                            args
                        )
                    )


                    /*replaceFragmentSafely(
                        DatingPartnerMeetGreetFragment.newInstance(
                            Gson().toJson(it.response),
                            "0",
                            "1",
                            notificationModels!!.item_id
                        ),
                        containerViewId = R.id.navigation_root_container,
                        addToStack = true
                    )*/

                }
            }
        }
    }

    private fun initMenu() {
        val searchView =
            binding.toolbar.menu.findItem(R.id.notification_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.clearTv.isVisible = true
                searchTerm = p0
                notificationType = true
                fetchNotifications(1)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

    }

    private fun setUpObserver() {
        viewModel.notificationsResponse.observe(viewLifecycleOwner,
            EventObserver {
                when (it) {
                    is PushAlertViewModel.UiEventNotification.Notification -> {
                        next_page = it.response.next_page
                        if (next_page == 0) {
                            next_page
                        }
                        it.response.notifications?.let {
                            binding.notificationInfoTv.isVisible = false

                            adapter?.updateList(it, notificationType)
                            binding.notificationRv.adapter = adapter
                        } ?: kotlin.run {
                            adapter?.updateList(null, notificationType)
                            binding.notificationInfoTv.isVisible = true
                        }

                    }
                    is PushAlertViewModel.UiEventNotification.Error -> {
                        Log.d("DashboardActivity", it.errorMsg)
                    }
                }
            })

        viewModel.upcomingPlanReviewResponse.observe(viewLifecycleOwner,
            EventObserver {
                when (it) {
                    is PushAlertViewModel.UpcomingPlanReviewNotificationEvent.UpcomingPlanReviewNotification -> {
                        it.response.let {
                            val bundle = Bundle()
                            bundle.putString(Constants.PASSED_GROUP_ID, it.group_id)
                            bundle.putString(
                                Constants.PASSED_TIME_FOR_CALENDAR,
                                it.event_start_date
                            )

                            startActivity(
                                NavigationActivity.createIntent(
                                    requireActivity(),
                                    Constants.RESCHEDULE_EVENT_OR_OFFER, bundle = bundle
                                )
                            )
//                            startActivity(
//                                Intent(
//                                    requireContext(),
//                                    HelperActivity::class.java
//                                ).apply {
//                                    putExtra(
//                                        Constants.OPEN_FRAGMENT,
//                                        UpcomingPlansFragment::class.java.simpleName
//                                    )
//
//                                    putExtras(bundle)
//                                })
                        }
                    }
                    is PushAlertViewModel.UpcomingPlanReviewNotificationEvent.Error -> {
                        showToast(it.errorMsg)
                    }
                }
            })
    }


    private fun initDrawer() {
        val drawer = binding.root
        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            drawer,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawer.addDrawerListener(toggle)

        val notificationType1 =
            binding.navigationView.findViewById<LinearLayout>(R.id.notification_type_text_1)
        val notificationType2 =
            binding.navigationView.findViewById<LinearLayout>(R.id.notification_type_text_2)
        val notificationType3 =
            binding.navigationView.findViewById<LinearLayout>(R.id.notification_type_text_3)
        val notificationType4 =
            binding.navigationView.findViewById<LinearLayout>(R.id.notification_type_text_4)
        val notificationType5 =
            binding.navigationView.findViewById<LinearLayout>(R.id.notification_type_text_5)
        val notificationType6 =
            binding.navigationView.findViewById<LinearLayout>(R.id.notification_type_text_6)
        val notificationType7 =
            binding.navigationView.findViewById<LinearLayout>(R.id.notification_type_text_7)
        val notificationType8 =
            binding.navigationView.findViewById<LinearLayout>(R.id.notification_type_text_8)
        val notificationSettings =
            binding.navigationView.findViewById<MaterialCardView>(R.id.update_notifcation_cardview)

        notificationSettings.setOnClickListener {
            startActivity(
                NavigationActivity.createIntent(requireActivity(), Constants.PUSH_SETTING)
            )
        }
        filterNotificationByType(notificationType1)
        filterNotificationByType(notificationType2)
        filterNotificationByType(notificationType3)
        filterNotificationByType(notificationType4)
        filterNotificationByType(notificationType5)
        filterNotificationByType(notificationType6)
        filterNotificationByType(notificationType7)
        filterNotificationByType(notificationType8)
        toggle.syncState()
    }

    private fun filterNotificationByType(view: View) {
        view.setOnClickListener {
            binding.clearTv.isVisible = true
            val type: String = it.tag as String
            component = type
            notificationType = true
            fetchNotifications(1)
            binding.root.closeDrawers()
        }
    }

    private fun fetchNotifications(page: Int) {
        viewModel.getNotifications(
            component = component,
            searchTerms = searchTerm,
            isNew = isNew,
            page = page
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}