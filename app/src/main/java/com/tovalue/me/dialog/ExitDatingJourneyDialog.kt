package com.tovalue.me.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tovalue.me.R
import com.tovalue.me.databinding.ExitDatingJourneyDialogLayoutBinding

class ExitDatingJourneyDialog:DialogFragment() {

    private lateinit var  vb:ExitDatingJourneyDialogLayoutBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        isCancelable = false



        vb = ExitDatingJourneyDialogLayoutBinding.inflate(inflater,container,false)
        return vb.root
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListener()


    }

    private fun initClickListener() {

        vb.close.setOnClickListener {

            dismiss()
        }

    }


}


