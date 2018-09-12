package ru.alexeypan.imagegallery

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import kotlinx.android.synthetic.main.detail_activity.*


class DetailActivity : AppCompatActivity() {

  private val ANIM_DURATION = 250

  companion object {
    fun newIntent(context: Context, left: Int, top: Int, width: Int, height: Int): Intent {
      return Intent(context, DetailActivity::class.java)
        .apply {
          putExtra("left", left)
          putExtra("top", top)
          putExtra("width", width)
          putExtra("height", height)
        }
    }
  }

  private var mLeftDelta: Int = 0
  private var mTopDelta: Int = 0
  private var mWidthScale: Float = 0.toFloat()
  private var mHeightScale: Float = 0.toFloat()

  private var colorDrawable: ColorDrawable? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.detail_activity)
    val bundle = intent.extras
    val thumbnailTop = bundle.getInt("top")
    val thumbnailLeft = bundle.getInt("left")
    val thumbnailWidth = bundle.getInt("width")
    val thumbnailHeight = bundle.getInt("height")

    colorDrawable = ColorDrawable(Color.BLACK)
    rlParent.background = colorDrawable

    if (savedInstanceState == null) {
      val observer = ivImage.viewTreeObserver
      observer.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {

        override fun onPreDraw(): Boolean {
          ivImage.viewTreeObserver.removeOnPreDrawListener(this)

          // Figure out where the thumbnail and full size versions are, relative
          // to the screen and each other
          val screenLocation = IntArray(2)
          ivImage.getLocationOnScreen(screenLocation)
          mLeftDelta = thumbnailLeft - screenLocation[0]
          mTopDelta = thumbnailTop - screenLocation[1]

          // Scale factors to make the large version the same size as the thumbnail
          mWidthScale = thumbnailWidth.toFloat() / ivImage.width
          mHeightScale = thumbnailHeight.toFloat() / ivImage.height

          enterAnimation()

          return true
        }
      })
    }
  }

  fun enterAnimation() {

    // Set starting values for properties we're going to animate. These
    // values scale and position the full size version down to the thumbnail
    // size/location, from which we'll animate it back up
    ivImage.pivotX = 0F
    ivImage.pivotY = 0F
    ivImage.scaleX = mWidthScale
    ivImage.scaleY = mHeightScale
    ivImage.translationX = mLeftDelta.toFloat()
    ivImage.translationY = mTopDelta.toFloat()

    // interpolator where the rate of change starts out quickly and then decelerates.
    val sDecelerator = DecelerateInterpolator()

    // Animate scale and translation to go from thumbnail to full size
    ivImage.animate()
      .setDuration(ANIM_DURATION.toLong())
      .scaleX(1F)
      .scaleY(1F)
      .translationX(0F)
      .translationY(0F)
      .interpolator = sDecelerator

    // Fade in the black background
    val bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0, 255)
    bgAnim.duration = ANIM_DURATION.toLong()
    bgAnim.start()

  }

  fun exitAnimation(endAction: Runnable) {

    val sInterpolator = AccelerateInterpolator()
    ivImage.animate()
      .setDuration(ANIM_DURATION.toLong())
      .scaleX(mWidthScale)
      .scaleY(mHeightScale)
      .translationX(mLeftDelta.toFloat())
      .translationY(mTopDelta.toFloat())
      .setInterpolator(sInterpolator)
      .withEndAction(endAction)

    // Fade out background
    val bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0)
    bgAnim.duration = ANIM_DURATION.toLong()
    bgAnim.start()
  }

  override fun onBackPressed() {
    exitAnimation(Runnable {
      finish()
      overridePendingTransition(0, 0)
    })
  }


}