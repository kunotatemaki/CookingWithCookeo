package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentStep1Binding
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import com.rukiasoft.androidapps.cocinaconroll.utils.GeneralConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
class Step1Fragment : ChildBaseFragment(), CoroutineScope by MainScope() {

    private lateinit var binding: FragmentStep1Binding

//    private var imageCaptureUri: Uri? = null
//    private var imageCropUri: Uri? = null

    private lateinit var viewModel: Step1ViewModel

    companion object {
        const val CAMERA_PERMISSION_CODE = 8989
        const val PICK_FROM_CAMERA_CODE = 8990
        const val PICK_FROM_FILE_CODE = 8991
        const val CROP_FROM_CAMERA_CODE = 8992
        const val CROP_FROM_FILE_CODE = 8993
    }

    @Inject
    lateinit var appContext: Context

    override val childPosition: NewRecipeParent.ChildPosition
        get() = NewRecipeParent.ChildPosition.FIRST


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step1, container, false, cookeoBindingComponent)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        listener.setStep1(
            name = binding.createRecipeNameEditText.text.toString(),
            minutes = binding.editRecipeMinutes.text.toString(),
            portions = binding.editRecipePortions.text.toString(),
            vegetarian = binding.checkboxVegetarian.isChecked,
            type = getTypeFromSpinner(),
            picture = viewModel.getImageName().value ?: PersistenceConstants.DEFAULT_PICTURE_NAME
        )
    }

    private fun getTypeFromSpinner(): String {
        val position = binding.spinnerTypeDish.selectedItemPosition
        val type: String
        type = when (position) {
            0 -> PersistenceConstants.TYPE_STARTERS
            1 -> PersistenceConstants.TYPE_MAIN
            else -> PersistenceConstants.TYPE_DESSERTS
        }
        return type

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(Step1ViewModel::class.java)

        viewModel.getImageName().observe(this, Observer {
            it?.let {imageName->
                binding.imageName = imageName
            }
        })
        val list = listOf(
            resourcesManager.getString(R.string.starters),
            resourcesManager.getString(R.string.main_courses),
            resourcesManager.getString(R.string.desserts)
        )

        val dataAdapter = ArrayAdapter(appContext, android.R.layout.simple_spinner_item, list)

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerTypeDish.adapter = dataAdapter

        listener.getRecipe().observe(this, Observer {
            it?.let { recipe ->
                if(viewModel.getImageName().value == null && recipe.recipe.picture.isNotBlank()){
                    viewModel.setImageName(recipe.recipe.picture)
                }
                binding.recipe = recipe.recipe
                binding.minutes = if (recipe.recipe.minutes > 0) recipe.recipe.minutes.toString() else ""
                binding.portions = if (recipe.recipe.portions > 0) recipe.recipe.portions.toString() else ""
                val type = when (recipe.recipe.type) {
                    PersistenceConstants.TYPE_STARTERS -> resources.getString(R.string.starters)
                    PersistenceConstants.TYPE_DESSERTS -> resources.getString(R.string.desserts)
                    else -> resources.getString(R.string.main_courses)
                }
                binding.spinnerTypeDish.setSelection(dataAdapter.getPosition(type))
            }
        })
        binding.editRecipePhoto.setOnClickListener {
            selectPhoto()
        }
    }

    override fun validateFields(): Boolean {
        (activity as? MainActivity)?.hideKeyboard()

        var ret = true
        val requiredField = resourcesManager.getString(R.string.required_field)
        try {
            binding.editRecipeMinutesLayout.error = null
            Integer.valueOf(binding.editRecipeMinutes.text.toString())
        } catch (e: NumberFormatException) {
            binding.editRecipeMinutesLayout.error = requiredField
            ret = false
        }

        try {
            binding.editRecipePortionsLayout.error = null
            Integer.valueOf(binding.editRecipePortions.text.toString())
        } catch (e: NumberFormatException) {
            binding.editRecipePortionsLayout.error = requiredField
            ret = false
        }

        //create case
        binding.createRecipeNameLayout.error = null

        val sName = binding.createRecipeNameEditText.text.toString()
        if (sName.isBlank()) {
            binding.createRecipeNameLayout.error = requiredField
            ret = false
        }

        return ret
    }

    private fun selectPhoto() {

        val items = listOf(
            resourcesManager.getString(R.string.pick_from_camera),
            resourcesManager.getString(R.string.pick_from_gallery)
        )

        val adapter = ArrayAdapter(appContext, android.R.layout.select_dialog_item, items)
        AlertDialog.Builder(activity).apply {
            setTitle(resourcesManager.getString(R.string.pick_photo))
            setAdapter(adapter) { dialog, item ->
                //pick from camera
                when (item) {
                    0 -> {
                        permissionManager.requestPermissions(
                            fragment = this@Step1Fragment,
                            callbackAllPermissionsGranted = { takePic() },
                            permissions = listOf(Manifest.permission.CAMERA),
                            messageRationale = resourcesManager.getString(R.string.camera_explanation),
                            code = CAMERA_PERMISSION_CODE,
                            showRationaleMessageIfNeeded = true
                        )
//                        val intent = Intent()
//                        intent.type = "image/*
//                        intent.action = Intent.ACTION_PICK
//                        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE)
                    }
                    1 -> {
                        //todo galery
//                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                        mImageCaptureUri = Uri.fromFile(
//                            File(
//                                rwTools.getOriginalStorageDir(context),
//                                RecetasCookeoConstants.TEMP_CAMERA_NAME + System.currentTimeMillis().toString() + ".jpg"
//                            )
//                        )
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri)
//                        try {
//                            takePictureIntent.putExtra("return-data", true)
//                            if (takePictureIntent.resolveActivity(activity.getPackageManager()) == null) {
//                                Toast.makeText(activity, resources.getString(R.string.no_camera), Toast.LENGTH_LONG)
//                            }
//                            startActivityForResult(takePictureIntent, PICK_FROM_CAMERA)
//                        } catch (e: ActivityNotFoundException) {
//                            e.printStackTrace()
//                            Toast.makeText(activity, resources.getString(R.string.no_camera), Toast.LENGTH_LONG).show()
//                        }

                    }
                }
            }
            create()?.show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                    takePic()
                }
            }
        }
    }

    private fun takePic() {
        activity?.let {
            launch {
                Uri.fromFile(readWriteUtils.createImageFile(GeneralConstants.TEMP_CAMERA_NAME))?.let { uri ->
                    //change the uri from file:// schema to content://
                    //if not, app will crash in marshmallow and above
                    fileProviderUtils.getConvertedUri(uri).let { convertedUri ->
                        mediaUtils.takePicFromCamera(this@Step1Fragment, convertedUri, PICK_FROM_CAMERA_CODE)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            viewModel.deleteFile(GeneralConstants.TEMP_CAMERA_NAME)
            viewModel.deleteFile(GeneralConstants.TEMP_CROP_NAME)
            return
        }
        when (requestCode) {
            PICK_FROM_CAMERA_CODE -> {
                launch {
                    val file = readWriteUtils.createImageFile(GeneralConstants.TEMP_CAMERA_NAME)
                    mediaUtils.doCrop(this@Step1Fragment, Uri.fromFile(file), CROP_FROM_CAMERA_CODE)
                }

            }
            PICK_FROM_FILE_CODE -> {
//                val selectedImage = data.getData()
//                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//                val cursor = activity.getContentResolver().query(
//                    selectedImage,
//                    filePathColumn, null, null, null
//                ) ?: return
//                cursor.moveToFirst()
//                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//                val picturePath = cursor.getString(columnIndex)
//                cursor.close()
//
//                val file = File(picturePath)
//                if (file.exists()) {
//                    mImageCaptureUri = Uri.fromFile(File(picturePath))
//                }
//
//                doCrop(CROP_FROM_FILE)
            }
            CROP_FROM_CAMERA_CODE -> {
                launch {
                    viewModel.deleteFile(GeneralConstants.TEMP_CAMERA_NAME)
                    val file = readWriteUtils.createImageFile(GeneralConstants.TEMP_CROP_NAME)
                    val imageName = readWriteUtils.getPersonalImageName()
                    viewModel.renameFile(Uri.fromFile(file), imageName)?.let { name->
                        viewModel.setImageName(name)
                    }
                }
            }
            CROP_FROM_FILE_CODE -> {
//                photo = BitmapFactory.decodeFile(mImageCropUri.getPath())
//                updateNameOfNewImage()
//                mNewPicName = rwTools.saveBitmap(activity.getApplicationContext(), photo, getPictureNameFromFileName())
//                rwTools.deleteFile(mImageCropUri.getPath())
//                rwTools.loadImageFromPath(
//                    activity.getApplicationContext(),
//                    mImageView, mNewPicName,
//                    R.drawable.default_dish, System.currentTimeMillis()
//                )
            }
        }
    }


}
