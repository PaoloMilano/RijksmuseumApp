package com.magicbluepenguin.rijksmuseumapp.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BindingsAdaptersKtTest {

    @Before
    fun setUp() {
        mockkStatic(Picasso::class)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `ensure image binding ignores null urls`() {
        val mockImageView = mockk<ImageView> { every { setImageDrawable(any()) } answers { Unit } }
        bindImage(mockImageView, null)
        verifySequence { mockImageView.setImageDrawable(null) }
        verify { Picasso.get() wasNot called }
    }

    @Test
    fun `ensure image binding calls picasso for non-null urls`() {
        val mockRequestCreator = mockk<RequestCreator>(relaxed = true) {
            every { stableKey(any()) } answers { this@mockk }
        }
        val mockPicasso = mockk<Picasso> {
            every { load(any<String>()) } answers { mockRequestCreator }
        }
        every { Picasso.get() } answers { mockPicasso }

        val mockImageView = mockk<ImageView> { every { setImageDrawable(any()) } answers { Unit } }

        val fakeImage = "https://something"
        bindImage(mockImageView, fakeImage)
        verifySequence {
            Picasso.get()
            mockPicasso.load(fakeImage)
            mockRequestCreator.stableKey(fakeImage)
            mockRequestCreator.into(mockImageView)
        }
    }
}
