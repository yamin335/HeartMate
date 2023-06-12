package com.tovalue.me.ui.datenightcatalog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentDateNightCatalogIdeaDetailsBinding
import com.tovalue.me.dialog.DateNightCatalogEventOfferDialog
import com.tovalue.me.model.SendOfferDateNight
import com.tovalue.me.model.datenightcatalog.DateNightCatalogDetailResponse
import com.tovalue.me.ui.datenightcatalog.viewmodel.DateNightCatalogViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils


data class DateNightCatalogIdeaDetailsDataObj(
    val dateNightId: String,
    val partnerUserId: Int,
    val topics: String,
    val week_id: Int,
    val groupId: Int
)

class DateNightCatalogIdeaDetailsFragment() : Fragment() {


    private var _binding: FragmentDateNightCatalogIdeaDetailsBinding? = null
    private val binding get() = _binding!!

    private val dateNightCatalogViewModel: DateNightCatalogViewModel by activityViewModels()
    private var dataDetailResponse: DateNightCatalogDetailResponse? = null
    private var bundleData:DateNightCatalogIdeaDetailsDataObj?= null


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateNightCatalogIdeaDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            arguments?.let {
                bundleData = Gson().fromJson(it.getString(Constants.DATE_NIGHT_CATALOG_IDEAS_DETAILS_DATA),DateNightCatalogIdeaDetailsDataObj::class.java)
            }


        bundleData?.let {
            getDateNightCatalogDetail(it.dateNightId)
            setClickListener()
        }

    }

    private fun setClickListener() {

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.sendDAteNightOfferBtn.setOnClickListener {

            bundleData?.let {bd->
                val dialog = DateNightCatalogEventOfferDialog(bd.topics) {
                    it.dateNightId = dataDetailResponse?.date_night_id.toString() ?: ""
                    it.dateWeekId = bd.week_id
                    it.groupId = bd.groupId
                    it.partnerUserId = bd.partnerUserId
                    it.inventoryTopic = bd.topics
                    sendDateNightOffer(it)
                }
                dialog.show(
                    parentFragmentManager,
                    DateNightCatalogIdeaDetailsFragment::class.java.simpleName
                )

            }
        }

    }

    /* Send Date Night Offer*/
    private fun sendDateNightOffer(sendOfferDataNight: SendOfferDateNight) {

        DialogUtils.showDialog(requireContext(), false)
        dateNightCatalogViewModel.sendDateNightOffer(sendOfferDataNight)


        dateNightCatalogViewModel.getDateNightCatalogOfferResponseLiveData()
            .observe(viewLifecycleOwner) {
                when (it) {
                    is DateNightCatalogViewModel.UIEventSendDateNightOffer.Data -> {
                        DialogUtils.hideDialog()
                        Toast.makeText(
                            requireContext(),
                            "Successfully send Date Night Offer your date night offer id is ${it.data.date_offer_id}",
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().onBackPressed()
                    }
                    is DateNightCatalogViewModel.UIEventSendDateNightOffer.Error -> {
                        DialogUtils.hideDialog()
                        showToast(it.errorMsg.toString())
                    }
                }


            }


    }

    private fun getDateNightCatalogDetail(dateNightId: String) {

        dateNightCatalogViewModel.getDateNightCatalogDetail(dateNightId)
        dateNightDetailObserver()

    }


    private fun dateNightDetailObserver() {

        dateNightCatalogViewModel.getDateNightCatalogDetailLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is DateNightCatalogViewModel.UiEventDateNightCatalogDataDetail.Data -> {
                    DialogUtils.hideDialog()
                    dataDetailResponse = it.data
                    setViews(it.data)
                }
                is DateNightCatalogViewModel.UiEventDateNightCatalogDataDetail.Error -> {
                    DialogUtils.hideDialog()
                    showToast(it.errorMsg.toString())
                }
            }


        }
    }

    private fun setViews(data: DateNightCatalogDetailResponse) {

        binding.toolTitle.text = bundleData?.topics?:""

        Glide.with(requireContext()).load(data.image_url)
            .error(R.drawable.default_avatar)
            .into(binding.bgImg)

            binding.settingTV.text = data.setting

            binding.possibilityTV.text = data.possibilities


            binding.experienceTV.text = data.experience

    }


}