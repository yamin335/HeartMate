package com.tovalue.me.ui.auth.invitation

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentInvitationDialogBinding


class InvitationDialogFragment : DialogFragment() {
	private var _binding: FragmentInvitationDialogBinding ?= null
	private val binding get() = _binding!!
	
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val dialog = Dialog(requireContext(), android.R.style.Theme_Light)
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
		dialog.setContentView(R.layout.fragment_invitation_dialog)
		dialog.show()
		return dialog
	}
	
//	override fun onCreateView(
//		inflater: LayoutInflater, container: ViewGroup?,
//		savedInstanceState: Bundle?
//	): View {
//		_binding = FragmentInvitationDialogBinding.inflate(inflater)
//		return binding.root
//	}
	
}