package com.example.composetest.util.previewparameterprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.composetest.model.User

class UsersPreviewParameterProvider : PreviewParameterProvider<List<User>> {

    override val values: Sequence<List<User>> = sequenceOf(
        listOf(
            User(
                1, "", "Jeff", "Emuveyan", "jeffemuveyan@gmail.com", "080", 28, "30th"
            ),
            User(
                1, "", "James", "Olomu", "jeffemuveyan@gmail.com", "080", 28, "30th"

            ),
            User(
                1, "", "Joy", "Olomu", "jeffemuveyan@gmail.com", "080", 28, "30th"

            ),
            User(
                1, "", "Faith", "Olomu", "jeffemuveyan@gmail.com", "080", 28, "30th"

            )
        )
    )

}