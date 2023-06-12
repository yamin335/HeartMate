package com.tovalue.me.ui.datenightcatalog.datenightcatalogscratchoffer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.tovalue.me.R
import com.tovalue.me.databinding.DialogSelectCreateDateNightOfferBinding
import com.tovalue.me.model.datingJourney.HomeJourney
import com.tovalue.me.util.Constants

class SelectAndCreateDateNightOfferDialog(
    val homeJourney: HomeJourney
) : DialogFragment() {
    private var _binding: DialogSelectCreateDateNightOfferBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSelectCreateDateNightOfferBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectBtn.setOnClickListener {

        }
        binding.createBtn.setOnClickListener {
            openDateNightScratchOfferDialog()
            //dismiss()
        }

    }

    private fun openDateNightScratchOfferDialog() {
        val dialogFragment = DateNightScratchOfferDialog(homeJourney) {
            dismiss()
        }
        dialogFragment.isCancelable = true
        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        val prev: Fragment? =
            childFragmentManager.findFragmentByTag(Constants.DATE_NIGHT_SCRATCH_OFFER_DIALOG_FRAGMENT)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        dialogFragment.show(ft, Constants.DATE_NIGHT_SCRATCH_OFFER_DIALOG_FRAGMENT)
    }
}