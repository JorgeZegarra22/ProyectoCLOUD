package com.example.proyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ScanHistoryAdapter :
    ListAdapter<ScanHistory, ScanHistoryAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.itemImage)
        val title = view.findViewById<TextView>(R.id.itemPrediction)
        val desc = view.findViewById<TextView>(R.id.itemDescription)
        val date = view.findViewById<TextView>(R.id.itemDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        Glide.with(holder.itemView.context)
            .load(item.imageUri)
            .into(holder.image)

        holder.title.text = item.prediction
        holder.desc.text = item.description
        holder.date.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .format(Date(item.date))
    }

    class DiffCallback : DiffUtil.ItemCallback<ScanHistory>() {
        override fun areItemsTheSame(o: ScanHistory, n: ScanHistory) = o.id == n.id
        override fun areContentsTheSame(o: ScanHistory, n: ScanHistory) = o == n
    }
}
