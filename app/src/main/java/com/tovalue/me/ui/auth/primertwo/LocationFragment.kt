package com.tovalue.me.ui.auth.primertwo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.tovalue.me.BuildConfig
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentLocationBinding
import com.tovalue.me.helper.PermissionHelper
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.livedata.EventObserver
import java.util.*


class LocationFragment : Fragment()/*, OnMapReadyCallback*/ {
	private var _binding: FragmentLocationBinding? = null
	private val binding get() = _binding!!
	
	private var locationlLatitude = 0.0
	private var locationLongitude =  0.0
	private lateinit var address: String
//	private val permissionHelper: PermissionHelper = PermissionHelper()
//	private var googleMap: GoogleMap? = null
//	private var locationPermissionGranted = false
//	private var lastKnownLocation: Location? = null
//	private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
//		fusedLocationProviderClient =
//			LocationServices.getFusedLocationProviderClient(requireActivity())
//		val mapFragment = requireActivity().supportFragmentManager
//			.findFragmentById(R.id.map) as SupportMapFragment?
//		mapFragment?.getMapAsync(this)
	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentLocationBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)



		setUpClickListener()
		observeStates()
	}
	
	private fun observeStates() {
		authViewModel.locationState.observe(viewLifecycleOwner, EventObserver {
			binding.loader.isVisible = false
			when(it) {
				is AuthViewModel.UiEvent.GotoNextScreen -> {
					authViewModel.setBookMarkProgress(Constants.AVAILABILITY_STAGE)
					goToAvailabilityFragment()
				}
				is AuthViewModel.UiEvent.Error -> {
					showToast(it.errorMsg.toString())
				}
			}
		})
	}
	
	private fun setUpClickListener() {
		Places.initialize(requireActivity(), BuildConfig.GOOGLE_MAPS_API_KEY)
		val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)


		binding.myLocationTv.setOnClickListener {
			val intent = Autocomplete.IntentBuilder(
				AutocompleteActivityMode.FULLSCREEN, fields
			)
				.build(requireActivity())
			startActivityForResult(intent, 101)
		}


		binding.nextImg.setOnClickListener {
			if (address.isNotEmpty()) sendLocation()
		}
	}
	
	private fun sendLocation() {
		binding.loader.isVisible = true
		authViewModel.updateLocation(locationlLatitude, locationLongitude, address)
	}
	
	private fun goToAvailabilityFragment() {
			replaceFragmentSafely(
				AvailabilityLandingFragment(),
				containerViewId = R.id.host_root,
				addToStack = false
			)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
//	override fun onMapReady(googleMap: GoogleMap) {
//		this.googleMap = googleMap
//
//		checkPermissions()
//		updateLocation()
//		getDeviceLocation()
//	}
//
//	@SuppressLint("MissingPermission")
//	private fun getDeviceLocation() {
//		try {
//			if (locationPermissionGranted) {
//				val locationResult = fusedLocationProviderClient.lastLocation
//				locationResult.addOnCompleteListener(requireActivity()) { task ->
//					if (task.isSuccessful) {
//						// Set the map's camera position to the current location of the device.
//						lastKnownLocation = task.result
//						if (lastKnownLocation != null) {
//							googleMap?.moveCamera(
//								CameraUpdateFactory.newLatLngZoom(
//									LatLng(
//										lastKnownLocation!!.latitude,
//										lastKnownLocation!!.longitude
//									), 15f
//								)
//							)
//						}
//					} else {
//						showToast("unable to get location.")
//						googleMap?.uiSettings?.isMyLocationButtonEnabled = false
//					}
//				}
//			}
//		} catch (e: SecurityException) {
//			Log.e("Exception: %s", e.message, e)
//		}
//
//	}
//
//	@SuppressLint("MissingPermission")
//	private fun updateLocation() {
//		if (googleMap == null) {
//			return
//		}
//		try {
//			if (locationPermissionGranted) {
//				googleMap?.isMyLocationEnabled = true
//				googleMap?.uiSettings?.isMyLocationButtonEnabled = true
//			} else {
//				googleMap?.isMyLocationEnabled = false
//				googleMap?.uiSettings?.isMyLocationButtonEnabled = false
//				lastKnownLocation = null
//				checkPermissions()
//			}
//		} catch (e: SecurityException) {
//			Log.e("Exception: %s", e.message, e)
//		}
//	}
//
//	private fun checkPermissions() {
//		if (!permissionHelper.isGrantedLocationPermission(requireContext())) {
//			permissionHelper.checkLocationPermission(
//				binding.root,
//				onPermissionDenied = ::onPermissionDenied,
//				onPermissionGranted = ::onPermissionGranted
//			)
//			return
//		}
//		onPermissionGranted()
//	}
//
//	private fun onPermissionGranted() {
//		locationPermissionGranted = true
//	}
//
//	private fun onPermissionDenied() {
//		locationPermissionGranted = false
//		showToast("Location Permission Denied.")
//	}

	override fun onActivityResult(
		requestCode: Int,
		resultCode: Int,
		@Nullable data: Intent?
	) {

		if (requestCode == 101) {
			if (resultCode == Activity.RESULT_OK) {
				val place = Autocomplete.getPlaceFromIntent(data!!)
				Log.i("TAG-->", "Place: " + place.name + ", " + place.id + ", " + place.address)
				address = place.name?.toString() ?: ""
				locationlLatitude = place.latLng?.latitude ?: 0.0
				locationLongitude = place.latLng?.longitude ?: 0.0
				binding.myLocationTv.setText(address)
				// do query with address
			} else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
				// TODO: Handle the error.
				val status: Status = Autocomplete.getStatusFromIntent(data!!)
				Toast.makeText(
					requireActivity(),
					"Error: " + status.getStatusMessage(),
					Toast.LENGTH_LONG
				).show()
				Log.i("TAG-->", status.getStatusMessage()!!)
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i("TAG-->", resultCode.toString())
				// The user canceled the operation.
			}
		}
	}
}