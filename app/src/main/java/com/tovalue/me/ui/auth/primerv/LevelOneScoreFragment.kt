package com.tovalue.me.ui.auth.primerv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentLevelOneScoreBinding
import com.tovalue.me.ui.dashboard.favorite.LevelOneInvitationFragment
import com.tovalue.me.util.extensions.replaceFragmentSafely

class LevelOneScoreFragment : Fragment() {
	private var _binding: FragmentLevelOneScoreBinding? = null
	private val binding get() = _binding!!
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentLevelOneScoreBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setUpClickListener()
	}
	
	private fun setUpClickListener() {
		binding.startBtn.setOnClickListener {
			replaceFragmentSafely(
				LevelOneInvitationFragment(),
				containerViewId = R.id.host_root
			)
		}
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}