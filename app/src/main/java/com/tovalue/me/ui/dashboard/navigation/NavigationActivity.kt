package com.tovalue.me.ui.dashboard.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tovalue.me.R
import com.tovalue.me.databinding.ActivityNavigationBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primeriii.Aspects
import com.tovalue.me.ui.auth.primeriii.LifeSpectrum
import com.tovalue.me.ui.auth.primeriii.visualizer.AudioVisualizerFragment
import com.tovalue.me.ui.auth.primeriii.visualizer.InventoryCategoryFragment
import com.tovalue.me.ui.auth.primeriv.GuideFragment
import com.tovalue.me.ui.auth.primeriv.PersonalityFacetFragment
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.catalog.UpGradeToLevelFragment
import com.tovalue.me.ui.dashboard.membership.PlanMemberShipFragment
import com.tovalue.me.ui.dashboard.myMoodRing.MoodRingHistoryFragment
import com.tovalue.me.ui.dashboard.myMoodRing.MyMoodRingFragment
import com.tovalue.me.ui.dashboard.profile.EditProfileFragment
import com.tovalue.me.ui.dashboard.relationshipGoals.MyRelationshipPlanDetailsFragment
import com.tovalue.me.ui.dashboard.relationshipGoals.RelationshipGoalPurchaseSuccessFragment
import com.tovalue.me.ui.dashboard.relationshipGoals.MyRelationshipPlansFragment
import com.tovalue.me.ui.dashboard.setting.EmailSettingFragment
import com.tovalue.me.ui.dashboard.setting.PushSettingFragment
import com.tovalue.me.ui.dashboard.setting.SettingFragment
import com.tovalue.me.ui.dashboard.upcomingplans.UpcomingPlanDetailFragment
import com.tovalue.me.ui.dashboard.upcomingplans.UpcomingPlansFragment
import com.tovalue.me.ui.dashboard.upcomingplans.dialog.RescheduleEventOrOfferDialog
import com.tovalue.me.ui.datenightcatalog.*
import com.tovalue.me.ui.datingjourneyjournal.DatingJourneyHomeFragment
import com.tovalue.me.ui.datingjourneyjournal.DatingPartnerDatingJournalFragment
import com.tovalue.me.ui.datingjourneyjournal.DatingPartnerMeetGreetFragment
import com.tovalue.me.ui.filter.FilterPreferencesFragment
import com.tovalue.me.ui.internalInvitation.LevelTwoInvitationPromptFragment
import com.tovalue.me.ui.visionboard.VisionBoardFragment
import com.tovalue.me.util.Constants.DATE_NIGHT
import com.tovalue.me.util.Constants.DATE_NIGHT_CATALOG
import com.tovalue.me.util.Constants.DATE_NIGHT_CATALOG_IDEAS
import com.tovalue.me.util.Constants.DATE_NIGHT_CATALOG_IDEAS_LIST
import com.tovalue.me.util.Constants.DATE_NIGHT_CATALOG_IDEA_DETAILS
import com.tovalue.me.util.Constants.DATE_NIGHT_CATALOG_IDEA_DETAILS_HISTORY
import com.tovalue.me.util.Constants.DATE_NIGHT_CATALOG_IDEA_LIST_DETAILS
import com.tovalue.me.util.Constants.DATING_JOURNEY_HOME
import com.tovalue.me.util.Constants.DATING_PARTNER_DATING_JOURNAL_FRAGMENT
import com.tovalue.me.util.Constants.DATING_PARTNER_MEET_GREET_FRAGMENT
import com.tovalue.me.util.Constants.EDIT_PROFILE
import com.tovalue.me.util.Constants.EMAIL_SETTING
import com.tovalue.me.util.Constants.FILTER_PREFERENCE
import com.tovalue.me.util.Constants.LEVEL_2_INVITATION_PROMPT
import com.tovalue.me.util.Constants.LIFE_RHYTHM
import com.tovalue.me.util.Constants.MOOD_RING
import com.tovalue.me.util.Constants.MOOD_RING_HISTORY

import com.tovalue.me.util.Constants.PROMOTED_EVENT_SEE_ALL
import com.tovalue.me.util.Constants.PUSH_SETTING
import com.tovalue.me.util.Constants.RELATIONSHIP_GOAL
import com.tovalue.me.util.Constants.RELATIONSHIP_GOAL_DETAILS
import com.tovalue.me.util.Constants.RELATIONSHIP_GOAL_GIFT
import com.tovalue.me.util.Constants.RELATIONSHIP_GOAL_PURCHASE_SUCCESS
import com.tovalue.me.util.Constants.RESCHEDULE_EVENT_OR_OFFER
import com.tovalue.me.util.Constants.SETTING
import com.tovalue.me.util.Constants.SUBSCRIPTION
import com.tovalue.me.util.Constants.UPCOMING_PLANS_FRAGMENT
import com.tovalue.me.util.Constants.UPCOMING_PLAN_DETAIL
import com.tovalue.me.util.Constants.UPGRADE_TO_LEVEL
import com.tovalue.me.util.Constants.UNITY_PLAYER
import com.tovalue.me.util.Constants.VISION_BOARD
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.ui.dashboard.relationshipGoals.RelationshipPlansFragment

