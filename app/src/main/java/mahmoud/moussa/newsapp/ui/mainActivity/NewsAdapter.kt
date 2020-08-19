package mahmoud.moussa.newsapp.ui.mainActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import mahmoud.moussa.newsapp.R
import mahmoud.moussa.newsapp.api.model.ArticlesItem
import mahmoud.moussa.newsapp.basepackage.Consts
import mahmoud.moussa.newsapp.database.ArticlesDatabase
import mahmoud.moussa.newsapp.databinding.ItemNewsBinding

class NewsAdapter(val context: Context): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private  var favItem: MutableList<ArticlesItem?>?= null;
    private var data= mutableListOf<ArticlesItem?>();
    var onItemClick: SetOnItemClickListener?=null;
    var onFavoriteIconClick: SetOnItemClickListener?=null;


    fun changeData(newsDate: MutableList<ArticlesItem?>?){
        if (newsDate != null) {
            data=newsDate
        };
        notifyDataSetChanged();
    }

    class ViewHolder(val binding: ItemNewsBinding):RecyclerView.ViewHolder(binding.root) {
        var favoriteIv:ImageView =itemView.findViewById(R.id.item_news_favorite)

        fun bind(news :ArticlesItem?){
            binding.news=news
            binding.invalidateAll();
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val dataBinding:ItemNewsBinding=DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_news,parent,false);
        return ViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {

        return data.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item= data[position];
        holder.bind(item)

        if (isFavorite(item?.title.toString()))
            holder.favoriteIv.setImageResource(R.drawable.ic_favorite_red)
        else
            holder.favoriteIv.setImageResource(R.drawable.ic_favorite_border)

        holder.favoriteIv.setOnClickListener{
            if (!isFavorite(item?.title.toString())){
                onFavoriteIconClick?.onClick(item,1)
                holder.favoriteIv.setImageResource(R.drawable.ic_favorite_red)
                Toast.makeText(context,"added to favorite ♥",Toast.LENGTH_LONG).show()
            }else{
                onFavoriteIconClick?.onClick(favItem?.get(0),2)
                holder.favoriteIv.setImageResource(R.drawable.ic_favorite_border)
                Toast.makeText(context,"remove from favorite ☺",Toast.LENGTH_LONG).show()
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick?.onClick(item);
        }
    }



    interface SetOnItemClickListener{
        fun onClick(item: ArticlesItem?,state:Int?=null);
    }


    private fun isFavorite(title: String):Boolean {
      favItem= ArticlesDatabase
           .getInstance(context)
           ?.articlesDao()
           ?.getFavorite(title)
        return favItem?.size != 0
    }





}