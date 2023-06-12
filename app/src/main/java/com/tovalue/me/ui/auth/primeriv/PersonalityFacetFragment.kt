package com.tovalue.me.ui.auth.primeriv

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nikola.jakshic.spiderchart.SpiderData
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentPersonalityFacetBinding
import com.tovalue.me.model.TvmReport
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.datenightcatalog.DateNightCatalogFragment
import com.tovalue.me.util.Constants.GUIDE_STAGE
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.livedata.EventObserver


class PersonalityFacetFragment : Fragment() {
	private var _binding: FragmentPersonalityFacetBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPersonalityFacetBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		if (authViewModel.isEditMode) {
			binding.backBtn.isVisible = false
			binding.startBtn.isVisible = true
		} else {
			binding.backBtn.isVisible = true
			binding.startBtn.isVisible = false
		}
		
		binding.loader.isVisible = true
		authViewModel.getPersonalityFacts()
		setUpClickListener()
		observeEvents()
	}
	
	private fun observeEvents() {
		authViewModel.reportState.observe(viewLifecycleOwner, EventObserver {
			binding.loader.isVisible = false
			when(it) {
				is AuthViewModel.UiEventTvmReport.Report -> {
					setUpViews(it.data)
				}
				is AuthViewModel.UiEventTvmReport.Error -> {
				showToast(it.errorMsg.toString())
				}
			}
		})
	}
	
	private fun setUpViews(data: TvmReport) {
		val categoryList =
			listOf(
				"Entertainment",
				"Sexual",
				"Aesthetic",
				"Professional",
				"Village",
				"Spiritual",
				"Emotional",
				"Intellectual"
			)
		val listRanges = floatArrayOf(
			data.entertainment.toFloat(), data.sexual.toFloat(),
			data.aesthetic.toFloat(), data.professional.toFloat(), data.village.toFloat(),
			data.spiritual.toFloat(), data.emotional.toFloat(), data.intellectual.toFloat()
		)
		
		val color = Color.argb(21, 150, 161, 1)
		val webChartData: List<SpiderData> = listOf(SpiderData(listRanges, color))
		
		binding.radarChart.setLabels(categoryList)
		binding.radarChart.setData(webChartData)
		binding.radarChart.setBackgroundColor(Color.WHITE)
		binding.radarChart.setLabelColor(requireActivity().resources.getColor(R.color.teal, requireContext().theme))
		binding.radarChart.setDrawWeb(true)
		binding.radarChart.setDrawLabels(true)
		binding.radarChart.setRotationAngle(90f)
		binding.radarChart.refresh()
		
		binding.titleTv.text = "There’s a lot to ${data.firstName}, you have over"
		binding.facetTv.text = data.totalPersonalityFacets.toString()
		binding.subTitleTv.visibility = View.VISIBLE
		if (data.summary != null)
			binding.subTitleTv.text = HtmlCompat.fromHtml(data.summary[0], HtmlCompat.FROM_HTML_MODE_LEGACY)
		authViewModel.UserTotalLifeScore = data.totalPersonalityFacets
	}
	
	private fun setUpClickListener() {
		binding.startBtn.setOnClickListener {
			authViewModel.setBookMarkProgress(GUIDE_STAGE)
			goToDateNightCataLogFrag()
		}
		binding.backBtn.setOnClickListener{
			requireActivity().onBackPressed()
		}
	}
	
	private fun goToDateNightCataLogFrag() {
		replaceFragmentSafely(
			DateNightCatalogFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}