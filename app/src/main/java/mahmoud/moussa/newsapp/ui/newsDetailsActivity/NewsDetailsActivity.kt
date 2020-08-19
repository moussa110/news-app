package mahmoud.moussa.newsapp.ui.newsDetailsActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_news_details.*
import mahmoud.moussa.newsapp.basepackage.Consts
import mahmoud.moussa.newsapp.R
import mahmoud.moussa.newsapp.api.model.ArticlesItem

class NewsDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)
        val item=intent.getSerializableExtra(Consts.ARTICLE_EXTRA) as ArticlesItem?;
        Glide.with(this)
            .load(item?.urlToImage)
            .into(details_image);
        collabse_toolbar.title=item?.source?.name;
        var str=item?.publishedAt.toString();
        var date=str.substring(0,10);
        details_title.text=item?.title.toString();
        details_author.text=item?.author.toString();
        details_desc.text=item?.description.toString();
        details_url.text=item?.url.toString();
        details_content.text=item?.content.toString();
        details_publishat.text=date
    }
}

