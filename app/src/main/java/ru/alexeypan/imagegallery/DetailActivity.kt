package ru.alexeypan.imagegallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

  companion object {
    fun newIntent(context: Context, left: Int, top: Int, width: Int, height: Int) {
      val intent = Intent(context, DetailActivity::class.java)
        .apply {
          putExtra("left", left)
          putExtra("top", top)
          putExtra("width", width)
          putExtra("height", height)
        }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val bundle = intent.extras
    val thumbnailTop = bundle!!.getInt("top")
    val thumbnailLeft = bundle.getInt("left")
    val thumbnailWidth = bundle.getInt("width")
    val thumbnailHeight = bundle.getInt("height")
  }
}