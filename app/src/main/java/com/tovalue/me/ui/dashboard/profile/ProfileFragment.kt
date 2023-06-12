package com.tovalue.me.ui.dashboard.profile

//import com.unity3d.player.UnityPlayerActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentProfileBinding
import com.tovalue.me.helper.BannerPagerTransformer
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.CorporateInfoResponse
import com.tovalue.me.model.ProfileBanner
import com.tovalue.me.model.ProfileInfo
import com.tovalue.me.ui.auth.AuthHostActivity.Companion.createAuthIntent
import com.tovalue.me.ui.browser.VisitLinksActivity
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity.Companion.createIntent
import com.tovalue.me.util.Constants
import com.tovalue.me.util.Constants.EDIT_PROFILE
import com.tovalue.me.util.Constants.FILTER_PREFERENCE
import com.tovalue.me.util.Constants.MOOD_RING
import com.tovalue.me.util.Constants.RELATIONSHIP_GOAL
import com.tovalue.me.util.Constants.RELATIONSHIP_GOAL_GIFT
import com.tovalue.me.util.Constants.SETTING
import com.tovalue.me.util.Constants.UNITY_PLAYER
import com.tovalue.me.util.Constants.VISION_BOARD
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.extensions.animateVisibility
import com.tovalue.me.util.livedata.EventObserver
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by activityViewModels()
    private lateinit var userResponse: ProfileInfo
    private lateinit var corporateLinks: CorporateInfoResponse
    private var pagerCount = 0


    private var subscriptionStatus: String? = null
    private var subscriptionType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.profileResponse.value == null) getUserData()
        setUpClickListener()
        setUpObserver()
        viewModel.getCorporateInfo()

    }

    private fun setUpObserver() {
        viewModel.profileResponse.observe(viewLifecycleOwner,
            EventObserver {
                binding.loader.animateVisibility(View.GONE)
                when (it) {
                    is DashboardViewModel.UiEventProfile.Profile -> {
                        userResponse = it.response
                        setUserData(it.response)
                    }
                    is DashboardViewModel.UiEventProfile.COOKIEXPIRE -> {
                        viewModel.flushSavedData()
                        goToLoginScreen()
                    }
                    is DashboardViewModel.UiEventProfile.Error -> {
                        showToast(it.errorMsg)
                    }
                }
            })

        viewModel.corporateInfoResponse.observe(viewLifecycleOwner,
            EventObserver {
                DialogUtils.hideDialog()
                when (it) {
                    is DashboardViewModel.UiEventCorporate.CorporateLinks -> {
                        corporateLinks = it.response
                    }
                    is DashboardViewModel.UiEventCorporate.Error -> showToast(
                        it.errorMsg
                    )
                }
            })

        viewModel.memberShipState.observe(viewLifecycleOwner) { res ->
            when (res) {
                is DashboardViewModel.MemberShipResponseEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                }
                is DashboardViewModel.MemberShipResponseEvent.Error -> {
                    DialogUtils.hideDialog()
                }
            }
        }
    }

    private fun goToLoginScreen() {
        // if cookie expire goto starter screen
        // not good practice to use user creds to get the fresh cookie
        startActivity(
            createAuthIntent(requireActivity()).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
        )
        requireActivity().finish()
    }

    private fun setUpClickListener() {
//		binding.whatWorksTv.setOnClickListener {
//			goToLinksActivity(userResponse.privacyPolicy)
//		}

        binding.iconHelp.setOnClickListener {
            goToLinksActivity(corporateLinks.helpCenter)
        }
//		binding.learnMoreLayout.moreLayout.setOnClickListener {
//			startActivity(createIntent(requireActivity(), SUBSCRIPTION))
//		}
        binding.datingPrefNav.setOnClickListener {
            startActivity(createIntent(requireActivity(), FILTER_PREFERENCE))
        }
        binding.updateMyProfile.setOnClickListener {
            startActivity(createIntent(requireActivity(), EDIT_PROFILE))
        }
		binding.layoutIceBreaker.setOnClickListener {
			startActivity(createIntent(requireActivity(), VISION_BOARD))
		}
        binding.dateNightCateloge.setOnClickListener {
//            if (getSubscriptionStatus) {
                startActivity(createIntent(requireActivity(), Constants.DATE_NIGHT_CATALOG))
//            } else {
//                val bundle = Bundle()
//                bundle.putString(Constants.PASSED_PLAN_TYPE, Constants.LEVEL_2)
//                bundle.putBoolean(Constants.PASSED_PLAN_STATUS, getSubscriptionStatus)
//                startActivity(createIntent(requireContext(), Constants.SUBSCRIPTION, bundle))
//            }
        }
        binding.containerRhythmOfLife.setOnClickListener {
            startActivity(createIntent(requireActivity(), UNITY_PLAYER))
        }
        binding.UserImageNav.setOnClickListener {
            startActivity(createIntent(requireActivity(), EDIT_PROFILE))
        }
        binding.settingsNav.setOnClickListener {
            startActivity(createIntent(requireActivity(), SETTING))
        }

        binding.dailyStatusButtonLabel.setOnClickListener {
            startActivity(createIntent(requireActivity(), MOOD_RING))
        }
        binding.btnManageMine.setOnClickListener {
            startActivity(createIntent(requireActivity(), RELATIONSHIP_GOAL))
        }
        binding.btnGift.setOnClickListener {
            startActivity(createIntent(requireActivity(), RELATIONSHIP_GOAL_GIFT))
        }
    }


    private fun goToLinksActivity(link: String) {
        val intent = Intent(context, VisitLinksActivity::class.java).apply {
            putExtra("url", link)
        }
        startActivity(intent)
    }

    private fun getUserData() {
        binding.loader.animateVisibility(View.VISIBLE)
        viewModel.getProfileInfo(viewModel.getUserId())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*Need to make a separate class to load images*/
    private fun loadImage(imageUrl: String) {
        Glide.with(requireContext()).load(imageUrl)
            .error(R.drawable.default_avatar)
            .into(binding.profileImg)
    }

    private fun setUserData(userResponse: ProfileInfo?) {
        binding.nameTv.text = userResponse!!.displayname
        MomensityBingoApp.preferencesManager!!.setStringValue(
            Constants.NAME_KEY, userResponse.displayname
        )
        if(userResponse==null) return
        binding.nameTv.text = userResponse.displayname
        //binding.datingNumberTv.text = userResponse.dateNumber
        binding.numberOfExperience.text = userResponse.toValueMe?.value?.toString() ?: "0"
        binding.numberOfExperienceLabel.text = userResponse.toValueMe?.title

        binding.activeDatingJourney.text =
            userResponse.activeDatingJourney?.value?.toString() ?: "0"
        binding.activeDatingJourneyLabel.text = userResponse.activeDatingJourney?.title

        binding.receivedObservations.text =
            userResponse.receivedObservations?.value?.toString() ?: "0"
        binding.receivedObservationsLabel.text = userResponse.receivedObservations?.title

//		binding.dailyStatusLabel.text = userResponse.dailyStatus.title
//		binding.dailyStatusButtonLabel.text = userResponse.dailyStatus.button

        binding.inventoryOfMyLifeLabel.text = userResponse.rhythmOfLife.title
        Glide.with(requireContext()).load(userResponse.rhythmOfLife.backgroundImage)
            .transform(CenterCrop(), RoundedCorners(20))
            .into(binding.imageRhythmOfLife)
        binding.lastUpdatedRhythmOfLife.text = userResponse.rhythmOfLife.lastUpdated ?: "n/a"


        binding.iceBreakerHaveRightQuestion.text = userResponse.iceBreakers.title
        Glide.with(requireContext()).load(userResponse.iceBreakers.backgroundImage)
            .transform(CenterCrop(), RoundedCorners(20))
            .into(binding.imageIceBreaker)
        binding.iceBreakerEasilyFind.text = userResponse.iceBreakers.subHeading


        loadImage(userResponse.avatar)
        setBannerData(userResponse.bannerInfo)
        updateSubscriptionStatus(userResponse)
    }

    private fun updateSubscriptionStatus(profileInfo: ProfileInfo) {
        subscriptionStatus = MomensityBingoApp.preferencesManager?.getStringValue(
            Constants.SUBSCRIPTION_PLAN_STATUS
        )
        subscriptionType = MomensityBingoApp.preferencesManager?.getStringValue(
            Constants.SUBSCRIPTION_PLAN_TYPE
        )
        if (profileInfo.subscriptionStatus.equals(subscriptionStatus, true)
                .not() or profileInfo.subscriptionType.equals(subscriptionType, true).not()
        ) {
            MomensityBingoApp.preferencesManager?.setStringValue(Constants.SUBSCRIPTION_PLAN_STATUS,profileInfo.subscriptionStatus)
            MomensityBingoApp.preferencesManager?.setStringValue(Constants.SUBSCRIPTION_PLAN_TYPE,profileInfo.subscriptionType)
            viewModel.updateSubscriptionStatus(
                subscriptionStatus = subscriptionStatus ?: Constants.INACTIVE,
                subscriptionType = subscriptionType ?: Constants.LEVEL_1,
                subscriptionExpiryDate = "",
                subscriptionToken = "",
            )
        }


    }


    private fun setBannerData(bannerInfo: ProfileBanner) {
        binding.bannerRv.apply {
            adapter = BannerAdapter(this@ProfileFragment.requireContext(), bannerInfo.slider.panels)
            currentItem = pagerCount
        }

        binding.bannerRv.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                pagerCount = position
            }
        })

        binding.bannerRv.setPageTransformer(BannerPagerTransformer())
        lifecycleScope.launch { autoScrollBanner() }
    }

    private suspend fun autoScrollBanner() {
        delay(userResponse.bannerInfo.timer.toLong())
        if (pagerCount == userResponse.bannerInfo.slider.panels.size - 1) pagerCount = 0
        else pagerCount++

        binding.bannerRv.setCurrentItem(pagerCount, true)
        autoScrollBanner()
    }
}