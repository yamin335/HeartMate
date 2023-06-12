package com.tovalue.me.ui.datenightcatalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentDateNightCatalogIdeaListDetailsBinding
import com.tovalue.me.model.datenightcatalog.DateNightCatalogDetailResponse
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.ui.datenightcatalog.viewmodel.DateNightCatalogViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.livedata.EventObserver

class DateNightCatalogIdeaListDetailsFragment : Fragment() {

    private var _binding: FragmentDateNightCatalogIdeaListDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DateNightCatalogViewModel by activityViewModels()
    private var dataDetailResponse: DateNightCatalogDetailResponse? = null


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateNightCatalogIdeaListDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        DialogUtils.showDialog(requireContext(), true)
        viewModel.getDateNightIdeaDetails(dateNightId = dateNightId, isPartner = 1)
    }

    private fun initViews() {

        binding.titleHeader.text = dateNightTitle

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.dateNightIdeaDetailResponse.observe(viewLifecycleOwner,
            EventObserver {
                DialogUtils.hideDialog()
                when (it) {
                    is DateNightCatalogViewModel.UIEventDateNightIdeaDetails.Data -> {
                        it.data?.let { data ->
                            bind(data = data)
                        }
                    }
                    is DateNightCatalogViewModel.UIEventDateNightIdeaDetails.Error -> {
                        showToast(it.errorMsg ?: "Undefined Error!")
                    }
                }
            })

    }

    private fun bind(data: DateNightCatalogDetailResponse) {
        Glide.with(requireContext()).load(data.image_url)
            .error(R.drawable.date_night_catalog_cover)
            .into(binding.coverImage)

        val completed = data.completed ?: 0
        binding.completed.text = completed.toString()

       binding.settings.text = data.setting

        binding.experience.text = data.experience

        binding.possibilities.text = data.possibilities

        binding.tvHistory.setOnClickListener {
            startActivity(NavigationActivity.createIntent(requireActivity(), Constants.DATE_NIGHT_CATALOG_IDEA_DETAILS_HISTORY))
        }
    }

    companion object {
        var dateNightId: Int =  0
        var dateNightTitle: String = ""
    }
}