package com.tovalue.me.ui.auth.primer


import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentLandingPageBinding
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.model.CorporateInfoResponse
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.invitation.AcceptInvitationFragment
import com.tovalue.me.ui.auth.invitation.UnlockInvitationFragment
import com.tovalue.me.ui.auth.primeriii.PrimerThreeFragment
import com.tovalue.me.ui.auth.primeriii.sides.LifeInventoryFragment
import com.tovalue.me.ui.auth.primeriv.GuideFragment
import com.tovalue.me.ui.auth.primeriv.PersonalityFacetFragment
import com.tovalue.me.ui.auth.primeriv.PrimerFourFragment
import com.tovalue.me.ui.auth.primerone.*
import com.tovalue.me.ui.auth.primertwo.AvailabilityLandingFragment
import com.tovalue.me.ui.auth.primertwo.LocationFragment
import com.tovalue.me.ui.auth.primertwo.PrimerTwoFragment
import com.tovalue.me.ui.auth.primerv.LevelOneScoreFragment
import com.tovalue.me.ui.auth.primerv.PrimerFiveFragment
import com.tovalue.me.ui.auth.primerv.TvmNumberFragment
import com.tovalue.me.ui.auth.primervi.PrimerSixFragment
import com.tovalue.me.ui.auth.primerx.PrimerXFragment
import com.tovalue.me.ui.browser.VisitLinksActivity
import com.tovalue.me.ui.dashboard.favorite.LevelOneInvitationFragment
import com.tovalue.me.ui.datenightcatalog.DateNightCatalogFragment
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.livedata.EventObserver


class LandingPageFragment : Fragment() {
    private var _binding: FragmentLandingPageBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()

