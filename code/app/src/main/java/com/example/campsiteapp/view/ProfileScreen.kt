/*
Display a specified profile, or user's own profile
Outstanding issues:
- lacks a listener, so dynamic updates in compose are not automatically triggered (only an issue if viewing others' profiles)
- messy and redundant structuring of composable functions
 */
package com.example.campsiteapp.view

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.campsiteapp.data.ProfileDatabase
import kotlinx.coroutines.launch
import com.example.campsiteapp.model.User
import com.example.campsiteapp.controller.ImageUploadService
import com.example.campsiteapp.view.components.CancelOrProceedTextButtonsPair
import com.example.campsiteapp.view.components.CustomPopup
import com.example.campsiteapp.view.components.ProfilePicture
import com.example.campsiteapp.view.components.ProfilePictureOptionButton
import com.example.campsiteapp.view.components.TopAppBarButton
import kotlin.text.contains

enum class  ProfileScreenState {
    /**
     * While LaunchedEffect is running, screens cannot be rendered
     */
    LOADING,

    /**
     * Show a prompt for the user to create a profile
     */
    CREATEPROMPT,

    /**
     * Allow user to interact with profile creation form
     */
    CREATE,

    /**
     * Allow user to interact with profile editing form
     */
    EDIT,

    /**
     * Display the user's own profile
     */
    VIEWOWN,

    /**
     * Display a different user's profile
     */
    VIEW,

