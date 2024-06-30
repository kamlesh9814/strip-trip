package com.app.stripstrips.fragment
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.stripstrips.activity.SearchLocationActivity
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.FragmentAddPostBinding
import com.app.stripstrips.model.addPostApi.AddPostRequest
import com.app.stripstrips.model.tripsDiaryListingApi.TripsDiaryDataItem
import com.app.stripstrips.model.tripsDiaryListingApi.TripsDiaryListingRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.utils.ConstantsVar
import com.app.stripstrips.utils.Status
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class AddPostFragment : Fragment() {
    lateinit var binding: FragmentAddPostBinding

    private var trisDiaryList: ArrayList<TripsDiaryDataItem> = ArrayList()
    var postImage: Uri? = null
    var CAMERA_CODE = 1002
    var CAMERA_IMAGE = 1003
    var GALLERY_CODE = 208
    var REQUEST_VIDEO_CAPTURED = 2003
    var typesList: ArrayList<String>? = ArrayList()
    var privacyList: ArrayList<String>? = ArrayList()
    var media_type = ""
    var diary_id = ""
    var latitude = 0.0
    var longitude = 0.0
    var width = 0
    var height = 0
    var bitmap: Bitmap? = null
    var street = ""
    var city = ""
    var state = ""
    var zip_code = ""
    var isPublic = "0"
    var setDate = ""
    var dateToSend = ""

    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onDestroyView() {
        super.onDestroyView()
        bitmap = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.tripDiaryListingApiData(TripsDiaryListingRequest("1", "1000"), requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddPostBinding.inflate(layoutInflater, container, false)
        onClick()
        observer()
        tripDiaryObserver()
        descriptionET()
        return binding.root

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
            postImage == null -> {
                Toast.makeText(
                    requireActivity(),
                    "upload video and photo",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.postName.text.toString().trim().isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    "please enter name of the picture & video",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            binding.locationET.text.toString().trim().isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    "please fill location",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            binding.dateTimeET.text.toString().trim().isEmpty() -> {
                Toast.makeText(requireContext(), "please fill date", Toast.LENGTH_SHORT)
                    .show()
            }
            (trisDiaryList.isEmpty()) -> {
                ConstantsVar.showDiaryAlertDialog(requireActivity())
            }

            binding.typesAutoComplete.text.toString().trim().isEmpty() -> {
                Toast.makeText(requireContext(), "please select trip diary", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.descriptionET.text.toString().trim().isEmpty() -> {
                Toast.makeText(requireContext(), "please fill description", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                return true
            }
        }
        return false
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "SuspiciousIndentation", "IntentReset")
    private fun onClick() {
        /** code For disable dropdown **/
        binding.typesAutoComplete.isClickable = false
        binding.typesAutoComplete.isEnabled = false
        binding.typesAutoComplete.isFocusable = false
        binding.typesAutoComplete1.isClickable = false
        binding.typesAutoComplete1.isEnabled = false
        binding.typesAutoComplete1.isFocusable = false
        dropDown()
        binding.locationET.setOnClickListener {
            val intent = Intent(requireActivity(), SearchLocationActivity::class.java)
            startActivityForResult(intent, 786)
        }

        /** Date Picker Code **/
        binding.dateTimeET.setOnClickListener {
            showDateTimePicker(requireActivity(), binding.dateTimeET)
            Log.e("TAG", "DateShow $setDate")
        }

        binding.SignUpLayout.setOnClickListener {
            if (checkValidation()) {
                viewModel.addPostApi(
                    requireActivity(),
                    AddPostRequest(
                        "",
                        binding.postName.text.toString().trim(),
                        binding.descriptionET.text.toString().trim(),
                        binding.locationET.text.toString().trim(),
                        diary_id,
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
            val dialog = BottomSheetDialog(requireContext())
            val view =
                layoutInflater.inflate(com.app.stripstrips.R.layout.activity_dialogue_video, null)
            val btnCancel = view.findViewById<TextView>(com.app.stripstrips.R.id.CancelTV)
            val btnGallery = view.findViewById<TextView>(com.app.stripstrips.R.id.galleryTV)
            val btnGalleryVideo =
                view.findViewById<TextView>(com.app.stripstrips.R.id.galleryVideoTV)
            val btnCamera = view.findViewById<TextView>(com.app.stripstrips.R.id.tv_camera)
            val btnVideoRecord = view.findViewById<TextView>(com.app.stripstrips.R.id.tvRecordVideo)

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

        binding.typesAutoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                diary_id = trisDiaryList[position].diaryId!!

            }
    }

    /** DropDown Code **/
    private fun dropDown() {
        setupDropdown(typesList as ArrayList<String>)
        privacyList!!.add("Private")
        privacyList!!.add("Public")
        setupDropdown1(privacyList as ArrayList<String>)

    }


    private fun setupDropdown(typesList: ArrayList<String>) {
        binding.typesDropdown.visibility = View.VISIBLE
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1, typesList
        )
        (binding.typesDropdown.editText as? AutoCompleteTextView)?.setAdapter(
            adapter
        )
    }

    private fun setupDropdown1(typesList: ArrayList<String>) {
        binding.typesDropdown1.visibility = View.VISIBLE
        val adapter = ArrayAdapter(
            requireContext(),
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
            Glide.with(requireActivity())
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
            Glide.with(requireActivity())
                .load(postImage)
                .into(iv)

        }

        /** Gallery Video Code **/
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_CODE && data != null) {
            postImage = data.data
            var mediaMetadataRetriever: MediaMetadataRetriever? = null
            try {
                mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(context, postImage)
                bitmap = mediaMetadataRetriever.getFrameAtTime(
                    1000,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                )
            } catch (e: Exception) {
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
            val filePathColumn = arrayOf(Images.Media.DATA)
            val cursor = requireActivity().contentResolver.query(
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
            Glide.with(requireActivity())
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
            Log.e("street", street)
            binding.locationET.setText(street)
        }

    }


    private fun getVideoSize(data: Uri?) {
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(requireContext().applicationContext, data)
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

    private fun clearData() {
        binding.postName.setText("")
        binding.locationET.setText("")
        binding.typesAutoComplete.setText("")
        binding.descriptionET.setText("")
        binding.typesAutoComplete1.setText("")
        binding.dateTimeET.setText("")
        val myImage = binding.addPost
        if (null == myImage.drawable) {
            binding.addPost.setImageBitmap(null)
        } else {
            binding.addPost.setImageResource(com.app.stripstrips.R.drawable.uploadimage)
        }
    }

    private fun observer() {
        viewModel.addPostData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    Log.e("Success", Gson().toJson(it))
                    clearData()

                }
                Status.LOADING -> {
                    MyApplication.showLoader(requireActivity())

                }

                Status.ERROR -> {
                    MyApplication.hideLoader()
                    Log.e("exceptionError", it.message!!)

                    if ( it.data!!.status == 401) {
                        ConstantsVar.ClearDataDialogAlart(requireActivity())
                    } else {
                        ConstantsVar.AppDialogAlart(requireActivity(),it.data.message)
                    }
                }
            }
        }
    }

    private fun tripDiaryObserver() {
        viewModel.tripDiaryListingApi.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    trisDiaryList.clear()
                    typesList!!.clear()
                    trisDiaryList = it.data?.data!!

                    for (i in trisDiaryList.indices) {
                        typesList!!.add(trisDiaryList[i].tripName!!)
                    }
                }
                Status.LOADING -> {
                    MyApplication.showLoader(requireActivity())
                }

                Status.ERROR -> {
                    if (trisDiaryList.isEmpty()) {
                        ConstantsVar.showDiaryAlertDialog(requireActivity())
                    }
                    MyApplication.hideLoader()

                }
            }
        }
    }
    var datee: String?=""
    var monthhh: String?=""
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

                if (week < 10 || month <10) {
                    val f: NumberFormat = DecimalFormat("00")
                    datee = java.lang.String.valueOf(f.format(dayOfMonth))
                    monthhh = java.lang.String.valueOf(f.format(month))
                }
                else{
                    monthhh = month.toString()
                }

                dat = ("$year-$monthhh-$datee")
                dateToSend = dat
                et2.setText(convertDateFormat(dat))
                val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")

                val datee = formatter.parse(dat)

                setDate = datee!!.time.toString()
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
}
