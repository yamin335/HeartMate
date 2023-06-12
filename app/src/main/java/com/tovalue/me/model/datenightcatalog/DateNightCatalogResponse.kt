package com.tovalue.me.model.datenightcatalog


sealed class HolderData {


    data class DateNightCatalogResponse(
        var status: String?,
        var error: String?,
        var date_count: Int?,
        var date_night_catalog: List<DateNightCatalog>,
        var date_night_catalog_cover: DateNightCatalogCover,


        ) : HolderData()


    data class DateNightCatalog(
        var week_number: String,
        var dates: List<DateIdea>
    ) : HolderData()


    data class DateIdea(
        var title: String,
        val date_night_id: Int,
        var date_number: String,
        var topics: List<String>,
        var setting: String?,
        var experience: Any?,
        var activity: String?,
        var possibilitiies: Any?,
        var image_url: String?,
        var total_aspects: Int,
        var event_type: String?
    ) : HolderData()


    data class DateNightCatalogCover(
        var name: String,
        var url: String
    ) : HolderData()


    /* Premium promoted Data */
    data class PromotedEventResponse(
        val events: List<Event>,
        val status: String
    ) : HolderData()

    data class Event(
        val details: String,
        val id: String,
        val images: List<Image>,
        val location: Location,
        val name: String,
        val type: String,
        val url: String
    ) : HolderData()

    data class Market(
        val id: String,
        val name: String
    ) : HolderData()

    data class LocationX(
        val latitude: String,
        val longitude: String
    ) : HolderData()

    data class Location(
        val _links: Links,
        val accessibleSeatingDetail: String,
        val address: Address,
        val aliases: List<String>,
        val boxOfficeInfo: BoxOfficeInfo,
        val city: City,
        val country: Country,
        val dmas: List<Dma>,
        val generalInfo: GeneralInfo,
        val id: String,
        val images: List<ImageX>,
        val locale: String,
        val location: LocationX,
        val markets: List<Market>,
        val name: String,
        val parkingDetail: String,
        val postalCode: String,
        val social: Social,
        val state: State,
        val test: Boolean,
        val timezone: String,
        val type: String,
        val upcomingEvents: UpcomingEvents,
        val url: String
    ) : HolderData()

    data class Links(
        val self: Self
    ) : HolderData()


    data class Dma(
        val id: Int
    ) : HolderData()

    data class City(
        val name: String
    ) : HolderData()

    data class BoxOfficeInfo(
        val acceptedPaymentDetail: String,
        val openHoursDetail: String,
        val phoneNumberDetail: String,
        val willCallDetail: String
    ) : HolderData()

    data class Address(
        val line1: String
    ) : HolderData()

    data class Country(
        val countryCode: String,
        val name: String
    ) : HolderData()

    data class GeneralInfo(
        val childRule: String,
        val generalRule: String
    ) : HolderData()

    data class Image(
        val attribution: String,
        val fallback: Boolean,
        val height: Int,
        val ratio: String,
        val url: String,
        val width: Int
    ) : HolderData()

    data class ImageX(
        val fallback: Boolean,
        val height: Int,
        val ratio: String,
        val url: String,
        val width: Int
    ) : HolderData()

    data class Self(
        val href: String
    ) : HolderData()

    data class Social(
        val twitter: Twitter
    ) : HolderData()

    data class State(
        val name: String,
        val stateCode: String
    ) : HolderData()

    data class Twitter(
        val handle: String
    ) : HolderData()

    data class UpcomingEvents(
        val _filtered: Int,
        val _total: Int,
        val ticketmaster: Int,
        val tmr: Int
    ) : HolderData()


}



