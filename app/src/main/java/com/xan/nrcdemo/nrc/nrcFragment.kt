package com.xan.nrcdemo.nrc

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.xan.nrcdemo.R
import com.xan.nrcdemo.databinding.FragNrcBinding
import com.xan.nrcdemo.isCameraGranted
import com.xan.nrcdemo.requestUserPermissions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class nrcFragment : Fragment() {

    lateinit var binding: FragNrcBinding
    var imageStringFront: String? = null
    var imageStringBack: String? = null
    var bitmap: Bitmap? = null
    private val REQUEST_STORAGE = 112
    private var clickBtn = true
    private val REQUEST_CAMERA = 100
    val COMPRESSED_RATIO = 13
    val perPixelDataSize = 4
    var mCurrentPhotoPath: String? = null
    private var sharedPreferences: SharedPreferences? = null
    var STORAGE_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val viewModel: nrcViewModel by lazy {
        ViewModelProviders.of(this).get(nrcViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.frag_nrc, container, false
        )

        binding.lifecycleOwner = this
        viewModel.context = requireActivity()
        binding.button.setOnClickListener {
            viewModel.setFrontMyanRequest(imageStringFront!!)
            viewModel.extractMyanmar()
        }

        binding.btnChange.setOnClickListener {
            if (clickBtn) {
                d("##false ","${viewModel.strList.get(0)}")
                binding.Dname.text = viewModel.strList.get(0)
                binding.DfatherName.text = viewModel.strList.get(1)
                binding.Dnrc.text = viewModel.strList.get(2)
                binding.Dbirthday.text = viewModel.strList.get(3)
                binding.Dcardid.text = viewModel.strList.get(4)
                binding.Dprofession.text = viewModel.strList.get(5)
                binding.Daddress.text = viewModel.strList.get(6)
                clickBtn = false

            } else {
                d("##true ","${viewModel.responseModel.value?.data?.translations?.get(0)?.translatedText}")
                binding.Dname.text =
                    viewModel.responseModel.value?.data?.translations?.get(0)?.translatedText
                binding.DfatherName.text =
                    viewModel.responseModel.value?.data?.translations?.get(1)?.translatedText
                binding.Dnrc.text =
                    viewModel.responseModel.value?.data?.translations?.get(2)?.translatedText
                binding.Dbirthday.text =
                    viewModel.responseModel.value?.data?.translations?.get(3)?.translatedText
                binding.Dcardid.text =
                    viewModel.responseModel.value?.data?.translations?.get(4)?.translatedText
                binding.Dprofession.text =
                    viewModel.responseModel.value?.data?.translations?.get(5)?.translatedText
                binding.Daddress.text =
                    viewModel.responseModel.value?.data?.translations?.get(6)?.translatedText
                clickBtn = true
            }
        }


        binding.ivFront.setOnClickListener {
            if (!isCameraGranted(requireActivity())) {
                requestUserPermissions(
                    this,
                    REQUEST_CAMERA,
                    arrayOf(Manifest.permission.CAMERA)
                )
            } else {
                selectImage(requireActivity(), 0)
            }
        }


        binding.ivBack.setOnClickListener {
            if (!isCameraGranted(requireActivity())) {
                requestUserPermissions(
                    this,
                    REQUEST_CAMERA,
                    arrayOf(Manifest.permission.CAMERA)
                )
            } else {
                selectImage(requireActivity(), 1)
            }

        }


        viewModel.responseModel.observe(requireActivity(), androidx.lifecycle.Observer {
            if (it != null) {
                binding.Dname.text =
                    viewModel.responseModel.value?.data?.translations?.get(0)?.translatedText
                binding.DfatherName.text =
                    viewModel.responseModel.value?.data?.translations?.get(1)?.translatedText
                binding.Dnrc.text =
                    viewModel.responseModel.value?.data?.translations?.get(2)?.translatedText
                binding.Dbirthday.text =
                    viewModel.responseModel.value?.data?.translations?.get(3)?.translatedText
                binding.Dcardid.text =
                    viewModel.responseModel.value?.data?.translations?.get(4)?.translatedText
                binding.Dprofession.text =
                    viewModel.responseModel.value?.data?.translations?.get(5)?.translatedText
                binding.Daddress.text =
                    viewModel.responseModel.value?.data?.translations?.get(6)?.translatedText
                binding.dataLayout.visibility = View.VISIBLE
            }
        })

        viewModel.myanResponseModel.observe(requireActivity(), androidx.lifecycle.Observer {
            if (it != null) {
                viewModel.setRequestModel1()
                viewModel.setBackMyanRequest(imageStringBack!!)
                viewModel.extractBackMyanmar()
            }
        })

        viewModel.backResponModel.observe(requireActivity(), androidx.lifecycle.Observer {
            if (it != null) {
                viewModel.setRequestModel2()
                viewModel.changeLanguage()
            }
        })


        viewModel.showLoading.observe(requireActivity(), Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
            }
        })
        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            REQUEST_CAMERA -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (!hasPermissions(
                            requireActivity(),
                            *STORAGE_PERMISSIONS
                        )
                    ) {
                        ActivityCompat.requestPermissions(
                            (requireActivity() as Activity?)!!,
                            STORAGE_PERMISSIONS,
                            REQUEST_CAMERA
                        )
                    }
                }
            }
            REQUEST_STORAGE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //selectImage(requireActivity())
                } else {
                    if (!hasPermissions(
                            requireActivity(),
                            *STORAGE_PERMISSIONS
                        )
                    ) {
                        ActivityCompat.requestPermissions(
                            (requireActivity() as Activity?)!!,
                            STORAGE_PERMISSIONS,
                            REQUEST_STORAGE
                        )
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != AppCompatActivity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == AppCompatActivity.RESULT_OK) {
                    val file = File(mCurrentPhotoPath)
                    val path = mCurrentPhotoPath!!
                    galleryAddPic()
                    try {
                        val imagebitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            Uri.fromFile(file)
                        )
                        val rotatedBitmap: Bitmap? = setRotate(imagebitmap, path)
                        val bitmapGallery =
                            getResizedBitmapLessThanMaxSize(rotatedBitmap!!, 150)
                        binding.ivFront.setImageBitmap(bitmapGallery)
                        imageStringFront = bitmapToBase64(bitmapGallery!!)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                1 -> if (resultCode == AppCompatActivity.RESULT_OK) {
                    val file = File(mCurrentPhotoPath)
                    val path = mCurrentPhotoPath!!
                    galleryAddPic()
                    try {
                        val imagebitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            Uri.fromFile(file)
                        )
                        val rotatedBitmap: Bitmap? = setRotate(imagebitmap, path)
                        val bitmapGallery =
                            getResizedBitmapLessThanMaxSize(rotatedBitmap!!, 150)
                        binding.ivBack.setImageBitmap(bitmapGallery)
                        imageStringBack = bitmapToBase64(bitmapGallery!!)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                2 -> if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                    val selectedImage = data.data
                    val filePathColumn =
                        arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        val cursor = requireActivity().contentResolver.query(
                            selectedImage,
                            filePathColumn, null, null, null
                        )
                        if (cursor != null) {
                            cursor.moveToFirst()
                            try {
                                val url: String = getPath(selectedImage)
                                val imagebitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    data.data
                                )
                                val rotatedBitmap: Bitmap? = setRotate(imagebitmap, url)
                                val bitmapGallery =
                                    getResizedBitmapLessThanMaxSize(rotatedBitmap!!, 150)
                                val imageString = bitmapToBase64(bitmapGallery!!)
                                binding.ivFront.setImageBitmap(bitmapGallery)
                                //myProfileViewModel.callProfileImageApi(imageString)
                                cursor.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
                else -> throw IllegalStateException("Unexpected value: $requestCode")
            }
        }
    }

    private fun selectImage(context: Context, reqCode: Int) {
        val options =
            arrayOf<CharSequence>("Take Photo", "Cancel")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose your profile picture")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.d(
                        "mylog",
                        "Exception while creating file: $ex"
                    )
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Log.d("mylog", "Photofile not null")
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireActivity(),
                        "com.xan.nrcdemo.fileprovider",
                        photoFile
                    )
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePicture, reqCode)
                }
            } /*else if (options[item] == "Choose from Gallery") {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 1)
            }*/ else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        Log.d("mylog", "Path: $mCurrentPhotoPath")
        return image
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    private fun bitmapToBase64(bitmap: Bitmap): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun getResizedBitmapLessThanMaxSize(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()

        // For uncompressed bitmap, the data size is:
        // H * W * perPixelDataSize = H * H * bitmapRatio * perPixelDataSize
        //
        height =
            Math.sqrt(maxSize * 1024 * COMPRESSED_RATIO / perPixelDataSize / bitmapRatio.toDouble())
                .toInt()
        width = (height * bitmapRatio).toInt()
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    private fun hasPermissions(
        context: Context?,
        vararg permissions: String
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) !== PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        requireActivity().sendBroadcast(mediaScanIntent)
    }

    private fun setRotate(bitmap: Bitmap, url: String): Bitmap? {
        var ei: ExifInterface? = null
        try {
            ei = ExifInterface(url)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val orientation: Int = ei!!.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        var rotatedBitmap: Bitmap? = null
        rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(
                bitmap,
                90f
            )
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
                bitmap,
                180f
            )
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
                bitmap,
                270f
            )
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
        return rotatedBitmap
    }

    fun getPath(uri: Uri?): String {
        val projection =
            arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().managedQuery(uri, projection, null, null, null)
        requireActivity().startManagingCursor(cursor)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
}

