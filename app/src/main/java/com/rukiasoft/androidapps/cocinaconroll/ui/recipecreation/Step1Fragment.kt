package com.rukiasoft.androidapps.cocinaconroll.ui.recipecreation


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.databinding.FragmentStep1Binding
import com.rukiasoft.androidapps.cocinaconroll.persistence.utils.PersistenceConstants
import com.rukiasoft.androidapps.cocinaconroll.ui.common.MainActivity
import timber.log.Timber
import javax.inject.Inject


class Step1Fragment : ChildBaseFragment() {

    private lateinit var binding: FragmentStep1Binding

    private val CAMERA_PERMISSION_CODE = 8989

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
            type = getTypeFromSpinner()
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
                        //todo pedir permiso
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

    fun takePic() {
        Timber.d("cretino FOTOOOOOO")
//                        val intent = Intent()
//                        intent.type = "image/*
//                        intent.action = Intent.ACTION_PICK
//                        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE)
    }


}
