package com.example.academiaunifor

import android.os.Parcel
import android.os.Parcelable

data class GrupoMuscular(
    val nomeGrupo: String,
    val exercicios: MutableList<String>,
    val idDocumento: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        mutableListOf<String>().apply {
            parcel.readStringList(this)
        },
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nomeGrupo)
        parcel.writeStringList(exercicios)
        parcel.writeString(idDocumento)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<GrupoMuscular> {
        override fun createFromParcel(parcel: Parcel): GrupoMuscular {
            return GrupoMuscular(parcel)
        }

        override fun newArray(size: Int): Array<GrupoMuscular?> {
            return arrayOfNulls(size)
        }
    }
}
