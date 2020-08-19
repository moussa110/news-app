package mahmoud.moussa.newsapp.ui.splashActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_splash.*
import mahmoud.moussa.newsapp.R
import mahmoud.moussa.newsapp.ui.mainActivity.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        img_splash.animate().rotation(360F).duration = 1700;
        Handler().postDelayed(Runnable {
            startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            finish();
        },2000)
    }
}
