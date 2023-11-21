package com.candroid.pazaramafinalproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.candroid.pazaramafinalproject.util.SortOption
import com.candroid.pazaramafinalproject.util.SortOptionDrawable

class HomeFragmentViewModel : ViewModel() {
    val sortOption = MutableLiveData<SortOption>(SortOption.NUMBER)
    val sortOptionDrawable = MutableLiveData<SortOptionDrawable>(SortOptionDrawable.NUMBER)
    val searchQuery = MutableLiveData<String>("")
}