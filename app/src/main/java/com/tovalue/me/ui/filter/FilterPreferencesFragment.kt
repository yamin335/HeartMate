package com.tovalue.me.ui.filter

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.slider.Slider
import com.tovalue.me.BuildConfig
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentFilterPreferencesBinding
import com.tovalue.me.model.ProfileInfo
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.navigation.NavigationViewModel
import com.tovalue.me.util.extensions.animateVisibility
import com.tovalue.me.util.livedata.EventObserver
import com.tovalue.me.util.metaFilterValues
import java.util.*

class FilterPreferencesFragment : Fragment(), CompoundButton.OnCheckedChangeListener {
    private var _binding: FragmentFilterPreferencesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NavigationViewModel by activityViewModels()

    //	var leveScoreOne: Int = 50
    var levelScoreTwo: Int = 50

    var radiusList: MutableList<String> =
        listOf("10", "15", "25", "30", "50", "100").toMutableList()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val dashboardViewModel: DashboardViewModel by activityViewModels()
    private lateinit var userResponse: ProfileInfo
    private val checkBoxList = ArrayList<String>()

    private var isDistanceChange = false
    private var isLocationChange = false
    private var isavailabilityChange = false
    private var selecetedDistance = ""
    private var selectedLocation = ""

    var commaSeparatedDays = "";
    var isFromBack = false;
    var isUpdate = false;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterPreferencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (dashboardViewModel.profileResponse.value == null) getUserData()

        setUpObserver()
        observeStateUpdate()
        setUpClickListeners()
    }

    private fun getUserData() {
        binding.loader.animateVisibility(View.VISIBLE)
        dashboardViewModel.getProfileInfo(dashboardViewModel.getUserId())
    }

    private fun setUpObserver() {
        dashboardViewModel.profileResponse.observe(viewLifecycleOwner,
            EventObserver {
                binding.loader.animateVisibility(View.GONE)
                when (it) {
                    is DashboardViewModel.UiEventProfile.Profile -> {
                        userResponse = it.response

                        setUpViews(it.response)

                    }
                    is DashboardViewModel.UiEventProfile.COOKIEXPIRE -> {
                        dashboardViewModel.flushSavedData()
                    }
                    is DashboardViewModel.UiEventProfile.Error -> {
                        showToast(it.errorMsg)
                    }
                }
            })
    }


    private fun setUpViews(response: ProfileInfo) {


        /*     val items = ArrayList(response.datingAvailability.split("\\s*,\\s*"))
                 for (day in items){*/
        val strs = response.datingAvailability.split(",").toTypedArray()

        strs.forEach {
            if (it == MONDAY) {
                binding.availabilityLayout.mondayCb.isChecked = true
            }
            if (it == TUESDAY) {
                binding.availabilityLayout.tuesdayCd.isChecked = true
            }
            if (it == WEDNESDAY) {
                binding.availabilityLayout.wednesdayCb.isChecked = true
            }
            if (it == THURSDAY) {
                binding.availabilityLayout.thursdayCb.isChecked = true
            }
            if (it == FRIDAY) {
                binding.availabilityLayout.fridayCb.isChecked = true
            }
            if (it == SATURDAY) {
                binding.availabilityLayout.saturdayCb.isChecked = true
            }
            if (it == SUNDAY) {
                binding.availabilityLayout.sundayCb.isChecked = true
            }
        }

//        if (response.datingAvailability.contains(ALL)) {
//            binding.availabilityLayout.allDayCb.isChecked = true
//        }
        //   }

//        binding.availabilityLayout.allDayCb.visibility = VISIBLE
        binding.topBarLayout.topBarTv.text = resources.getString(R.string.filter_preferences_title)
        binding.topBarLayout.topBarTv.setOnClickListener {
            if (checkBoxList.isEmpty()) {
                showToast("Please select at least one option")
            } else {
                commaSeparatedDays = checkBoxList.joinToString(separator = ",")
            }

            selectedLocation = binding.myLocationTv.text.toString()
            selecetedDistance = binding.spEventDuration.selectedItem.toString()

            Log.i(
                "TAG-->",
                "selectedLocation:" + selectedLocation + " ----- " + "resp:" + userResponse.location
            )
            Log.i(
                "TAG-->",
                "selecetedDistance:" + selecetedDistance + " ----- " + "resp:" + userResponse.maxDistance
            )
            Log.i(
                "TAG-->",
                "datingAvailability:" + commaSeparatedDays + " ----- " + "resp:" + userResponse.datingAvailability
            )

            isDistanceChange = !userResponse.maxDistance.equals(selecetedDistance.toString())
            isLocationChange = !userResponse.location.equals(selectedLocation)
            isavailabilityChange = !userResponse.datingAvailability.equals(commaSeparatedDays)

            if (!isUpdate) {
                if (isavailabilityChange || isLocationChange || isDistanceChange) {
                    showDialog()
                } else {
                    requireActivity().onBackPressed()
                }
            } else {
                requireActivity().onBackPressed()
            }
        }
        checkFilteredSavedValues()

        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.item_filter_radius, R.id.radius_tv, radiusList
        )

        binding.spEventDuration.adapter = adapter

        binding.spEventDuration.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                selecetedDistance = radiusList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.myLocationTv.text = response.location

        for ((i, radius) in radiusList.withIndex()) {

            if (radius.equals(response.maxDistance)) {
                binding.spEventDuration.setSelection(i)
                break
            }
        }
    }

    private fun checkFilteredSavedValues() {
//		if (metaFilterValues != null && metaFilterValues?.levelOneScore != null){
//			leveScoreOne = metaFilterValues?.levelOneScore!!
//			binding.levelOneSb.value = leveScoreOne.toFloat()
//		}
        if (metaFilterValues != null && metaFilterValues?.levelTwoScore!!.isNotEmpty()) {
            levelScoreTwo = metaFilterValues?.levelTwoScore!!.toInt()
            binding.levelTwoSb!!.value = levelScoreTwo.toFloat()
        }

//		setSliderValueOne()
        setSliderValueTwo()
    }