    /**
     * Display message interface
     */
    MESSAGE,
}
/**
 * Displays info of specified profile, options relevant to context of profile viewing
 *
 * If nothing is passed in for profile:
 * - Display own profile if they have one
 * - Prompt them to create one if they don't have one
 * - Editing interface and options to delete profile, opt out of notifications
 * - Can switch between view and edit modes
 * If profile is passed in:
 * - displays that user’s profile with messaging option
 *
 * @param deviceId the unique device id associated with the user
 * @param role the role of the user opening a profile, used in creating profile
 * @param targetProfileDeviceId optional User passed in when viewing another user’s profile
 * @param onBack action taken when back button is pressed
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    deviceId: String,
    role: String?,
    targetProfileDeviceId: String?,
    onBack: () -> Unit
){
    val scope = rememberCoroutineScope()
    val profileDatabase = ProfileDatabase()

    //UI state (see possible states in enum class)
    var currentProfileScreenState by remember { mutableStateOf(ProfileScreenState.LOADING) }
    var profileScreenMode by remember { mutableStateOf("") }
    var newUserName by remember { mutableStateOf("") }
    var newUserEmail by remember { mutableStateOf("") }
    var newUserPhoneNumber by remember { mutableStateOf("") }
    var newUserNotificationPreference by remember { mutableStateOf(true) }


    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var openDeletionConfirmation by remember { mutableStateOf(false) }
    var viewGuidelines by remember { mutableStateOf(false) }
    var showImagePicker by remember { mutableStateOf(false) }


    val imageUploadService = remember {
        ImageUploadService()
    }

    val context = LocalContext.current


    val modifier = Modifier

    val blankUser = User()
    blankUser.deviceId = targetProfileDeviceId?:deviceId
    var profileToDisplay by remember { mutableStateOf(blankUser) }



    //State and related variables must be determined inside LaunchedEffect so they are decided before rendering something other than LOADING screen
    LaunchedEffect(key1 = true) {
        val getProfile: User?  = profileDatabase.getProfile(targetProfileDeviceId ?: deviceId)
        Log.d("debug","deviceId: $deviceId")

        Log.d("debug","got profile: ${getProfile?.deviceId ?:"null"}")

        currentProfileScreenState = if (targetProfileDeviceId != null){
            // Display messaging option if is someone else's profile (something is passed in for profile)
            ProfileScreenState.VIEW
        } else if (getProfile != null){
            // Display editing options, settings if is user's own profile
            ProfileScreenState.VIEWOWN
        } else {
            // Prompt user to create profile
            ProfileScreenState.CREATEPROMPT
        }
        profileToDisplay = getProfile?: blankUser
    }

    if ((targetProfileDeviceId==null)&&(role!=null)){
        if(!profileToDisplay.roles.contains(role)){
            profileToDisplay.roles.add(role)
            blankUser.roles.add(role)
        }
    }

    Log.d("profile", "profile screen state $currentProfileScreenState")


    //All composables are below
    when (currentProfileScreenState){
        ProfileScreenState.MESSAGE -> {
            val otherUserId = profileToDisplay.deviceId
            ChatScreen(
                { currentProfileScreenState = ProfileScreenState.VIEW },
                deviceId,
                otherUserId
            )
        }
        else -> {
            Box (
                modifier
                    .fillMaxSize()
                    .imePadding()
            ){
                var imageToDisplay by remember { mutableStateOf("") }
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                if (!((currentProfileScreenState == ProfileScreenState.CREATEPROMPT) || (currentProfileScreenState == ProfileScreenState.LOADING))) {
                                    Text(profileScreenMode)
                                }
                            },
                            navigationIcon = {
                                TopAppBarButton(
                                    {
                                        Log.d("debug", "clicked back button")
                                        when (currentProfileScreenState) {
                                            ProfileScreenState.CREATE -> {
                                                profileScreenMode = "Create Profile"
                                                currentProfileScreenState =
                                                    ProfileScreenState.CREATEPROMPT
                                            }
                                            ProfileScreenState.EDIT -> {
                                                newUserName = profileToDisplay.name
                                                newUserEmail = profileToDisplay.email
                                                newUserPhoneNumber = profileToDisplay.phone
                                                imageToDisplay = profileToDisplay.profileImage
                                                profileScreenMode = "My Profile"
                                                currentProfileScreenState =
                                                    ProfileScreenState.VIEWOWN
                                            }
                                            else -> {
                                                onBack()
                                            }
                                        }
                                    },
                                    Color.Gray,
                                    Color.White,
                                    Icons.Default.ChevronLeft,
                                    "Back button"
                                )
                            },
                            actions = {
                                if (currentProfileScreenState == ProfileScreenState.EDIT) {
                                    TopAppBarButton(
                                        { openDeletionConfirmation = true },
                                        Color.Red,
                                        Color.White,
                                        Icons.Default.Delete,
                                        "Delete button"
                                    )
                                } else if (currentProfileScreenState == ProfileScreenState.VIEW) {
                                    TopAppBarButton(
                                        {
                                            currentProfileScreenState =
                                                ProfileScreenState.MESSAGE
                                        },
                                        Color.DarkGray,
                                        Color.White,
                                        Icons.AutoMirrored.Default.Message,
                                        "Message button"
                                    )
                                } else if (currentProfileScreenState == ProfileScreenState.VIEWOWN) {
                                    TopAppBarButton(
                                        {
                                            newUserName = profileToDisplay.name
                                            newUserEmail = profileToDisplay.email
                                            newUserPhoneNumber = profileToDisplay.phone
                                            imageToDisplay = profileToDisplay.profileImage
                                            profileScreenMode = "Edit Profile"
                                            currentProfileScreenState =
                                                ProfileScreenState.EDIT
                                        },
                                        Color.DarkGray,
                                        Color.White,
                                        Icons.Default.Edit,
                                        "Edit button"
                                    )
                                }
                            }
                        )
                    },
                    content = { padding ->
                        Column(
                            modifier
                                .padding(top = TopAppBarDefaults.TopAppBarExpandedHeight)
                                .padding(20.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            when (currentProfileScreenState) {
                                ProfileScreenState.CREATEPROMPT -> {
                                    Row {
                                        Text(
                                            "You have no profile."
                                        )
                                    }
                                    Row {
                                        //Exit prompt and enter creation screen
                                        Button(
                                            onClick = {
                                                currentProfileScreenState =
                                                    ProfileScreenState.CREATE
                                            },
                                        ) {
                                            Text("Create profile")
                                        }
                                    }
                                }

                                ProfileScreenState.VIEW, ProfileScreenState.VIEWOWN, ProfileScreenState.CREATE, ProfileScreenState.EDIT -> {
                                    Box(
                                        modifier
                                            .size(120.dp)
                                    ) {
                                        //profile image is displayed
                                        //If a new image is selected, display it

                                        fun onImageRemove() {
                                            imageUri = null
                                            imageToDisplay = ""
                                        }
                                        ProfilePicture(
                                            modifier,
                                            imageToDisplay,
                                            120.dp,
                                            "Profile image",
                                            {
                                                if ((currentProfileScreenState == ProfileScreenState.CREATE) || (currentProfileScreenState == ProfileScreenState.EDIT)) {

                                                    fun onImageSelected(uri: Uri?) {
                                                        imageUri = uri
                                                        imageToDisplay = imageUri.toString()
                                                    }

                                                    val imagePicker =
                                                        rememberLauncherForActivityResult(
                                                            contract = ActivityResultContracts.PickVisualMedia()
                                                        ) { uri ->
                                                            onImageSelected(uri)
                                                        }
                                                    //click to change image/open image picker


                                                    ProfilePictureOptionButton(
                                                        {
                                                            imagePicker.launch(
                                                                PickVisualMediaRequest(
                                                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                                                )
                                                            )
                                                        },
                                                        Color.DarkGray,
                                                        Color.White,
                                                        Icons.Default.Edit,
                                                        "Edit profile image button",
                                                        Alignment.BottomEnd
                                                    )

                                                    if (imageToDisplay != "") {
                                                        ProfilePictureOptionButton(
                                                            {
                                                                onImageRemove()
                                                            },
                                                            Color.Red,
                                                            Color.White,
                                                            Icons.Default.Close,
                                                            "Remove profile image button",
                                                            Alignment.TopEnd
                                                        )
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }

                                else -> {

                                }
                            }
                            if ((currentProfileScreenState == ProfileScreenState.CREATE) || (currentProfileScreenState == ProfileScreenState.EDIT)) {
                                Column{
                                    var outlinedTextFieldModifier by remember { mutableStateOf(
                                        modifier
                                            .padding(3.dp)
                                            .fillMaxWidth()
                                    )}
                                    OutlinedTextField(
                                        value = newUserName,
                                        onValueChange = { newUserName = it },
                                        modifier = outlinedTextFieldModifier,
                                        shape = RoundedCornerShape(16.dp),
                                        placeholder = { Text("Name") },
                                    )
                                    OutlinedTextField(
                                        value = newUserEmail,
                                        onValueChange = { newUserEmail = it },
                                        modifier = outlinedTextFieldModifier,
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            16.dp
                                        ),
                                        placeholder = { Text("Email") },
                                    )
                                    OutlinedTextField(
                                        value = newUserPhoneNumber,
                                        onValueChange = { newUserPhoneNumber = it },
                                        modifier = outlinedTextFieldModifier,
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            16.dp
                                        ),
                                        placeholder = { Text("Phone Number") },
                                    )
                                }
                                Box(
                                    modifier = modifier.fillMaxSize()
                                ) {
                                    FloatingActionButton(
                                        {
                                            if (newUserName != "") {
                                                scope.launch {
                                                    //Upload image if one was chosen
                                                    if (imageUri != null) {
                                                        imageToDisplay =
                                                            imageUploadService.uploadImage(
                                                                context,
                                                                imageUri!!,
                                                                deviceId
                                                            )
                                                    }
                                                    //copy values to profile
                                                    profileToDisplay.deviceId = deviceId
                                                    profileToDisplay.name = newUserName
                                                    profileToDisplay.email =
                                                        newUserEmail
                                                    profileToDisplay.phone =
                                                        newUserPhoneNumber
                                                    profileToDisplay.profileImage =
                                                        imageToDisplay
                                                    imageUri = null
                                                    //Add profile if in create mode
                                                    if (currentProfileScreenState == ProfileScreenState.CREATE) {
                                                        profileDatabase.addProfile(
                                                            profileToDisplay
                                                        )
                                                        //Update existing profile if in edit mode
                                                    } else {
                                                        profileDatabase.updateProfile(
                                                            deviceId,
                                                            profileToDisplay
                                                        )
                                                    }
                                                    //display new/updated profile
                                                    profileScreenMode = "My Profile"
                                                    currentProfileScreenState =
                                                        ProfileScreenState.VIEWOWN
                                                }
                                            } else {
                                                //TODO: write message for if user doesnt give name
                                            }
                                        },
                                        modifier
                                            .align(Alignment.BottomEnd)
                                            //.padding(16.dp)
                                            .size(70.dp)
                                        ,
                                        containerColor = Color.DarkGray,
                                        contentColor = Color.White,
                                        shape = CircleShape
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Complete button",
                                            modifier = modifier
                                                .padding(8.dp)
                                                .fillMaxSize(),
                                        )
                                    }
                                    //clicking completes editing or creating and uploads image, saves profile info in database

                                }
                            } else if ((currentProfileScreenState == ProfileScreenState.VIEW) || (currentProfileScreenState == ProfileScreenState.VIEWOWN)) {
                                //Text is not displayed as editable fields in view mode
                                //profile name
                                Row {
                                    Text(
                                        profileToDisplay.name,
                                        textAlign = TextAlign.Center,
                                        modifier = modifier.fillMaxWidth()
                                    )
                                }
                                //profile email
                                Row {
                                    Text(
                                        profileToDisplay.email,
                                        textAlign = TextAlign.Left,
                                        modifier = modifier.fillMaxWidth()
                                    )
                                }
                                //profile phone number
                                Row {
                                    Text(
                                        profileToDisplay.phone,
                                        textAlign = TextAlign.Left,
                                        modifier = modifier.fillMaxWidth()
                                    )
                                }
                                //Opt in/out of notifications, view guidelines options if is user's profile
                                if (currentProfileScreenState == ProfileScreenState.VIEWOWN) {
                                    Row {
                                        var buttonText by remember {
                                            if (profileToDisplay.notificationsEnabled) {
                                                mutableStateOf("Opt out of notifications")
                                            } else {
                                                mutableStateOf("Opt in to notifications")
                                            }
                                        }
                                        Button(
                                            onClick = {
                                                //replaces profile with copy
                                                profileToDisplay.notificationsEnabled = !profileToDisplay.notificationsEnabled
                                                profileDatabase.updateProfile(deviceId, profileToDisplay)
                                                //update button text when clicked
                                                buttonText =
                                                    if (profileToDisplay.notificationsEnabled) {
                                                        "Opt out of notifications"
                                                    } else {
                                                        "Opt in to notifications"
                                                    }
                                            }
                                        ) {
                                            Text(buttonText)
                                        }
                                    }
                                    Row {
                                        //opens View App Policy and Guidelines popup
                                        Button(
                                            onClick = {
                                                viewGuidelines = true
                                            }
                                        ) {
                                            Text("View App Policy and Guidelines")
                                        }
                                    }
                                }
                            }
                        }
                    }
                )

                //Popup appears when View App Policy and Guidelines button is clicked
                if (viewGuidelines) {
                    CustomPopup(
                        Modifier.Companion,
                        "View App Policy and Guidelines",
                        { viewGuidelines = !viewGuidelines },
                        false,
                        @Composable {
                            Row(
                                modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier
                                        .heightIn(max = 200.dp)
                                        .verticalScroll(rememberScrollState())
                                        .padding(25.dp)
                                ) {
                                    Text(
                                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                                    )
                                }
                            }
                            Row(
                                modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                //close popup
                                Button(
                                    onClick = {
                                        viewGuidelines = false
                                    },
                                    modifier.padding(4.dp)
                                ) {
                                    Text("Okay")
                                }
                            }
                        }
                    )
                }

                //Popup appears when delete button is clicked
                if (openDeletionConfirmation) {
                    CustomPopup(
                        modifier,
                        "Delete Profile",
                        { openDeletionConfirmation = !openDeletionConfirmation },
                        true,
                        @Composable
                        fun() {
                            Row(
                                modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    "Are you sure you want to delete your profile?"
                                )
                            }
                            CancelOrProceedTextButtonsPair(
                                modifier,
                                "Cancel",
                                "Delete",
                                { openDeletionConfirmation = false },
                                {
                                    //delete user's profile and return them to creation prompt
                                    profileDatabase.deleteProfile(profileToDisplay)
                                    newUserName = ""
                                    newUserEmail = ""
                                    newUserPhoneNumber = ""
                                    imageToDisplay = ""
                                    newUserNotificationPreference = true
                                    profileToDisplay = blankUser
                                    profileScreenMode = "Create Profile"
                                    currentProfileScreenState = ProfileScreenState.CREATEPROMPT
                                    openDeletionConfirmation = false
                                },
                                Color.Red
                            )
                        }
                    )
                }
                //appears if option to change profile image is selected
                if (showImagePicker) {
                    imageUploadService.ImagePicker(
                        imageUri = imageUri,
                        onImageSelected = { uri ->
                            imageUri = uri
                            showImagePicker = false
                        }
                    )
                }
            }

        }
    }
}