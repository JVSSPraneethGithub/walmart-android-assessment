package com.walmart.assessment.ui.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.walmart.assessment.data.Country
import com.walmart.assessment.databinding.CountryBinding

class CountryListAdapter : ListAdapter<Country, CountryViewHolder>(CountryListDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CountryViewHolder(
            CountryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CountryViewHolder(val binding: CountryBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(country: Country) {
        binding.run {
            countryTitle.text = "${country.name}, ${country.region}"
            countryCode.text = country.code
            countryCapital.text = country.capital
        }
    }
}

private class CountryListDiffUtilCallback : ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country) =
        oldItem.code == newItem.code && oldItem.name == newItem.name &&
                oldItem.region == newItem.region && oldItem.capital == newItem.capital

    override fun areContentsTheSame(oldItem: Country, newItem: Country) =
        oldItem == newItem
}