//	private fun setSliderValueOne() {
//		binding.levelOneProgressTv.text = leveScoreOne.toString().plus("%")
//	}

    private fun setSliderValueTwo() {
        binding.levelTwoProgressTv.text = levelScoreTwo.toString().plus("%")
    }

    private fun setUpClickListeners() {
        Places.initialize(requireActivity(), BuildConfig.GOOGLE_MAPS_API_KEY)
        val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)

//		binding.levelOneSb.addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
//			override fun onStartTrackingTouch(slider: Slider) {
//			}
//
//			override fun onStopTrackingTouch(slider: Slider) {
//				leveScoreOne = slider.value.toInt()
//				setSliderValueOne()
//			}
//		})

        listOf(
            binding.availabilityLayout.mondayCb,
            binding.availabilityLayout.tuesdayCd,
            binding.availabilityLayout.wednesdayCb,
            binding.availabilityLayout.thursdayCb,
            binding.availabilityLayout.fridayCb,
            binding.availabilityLayout.saturdayCb,
            binding.availabilityLayout.sundayCb,
//            binding.availabilityLayout.allDayCb
        ).forEach {
            it.setOnCheckedChangeListener(this)
        }
        binding.levelTwoSb.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                levelScoreTwo = slider.value.toInt()
                setSliderValueTwo()
            }
        })

