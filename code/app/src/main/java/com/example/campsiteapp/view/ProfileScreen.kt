/*
Display a specified profile, or user's own profile
Outstanding issues:
- lacks a listener, so dynamic updates in compose are not automatically triggered (only an issue if viewing others' profiles)
- messy and redundant structuring of composable functions
 */
package com.example.campsiteapp.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.campsiteapp.data.ProfileDatabase
import com.example.campsiteapp.data.DeviceIDs
import kotlinx.coroutines.launch
import com.example.campsiteapp.model.User
import com.example.campsiteapp.controller.ImageUploadService
import com.example.campsiteapp.view.components.CancelOrProceedTextButtonsPair
import com.example.campsiteapp.view.components.CustomPopup
import com.example.campsiteapp.view.components.ProfilePicture

/**
 * Used to handle distinct ui states
 *
 * Used by [ProfileScreen] to display and differentiate between ui states
 */
enum class  ProfileScreenState{
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
 * @param profile optional User passed in when viewing another user’s profile
 * @param roles the roles of the user
 */
@Composable
fun ProfileScreen(
    navController: NavController,
    //deviceId is probably temporary but works better to pass in test values for now
    deviceId: String,
    roles: MutableList<String>,
    profile: User?,
){
    val scope = rememberCoroutineScope()
    val profileDatabase = ProfileDatabase()

    //UI state (see possible states in enum class)
    var currentProfileScreenState by remember { mutableStateOf(ProfileScreenState.LOADING) }
    var profileScreenMode by remember { mutableStateOf("") }
    var userProfile by remember { mutableStateOf<User?>(null) }
    var profileToDisplay: User by remember { mutableStateOf(User()) }
    var deviceId = ""

    var newUserName by remember { mutableStateOf("") }
    var newUserEmail by remember { mutableStateOf("") }
    var newUserPhoneNumber by remember { mutableStateOf("") }
    var newUserProfileImage by remember { mutableStateOf("") }
    var newUserNotificationPreference by remember { mutableStateOf(true) }

    //This shouldn't be changed anywhere
    val newUserRoles:MutableList<String> = remember {roles}

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var openDeletionConfirmation by remember { mutableStateOf(false) }
    var viewGuidelines by remember { mutableStateOf(false) }
    var showImagePicker by remember { mutableStateOf(false) }


    val imageUploadService = remember {
        ImageUploadService()
    }

    val context = LocalContext.current
    suspend {
        val deviceIds = DeviceIDs(context)
        deviceId = deviceIds.fetchDeviceId()?:deviceIds.registerDeviceId()
    }
    //State and related variables must be determined inside LaunchedEffect so they are decided before rendering something other than LOADING screen
    LaunchedEffect(key1 = deviceId) {
        //Return profile with specified device id if it exists
        userProfile = profileDatabase.getProfile(deviceId)

        //Display profile if something is passed in, display own profile if it exists, else, prompt to create profile
        profileToDisplay = profile ?: userProfile ?: User()


        //Set defaults for creation form based on whether a profile exists for the user
        newUserName = if (profileToDisplay.name == "") {
            "Name"
        } else {
            profileToDisplay.name
        }
        newUserEmail = if (profileToDisplay.email == "") {
            "Email"
        } else {
            profileToDisplay.email
        }
        newUserPhoneNumber = if (profileToDisplay.phone == "") {
            "Phone number"
        } else {
            profileToDisplay.phone
        }
        if (profileToDisplay.profileImage != "") {
            newUserProfileImage = profileToDisplay.profileImage
        }
        if (!profileToDisplay.notificationsEnabled) {
            newUserNotificationPreference = false
        }

        //set state and title
        if (profile != null) {
            // Display messaging option if is someone else's profile (something is passed in for profile)
            profileScreenMode = "View Profile"
            currentProfileScreenState = ProfileScreenState.VIEW
        } else if (userProfile != null) {
            // Display editing options, settings if is user's own profile
            profileScreenMode = "My Profile"
            currentProfileScreenState = ProfileScreenState.VIEWOWN
        } else {
            // In create mode if no profile with device id is found
            profileScreenMode = "Create Profile"
            currentProfileScreenState = ProfileScreenState.CREATEPROMPT
        }

    }

    //All composables are below
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            //Only display top text if not in CREATEPROMPT or LOADING state
            if ((currentProfileScreenState != ProfileScreenState.CREATEPROMPT) and (currentProfileScreenState != ProfileScreenState.LOADING)) {
                Text(
                    profileScreenMode,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            when (currentProfileScreenState) {
                //Do not render anything that is dependent on LaunchedEffect outcomes until it is done
                ProfileScreenState.LOADING -> {
                    //could have loading indicator/animation here, but should be less than a second, so is probably not needed
                }
                //Display create option if user has no profile
                ProfileScreenState.CREATEPROMPT -> {
                    Row {
                        Text(
                            "You have no profile."
                        )
                    }
                    Row {
                        //Exit prompt and enter creation screen
                        Button(
                            onClick = { currentProfileScreenState = ProfileScreenState.CREATE }
                        ) {
                            Text("Create profile")
                        }
                    }
                }
                //Creation and editing have mostly same options
                (ProfileScreenState.CREATE), (ProfileScreenState.EDIT) -> {
                    Box(
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        //clicking cancels editing
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            "Back button",
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.DarkGray)
                                .padding(8.dp)
                                .clickable {
                                    profileScreenMode = "My Profile"
                                    currentProfileScreenState = ProfileScreenState.VIEWOWN
                                },
                            tint = Color.White
                        )
                        //Only if editing
                        if (currentProfileScreenState == ProfileScreenState.EDIT) {
                            //clicking opens a window to confirm deletion
                            Icon(
                                imageVector = Icons.Default.Delete,
                                "Delete button",
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .padding(8.dp)
                                    .clickable {
                                        openDeletionConfirmation = true
                                    },
                                tint = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(120.dp)
                        ) {
                            //profile image is displayed
                            //If a new image is selected, display it
                            var imageToDisplay by remember { mutableStateOf("") }
                            imageToDisplay = imageUri?.toString() ?: newUserProfileImage
                            ProfilePicture(
                                Modifier.Companion,
                                imageToDisplay,
                                120.dp,
                                "Selected image",
                                {
                                    //click to change image/open image picker
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        "Edit button",
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(Color.DarkGray)
                                            .padding(8.dp)
                                            .align(Alignment.BottomEnd)
                                            .clickable {
                                                showImagePicker = true
                                            },
                                        tint = Color.White
                                    )
                                }
                            )
                        }
                    }
                    //OutlinedTextFields allow for changing of profile details
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                    ) {
                        OutlinedTextField(
                            value = newUserName,
                            onValueChange = { newUserName = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                    ) {
                        OutlinedTextField(
                            value = newUserEmail,
                            onValueChange = { newUserEmail = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                    ) {
                        OutlinedTextField(
                            value = newUserPhoneNumber,
                            onValueChange = { newUserPhoneNumber = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                        )
                    }

                    val context = LocalContext.current
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {

                        //clicking completes editing or creating and uploads image, saves profile info in database
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Complete button",
                            modifier = Modifier
                                .align(Alignment.Bottom)
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(Color.DarkGray)
                                .padding(8.dp)
                                .clickable {

                                    scope.launch {
                                        //Upload image if one was chosen
                                        if (imageUri != null) {
                                            imageUploadService.uploadImage(
                                                context,
                                                imageUri!!
                                            )
                                        }
                                        //copy values to an empty User object
                                        val newProfile = User()
                                        newProfile.roles=newUserRoles
                                        newProfile.deviceId = deviceId
                                        newProfile.name = newUserName
                                        newProfile.email = newUserEmail
                                        newProfile.phone = newUserPhoneNumber
                                        newProfile.profileImage = newUserProfileImage
                                        newProfile.notificationsEnabled =
                                            newUserNotificationPreference
                                        //Add profile if in create mode
                                        if (currentProfileScreenState == ProfileScreenState.CREATE) {
                                            profileDatabase.addProfile(newProfile)
                                            //Update existing profile if in edit mode
                                        } else {
                                            profileDatabase.updateProfile(deviceId,newProfile)
                                        }
                                        //display new/updated profile
                                        profileScreenMode = "My Profile"
                                        profileToDisplay = newProfile
                                        currentProfileScreenState = ProfileScreenState.VIEWOWN
                                    }
                                },
                            tint = Color.White
                        )
                    }
                }
                //Text is not displayed as editable fields in view mode
                ProfileScreenState.VIEW, ProfileScreenState.VIEWOWN -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            //Edit option if is user's own profile
                            if (currentProfileScreenState == ProfileScreenState.VIEWOWN) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    "Edit button",
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.DarkGray)
                                        .padding(8.dp)
                                        .clickable {
                                            profileScreenMode = "Edit Profile"
                                            currentProfileScreenState = ProfileScreenState.EDIT
                                        },
                                    tint = Color.White
                                )
                                //message option, back button if is other's profile
                            } else if (currentProfileScreenState == ProfileScreenState.VIEW) {
                                Icon(
                                    imageVector = Icons.Default.Mail,
                                    "Message button",
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.DarkGray)
                                        .padding(8.dp)
                                        .clickable {
                                            //Messaging not implemented
                                        },
                                    tint = Color.White
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                "Back button",
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.DarkGray)
                                    .padding(8.dp)
                                    .clickable {
                                        navController.popBackStack()
                                    },
                                tint = Color.White
                            )
                            //profile picture
                            ProfilePicture(
                                Modifier.Companion,
                                profileToDisplay.profileImage,
                                120.dp,
                                "Selected image",
                                @Composable {},
                            )
                        }
                    }

                    //profile name
                    Row {
                        Text(
                            profileToDisplay.name,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    //profile email
                    Row {
                        Text(
                            profileToDisplay.email,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    //profile phone number
                    Row {
                        Text(
                            profileToDisplay.phone,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth()
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
                                    val newProfile = User()
                                    newProfile.deviceId = profileToDisplay.deviceId
                                    newProfile.name = profileToDisplay.name
                                    newProfile.email = profileToDisplay.email
                                    newProfile.phone = profileToDisplay.phone
                                    newProfile.profileImage = profileToDisplay.profileImage
                                    newProfile.notificationsEnabled =
                                        !profileToDisplay.notificationsEnabled
                                    profileDatabase.updateProfile(deviceId,newProfile)
                                    //ensure modification is conserved in display version
                                    profileToDisplay.notificationsEnabled =
                                        newProfile.notificationsEnabled
                                    //update button text when clicked
                                    buttonText = if (profileToDisplay.notificationsEnabled) {
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

        //Popup appears when View App Policy and Guidelines button is clicked
        if (viewGuidelines) {
            CustomPopup(
                Modifier.Companion,
                "View App Policy and Guidelines",
                { viewGuidelines = !viewGuidelines },
                false,
                @Composable {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        //close popup
                        Button(
                            onClick = {
                                viewGuidelines = false
                            },
                            modifier = Modifier.padding(4.dp)
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
                modifier = Modifier.Companion,
                "Delete Profile",
                { openDeletionConfirmation = !openDeletionConfirmation },
                true,
                @Composable
                fun() {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Are you sure you want to delete your profile?"
                        )
                    }
                    CancelOrProceedTextButtonsPair(
                        Modifier.Companion,
                        "Cancel",
                        "Delete",
                        { openDeletionConfirmation = false },
                        {
                            //delete user's profile and return them to creation prompt
                            profileDatabase.deleteProfile(profileToDisplay)
                            newUserName = "Name"
                            newUserEmail = "Email"
                            newUserPhoneNumber = "Phone number"
                            newUserProfileImage = ""
                            newUserNotificationPreference = true
                            profileToDisplay = User()
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