    private lateinit var httpDataSourceFactory: HttpDataSource.Factory
    private lateinit var defaultDataSourceFactory: DefaultDataSource.Factory
    private lateinit var cacheDataSourceFactory: DataSource.Factory
    private var simpleExoPlayer: ExoPlayer? = null
    private val simpleCache: SimpleCache = MomensityBingoApp.simpleCache
    var link_terms = ""
    var link_privacy = ""
    var link_cookies = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingPageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOnBoardingVideo()
        setUpViews()
        setUpClickListener()
        observeEvents()
    }

    private fun getOnBoardingVideo() {

        authViewModel.getOnBoardingVideo()
        authViewModel.onBoardingResponse.observe(viewLifecycleOwner) {
            when (it) {

                is AuthViewModel.UiEventOnBoardingResponse.Data -> {
                    link_terms = it.success.termsOfService
                    link_cookies = it.success.cookies
                    link_privacy = it.success.privacyPolicy
                    setOnBoardingVideo(it.success)
                }
                is AuthViewModel.UiEventOnBoardingResponse.Error -> {

                }


            }

        }

    }

    private fun setOnBoardingVideo(success: CorporateInfoResponse) {

        initPlayer(success.onboardingVideo)
    }

    private fun initPlayer(videoUrl:String) {
        binding.videoView.useController = false
        binding.videoView.hideController()
        httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)

        defaultDataSourceFactory = DefaultDataSource.Factory(requireContext(),httpDataSourceFactory)
        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter.Builder(requireContext()).build()
        val trackSelector = DefaultTrackSelector(requireContext()).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        simpleExoPlayer = ExoPlayer.Builder(requireContext())
            .setTrackSelector(trackSelector)
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
            .setLoadControl(DefaultLoadControl.Builder().setPrioritizeTimeOverSizeThresholds(false).build()).build()

        val videoUri = Uri.parse(videoUrl)
        val mediaItem = MediaItem.fromUri(videoUri)
        val mediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)

        binding.videoView.player = simpleExoPlayer
        simpleExoPlayer?.playWhenReady = true
        simpleExoPlayer?.seekTo(0, 0)
        simpleExoPlayer?.repeatMode = Player.REPEAT_MODE_ONE
        simpleExoPlayer?.setMediaSource(mediaSource, true)
        simpleExoPlayer?.prepare()
    }




    private fun observeEvents() {
        authViewModel.screenEvent.observe(viewLifecycleOwner,
            EventObserver {
                when (it) {
                    is AuthViewModel.UiEventStage.ResumeScreenOnSatrt -> {
                        when (it.stage) {
                            AuthViewModel.ScreenStage.PRIMERI -> goToPrimerOneFrag()
                            AuthViewModel.ScreenStage.PRIMERII -> goToPrimerTwoFrag()
                            AuthViewModel.ScreenStage.PRIMERIII -> goToPrimerThreeFrag()
                            AuthViewModel.ScreenStage.PRIMERIV -> goToPrimerIvFrag()
                            AuthViewModel.ScreenStage.PRIMERV -> goToPrimerVFrag()
                            AuthViewModel.ScreenStage.PRIMERX -> goToPrimerXFrag()
                            AuthViewModel.ScreenStage.PRIMERVI -> goToPrimerVIFrag()
                            AuthViewModel.ScreenStage.NAME -> goToNameFrag()
                            AuthViewModel.ScreenStage.EMAIL -> goToEmailFrag()
                            AuthViewModel.ScreenStage.PASSWORD -> goToPasswordFrag()
                            AuthViewModel.ScreenStage.NOTIFICATION -> goToNotificationFrag()
                            AuthViewModel.ScreenStage.BIRTHDAY -> goToBirthdayFrag()
                            AuthViewModel.ScreenStage.PHOTO -> goToUploadPhotoFrag()
                            AuthViewModel.ScreenStage.LOCATION -> goToLocationFrag()
                            AuthViewModel.ScreenStage.AVAILABILITY -> goToAvailabilityFrag()
                            AuthViewModel.ScreenStage.INVENTORY -> goToInventoryFrag()
                            AuthViewModel.ScreenStage.FACET -> goToPersonalityFacetFrag()
                            AuthViewModel.ScreenStage.GUIDE -> goToGuideFrag()
                            AuthViewModel.ScreenStage.TVMNUMBER -> goToTVMNumberFrag()
                            AuthViewModel.ScreenStage.LOS -> goToLevelOneSettingFrag()
                            AuthViewModel.ScreenStage.LOI -> goToLevelOneInvitationFrag()
                            AuthViewModel.ScreenStage.UNLOCK -> goToUnlockInvitationFrag()
                            AuthViewModel.ScreenStage.INVITATION -> goToInvitationFrag()
                        }
                    }
                }
            })
    }
    
    private fun goToPrimerVIFrag() {
        replaceFragmentSafely(
            PrimerSixFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }
    
    private fun goToPrimerXFrag() {
        replaceFragmentSafely(
            PrimerXFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }
    
    private fun goToInvitationFrag() {
        replaceFragmentSafely(
            AcceptInvitationFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToUnlockInvitationFrag() {
        replaceFragmentSafely(
            UnlockInvitationFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToLevelOneInvitationFrag() {
        replaceFragmentSafely(
            LevelOneInvitationFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToLevelOneSettingFrag() {
        replaceFragmentSafely(
            LevelOneScoreFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToTVMNumberFrag() {
        replaceFragmentSafely(
            TvmNumberFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToGuideFrag() {
        replaceFragmentSafely(
            DateNightCatalogFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToPersonalityFacetFrag() {
        replaceFragmentSafely(
            PersonalityFacetFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToInventoryFrag() {
        replaceFragmentSafely(
            PrimerThreeFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToPrimerVFrag() {
        replaceFragmentSafely(
            PrimerFiveFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToPrimerIvFrag() {
        replaceFragmentSafely(
            PrimerThreeFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToPrimerThreeFrag() {
        replaceFragmentSafely(
            PrimerThreeFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToNotificationFrag() {
        replaceFragmentSafely(
            NotificationLandingFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToLocationFrag() {
        replaceFragmentSafely(
            LocationFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToAvailabilityFrag() {
        replaceFragmentSafely(
            AvailabilityLandingFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToUploadPhotoFrag() {
        replaceFragmentSafely(
            PrimerOneFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToBirthdayFrag() {
        replaceFragmentSafely(
            BirthdayFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToPasswordFrag() {
        replaceFragmentSafely(
            PasswordFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToEmailFrag() {
        replaceFragmentSafely(
            EmailFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToNameFrag() {
        replaceFragmentSafely(
            NameFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToPrimerTwoFrag() {
        replaceFragmentSafely(
            PrimerTwoFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    private fun goToPhoneAuthFrag() {
        replaceFragmentSafely(
            PhoneAuthFragment(),
            containerViewId = R.id.host_root
        )
    }

    private fun goToPrimerOneFrag() {
        replaceFragmentSafely(
            PrimerOneFragment(),
            containerViewId = R.id.host_root,
            addToStack = false
        )
    }

    // need to remove this movement method for links and use spannable
    private fun setUpViews() {
        var intent: Intent? = null
        val linkSpanTxt = SpannableString(resources.getString(R.string.account_terms))
        val termsOfServiceTxt = object : ClickableSpan() {
            override fun onClick(tv: View) {
                intent = Intent(requireActivity(), VisitLinksActivity::class.java)
                intent!!.putExtra(LINK_URL, link_terms)
                startActivity(intent)
            }

            override fun updateDrawState(paint: TextPaint) {
                paint.color = Color.WHITE
                paint.isFakeBoldText = true
                paint.isUnderlineText = true
            }
        }

        val privacyPolicyTxt = object : ClickableSpan() {
            override fun onClick(tv: View) {
                intent = Intent(requireActivity(), VisitLinksActivity::class.java)
                intent!!.putExtra(LINK_URL, link_privacy)
                startActivity(intent)
            }

            override fun updateDrawState(paint: TextPaint) {
                paint.color = Color.WHITE
                paint.isFakeBoldText = true
                paint.isUnderlineText = true
            }
        }

        val cookiePolicyTxt = object : ClickableSpan() {
            override fun onClick(tv: View) {
                intent = Intent(requireActivity(), VisitLinksActivity::class.java)
                intent!!.putExtra(LINK_URL, link_cookies)
                startActivity(intent)
            }

            override fun updateDrawState(paint: TextPaint) {
                paint.color = Color.WHITE
                paint.isFakeBoldText = true
                paint.isUnderlineText = true
            }
        }

        // magic numbers are for link start index and end index
        linkSpanTxt.setSpan(termsOfServiceTxt, 40, 56, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        linkSpanTxt.setSpan(privacyPolicyTxt, 97, 111, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        linkSpanTxt.setSpan(cookiePolicyTxt, 116, 130, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.linkTv.apply {
            text = linkSpanTxt
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }

    private fun setUpClickListener() {
        binding.createBtn.setOnClickListener {
            goToPhoneAuthFrag()
        }

        binding.loginTv.setOnClickListener {
        }

        binding.signTv.setOnClickListener {
            replaceFragmentSafely(
                PrimerThreeFragment(),
                containerViewId = R.id.host_root,
                addToStack = false
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        simpleExoPlayer?.release()
        simpleExoPlayer = null
        binding.videoView.player = null
        _binding = null
    }

    companion object {
        const val LINK_URL = "url"
        const val WEB_URL = "https://seemehearmebingo.com"
    }
}