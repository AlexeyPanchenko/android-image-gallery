package ru.alexeypan.imagegallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)


    ivImage.setOnClickListener {
      val screenLocation = IntArray(2)
      ivImage.getLocationOnScreen(screenLocation)
      startActivity(
        DetailActivity.newIntent(
          this,
          screenLocation[0],
          screenLocation[1],
          ivImage.width,
          ivImage.height
        )
      )
    }
  }
}
