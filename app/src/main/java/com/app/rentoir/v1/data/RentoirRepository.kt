package com.app.rentoir.v1.data
import com.app.rentoir.v1.R
import com.app.rentoir.v1.model.RentItem

object RentoirRepository {
    fun getRents(): List<RentItem> {
        return listOf(
            RentItem(
                id = 1,
                name = "No Country for Old Men",
                restaurant = "Joel Coen, Ethan Coen",
                price = 28000,
                rating = 8.2,
                description = "Llewelyn Moss stumbles upon dead bodies 2 million and a hoard of heroin in a Texas desert",
                        imageResId = R.drawable.film1
            ),
            RentItem(
                id = 2,
                name = "Marty Supreme",
                restaurant = "Josh Safdie",
                price = 24000,
                rating = 7.7,
                description = "Marty Mauser, a young man with a dream ",
                        imageResId = R.drawable.film2
            ),
            RentItem(
                id = 3,
                name = "The Life of Chuck",
                restaurant = "Mike Flanagan",
                price = 26000,
                rating = 7.3,
                description = "Chuck experiences the wonder of love, the heartbreak of loss, and the multitudes contained in all of us",
                        imageResId = R.drawable.film3
            )
        )
    }
    fun getRentById(id: Int): RentItem? = getRents().find { it.id == id }
}
