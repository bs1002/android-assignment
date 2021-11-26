package com.mahfuznow.android_assignment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mahfuznow.android_assignment.model.Country
import com.mahfuznow.android_assignment.model.CountryService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers


class SimpleActivityViewModel : ViewModel() {
    private val service: CountryService = CountryService()
    private val countriesLiveData: MutableLiveData<List<Country>> = MutableLiveData<List<Country>>()
    val isErrorLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        //instantiate the mutable live data
        fetchCountries()
    }

    private fun fetchCountries() {
        service.countries
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                override fun onSuccess(countries: List<Country>) {
                    countriesLiveData.value = countries
                    isErrorLiveData.value = false
                }
                override fun onError(e: Throwable) {
                    isErrorLiveData.value = true
                }
            })
    }

    fun getCountriesLiveData(): MutableLiveData<List<Country>> {
        return countriesLiveData
    }

    fun getIsErrorLiveData(): MutableLiveData<Boolean> {
        return isErrorLiveData
    }

}
