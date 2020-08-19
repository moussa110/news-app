package mahmoud.moussa.newsapp

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import mahmoud.moussa.newsapp.basepackage.Consts

@BindingAdapter("imageUrl")
fun loadImageIntoGlide(image:ImageView,url:String){
    Glide.with(image)
        .load(url)
        .into(image)
}

@BindingAdapter("changeDate")
fun changeDate(view:TextView,date:String){
    view.text=Consts.covertTimeToText(date)
}
