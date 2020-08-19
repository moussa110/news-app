package mahmoud.moussa.newsapp.ui.mainActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import mahmoud.moussa.newsapp.R
import mahmoud.moussa.newsapp.api.model.ArticlesItem
import mahmoud.moussa.newsapp.api.model.SourcesItem
import mahmoud.moussa.newsapp.databinding.ActivityMainBinding
import mahmoud.moussa.noteapp.BaseActivity

class MainActivity : BaseActivity() {

    var actionEg: MenuItem? = null
    var actionWorld: MenuItem? = null
    lateinit var databinding:ActivityMainBinding;
    private val category = listOf("sports", "business", "entertainment", "health", "science", "technology")

    lateinit var adapter: NewsAdapter
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        databinding=DataBindingUtil.setContentView(this,R.layout.activity_main);
        databinding.vm=viewModel

        setSupportActionBar(toolbar)
        adapter = NewsAdapter(this)
        news_recycler.adapter = adapter

        getNewsSources()
        subscribeToLiveData()

        adapter.onItemClick = object : NewsAdapter.SetOnItemClickListener {
            override fun onClick(item: ArticlesItem?, state: Int?) {
//                var intent=Intent(this@MainActivity,
//                NewsDetailsActivity::class.java);
//                intent.putExtra(Consts.ARTICLE_EXTRA,item);
//                startActivity(intent);
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item?.url))
                this@MainActivity.startActivity(browserIntent)
            }
        }

        adapter.onFavoriteIconClick = object :
            NewsAdapter.SetOnItemClickListener {
            override fun onClick(item: ArticlesItem?, state: Int?) {
                if (state == 1) {
                    viewModel.addToFavorite(this@MainActivity, item)
                } else {
                    viewModel.removeFromFavorite(this@MainActivity, item)
                    if (tabbar.visibility == View.GONE && tabbar2.visibility == View.GONE)
                        viewModel.refreshFavData(this@MainActivity)
                }
            }
        }

    }

    private fun subscribeToLiveData() {
        viewModel.sourcesLiveData.observe(this, Observer {
            showSourcesInTab(it)
        })
        viewModel.messageLiveData.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        viewModel.articlesItemLiveData.observe(this, Observer {
            adapter.changeData(it as MutableList<ArticlesItem?>?)
        })
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        actionEg = menu?.findItem(R.id.action_eg) as MenuItem
        actionWorld = menu.findItem(R.id.action_world) as MenuItem

        val search = menu.findItem(R.id.action_search)
        val searchView = search?.actionView as androidx.appcompat.widget.SearchView
        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                tabbar.visibility = View.GONE
                tabbar2.visibility = View.GONE

                viewModel.getNewsByQuery( p0)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        searchView.setOnCloseListener {
            getNewsSources()

            return@setOnCloseListener false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_eg) {

            viewModel.showHeadlines( "eg")
            showCategoryInTab(category)
            return true
        } else if (item.itemId == R.id.action_world) {
            getNewsSources()
            return true
        } else if (item.itemId == R.id.action_favorite) {
            handleView()
            showFavoriteArticles()
            return true
        }
        return true
    }

    private fun showFavoriteArticles() {
        handleView()
        supportActionBar?.title = "Favorite"
        viewModel.showFavoriteArticles(this)
    }

    private fun showCategoryInTab(category: List<String>) {
        handleView(tab2 = View.VISIBLE, egypt = false)
        supportActionBar?.title = "Egypt"
        category.forEach { cat ->
            var tab = tabbar2.newTab()
            tab.text = cat
            tabbar2.addTab(tab)
        }
        tabbar2.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                viewModel.showHeadlines( "eg", "${tab?.text}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.showHeadlines( "eg", "${tab?.text}")
            }
        })
        tabbar2.getTabAt(0)?.select()
    }

    private fun getNewsSources() {
        handleView(tab1 = View.VISIBLE, world = false)
        supportActionBar?.title = "Home"

        viewModel.getNewsSources()
    }

    private fun showSourcesInTab(sources: List<SourcesItem?>?) {
        sources?.forEach { source ->
            var tab = tabbar.newTab()
            tab.tag = source
            tab.text = source?.name
            tabbar.addTab(tab)
        }
        tabbar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                var source = tab?.tag as SourcesItem
                viewModel.showNewsById(source.id)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                var source = tab?.tag as SourcesItem
                viewModel.showNewsById( source.id)
            }
        })
        tabbar.getTabAt(0)?.select()

    }

    fun handleView(
        tab1: Int = View.GONE,
        tab2: Int = View.GONE,
        world: Boolean = true,
        egypt: Boolean = true
    ) {
        tabbar.visibility = tab1
        tabbar2.visibility = tab2
        actionWorld?.isEnabled = world
        actionEg?.isEnabled = egypt
    }

}



