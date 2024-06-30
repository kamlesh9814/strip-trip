package com.app.stripstrips.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.app.stripstrips.R
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.ActivityEditPostBinding
import com.app.stripstrips.model.addPostApi.AddPostRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.utils.ConstantsVar.ISBACK
import com.app.stripstrips.utils.Status
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class EditPostActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditPostBinding

    // private var trisDiaryList: ArrayList<TripsDiaryDataItem> = ArrayList()
    var postImage: Uri? = null
    var CAMERA_CODE = 1002
    var CAMERA_IMAGE = 1003
    var GALLERY_CODE = 208
    var REQUEST_VIDEO_CAPTURED = 2003
    var privacyList: ArrayList<String>? = ArrayList()
    var latitude = 0.0
    var longitude = 0.0
    var width = 0
    var height = 0
    var bitmap: Bitmap? = null
    var street = ""
    var city = ""
    var state = ""
    var zip_code = ""
    var setDate = ""
    var dateToSend = ""
    var postId = ""
    var diaryId = ""
    var media_type = ""
    var isPublic = ""
    var postName = ""
    var postDescription = ""
    var location = ""
    var postPrivacy = ""
    var image = ""
    var video = ""
    var thumbImage = ""

    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
        observer()
        descriptionET()
        postId = intent.getStringExtra("post_ids")!!
        Log.e("TAG", "onCreate: $postId")
        diaryId = intent.getStringExtra("diaryId")!!
        postName = intent.getStringExtra("postName")!!
        postDescription = intent.getStringExtra("postDescription")!!
        location = intent.getStringExtra("location")!!
        latitude = intent.getStringExtra("latitude")!!.toDouble()
        longitude = intent.getStringExtra("longitude")!!.toDouble()
        dateToSend = intent.getStringExtra("date")!!
        postPrivacy = intent.getStringExtra("postPrivacy")!!
        image = intent.getStringExtra("image")!!
        video = intent.getStringExtra("video")!!
        thumbImage = intent.getStringExtra("thumbImage")!!
        binding.postName.setText(postName)
        binding.locationET.setText(location)
        binding.dateTimeET.setText(convertDateFormat(dateToSend))
        if (postPrivacy.contains("0")) {
            isPublic = "0"
            binding.typesAutoComplete1.setText("public")

        } else if (postPrivacy.contains("1")) {
            isPublic = "1"
            binding.typesAutoComplete1.setText("private")

        }
        /** code For disable dropdown **/
        binding.typesAutoComplete1.isClickable = false
        binding.typesAutoComplete1.isEnabled = false
        binding.typesAutoComplete1.isFocusable = false
        dropDown()
        binding.descriptionET.setText(postDescription)
        binding.postName.setText(postName)

        if (video.contains("mp4") || video.contains("mp3") || video.contains("mov")) {
            binding.addPost.load(thumbImage) {
                error(R.drawable.update_detail)
            }
        } else if (video != "") {
            binding.addPost.load(image) {
                error(R.drawable.update_detail)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun descriptionET() {
        /** EDitText Scroll Code **/
        binding.descriptionET.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }
    }

    private fun checkValidation(): Boolean {
        when {
            /*        postImage == null -> {
                        Toast.makeText(
                            this,
                            "upload video and photo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }*/
            binding.postName.text.toString().trim().isEmpty() -> {
                Toast.makeText(
                    this,
                    "please enter name of the picture & video",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            binding.locationET.text.toString().trim().isEmpty() -> {
                Toast.makeText(
                    this,
                    "please fill location",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            binding.dateTimeET.text.toString().trim().isEmpty() -> {
                Toast.makeText(this, "please fill date", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.descriptionET.text.toString().trim().isEmpty() -> {
                Toast.makeText(this, "please fill description", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                return true
            }
        }
        return false
    }

    @SuppressLint("IntentReset")
    private fun onClick() {


        binding.backImageView.setOnClickListener {
            finish()
        }

        binding.dateTimeET.setOnClickListener {
            showDateTimePicker(this, binding.dateTimeET)
        }
        binding.locationET.setOnClickListener {
            val intent = Intent(this, SearchLocationActivity::class.java)
            startActivityForResult(intent, 786)
        }
        binding.SignUpLayout.setOnClickListener {
            if (checkValidation()) {
                viewModel.updatePostApi(
                    this,
                    AddPostRequest(
                        postId,
                        binding.postName.text.toString().trim(),
                        binding.descriptionET.text.toString().trim(),
                        binding.locationET.text.toString().trim(),
                        diaryId,
                        postImage,
                        isPublic,
                        media_type,
                        longitude,
                        latitude,
                        height,
                        width,
                        bitmap,
                        dateToSend,
                    )
                )
            }
        }

        /** BottomSheet Dialogue Code **/
        binding.cvAddPost.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view =
                layoutInflater.inflate(R.layout.activity_dialogue_video, null)
            val btnCancel = view.findViewById<TextView>(R.id.CancelTV)
            val btnGallery = view.findViewById<TextView>(R.id.galleryTV)
            val btnGalleryVideo =
                view.findViewById<TextView>(R.id.galleryVideoTV)
            val btnCamera = view.findViewById<TextView>(R.id.tv_camera)
            val btnVideoRecord = view.findViewById<TextView>(R.id.tvRecordVideo)


            /** cancel code **/
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            /** camera open code **/
            btnCamera.setOnClickListener {
                ImagePicker.with(this)
                    .cameraOnly()
                    .crop()
                    .start(CAMERA_CODE)
                dialog.dismiss()
            }

            /** Gallery Image  code **/
            btnGallery.setOnClickListener {
                ImagePicker.with(this)
                    .galleryOnly()
                    .crop()
                    .start(CAMERA_IMAGE)
                dialog.dismiss()
            }

            /** Gallery Video **/
            btnGalleryVideo.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                intent.type = "video/*"
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, -1)
                intent.putExtra(MediaStore.EXTRA_BRIGHTNESS, 1)
                startActivityForResult(Intent.createChooser(intent, "Select Video"), GALLERY_CODE)
                dialog.dismiss()
            }

            /** Video Record Code **/
            btnVideoRecord.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                intent.putExtra("android.intent.extra.durationLimit", 60)
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, -1)
                intent.putExtra(MediaStore.EXTRA_BRIGHTNESS, 1)
                startActivityForResult(intent, REQUEST_VIDEO_CAPTURED)
                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()
        }
    }

    /** DropDown Code **/
    private fun dropDown() {
        privacyList!!.add("Private")
        privacyList!!.add("Public")
        setPublicPrivateDropDown(privacyList as ArrayList<String>)

    }

    private fun setPublicPrivateDropDown(typesList: ArrayList<String>) {
        binding.typesDropdown1.visibility = View.VISIBLE
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, typesList
        )
        (binding.typesDropdown1.editText as? AutoCompleteTextView)?.setAdapter(
            adapter
        )

        binding.typesAutoComplete1.setOnItemClickListener { parent, arg1, position, arg3 ->
            val item = parent.getItemAtPosition(position)
            Log.e("SPINNERDATA", "setupDropdown1: " + item)

            isPublic = if (position == 0)
                "1"
            else {
                "0"
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /** Camera Code **/
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_CODE) {
            val iv = binding.addPost as ImageView
            val vto = iv.viewTreeObserver
            vto.addOnPreDrawListener {
                height = iv.measuredHeight
                width = iv.measuredWidth
                Log.e("hilength", "Height: $height Width: $width")
                true
            }
            postImage = data!!.data
            Glide.with(this)
                .load(postImage)
                .into(iv)


        }

        /**Gallery Image Code **/
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_IMAGE) {
            val iv = binding.addPost as ImageView
            val vto = iv.viewTreeObserver
            vto.addOnPreDrawListener {
                height = iv.measuredHeight
                width = iv.measuredWidth
                Log.e("hilength", "Height: $height Width: $width")
                true
            }
            postImage = data!!.data
            Glide.with(this)
                .load(postImage)
                .into(iv)

        }

        /** Gallery Video Code **/
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_CODE && data != null) {
            postImage = data.data
            var mediaMetadataRetriever: MediaMetadataRetriever? = null
            try {
                mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(this, postImage)
                bitmap = mediaMetadataRetriever.getFrameAtTime(
                    1000,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                )
            } catch (e: Exception) {

                Log.e("GALLERYVIDEO", "onActivityResult: ${e.printStackTrace()}")

                e.printStackTrace()
            } finally {
                mediaMetadataRetriever?.release()
            }
            Glide.with(this)
                .load(bitmap)
                .into(binding.addPost)
            getVideoSize(data.data)
            Log.e("TAG", "onActivityResult: $bitmap")

        }

        /** Video Record Code **/
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURED) {
            postImage = data!!.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = this.contentResolver.query(
                postImage!!,
                filePathColumn,
                null,
                null,
                null
            )
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()

            bitmap = ThumbnailUtils.createVideoThumbnail(
                picturePath,
                MediaStore.Video.Thumbnails.FULL_SCREEN_KIND
            )
            Glide.with(this)
                .load(bitmap)
                .into(binding.addPost)
            Log.e("TAG", "onActivityResult: $bitmap")
            getVideoSize(data.data)
        }

        /**LatLOng Code **/
        if (resultCode == Activity.RESULT_OK && requestCode == 786) {
            street = data?.getStringExtra("street")!!
            city = data.getStringExtra("city")!!
            state = data.getStringExtra("state")!!
            zip_code = data.getStringExtra("zip_code")!!
            latitude = data.getStringExtra("latitude")!!.toDouble()
            longitude = data.getStringExtra("longitude")!!.toDouble()
            binding.locationET.setText(street)
        }

    }

    private fun getVideoSize(data: Uri?) {
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(this.applicationContext, data)
            width =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt()
                    ?: 0
            height =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()
                    ?: 0
            retriever.release()
            Log.e("ADDPOST", "getVideoSize:   $width &&&& height $height ")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var datee: String? = ""
    var monthhh: String? = ""

    @SuppressLint("SimpleDateFormat")
    private fun showDateTimePicker(context: Context, et2: EditText): String {
        var dat = ""
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            context, { _, year, monthOfYear, dayOfMonth ->
                val month = monthOfYear + 1
                val week = dayOfMonth

                if (week < 10 || month < 10) {
                    val f: NumberFormat = DecimalFormat("00")
                    datee = java.lang.String.valueOf(f.format(dayOfMonth))
                    monthhh = java.lang.String.valueOf(f.format(month))
                } else {
                    monthhh = month.toString()
//                    datee = week.toString()
                }
                dat = ("$year-$monthhh-$datee")
                dateToSend = dat
                Log.e("TAG", "dateToSend: $dateToSend")
                et2.setText(convertDateFormat(dat))
                val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")

                val datee = formatter.parse(dat)
                Log.e("TAG", "showDateTimePicker:${dat} ")

                setDate = datee.time.toString()
                dat = setDate
                Log.e("TAG", "test: $setDate")
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
        return dat
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertDateFormat(date: String): String {
        val dates: Date? = SimpleDateFormat("yyyy-MM-dd").parse(date)
        return SimpleDateFormat("MMM dd, yyyy").format(dates!!)
    }

    private fun observer() {
        viewModel.updatePostData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    ISBACK ="1"
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
                Status.LOADING -> {
                    MyApplication.showLoader(this)

                }

                Status.ERROR -> {
                    MyApplication.hideLoader()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}