/*
 *  Copyright 2018, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.retrofit.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.retrofit.R
import com.example.retrofit.network.MarsProperty

/**
 * The [ViewModel] that is associated with the [DetailFragment].
 */
class DetailViewModel(marsProperty: MarsProperty, app: Application) : AndroidViewModel(app) {
    private val _marsPropertyDetails = MutableLiveData<MarsProperty?>()
    val marsPropertyDetails: LiveData<MarsProperty?> = _marsPropertyDetails

    val displayPropertyPrice = marsPropertyDetails.map {
        it?.let {
            app.resources.getString(
                when (it.isRental) {
                    true -> R.string.display_price_monthly_rental
                    false -> R.string.display_price
                }, it.price
            )
        }
    }

    val displayPropertyType = marsPropertyDetails.map {
        it?.let {
            app.resources.getString(
                when (it.isRental) {
                    true -> R.string.type_rent
                    false -> R.string.type_sale
                }
            )
        }
    }

    init {
        _marsPropertyDetails.value = marsProperty
    }
}
