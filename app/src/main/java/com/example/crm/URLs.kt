package com.example.crm

object URLs {
     private val ROOT_URL = "http://192.168.0.104/crm/registrationapi.php?apicall="
 //private val ROOT_URL = "https://ecolods.com/crm_api/registrationapi.php?apicall="

        val URL_REGISTER = ROOT_URL + "signup"
        val URL_LOGIN = ROOT_URL + "login"
        val URL_FORGOT_PASSWORD = ROOT_URL + "forgotPassword"
   val URL_SEND_PASSWORD = ROOT_URL + "sendPassword"
   val URL_ADD_USER = ROOT_URL + "addUser"
        val URL_ACTIVITIES = ROOT_URL + "activities"
        val URL_ACTIVITIES_REMINDER_NOTIFICATION_DATA = ROOT_URL + "getActivitiesReminderNotificationData"
        val URL_ADMIN_ACTIVITIES_COUNT = ROOT_URL + "adminActivityCount"
        val URL_EXECUTIVE_ACTIVITIES_STATUS_COUNT = ROOT_URL + "executiveActivitiesStatusCount"
        val URL_ADMIN_ACTIVITIES_STATUS_COUNT = ROOT_URL + "adminActivitiesStatusCount"
        val URL_COMPLETED_ACTIVITIES_COUNT = ROOT_URL + "completedActivityCount"
        val URL_CANCELLED_ACTIVITIES_COUNT = ROOT_URL + "cancelledActivityCount"
        val URL_EXECUTIVE_ACTIVITIES_COUNT = ROOT_URL + "executiveActivityCount"
        val URL_GET_EXECUTIVE_ACTIVITIES = ROOT_URL + "getExecutiveActivities"
        val URL_GET_ADMIN_ACTIVITIES = ROOT_URL + "getAdminActivities"
        val URL_ADMIN_UPCOMING_ACTIVITIES_COUNT = ROOT_URL + "getAdminUpcomingActivitiesCount"
        val URL_EXECUTIVE_UPCOMING_ACTIVITIES_COUNT = ROOT_URL + "getExecutiveUpcomingActivitiesCount"
        val URL_GET_USERS = ROOT_URL + "getUsers"
        val URL_EDIT_ACTIVITIES = ROOT_URL + "editActivities"
        val URL_UPLOAD_PIC = ROOT_URL + "uploadpic"
        val URL_GET_ADMIN_HISTORY = ROOT_URL + "getAdminHistory"
        val URL_GET_EXECUTIVE_HISTORY = ROOT_URL + "getExecutiveHistory"
        val URL_UPLOAD_IMAGE = ROOT_URL + "uploadImage"
        val URL_UPLOAD_VIDEO = ROOT_URL + "uploadVideo"
        val URL_GET_ADMIN_DATE_RANGE_DATA = ROOT_URL+ "getAdminDateRangedData"
        val URL_GET_EXECUTIVE_DATE_RANGE_DATA = ROOT_URL+"getExecutiveDateRangedData"
        val URL_ACTIVITIES_EXECUTIVE_IN_PROGRESS = ROOT_URL + "getExecutiveInProgressData"
        val URL_ACTIVITIES_ADMIN_IN_PROGRESS = ROOT_URL + "getAdminInProgressData"
        val URL_ACTIVITIES_ADMIN_COMPLETED = ROOT_URL + "getAdminCompletedData"
        val URL_ACTIVITIES_EXECUTIVE_COMPLETED = ROOT_URL + "getExecutiveCompletedData"
        val URL_ACTIVITIES_ADMIN_CANCELLED = ROOT_URL + "getAdminCancelledData"
        val URL_ACTIVITIES_EXECUTIVE_CANCELLED = ROOT_URL + "getExecutiveCancelledData"




}














