/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.retrofit.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofit.network.MarsApi
import com.example.retrofit.network.MarsApiFilter
import com.example.retrofit.network.MarsProperty
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
enum class MarsApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()
    private val _property = MutableLiveData<List<MarsProperty>?>()
    val property: LiveData<List<MarsProperty>?> = _property

    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty?>()
    val navigateToSelectedProperty: LiveData<MarsProperty?> = _navigateToSelectedProperty

    // The external immutable LiveData for the request status String
    val status: LiveData<MarsApiStatus> = _status

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    fun getMarsRealEstateProperties(filter: MarsApiFilter) {
        viewModelScope.launch {
            try {
                _status.value = MarsApiStatus.LOADING
                val listResult = MarsApi.retrofitService.getProperties(filter.value)
                if (listResult.isNotEmpty())
                    _property.value = listResult
                _status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _property.value = null
            }
        }
    }

    fun displayPropertyDetails(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.postValue(marsProperty)
    }

    fun onNavigation(){
        _navigateToSelectedProperty.value = null
    }
}
