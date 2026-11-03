package com.example.campsiteapp


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import kotlin.String

/**
 * Handles profile display
 *
 * Displays a profile using [displayProfileScreen]
 * Its nested class [ProfileScreen.ProfileScreenState] handles state logic
 */
class ProfileScreen(){
    /**
     * Used to handle distinct ui states
     *
     * Used by [displayProfileScreen] to display and differentiate between ui states
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
     * @param profile optional User passed in if viewing another user’s profile
     * @param onAddUser passed in so function can trigger ProfileDatabase.addProfile
     * @param onDeleteUser passed in so function can trigger ProfileDatabase.deleteProfile
     * @param onUpdateUser passed in so function can trigger ProfileDatabase.updateProfile
     */
    @Composable
    fun DisplayProfileScreen(
        navController: NavController,
        //deviceId is probably temporary but works better to pass in test values for now
        deviceId: String,
        profile: User?,
        onAddUser: (User) -> Unit,
        onDeleteUser: (User) -> Unit,
        onUpdateUser: (User, User) -> Unit,
    ){
        val scope = rememberCoroutineScope()
        //UI state (see possible states in enum class)
        var currentProfileScreenState by remember {mutableStateOf(ProfileScreenState.LOADING)}
        var profileScreenMode by remember {mutableStateOf("")}
        var userProfile by remember { mutableStateOf<User?>(null) }
        var profileToDisplay:User by remember{mutableStateOf(User())}

        var newUserName by remember {mutableStateOf("")}
        var newUserEmail by remember {mutableStateOf("")}
        var newUserPhoneNumber by remember {mutableStateOf("")}
        var newUserProfileImage by remember { mutableStateOf("")}
        var newUserNotificationPreference by remember { mutableStateOf(true) }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        var openDeletionConfirmation by remember { mutableStateOf(false) }

        //State and related variables must be determined inside LaunchedEffect so they are decided before rendering something other than LOADING screen
        LaunchedEffect(key1=deviceId){
            //Return profile with specified device id if it exists
            userProfile=getProfileDocument(deviceId)

            //Display profile if something is passed in, display own profile if it exists, else, prompt to create profile
            profileToDisplay=profile ?: userProfile ?: User()


            //Set defaults for creation form based on whether a profile exists for the user
            newUserName = if(profileToDisplay.userName==""){
                "Name"
            }else{
                profileToDisplay.userName
            }
            newUserEmail = if(profileToDisplay.userEmail==""){
                "Email"
            }else{
                profileToDisplay.userEmail
            }
            newUserPhoneNumber = if(profileToDisplay.userPhoneNumber==""){
                "Phone number"
            }else{
                profileToDisplay.userPhoneNumber
            }
            if(profileToDisplay.userProfileImage!=""){
                newUserProfileImage=profileToDisplay.userProfileImage
            }
            if(!profileToDisplay.notificationPreference){
                newUserNotificationPreference=false
            }

            //set state and title
            if(profile!=null){
                // Display messaging option if is someone else's profile (something is passed in for profile)
                profileScreenMode="View Profile"
                currentProfileScreenState=ProfileScreenState.VIEW
            }else if(userProfile != null){
                // Display editing options, settings if is user's own profile
                profileScreenMode="My Profile"
                currentProfileScreenState=ProfileScreenState.VIEWOWN
            }else{
                // In create mode if no profile with device id is found
                profileScreenMode="Create Profile"
                currentProfileScreenState=ProfileScreenState.CREATEPROMPT
            }

        }

        //All composables are below
        Box(){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                //Only display top text if not in CREATEPROMPT or LOADING state
                if((currentProfileScreenState!=ProfileScreenState.CREATEPROMPT)and(currentProfileScreenState!=ProfileScreenState.LOADING)) {
                    Text(
                        profileScreenMode,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                when(currentProfileScreenState) {
                    //Do not render anything that is dependent on LaunchedEffect outcomes until it is done
                    ProfileScreenState.LOADING->{
                        //could have loading indicator/animation here, but should be less than a second, so is probably not needed
                    }
                    //Display create option if user has no profile
                    ProfileScreenState.CREATEPROMPT -> {
                        Row() {
                            Text(
                                "You have no profile."
                            )
                        }
                        Row() {
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
                        val imagePicker = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.GetContent()
                        ) { uri ->
                            imageUri = uri
                        }
                        Box(
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            //Only if editing
                            if (currentProfileScreenState == ProfileScreenState.EDIT) {
                                //clicking cancels editing
                                Icon(
                                    painter = painterResource(id = R.drawable.chevron_left),
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
                                //clicking opens a window to confirm deletion
                                Icon(
                                    painter = painterResource(id = R.drawable.delete),
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
                                if (imageUri != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(imageUri),
                                        contentDescription = "Selected image",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .align(Alignment.Center)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                    // If the user already has a profile image, display it
                                }else if (profileToDisplay.userProfileImage!=""){
                                    Image(
                                        painter = rememberAsyncImagePainter(profileToDisplay.userProfileImage),
                                        contentDescription = "Profile image",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .align(Alignment.Center)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    //Display default profile picture icon
                                    Icon(
                                        painter = painterResource(id = R.drawable.account_circle),
                                        "Default profile image",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .align(Alignment.Center),
                                        tint = Color.Gray
                                    )
                                }
                                //click to change image/open image picker
                                Icon(
                                    painter = painterResource(id = R.drawable.edit),
                                    "Edit button",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.DarkGray)
                                        .padding(8.dp)
                                        .align(Alignment.BottomEnd)
                                        .clickable {
                                            imagePicker.launch("image/*")
                                        },
                                    tint = Color.White
                                )
                            }
                        }
                        //OutlinedTextFields allow for changing of profile details
                        Row(modifier = Modifier
                            .padding(3.dp)
                        ) {
                            OutlinedTextField(
                                value = newUserName,
                                onValueChange = { newUserName = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            )
                        }
                        Row(modifier = Modifier
                            .padding(3.dp)
                        ) {
                            OutlinedTextField(
                                value = newUserEmail,
                                onValueChange = { newUserEmail = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            )
                        }
                        Row(modifier = Modifier
                            .padding(3.dp)
                        ) {
                            OutlinedTextField(
                                value = newUserPhoneNumber,
                                onValueChange = { newUserPhoneNumber = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
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
                                painter = painterResource(id = R.drawable.check),
                                contentDescription = "Edit button",
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
                                                imageUri?.let {
                                                    newUserProfileImage = uploadImage(context, it)
                                                }
                                                //Fall back on default
                                            } else {
                                                newUserProfileImage = ""
                                            }
                                            //copy values to an empty User object
                                            val newProfile = User()
                                            newProfile.userDeviceId = deviceId
                                            newProfile.userName = newUserName
                                            newProfile.userEmail = newUserEmail
                                            newProfile.userPhoneNumber = newUserPhoneNumber
                                            newProfile.userProfileImage = newUserProfileImage
                                            newProfile.notificationPreference = newUserNotificationPreference
                                            //Add profile if in create mode
                                            if (currentProfileScreenState == ProfileScreenState.CREATE) {
                                                onAddUser(newProfile)
                                                //Update existing profile if in edit mode
                                            } else {
                                                onUpdateUser(profileToDisplay, newProfile)
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
                            ){
                                //Edit option if is user's own profile
                                if(currentProfileScreenState==ProfileScreenState.VIEWOWN){
                                    Icon(
                                        painter = painterResource(id = R.drawable.edit),
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
                                } else if (currentProfileScreenState==ProfileScreenState.VIEW){
                                    Icon(
                                        painter = painterResource(id = R.drawable.mail),
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
                                    Icon(
                                        painter = painterResource(id = R.drawable.chevron_left),
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
                                }
                                //profile picture
                                if (profileToDisplay.userProfileImage != "") {
                                    Image(
                                        painter = rememberAsyncImagePainter(profileToDisplay.userProfileImage),
                                        contentDescription = "Profile image",
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.account_circle),
                                        "Default profile image",
                                        modifier = Modifier.size(120.dp),
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }

                        //profile name
                        Row() {
                            Text(
                                profileToDisplay.userName,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        //profile email
                        Row() {
                            Text(
                                profileToDisplay.userEmail,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        //profile phone number
                        Row() {
                            Text(
                                profileToDisplay.userPhoneNumber,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        //Opt in/out of notifications option if is user's profile
                        if(currentProfileScreenState==ProfileScreenState.VIEWOWN){
                            Row(){
                                var buttonText by remember {
                                    if (profileToDisplay.notificationPreference) {
                                        mutableStateOf("Opt out of notifications")
                                    } else {
                                        mutableStateOf("Opt in to notifications")
                                    }
                                }
                                Button(onClick={
                                    //replaces profile with copy
                                    val newProfile = User()
                                    newProfile.userDeviceId = profileToDisplay.userDeviceId
                                    newProfile.userName = profileToDisplay.userName
                                    newProfile.userEmail = profileToDisplay.userEmail
                                    newProfile.userPhoneNumber = profileToDisplay.userPhoneNumber
                                    newProfile.userProfileImage = profileToDisplay.userProfileImage
                                    newProfile.notificationPreference = !profileToDisplay.notificationPreference
                                    onUpdateUser(profileToDisplay,newProfile)
                                    //ensure modification is conserved in display version
                                    profileToDisplay.notificationPreference=newProfile.notificationPreference
                                    //update button text when clicked
                                    buttonText = if (profileToDisplay.notificationPreference) {
                                        "Opt out of notifications"
                                    } else {
                                        "Opt in to notifications"
                                    }
                                }){
                                    Text(buttonText)
                                }
                            }
                        }
                    }
                }
            }

            //Popup appears when delete button is clicked
            if(openDeletionConfirmation){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                )
                Popup(
                    alignment = Alignment.Center,
                    onDismissRequest = { openDeletionConfirmation = false }
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                    ) {
                        Column(){
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ){
                                Text(
                                    "Delete Profile"
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ){
                                Text(
                                    "Are you sure you want to delete your profile?"
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ){
                                //close popup
                                Button(
                                    onClick={
                                        openDeletionConfirmation=false
                                    }
                                ) {
                                    Text("Cancel")
                                }
                                Button(
                                    //delete user's profile and return them to creation prompt
                                    onClick={
                                        onDeleteUser(profileToDisplay)
                                        profileToDisplay = User()
                                        profileScreenMode = "Create Profile"
                                        currentProfileScreenState = ProfileScreenState.CREATEPROMPT
                                        openDeletionConfirmation=false
                                    },
                                    colors= ButtonDefaults.buttonColors(
                                        containerColor = Color.Red,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}