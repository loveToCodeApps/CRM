package com.example.crm

import android.os.Parcel
import android.os.Parcelable

data class MyActivitiesData(
   var act_id: String?,
   var act_name: String?,
   var act_phone: String?,
   var act_address: String?,
   var act_state: String?,
   var act_city: String?,
   var act_pincode: String?,
   var act_date: String?,
   var act_email: String?,
   var act_assign_to: String?,
   var act_company: String?,
   var act_status: String?
   ) : Parcelable {
   constructor(parcel: Parcel) : this(
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString()
   ) {
   }

   override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeString(act_id)
      parcel.writeString(act_name)
      parcel.writeString(act_phone)
      parcel.writeString(act_address)
      parcel.writeString(act_state)
      parcel.writeString(act_city)
      parcel.writeString(act_pincode)
      parcel.writeString(act_date)
      parcel.writeString(act_email)
      parcel.writeString(act_assign_to)
      parcel.writeString(act_company)
      parcel.writeString(act_status)
   }

   override fun describeContents(): Int {
      return 0
   }

   companion object CREATOR : Parcelable.Creator<MyActivitiesData> {
      override fun createFromParcel(parcel: Parcel): MyActivitiesData {
         return MyActivitiesData(parcel)
      }

      override fun newArray(size: Int): Array<MyActivitiesData?> {
         return arrayOfNulls(size)
      }
   }
}