package com.udacity.asteroidradar.main
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidItemListBinding

class AsteroidsListAdapter(val onClickListener: OnClickListener):
   ListAdapter<Asteroid,AsteroidsListAdapter.AsteroidViewHolder>(DiffCallback) {

   companion object DiffCallback: DiffUtil.ItemCallback<Asteroid>(){
      override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
         return  oldItem==newItem
      }
      override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
         return oldItem.id==newItem.id
      }
   }
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
         return AsteroidViewHolder(
         AsteroidItemListBinding.inflate(LayoutInflater.from(parent.context))
      )
   }

   override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
      holder.bind(getItem(position))
      holder.itemView.setOnClickListener {
         onClickListener.onClick(getItem(position))
      }
   }
   class AsteroidViewHolder(private val binding:AsteroidItemListBinding): RecyclerView.ViewHolder(binding.root) {
      fun bind(asteroid: Asteroid){
         binding.asteroid=asteroid
         binding.executePendingBindings()
      }
   }
   class OnClickListener(val clickListener:(asteroid: Asteroid)->Unit){
      fun onClick(asteroid: Asteroid)=clickListener(asteroid)
   }
}