package it.xm.android.smartWeather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.xm.android.smartWeather.databinding.CardCallBinding

class CardAdapter(
    private val zones: List<zone>,
    private val clickListener: ZoneClickListener
    )
    : RecyclerView.Adapter<CardViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardCallBinding.inflate(from, parent, false)
        return CardViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int = zones.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindZone(zones[position])
    }
}