//import com.unity3d.player.UnityPlayerActivity

class NavigationActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityNavigationBinding
    private val navigationScreen: String by lazy {
        intent.getStringExtra(EXTRA_KEY_NAVIGATION).toString()
    }
    private val authViewModel: AuthViewModel by viewModels()
	private val dashboardViewModel: DashboardViewModel by viewModels() // [@refactor pass data instead of viewModel there ]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        authViewModel.isEditMode = false
        setUpView()
    }

    private fun setUpView() {
        when (navigationScreen) {
            EDIT_PROFILE -> goToEditProfileFrag()
            VISION_BOARD -> goToVisionBoardFrag()
            DATE_NIGHT -> goToDateNightCataLogFrag()
            LIFE_RHYTHM -> goToLifeRhythmFrag()
            PUSH_SETTING -> goToNotificationSettingFrag()
            EMAIL_SETTING -> goToEmailSettingFrag()
            SUBSCRIPTION -> goToPlanMemberShipFrag()
            FILTER_PREFERENCE -> goToFilterPreferencesFrag()
            SETTING -> goToSettingsFrag()
            MOOD_RING -> goToMoodRingFrag()
            MOOD_RING_HISTORY -> goToMoodRingHistFrag()
			UNITY_PLAYER -> goToUnityPlayer()
            /*DATE NIGHT CATALOG */
            DATE_NIGHT_CATALOG ->  goToDateNightCatalogFrag()
            UPGRADE_TO_LEVEL -> goToUpgradeToLevel()
            DATE_NIGHT_CATALOG_IDEAS -> goToDateNightCatalogIdeas()
            DATING_JOURNEY_HOME -> goToDatingJourneyHome()
            PROMOTED_EVENT_SEE_ALL -> goToPromotedEventSeeAll()
            DATE_NIGHT_CATALOG_IDEA_DETAILS -> goToDateNightCatalogIdeaDetails()
            DATING_PARTNER_DATING_JOURNAL_FRAGMENT -> goToDatingPartnerDatingJourny()
            DATING_PARTNER_MEET_GREET_FRAGMENT -> goToDatingMeetGreetData()
            UPCOMING_PLAN_DETAIL -> goToUpcomingPlanDetail()
            RESCHEDULE_EVENT_OR_OFFER -> goToRescheduleEventOrOffer()
            UPCOMING_PLANS_FRAGMENT -> goToUpcomingPlansFragment()
            DATE_NIGHT_CATALOG_IDEAS_LIST -> goToDateNightCatalogIdeaListFrag()
            DATE_NIGHT_CATALOG_IDEA_DETAILS_HISTORY -> goToDateNightCatalogIdeaDetailsHistoryFrag()
            DATE_NIGHT_CATALOG_IDEA_LIST_DETAILS -> goToDateNightCatalogIdeaDetailsFrag()
            LEVEL_2_INVITATION_PROMPT -> goToLevel2InvitationPromptFrag() // internal invitation
            RELATIONSHIP_GOAL -> goToRelationshipGoalsFrag()
            RELATIONSHIP_GOAL_DETAILS -> goToRelationshipGoalDetailsFrag()
            RELATIONSHIP_GOAL_GIFT -> goToRelationshipGoalGiftFrag()
            RELATIONSHIP_GOAL_PURCHASE_SUCCESS -> goToRelationshipGoalPurchaseSuccessFrag()

        }
    }

	fun goToUnityPlayer() {
		val profile = dashboardViewModel.getUserObj()
		val spectrum = LifeSpectrum()
		spectrum.unityModuleState = PROFILE_MODE
		spectrum.audioURL = authViewModel.getSpectrumDemoUrl()
		spectrum.aspectsOfMyLife = profile.spectrum.aspects_to_life
		spectrum.day = profile.spectrum.last_updated
		spectrum.imageURL = profile.avatar

		val aspect = Aspects()
		spectrum.aspects = aspect
//		val intent = Intent(this@NavigationActivity, UnityPlayerActivity::class.java)
//		intent.putExtra("arguments", Gson().toJson(spectrum))
		unityResultLauncher.launch(intent)
	}

	private val unityResultLauncher =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
			if (it.resultCode == RESULT_OK) {
				when(it.data!!.getStringExtra("unity_state")!!) {
					"3" -> goToInventoryCategoryFrag()
					"4" -> goToPersonalityFacetFrag()
					"2" -> finish()
				}
			}
		}

    private fun goToDateNightCatalogFrag() {
        replaceFragmentSafely(
            DateNightCatalogFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToDateNightCatalogIdeaDetailsFrag() {
        replaceFragmentSafely(
            DateNightCatalogIdeaListDetailsFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToDateNightCatalogIdeaDetailsHistoryFrag() {
        replaceFragmentSafely(
            DateNightCatalogIdeaListDetailsHistoryFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }


    private fun goToUpcomingPlansFragment() {
        replaceFragmentSafely(
            UpcomingPlansFragment().apply { arguments = intent.extras },
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToRescheduleEventOrOffer() {
        replaceFragmentSafely(
            RescheduleEventOrOfferDialog().apply { arguments = intent.extras },
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToUpcomingPlanDetail() {
        replaceFragmentSafely(
            UpcomingPlanDetailFragment().apply { arguments = intent.extras },
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )

    }

    private fun goToDatingPartnerDatingJourny() {
        replaceFragmentSafely(
            DatingPartnerDatingJournalFragment().apply { arguments = intent.extras },
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToDateNightCatalogIdeaListFrag() {
        replaceFragmentSafely(
            DateNightCatalogIdeaListFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }


    private fun goToDateNightCatalogIdeaDetails() {
        replaceFragmentSafely(
            DateNightCatalogIdeaDetailsFragment().apply { arguments = intent.extras },
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }


    /* DATE NIGHT CATALOG*/
    private fun goToUpgradeToLevel() {
        replaceFragmentSafely(
            UpGradeToLevelFragment().apply { arguments = intent.extras },
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToDateNightCatalogIdeas() {
        replaceFragmentSafely(
            DateNightCatalogIdeasFragment().apply {
                arguments = intent.extras
            },
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToDatingJourneyHome() {
        replaceFragmentSafely(
            DatingJourneyHomeFragment().apply { arguments = intent.extras },
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToPromotedEventSeeAll() {
        replaceFragmentSafely(
            PromotedEventSeeAllFragment().apply { arguments = intent.extras },
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }


    private fun goToMoodRingHistFrag() {
        replaceFragmentSafely(
            MoodRingHistoryFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToMoodRingFrag() {
        replaceFragmentSafely(
            MyMoodRingFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToInventoryCategoryFrag() {
        replaceFragmentSafely(
            InventoryCategoryFragment(),
            containerViewId = R.id.navigation_root_container
        )
    }

    private fun goToSettingsFrag() {
        replaceFragmentSafely(
            SettingFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToFilterPreferencesFrag() {
        replaceFragmentSafely(
            FilterPreferencesFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToNotificationSettingFrag() {
        replaceFragmentSafely(
            PushSettingFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToEmailSettingFrag() {
        replaceFragmentSafely(
            EmailSettingFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToEditProfileFrag() {
        replaceFragmentSafely(
            EditProfileFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToLifeRhythmFrag() {
        replaceFragmentSafely(
            AudioVisualizerFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToDateNightCataLogFrag() {
        replaceFragmentSafely(
            GuideFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

	private fun goToPersonalityFacetFrag() {
		replaceFragmentSafely(
			PersonalityFacetFragment(),
			containerViewId = R.id.navigation_root_container
		)
	}

    private fun goToDatingMeetGreetData() {
		replaceFragmentSafely(
			DatingPartnerMeetGreetFragment().apply { arguments = intent.extras },
			containerViewId = R.id.navigation_root_container
		)
	}

    private fun goToPlanMemberShipFrag(){
        replaceFragmentSafely(
            PlanMemberShipFragment().apply { arguments = intent.extras },
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )

//        val bundle = Bundle()
//        startActivity(
//            Intent(
//                requireContext(),
//                HelperActivity::class.java
//            ).apply {
//                putExtra(
//                    Constants.OPEN_FRAGMENT,
//                    PlanMemberShipFragment::class.java.simpleName
//                )
//                bundle.putInt(Constants.PASSED_PLAN_TYPE, 0)
//                putExtras(bundle)
//            })
    }

    private fun goToVisionBoardFrag() {
        replaceFragmentSafely(VisionBoardFragment(),
        containerViewId = R.id.navigation_root_container,
        addToStack = false)
    }
    
    
    private fun goToLevel2InvitationPromptFrag() {
        replaceFragmentSafely(
            LevelTwoInvitationPromptFragment().apply { arguments = intent.extras },
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }
    
    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count > 0) supportFragmentManager.popBackStack()
        else super.onBackPressed()
    }

    private fun goToRelationshipGoalsFrag() {
        replaceFragmentSafely(
            MyRelationshipPlansFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToRelationshipGoalDetailsFrag() {
        replaceFragmentSafely(
            MyRelationshipPlanDetailsFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToRelationshipGoalGiftFrag() {
        replaceFragmentSafely(
            RelationshipPlansFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    private fun goToRelationshipGoalPurchaseSuccessFrag() {
        replaceFragmentSafely(
            RelationshipGoalPurchaseSuccessFragment(),
            containerViewId = R.id.navigation_root_container,
            addToStack = false
        )
    }

    public companion object {
        private const val EXTRA_KEY_NAVIGATION: String = "extra_key_navigation"
		const val PROFILE_MODE = 2 // unity player states [0 to 4]
        public fun createIntent(
            context: Context,
            destination: String,
            bundle: Bundle? = null
        ): Intent {
            return Intent(context, NavigationActivity::class.java).apply {
                bundle?.let {
                    putExtras(it)
                }
                putExtra(EXTRA_KEY_NAVIGATION, destination)
            }
        }
    }
}