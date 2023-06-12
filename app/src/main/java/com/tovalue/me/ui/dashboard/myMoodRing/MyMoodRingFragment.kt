package com.tovalue.me.ui.dashboard.myMoodRing

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.tovalue.me.BuildConfig
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentMyMoodRingBinding
import com.tovalue.me.model.ProfileInfo
import com.tovalue.me.model.moodRingModels.MoodRingHistoryDetail
import com.tovalue.me.ui.auth.AuthHostActivity.Companion.createAuthIntent
import com.tovalue.me.ui.browser.VisitLinksActivity
import com.tovalue.me.ui.dashboard.DashboardActivity
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.home.HomeFragment
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.ui.datenightcatalog.DataNightCatalogCoverDialog
import com.tovalue.me.ui.datenightcatalog.DateNightCatalogIdeasFragment
import com.tovalue.me.util.*
import com.tovalue.me.util.extensions.animateVisibility
import com.tovalue.me.util.extensions.updateStatusBarBackgroundColor
import com.tovalue.me.util.livedata.EventObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MyMoodRingFragment : Fragment() {
	private var _binding: FragmentMyMoodRingBinding? = null
	private val binding get() = _binding!!
	private val viewModel: DashboardViewModel by activityViewModels()

	var emotionalValue: Int = 0
	var mentalValue: Int = 0
	var spiritualValue: Int = 0
	var communalValue: Int = 0
	var physicalValue: Int = 0
	var professionalValue: Int = 0

	private var isDetails = false
	var isCheckClickByUser = true
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentMyMoodRingBinding.inflate(layoutInflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		updateStatusBarBackgroundColor("#041F1B")
		if (historyDetailId != -1) {
			isDetails = true
		} else {
			emotionalValue = 30
			mentalValue = 40
			spiritualValue = 100
			communalValue = 70
			physicalValue = 50
			professionalValue = 60
		}

		setUpClickListener()
		setUpObserver()
		updateMoodRing()
		setUpViews()
	}

	override fun onResume() {
		super.onResume()
		if (isDetails) {
			DialogUtils.showDialog(requireContext(), true)
			viewModel.getMoodRingHistoryDetails(historyDetailId)
		}
	}

	private fun setUpViews() {
		binding.linearTracking.visibility = if (isDetails) View.GONE else View.VISIBLE
		binding.titleMoodRing.visibility = if (isDetails) View.VISIBLE else View.GONE
		binding.optionalRequirements.visibility = if (isDetails) View.GONE else View.VISIBLE
		binding.linearExplanation.visibility = if (isDetails) View.VISIBLE else View.GONE

		if (isDetails) binding.subHeaderTitle.text = ""
	}
	
	private fun setUpObserver() {
		viewModel.moodRingStoreResponse.observe(viewLifecycleOwner) {
			DialogUtils.hideDialog()
			clearSavedMoodRingData()
			showToast("Mood saved successfully")
		}

		viewModel.historyDetailResponse.observe(viewLifecycleOwner,
			EventObserver {
				DialogUtils.hideDialog()
				when (it) {
					is DashboardViewModel.UiEventHistoryDetails.HistoryDetails -> {
						bindHistoryDetailsData(it.response)
					}
					is DashboardViewModel.UiEventHistoryDetails.COOKIEXPIRE -> {
						viewModel.flushSavedData()
						goToAuthScreen()
					}
					is DashboardViewModel.UiEventHistoryDetails.Error -> {
						showToast(it.errorMsg)
					}
				}
			})

		viewModel.state.observe(viewLifecycleOwner,
			EventObserver {
				DialogUtils.hideDialog()
				when (it) {
					is DashboardViewModel.UiEventAccount.Exit -> {
						when (it.exitType) {
							DashboardViewModel.AppExitType.LOGOUT -> {
								viewModel.flushSavedData()
								goToAuthScreen()
							}
							DashboardViewModel.AppExitType.DEACTIVATE -> {
								viewModel.flushSavedData()
								goToAuthScreen()
							}
						}
					}
					is DashboardViewModel.UiEventAccount.Error -> it.errorMsg?.let { it1 ->
						showToast(
							it1
						)
					}
				}
			})
	}

	private fun bindHistoryDetailsData(data: MoodRingHistoryDetail) {
		binding.labelEmotional.text = "Emotionally I am at $emotionalValue% because"
		binding.labelMental.text = "Mentally I am at $mentalValue% because"
		binding.labelSpiritual.text = "Spiritually I am at $spiritualValue% because"
		binding.labelCommunal.text = "Communally I am at $communalValue% because"
		binding.labelPhysical.text = "Physically I am at $physicalValue% because"
		binding.labelProfessional.text = "Professionally I am at $professionalValue% because"


		binding.titleHeader.text = data.date_heading
		binding.subHeaderTitle.text = data.summary
		binding.titleMoodRing.text = "Mood Ring for ${data.timestamp}"
		binding.textFirstName.text = "${data.first_name} is in a"
		binding.moodSummary.text = data.summary
		binding.etEmotional.setText(data.emotional_explanation)
		binding.etMental.setText(data.mental_explanation)
		binding.etSpiritual.setText(data.spiritual_explanation)
		binding.etCommunal.setText(data.communal_explanation)
		binding.etPhysical.setText(data.physical_explanation)
		binding.etProfessional.setText(data.professional_explanation)

		emotionalValue = data.emotional ?: 10
		mentalValue = data.mental ?: 10
		spiritualValue = data.spiritual ?: 10
		communalValue = data.communal ?: 10
		physicalValue = data.physical ?: 10
		professionalValue = data.professional ?: 10

		updateMoodRing()

		DialogUtils.hideDialog()
	}

	private fun updateMoodRing() {
		binding.valueCommunal.text = "$communalValue%"
		binding.valueProfessional.text = "$professionalValue%"
		binding.valuePhysical.text = "$physicalValue%"
		binding.valueMental.text = "$mentalValue%"
		binding.valueEmotional.text = "$emotionalValue%"
		binding.valueSpiritual.text = "$spiritualValue%"

		binding.progressCommunal.progress = communalValue
		binding.progressProfessional.progress = professionalValue
		binding.progressPhysical.progress = physicalValue
		binding.progressMental.progress = mentalValue
		binding.progressEmotional.progress = emotionalValue
		binding.progressSpiritual.progress = spiritualValue
	}
	
	private fun setUpClickListener() {
		binding.btnBack.setOnClickListener {
			clearSavedMoodRingData()
			requireActivity().onBackPressed()
		}

		if (!isDetails) {
			binding.btnIncreaseSpiritually.setOnClickListener {
				if (spiritualValue <= 90) {
					spiritualValue += 10
					updateMoodRing()
				}
			}

			binding.btnIncreaseEmotionally.setOnClickListener {
				if (emotionalValue <= 90) {
					emotionalValue += 10
					updateMoodRing()
				}
			}

			binding.btnIncreasePhysically.setOnClickListener {
				if (physicalValue <= 90) {
					physicalValue += 10
					updateMoodRing()
				}
			}

			binding.btnIncreaseCommunally.setOnClickListener {
				if (communalValue <= 90) {
					communalValue += 10
					updateMoodRing()
				}
			}

			binding.btnIncreaseProfessionally.setOnClickListener {
				if (professionalValue <= 90) {
					professionalValue += 10
					updateMoodRing()
				}
			}

			binding.btnIncreaseMentally.setOnClickListener {
				if (mentalValue <= 90) {
					mentalValue += 10
					updateMoodRing()
				}
			}

			binding.btnDecreaseSpiritually.setOnClickListener {
				if (spiritualValue >= 10) {
					spiritualValue -= 10
					updateMoodRing()
				}
			}

			binding.btnDecreaseEmotionally.setOnClickListener {
				if (emotionalValue >= 10) {
					emotionalValue -= 10
					updateMoodRing()
				}
			}

			binding.btnDecreaseMentally.setOnClickListener {
				if (mentalValue >= 10) {
					mentalValue -= 10
					updateMoodRing()
				}
			}

			binding.btnDecreasePhysically.setOnClickListener {
				if (physicalValue >= 10) {
					physicalValue -= 10
					updateMoodRing()
				}
			}

			binding.btnDecreaseCommunally.setOnClickListener {
				if (communalValue >= 10) {
					communalValue -= 10
					updateMoodRing()
				}
			}

			binding.btnDecreaseProfessionally.setOnClickListener {
				if (professionalValue >= 10) {
					professionalValue -= 10
					updateMoodRing()
				}
			}

			binding.btnIncludeExplanation.setOnCheckedChangeListener { _, isChecked ->
				if (isChecked) {
					val dialog = MoodRingExplanationDialogFragment(
						emotionalValue = emotionalValue, mentalValue = mentalValue,
						physicalValue = physicalValue, communalValue = communalValue,
						professionalValue = professionalValue, spiritualValue = spiritualValue
					)
					dialog.show(parentFragmentManager, MyMoodRingFragment::class.java.simpleName)
					binding.btnShowExplanationSeeker.isChecked = false
				} else if (isCheckClickByUser) {
					binding.btnIncludeExplanation.isChecked = true
					val dialog = MoodRingExplanationDialogFragment(
						emotionalValue = emotionalValue, mentalValue = mentalValue,
						physicalValue = physicalValue, communalValue = communalValue,
						professionalValue = professionalValue, spiritualValue = spiritualValue
					)
					dialog.show(parentFragmentManager, MyMoodRingFragment::class.java.simpleName)
					binding.btnShowExplanationSeeker.isChecked = false
				}
				isCheckClickByUser = true
			}

			binding.btnShowExplanationSeeker.setOnCheckedChangeListener { _, isChecked ->
				if (isChecked) {
					isCheckClickByUser = false
					binding.btnIncludeExplanation.isChecked = false
				}
			}

			binding.btnShare.setOnClickListener {
				DialogUtils.showDialog(requireContext(), true)

				viewModel.saveMoodRing(emotionally = emotionalValue, mentally = mentalValue,
					physically = physicalValue, communally = communalValue,
					professionally = professionalValue, spiritually = spiritualValue,
					emotionallyExplanation, mentallyExplanation, spirituallyExplanation,
					communallyExplanation, physicallyExplanation, professionallyExplanation)
			}

			binding.btnHistory.setOnClickListener {
				startActivity(NavigationActivity.createIntent(requireActivity(), Constants.MOOD_RING_HISTORY))
			}
		} else {
			binding.btnBackToTop.setOnClickListener {
				binding.scrollView.smoothScrollTo(0, binding.scrollView.top)
			}
		}

		binding.btnUpload.setOnClickListener {
			DialogUtils.showDialog(requireContext(), true)
			takeSnapAndShare()
		}
	}

	private fun takeSnapAndShare() {
		CoroutineScope(Dispatchers.Main.immediate).launch {
			binding.appIcon.visibility = View.VISIBLE
			binding.appTag.visibility = View.VISIBLE
			if (!isDetails) binding.linearTracking.visibility = View.GONE
			binding.layoutSnap.background = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_rectangle_1)
			if (isDetails) binding.titleMoodRing.visibility = View.GONE
			if (isDetails) binding.linearMoodSummary.visibility = View.VISIBLE
			DialogUtils.hideDialog()
			delay(100)
			val snapBitmap = binding.layoutSnap.drawToBitmap()//takeSnapOfView(view, view.width, view.height)
			binding.layoutSnap.background = null
			binding.appIcon.visibility = View.GONE
			binding.appTag.visibility = View.GONE
			if (!isDetails) binding.linearTracking.visibility = View.VISIBLE
			if (isDetails) binding.titleMoodRing.visibility = View.VISIBLE
			if (isDetails) binding.linearMoodSummary.visibility = View.GONE
			delay(100)
			val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
			//shareSnap(ImageAndFileUtils.uriFromBitmap(snapBitmap, requireContext(), "JPEG_${timeStamp}_.jpg"))
			shareSnap(ImageAndFileUtils.saveBitmapFileIntoExternalStorageWithTitle(requireContext(), snapBitmap, "JPEG_${timeStamp}_.jpg"))
		}
	}

	private fun shareSnap(file: File) {
		try {
			val share = Intent(Intent.ACTION_SEND)
			share.type = "image/*"
			share.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file))
			requireContext().startActivity(
				Intent.createChooser(
					share,
					"Share Snap Via"
				)
			)
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	private fun takeSnapOfView(view: View, height: Int, width: Int): Bitmap {
		val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bitmap)
		val bgDrawable = view.background
		if (bgDrawable != null) {
			bgDrawable.draw(canvas)
		} else {
			canvas.drawColor(Color.WHITE)
		}
		view.draw(canvas)
		return bitmap
	}

	override fun onDestroy() {
		super.onDestroy()
		historyDetailId = -1
	}

	override fun onDetach() {
		super.onDetach()
		historyDetailId = -1
	}

	private fun clearSavedMoodRingData() {
		emotionallyExplanation = ""
		mentallyExplanation = ""
		spirituallyExplanation = ""
		communallyExplanation = ""
		physicallyExplanation = ""
		professionallyExplanation = ""
		historyDetailId = -1
	}
	
	private fun goToLinksActivity(link: String) {
		val intent = Intent(requireActivity(), VisitLinksActivity::class.java).apply {
			putExtra("url", link)
		}
		startActivity(intent)
	}
	
	private fun logout() {
		DialogUtils.showDialog(requireActivity(), false)
		viewModel.logOut()
	}
	
	private fun confirmAccountDeletion() {
		DialogUtils.showDialog(requireActivity(), false)
		viewModel.deactivateAccount()
	}
	
	private fun goToAuthScreen() {
		startActivity(
			createAuthIntent(requireActivity()).addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP or
						Intent.FLAG_ACTIVITY_NEW_TASK or
						Intent.FLAG_ACTIVITY_CLEAR_TASK
			)
		)
		requireActivity().finish()
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}