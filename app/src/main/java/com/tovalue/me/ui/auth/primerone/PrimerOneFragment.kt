package com.tovalue.me.ui.auth.primerone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentPrimerOneBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.util.Constants.NOTIFICATION_STAGE
import com.tovalue.me.util.Constants.Name_STAGE
import com.tovalue.me.util.extensions.replaceFragmentSafely

class PrimerOneFragment : Fragment() {
	private var _binding: FragmentPrimerOneBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPrimerOneBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpViews()
		setUpClickListener()
	}
	
	private fun setUpViews() {
		authViewModel.setBookMarkProgress(Name_STAGE)
	}
	
	private fun setUpClickListener() {
		binding.startBtn.setOnClickListener {
			replaceFragmentSafely(
				NameFragment(),
				containerViewId = R.id.host_root,
				addToStack = false
			)
		}
	}
	
	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}
}