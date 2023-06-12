package com.tovalue.me.ui.auth.primeriii

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentPrimerThreeBinding
import com.tovalue.me.model.CorporateInfoResponse
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.util.DateFormat
import com.tovalue.me.util.DateFormatterUtils
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.livedata.EventObserver
//import com.unity3d.player.UnityPlayerActivity
import java.util.*

class PrimerThreeFragment : Fragment() {
	private var _binding: FragmentPrimerThreeBinding? = null
	private val binding get() = _binding!!
	private var categoryListL = listOf<SideCategory>()
	private val authViewModel: AuthViewModel by activityViewModels()
	private var count = 0
	private var isUnityMode: Boolean = false
	private lateinit var corporateLinks: CorporateInfoResponse

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPrimerThreeBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if (authViewModel.inventory.value == null) {
			binding.loader.isVisible = true
			binding.startBtn.isClickable = false
			binding.startBtn.isEnabled = false
			authViewModel.getInventorySides()
		}
		observeCorporate()
		authViewModel.getCorporateInfo()
		setUpClickListener()
		observeState()
	}
	
	private fun observeState() {
		authViewModel.sidePosition.observe(viewLifecycleOwner) {
			count = it
			Log.d("spectrum", count.toString())
		}
		authViewModel.inventory.observe(viewLifecycleOwner) {
			if (it.sideCategory.isNotEmpty()) {
				binding.loader.isVisible = false
				binding.startBtn.isClickable = true
				binding.startBtn.isEnabled = true
				categoryListL = it.sideCategory
			}
		}

		authViewModel.corporateInfoResponse.observe(viewLifecycleOwner,
			EventObserver {
				DialogUtils.hideDialog()
				when (it) {
					is AuthViewModel.UiEventCorporate.CorporateLinks -> {
						corporateLinks = it.response
					}
					is AuthViewModel.UiEventCorporate.Error -> showToast(
						it.errorMsg
					)
				}
			})
	}


	private fun observeCorporate() {
		authViewModel.corporateInfoResponse.observe(viewLifecycleOwner,
			EventObserver {
				DialogUtils.hideDialog()
				when (it) {
					is AuthViewModel.UiEventCorporate.CorporateLinks -> {
						corporateLinks = it.response
					}
					is AuthViewModel.UiEventCorporate.Error -> showToast(
						it.errorMsg
					)
				}
			})
	}

	private fun setData() {
		val spectrum = LifeSpectrum()

		// for the first visit of user unity module will show demo
		if (count == 0) spectrum.unityModuleState = DEMO_MODE
		// continue the loop of registration visit for 8th iteration
		if (count >= 1) spectrum.unityModuleState = REGISTRATION_MODE
		
		spectrum.registrationLevel = count + 1
		spectrum.audioURL = authViewModel.getSpectrumDemoUrl()
		spectrum.imageURL = authViewModel.getAvatarForSpectrum()
		spectrum.day = "as of ${DateFormatterUtils.formatDateToString(Calendar.getInstance().time, DateFormat.dateFormat4)}"
		
		val aspect = Aspects()
		val response = authViewModel.inventoryResult.value
		spectrum.aspectsOfMyLife = response?.running_total ?: 0
		aspect.aesthetic = response?.aesthetic_side ?: 0
		aspect.emotional = response?.emotional_side ?: 0
		aspect.entertainment = response?.entertainment_side ?: 0
		aspect.intellectual = response?.intellectual_side ?: 0
		aspect.professional = response?.professional_side ?: 0
		aspect.sexual = response?.sexual_side ?: 0
		aspect.spiritual = response?.spiritual_side ?: 0
		aspect.village = response?.community_side ?: 0
		spectrum.aspects = aspect
		
		
//		val unityPlayer = Intent(requireActivity(), UnityPlayerActivity::class.java)
//		unityPlayer.putExtra("arguments", Gson().toJson(spectrum))
//		startActivity(unityPlayer)
		
		
		Handler(Looper.getMainLooper()).postDelayed({
			replaceFragmentSafely(
				QuestionnairesFragment(),
				containerViewId = R.id.host_root
			)
		}, 100)
		
		Log.d("spectrum", spectrum.toString())
		authViewModel.categoryKey = categoryListL[count].categoryId // could be removed later as we already setting [@inventoryData]
		authViewModel.setInventoryCategoryData(categoryListL[count])
		authViewModel.setSidePosition(count+1)
	}
	
	private fun setUpClickListener() {
		binding.startBtn.setOnClickListener {
//			authViewModel.setBookMarkProgress(INVENTORY_STAGE)
			isUnityMode = true
			setData()
		}

		binding.tagLineTv.setOnClickListener {
			val i = Intent(Intent.ACTION_VIEW)
			i.data = Uri.parse(corporateLinks.areMatchAndDating)
			startActivity(i)
		}
	}
	
	private fun goToLifeInventoryFrag() {
//		replaceFragmentSafely(
//			LifeInventoryFragment(),
//			containerViewId = R.id.host_root,
//			addToStack = false
//		)
	}
	
	override fun onResume() {
		super.onResume()
		if (count > 0 && isUnityMode) setData()
		Log.d("spectrum_resume", count.toString())
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	companion object {
		const val DEMO_MODE = 0
		const val REGISTRATION_MODE = 1
	}
}