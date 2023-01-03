package com.example.crm

import android.os.Parcel
import android.os.Parcelable

data class EditUpcomingActivitiesData(
    var id : String,
    var name:String,
    var date:String,
    var userId:String,
    var phone:String,
    var address:String,
    var state:String,
    var city:String,
    var pincode:String,
    var status:String,
    var email:String,
    var company:String,
    var assignTo:String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(date)
        parcel.writeString(userId)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(state)
        parcel.writeString(city)
        parcel.writeString(pincode)
        parcel.writeString(status)
        parcel.writeString(email)
        parcel.writeString(company)
        parcel.writeString(assignTo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EditUpcomingActivitiesData> {
        override fun createFromParcel(parcel: Parcel): EditUpcomingActivitiesData {
            return EditUpcomingActivitiesData(parcel)
        }

        override fun newArray(size: Int): Array<EditUpcomingActivitiesData?> {
            return arrayOfNulls(size)
        }
    }
}
