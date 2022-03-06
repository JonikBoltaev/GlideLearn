package ru.jonik.basic

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioButton
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.jonik.basic.databinding.ActivityMainBinding
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var useKeyword: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.tvText) {
            setText(R.string.app_name)
            setTextColor(Color.RED)
        }

        binding.btnGetRandomImage.setOnClickListener {

            onGetRandomImage()
        }
        binding.btnGetRandomImage.setOnLongClickListener {
            showToastWithRandomNumber()
        }
        binding.cbKey.setOnClickListener {
            this.useKeyword = binding.cbKey.isChecked
            updateUi()
        }
        binding.edKeyWord.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                return@setOnEditorActionListener onGetRandomImage()
            }
            return@setOnEditorActionListener false
        }
        binding.rgKeyWord.setOnCheckedChangeListener { _, checkedId ->
            onGetRandomImage()
        }
        updateUi()
    }


    private fun onGetRandomImage(): Boolean {
        val checkedId = binding.rgKeyWord.checkedRadioButtonId
        val keyword2 = binding.edKeyWord.text.toString()
        val keyword = binding.rgKeyWord.findViewById<RadioButton>(checkedId).text.toString()
        binding.imageView.requestLayout()
        val encodedKeyWord = URLEncoder.encode(keyword, StandardCharsets.UTF_8.name())
        binding.progressBar.visibility = View.VISIBLE
        Glide.with(this)
            .load("https://source.unsplash.com/random/800*600?$encodedKeyWord")
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.INVISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.INVISIBLE
                    return false
                }
            })
            .into(binding.imageView)
        return false
    }

    private fun showToastWithRandomNumber(): Boolean {
        val textValueWithRandom = Random.nextInt(100)
        Toast.makeText(this, "$textValueWithRandom", Toast.LENGTH_SHORT).show()
        return true
    }

    private fun updateUi() = with(binding) {
        cbKey.isChecked = useKeyword
        if (useKeyword) {
            edKeyWord.visibility = View.VISIBLE
        } else {
            edKeyWord.visibility = View.INVISIBLE
        }
    }
}