//        binding.topBarLayout.topBarTv.setOnClickListener {
//            lifecycleScope.launch {
////                senValues()
//                delay(400)
//                requireActivity().onBackPressed()
//            }
//        }
        binding.myLocationTv.setOnClickListener {
            //binding.frmContainer.visibility = VISIBLE
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
                .build(requireActivity())
            startActivityForResult(intent, 101)
        }

        binding.updateTv.setOnClickListener {

            if (checkBoxList.isEmpty()) {
                showToast("Please select at least one option")
            } else {
                commaSeparatedDays = checkBoxList.joinToString(separator = ",")
                Log.i("TAG-->", "setUpClickListeners: $commaSeparatedDays")
            }

            selectedLocation = binding.myLocationTv.text.toString()

            isDistanceChange = !userResponse.maxDistance.equals(selecetedDistance.toString())
            isLocationChange = !userResponse.location.equals(selectedLocation)
            isavailabilityChange = !userResponse.datingAvailability.equals(commaSeparatedDays)

            binding.loader.animateVisibility(View.VISIBLE)
            if (isDistanceChange || isLocationChange || isavailabilityChange) {
                isFromBack = false
                isUpdate = true
                authViewModel.updateDatingPreference(
                    selecetedDistance,
                    selectedLocation,
                    commaSeparatedDays
                )
            } else {
                binding.loader.isVisible = false
            }
        }
    }

    private fun observeStateUpdate() {
        authViewModel.updateDatingPreference.observe(viewLifecycleOwner) {
            binding.loader.isVisible = false
            when (it) {
                is AuthViewModel.UiEvent.GotoNextScreen -> {
                    showToast(getString(R.string.update_successfully))
                    isavailabilityChange = false
                    isLocationChange = false
                    isDistanceChange = false
                    if (isFromBack) {
                        requireActivity().onBackPressed()
                    } else {
                        if (dashboardViewModel.profileResponse.value == null) getUserData()
                    }
                }
                is AuthViewModel.UiEvent.Error -> {
                    showToast(it.errorMsg.toString())
                }
            }
        }
    }



    //[@GlobalScope ~ DelicateAPI ]
    private fun senValues() {
//		dashboardViewModel.isApiReuired = true
//		val metaValues = UpdatMetaKeys()
//		metaValues.levelOneScore = "0"
//		metaValues.levelTwoScore = levelScoreTwo.toString()
//		dashboardViewModel.onStateChangedAction(
//			DashboardViewModel.UiAction.Navigation(
//				DashboardViewModel.CurrentNavigationScreen.FILTER_SETTING,
//				metaValues
//			)
//		)

//        viewModel.updateMetaValues(
//            levelOneScore = 0,
//            levelTwoScore = levelScoreTwo
//        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        buttonView.let {
            when (it?.id) {
                binding.availabilityLayout.mondayCb.id -> {
                    if (it.isChecked) checkBoxList.add(MONDAY)
                    else checkBoxList.remove(MONDAY)
                }
                binding.availabilityLayout.tuesdayCd.id -> {
                    if (it.isChecked) checkBoxList.add(TUESDAY)
                    else checkBoxList.remove(TUESDAY)
                }
                binding.availabilityLayout.wednesdayCb.id -> {
                    if (it.isChecked) checkBoxList.add(WEDNESDAY)
                    else checkBoxList.remove(WEDNESDAY)
                }
                binding.availabilityLayout.thursdayCb.id -> {
                    if (it.isChecked) checkBoxList.add(THURSDAY)
                    else checkBoxList.remove(THURSDAY)
                }
                binding.availabilityLayout.fridayCb.id -> {
                    if (it.isChecked) checkBoxList.add(FRIDAY)
                    else checkBoxList.remove(FRIDAY)
                }
                binding.availabilityLayout.saturdayCb.id -> {
                    if (it.isChecked) checkBoxList.add(SATURDAY)
                    else checkBoxList.remove(SATURDAY)
                }
                binding.availabilityLayout.sundayCb.id -> {
                    if (it.isChecked) checkBoxList.add(SUNDAY)
                    else checkBoxList.remove(SUNDAY)
                }
//				binding.availabilityLayout.allDayCb.id -> {
//					if (it.isChecked) checkBoxList.add(ALL)
//					else checkBoxList.remove(ALL)
//				}
                else -> {

                }
            }
        }
    }

    companion object {
        const val MONDAY = "M"
        const val TUESDAY = "T"
        const val WEDNESDAY = "W"
        const val THURSDAY = "R"
        const val FRIDAY = "F"
        const val SATURDAY = "S"
        const val SUNDAY = "Su"
//        const val ALL = "All"
    }

    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_fragment_back_dating)

        val yesBtn = dialog.findViewById(R.id.confirm_yes_tv) as TextView
        val noBtn = dialog.findViewById(R.id.confirm_back_tv) as TextView
        yesBtn.setOnClickListener {
            binding.loader.animateVisibility(View.VISIBLE)
            if (isDistanceChange || isLocationChange || isavailabilityChange) {
                isFromBack = true
                authViewModel.updateDatingPreference(
                    selecetedDistance,
                    selectedLocation,
                    commaSeparatedDays
                )
            } else {
                binding.loader.isVisible = false
            }
            dialog.dismiss()
        }
        noBtn.setOnClickListener {
            requireActivity().onBackPressed()
            dialog.dismiss()
        }
        dialog.show()
        val window: Window = dialog.getWindow()!!
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

}