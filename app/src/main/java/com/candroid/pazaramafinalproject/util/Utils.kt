package com.candroid.pazaramafinalproject.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import com.candroid.pazaramafinalproject.HomeFragmentViewModel
import com.candroid.pazaramafinalproject.R

fun showCustomPopup(context: Context, view: View, homeFragmentViewModel: HomeFragmentViewModel){
    val popupView = LayoutInflater.from(context).inflate(R.layout.popup_menu, null)

    val popupWindow = PopupWindow(
        popupView,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    val sortGroup: RadioGroup = popupView.findViewById(R.id.popupSortRadioGroup)

    when (homeFragmentViewModel.sortOption.value) {
        SortOption.NUMBER -> sortGroup.check(R.id.popupSortNumber)
        SortOption.NAME -> sortGroup.check(R.id.popupSortName)
        else -> sortGroup.check(R.id.popupSortNumber)
    }

    sortGroup.setOnCheckedChangeListener { _, checkedId ->
        homeFragmentViewModel.sortOption.value = when (checkedId) {
            R.id.popupSortName -> SortOption.NAME
            R.id.popupSortNumber -> SortOption.NUMBER
            else -> SortOption.NUMBER
        }
        homeFragmentViewModel.sortOptionDrawable.value = when (checkedId) {
            R.id.popupSortName -> SortOptionDrawable.NAME
            R.id.popupSortNumber -> SortOptionDrawable.NUMBER
            else -> SortOptionDrawable.NUMBER
        }

        view.post {
            view.findViewById<ImageButton>(R.id.sortButton)
                .setImageResource(homeFragmentViewModel.sortOptionDrawable.value!!.optionDrawable)
        }
    }

    popupWindow.isFocusable = true
    popupWindow.showAsDropDown(view, -250, 0)
}

enum class SortOption(val option: String) {
    NUMBER("Number"),
    NAME("Name")
}

enum class SortOptionDrawable(val optionDrawable: Int) {
    NUMBER(R.drawable.ic_sort_tag),
    NAME(R.drawable.ic_sort_name)
}